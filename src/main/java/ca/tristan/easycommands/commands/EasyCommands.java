package ca.tristan.easycommands.commands;

import ca.tristan.easycommands.commands.music.*;
import ca.tristan.easycommands.commands.prefix.PrefixCommands;
import ca.tristan.easycommands.commands.prefix.PrefixExecutor;
import ca.tristan.easycommands.commands.slash.SlashCommands;
import ca.tristan.easycommands.commands.slash.SlashExecutor;
import ca.tristan.easycommands.database.MySQL;
import ca.tristan.easycommands.events.AutoDisconnectEvent;
import ca.tristan.easycommands.events.ButtonEvents;
import ca.tristan.easycommands.utils.Config;
import ca.tristan.easycommands.utils.ConsoleColors;
import ca.tristan.easycommands.utils.LogType;
import ca.tristan.easycommands.utils.Logger;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.io.IOException;
import java.sql.*;
import java.util.*;

public class EasyCommands {

    public JDA jda;

    private final boolean usePrefixCommands;
    private final boolean useMusicBot;
    private final JDABuilder jdaBuilder;

    private static MySQL mySQL;

    private static Connection connection;
    private Map<String, IExecutor> executorMap = new HashMap<>();

    private List<GatewayIntent> gatewayIntents = new ArrayList<>();
    private List<CacheFlag> enabledCacheFlags = new ArrayList<>();
    private List<CacheFlag> disabledCacheFlags = new ArrayList<>();

    private PrefixCommands prefixCommands;
    private SlashCommands slashCommands;

    private static Map<Guild, Channel> musicChannels = new HashMap<>();

    private final Long millisStart;

    private Config config;

    public EasyCommands() throws IOException {
        this.config = new Config();
        this.usePrefixCommands = this.config.getUsePrefixCommands();
        this.useMusicBot = this.config.getUseMusicBot();

        millisStart = System.currentTimeMillis();

        loadIntents();

        this.slashCommands = new SlashCommands(this);

        if(usePrefixCommands) {
            this.prefixCommands = new PrefixCommands(this);
            getGatewayIntents().add(GatewayIntent.MESSAGE_CONTENT);
        }

        jdaBuilder = JDABuilder.create(this.config.getToken(), gatewayIntents);
        jdaBuilder.addEventListeners(slashCommands);
    }

    public EasyCommands(String token, boolean usePrefixCommands, boolean useMusicBot) throws IOException {
        this.usePrefixCommands = usePrefixCommands;
        this.useMusicBot = useMusicBot;

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

    public JDA buildJDA() throws InterruptedException {

        jdaBuilder.setEnabledIntents(gatewayIntents);
        jdaBuilder.enableCache(enabledCacheFlags);
        jdaBuilder.disableCache(disabledCacheFlags);

        this.jda = jdaBuilder.build().awaitReady();

        Logger.log(LogType.LISTENERS, jda.getRegisteredListeners().toString());

        if(this.config.getUseMysql()) {
            try {
                this.mysqlInit();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        if(this.useMusicBot) {
            enableMusicBot();
            this.jda.addEventListener(new AutoDisconnectEvent(), new ButtonEvents());
        }

        if(usePrefixCommands) {
            this.jda.addEventListener(prefixCommands);
        }

        updateCommands();
        logCurrentExecutors();
        Logger.log(LogType.OK, "EasyCommands finished loading in " + ConsoleColors.GREEN_BOLD + (System.currentTimeMillis() - millisStart) + "ms" + ConsoleColors.GREEN + ".");
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

    public void addGatewayIntents(GatewayIntent... intents) {
        this.getGatewayIntents().addAll(List.of(intents));
    }

    public List<CacheFlag> getEnabledCacheFlags() {
        return enabledCacheFlags;
    }

    public void addEnabledCacheFlags(CacheFlag... flags) {
        this.getEnabledCacheFlags().addAll(List.of(flags));
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
        mySQL = new MySQL(this.config.getDB_Host(), this.config.getDB_Port(), this.config.getDB_Database(), this.config.getDB_User(), this.config.getDB_Password());
        try {
            mySQL.connect();
            Logger.log(LogType.OK, "Database connection successful.");
        } catch (SQLException e) {
            e.printStackTrace();
            Logger.log(LogType.ERROR, "Error while trying to connect to database. Try reloading maven project.");
            return;
        }

        if(mySQL.checkConnection(0)) {
            DatabaseMetaData dbm = mySQL.getConnection().getMetaData();
            ResultSet tables = dbm.getTables(null, null, "guilds", null);
            if(tables.next()) {
                loadMySQLGuilds();
                return;
            }
            String table = "CREATE TABLE guilds ( guild_id varchar(255) primary key, music_channel varchar(255) )";
            PreparedStatement preparedStatement = mySQL.getConnection().prepareStatement(table);
            preparedStatement.execute();
            loadMySQLGuilds();
        }
    }

    private void loadMySQLGuilds() {

        if(jda.getGuilds().isEmpty()) {
            return;
        }

        jda.getGuilds().forEach(guild -> {
            try {
                PreparedStatement ps = mySQL.getConnection().prepareStatement("SELECT * FROM guilds WHERE guild_id=?");
                ps.setString(1, guild.getId());
                ResultSet rs = ps.executeQuery();
                if(rs.next()) {
                    if(rs.getString(2) == null || rs.getString(2).isEmpty()) {
                        return;
                    }
                    musicChannels.put(guild, guild.getTextChannelById(rs.getString(2)));
                    return;
                }

                String query = "INSERT INTO guilds (guild_id) values(?)";
                PreparedStatement ps2 = mySQL.getConnection().prepareStatement(query);
                ps2.setString(1, guild.getId());
                ps2.execute();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static MySQL getMySQL() {
        return mySQL;
    }

    public static Connection getConnection() { return connection; }

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
        Logger.logNoType(ConsoleColors.BLUE_BOLD + "- [Prefix]");
        getExecutors().forEach((s, iExecutor) -> {
            if(iExecutor instanceof PrefixExecutor) {
                if(!iExecutor.getAliases().contains(s)) {
                    Logger.logNoType(getPrefixCommands().getPrefix() + s);
                }
            }
        });

    }

    /**
     * Updates all executors/commands to Discord Guild.
     */
    private void updateCommands() {
        List<CommandData> commands = new ArrayList<>();
        getExecutors().forEach((name, executor) -> {
            if(executor instanceof SlashExecutor executor1) {
                commands.add(Commands.slash(name, executor1.getDescription()).addOptions(executor1.getOptions()));
            }
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

    public static Map<Guild, Channel> getMusicChannels() {
        return musicChannels;
    }

    private void enableMusicBot() {
        this.addExecutor(new PlayCmd(), new StopCmd(), new NowPlayingCmd(), new SkipCmd(), new PauseCmd(), new LyricsCmd(), new SetMusicChannelCmd());
        Logger.log(LogType.OK, "EasyCommands MusicBot has been enabled successfully.");
    }

    public Config getConfig() {
        return config;
    }
}
