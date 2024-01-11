package ca.tristan.easycommands;

import ca.tristan.easycommands.commands.IExecutor;
import ca.tristan.easycommands.commands.defaults.SetLogChannel;
import ca.tristan.easycommands.commands.defaults.SetMusicChannel;
import ca.tristan.easycommands.commands.music.*;
import ca.tristan.easycommands.commands.prefix.PrefixCommands;
import ca.tristan.easycommands.commands.prefix.PrefixExecutor;
import ca.tristan.easycommands.commands.slash.SlashCommands;
import ca.tristan.easycommands.commands.slash.SlashExecutor;
import ca.tristan.easycommands.database.MySQL;
import ca.tristan.easycommands.events.AutoDisconnectEvent;
import ca.tristan.easycommands.events.AutoRoleEvents;
import ca.tristan.easycommands.events.ButtonEvents;
import ca.tristan.easycommands.guild.ECGuild;
import ca.tristan.easycommands.utils.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.*;

public class EasyCommands {

    public static File rootDir;
    public static File savedDir;
    public static File configDir;

    public JDA jda;
    private final JDABuilder jdaBuilder;

    /**
     * Local Storage will store guild settings on local machine. MySQL in the 'cloud'.
     * Depending on whether you use mysql or not in the config.
     */
    private static LocalStorage localStorage;
    private MySQL mysql;
    
    private final Map<String, IExecutor> executorMap = new HashMap<>();

    private final List<GatewayIntent> gatewayIntents = new ArrayList<>();
    private final List<CacheFlag> enabledCacheFlags = new ArrayList<>();
    private final List<CacheFlag> disabledCacheFlags = new ArrayList<>();

    private PrefixCommands prefixCommands;
    private final SlashCommands slashCommands;

    private final Map<Guild, Channel> guildsMusicChannel = new HashMap<>();

    private Long millisStart;

    private TextChannel logChannel;

    private Logger logger;

    private static ConfigFile configFile;

    private static ECGuild[] ecGuilds;

    /**
     * Default EasyCommands Constructor. Use for general purpose, works right away.
     */
    public EasyCommands() throws IOException, URISyntaxException {

        onShutdownHook();
        setupDirs();

        localStorage = new LocalStorage(savedDir);
        configFile = new ConfigFile(configDir);
        this.logger = new Logger(this);

        loadIntents();

        this.slashCommands = new SlashCommands(this);

        if(configFile.getUsePrefixCommands()) {
            this.prefixCommands = new PrefixCommands(this);
            getGatewayIntents().add(GatewayIntent.MESSAGE_CONTENT);
        }

        if (configFile.getToken().isEmpty()) {
            Logger.log(LogType.ERROR, "Token is invalid, please enter a valid token inside the config file.");
            System.exit(-1);
        }

        jdaBuilder = JDABuilder.create(configFile.getToken(), gatewayIntents);
        jdaBuilder.addEventListeners(slashCommands);
    }

    /**
     * Often used when hosting on a server which can't have a config file.
     * @param token Bot Token
     * @param usePrefixCommands Whether you wish to use prefix commands ex: !example;
     */
    @Deprecated
    public EasyCommands(String token, boolean usePrefixCommands) throws IOException {

        millisStart = System.currentTimeMillis();

        loadIntents();

        this.slashCommands = new SlashCommands(this);

        if(usePrefixCommands) {
            this.prefixCommands = new PrefixCommands(this);
            getGatewayIntents().add(GatewayIntent.MESSAGE_CONTENT);
        }

        jdaBuilder = JDABuilder.create(token, gatewayIntents);
        jdaBuilder.addEventListeners(slashCommands);
    }

    /**
     * Use this function to start the bot
     */
    public JDA buildJDA() throws InterruptedException {

        jdaBuilder.setEnabledIntents(gatewayIntents);
        jdaBuilder.enableCache(enabledCacheFlags);
        jdaBuilder.disableCache(disabledCacheFlags);

        long millisStart1 = System.currentTimeMillis();

        this.jda = jdaBuilder.build().awaitReady();

        millisStart = System.currentTimeMillis();

        Logger.log(LogType.NONE, "------- Loading EasyCommands -------");
        Logger.log(LogType.LISTENERS, jda.getRegisteredListeners().toString());

        if (configFile == null) {
            Logger.log(LogType.ERROR, "Launch aborted. Couldn't find the config file.");
            return jda;
        }

        if(configFile.getUseMySQL()) {
            try {
                this.mysqlInit();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        if(configFile.getUseMusicBot()) {
            enableMusicBot();
            this.jda.addEventListener(new AutoDisconnectEvent(), new ButtonEvents());
        }

        if(configFile.getUsePrefixCommands()) {
            this.jda.addEventListener(prefixCommands);
        }

        this.jda.addEventListener(new AutoRoleEvents());

        logChannel = !configFile.getLogChannelID().isBlank() ? this.jda.getTextChannelById(configFile.getLogChannelID()) : null;

        updateCommands();
        logCurrentExecutors();

        /*
         * Loads the properties for a guild. (Log channel id, auto roles, etc.);
         */
        ecGuilds = new ECGuild[jda.getGuilds().size()];
        for (int index = 0; index < jda.getGuilds().size(); index++) {
            Guild jdaGuild = jda.getGuilds().get(index);
            ECGuild guild = new ECGuild(jdaGuild.getId());
            ecGuilds[index] = guild;
        }

        Logger.log(LogType.OK, "EasyCommands finished loading in " + ConsoleColors.GREEN_BOLD + (System.currentTimeMillis() - millisStart) + "ms" + ConsoleColors.GREEN + "\nTotal: " + ConsoleColors.GREEN_BOLD + (System.currentTimeMillis() - millisStart1) + "ms" + ConsoleColors.GREEN + ".");
        return jda;
    }

    private void loadIntents() {
        gatewayIntents.addAll(List.of(GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS));
    }

    private void loadCacheFlags() {
        enabledCacheFlags.addAll(List.of(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE));
    }

    public List<GatewayIntent> getGatewayIntents() {
        return gatewayIntents;
    }

    public EasyCommands addGatewayIntents(GatewayIntent... intents) {
        this.getGatewayIntents().addAll(List.of(intents));
        return this;
    }

    public List<CacheFlag> getEnabledCacheFlags() {
        return enabledCacheFlags;
    }

    public EasyCommands addEnabledCacheFlags(CacheFlag... flags) {
        this.getEnabledCacheFlags().addAll(List.of(flags));
        return this;
    }

    public List<CacheFlag> getDisabledCacheFlags() {
        return disabledCacheFlags;
    }

    public void addDisabledCacheFlags(CacheFlag... flags) {
        this.getDisabledCacheFlags().addAll(List.of(flags));
    }

    public PrefixCommands getPrefixCommands() {
        return prefixCommands;
    }

    /**
     * Connects a MySQL database to EasyCommands.
     */
    private void mysqlInit() throws SQLException {
        //mySQL = new MySQL(config.getDB_Host(), config.getDB_Port(), config.getDB_Database(), config.getDB_User(), config.getDB_Password());
        try {
            mysql.connect();
            Logger.log(LogType.OK, "Database connection successful.");
        } catch (SQLException e) {
            Logger.log(LogType.ERROR, "Error while trying to connect to database. Try reloading maven project.");
            return;
        }

        if(mysql.checkConnection(0)) {
            DatabaseMetaData dbm = mysql.getConnection().getMetaData();
            ResultSet tables = dbm.getTables(null, null, "guilds", null);
            if(tables.next()) {
                loadMySQLGuilds();
                return;
            }
            String table = "CREATE TABLE guilds ( guild_id varchar(255) primary key, music_channel varchar(255) )";
            PreparedStatement preparedStatement = mysql.getConnection().prepareStatement(table);
            preparedStatement.execute();
            loadMySQLGuilds();
        }
    }

    private void loadMySQLGuilds() throws SQLException {

        if(jda.getGuilds().isEmpty()) {
            return;
        }

        PreparedStatement preparedStatement = mysql.getConnection().prepareStatement("SELECT * FROM guilds");
        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next()) {
            if((rs.getString(1) == null || rs.getString(1).isEmpty()) || (rs.getString(2) == null || rs.getString(2).isEmpty())) {
                continue;
            }
            if(jda.getGuilds().contains(jda.getGuildById(rs.getString(1)))) {
                Guild guild = jda.getGuildById(rs.getString(1));
                guildsMusicChannel.put(guild, guild.getTextChannelById(rs.getString(2)));
            }
        }
    }

    public MySQL getMySQL() {
        return mysql;
    }

    public Map<String, IExecutor> getExecutors() { return executorMap; }

    public EasyCommands addExecutor(IExecutor... executors) {
        for (IExecutor executor : executors) {
            if(executor.getName() == null || executor.getName().isEmpty()) {
                Logger.log(LogType.WARNING, "Command: '" + executor.getClass().getSimpleName() + "' doesn't have a name and could cause errors.");
            }
            if(executor.getDescription() == null || executor.getDescription().isEmpty()) {
                Logger.log(LogType.WARNING, "Command: '" + executor.getClass().getName() + "' doesn't have a description.");
            }
            this.executorMap.put(executor.getName(), executor);
            if(executor.getAliases() != null && !executor.getAliases().isEmpty()) {
                for (String alias : executor.getAliases()) {
                    if(alias.isEmpty()) {
                        Logger.log(LogType.WARNING, "Alias: '" + executor.getClass().getSimpleName() + "' doesn't have a name and could cause errors.");
                    }
                    this.executorMap.put(alias, executor);
                }
            }
        }
        return this;
    }

    public EasyCommands clearExecutors() {
        this.executorMap.clear();
        return this;
    }

    /**
     * Used to debug executors. Serve to identify if the commands are registered to Discord correctly.
     */
    private void logCurrentExecutors() {

        List<Command> commands = jda.retrieveCommands().complete();
        Logger.log(LogType.EXECUTORS, ConsoleColors.BLUE_BOLD + "- Logging registered Executors");
        Logger.logNoType(ConsoleColors.BLUE_BOLD + "- [Slash]");
        for (Command command : commands) {
            Logger.logNoType("/" + command.getName() + ConsoleColors.RESET + ":" + ConsoleColors.CYAN + command.getId());
        }
        if (configFile.getUsePrefixCommands()) {
            Logger.logNoType(ConsoleColors.BLUE_BOLD + "- [Prefix]");
            getExecutors().forEach((s, iExecutor) -> {
                if(iExecutor instanceof PrefixExecutor) {
                    if(!iExecutor.getAliases().contains(s)) {
                        Logger.logNoType(getPrefixCommands().getPrefix() + s);
                    }
                }
            });
        }


    }

    /**
     * Updates all SlashExecutor to Discord Guild.
     */
    private void updateCommands() {
        List<CommandData> commands = new ArrayList<>();
        getExecutors().forEach((name, executor) -> {
            if(executor instanceof SlashExecutor) {
                SlashExecutor executor1 = (SlashExecutor) executor;
                executor1.updateOptions();
                commands.add(Commands.slash(name, executor1.getDescription()).addOptions(executor1.getOptions()));
            }
            executor.updateAliases();
            executor.updateAuthorizedChannels(jda);
            executor.updateAuthorizedRoles(jda);
        });
        jda.updateCommands().addCommands(commands).queue();
    }

    public EasyCommands registerListeners(ListenerAdapter... listeners) {

        if(List.of(listeners).isEmpty()) {
            return this;
        }

        for (Object listener : listeners) {
            jdaBuilder.addEventListeners(listener);
        }

        return this;
    }

    private void enableMusicBot() {
        this.addExecutor(new PlayCmd(this), new StopCmd(this), new NowPlayingCmd(this), new SkipCmd(this), new PauseCmd(this), new LyricsCmd(this), new SetMusicChannel());
        Logger.log(LogType.OK, "EasyCommands MusicBot has been enabled successfully.");
    }

    public static ConfigFile getConfig() {
        return configFile;
    }

    public static LocalStorage getLocalStorage() {
        return localStorage;
    }

    public JDA getJDA() {
        return jda;
    }

    public TextChannel getLogChannel() {
        return logChannel;
    }

    public Logger getLogger() {
        return logger;
    }

    private void onShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                configFile.writeConfig();
                Logger.log(LogType.OK, "Saved config");

                for (ECGuild ecGuild : ecGuilds) {
                    ecGuild.saveProperties();
                }
                Logger.log(LogType.OK, "Guild properties have been saved for all guilds.");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Logger.log(LogType.NONE, "Shutting down");
        }));
    }

    private void setupDirs() throws URISyntaxException {
        rootDir = new File(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("")).toURI());
        savedDir = new File(rootDir, "Saved");
        configDir = new File(savedDir + File.separator + "Config");
    }

    public static ECGuild[] getEcGuilds() {
        return ecGuilds;
    }

}
