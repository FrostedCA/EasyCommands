package ca.tristan.easycommands.commands;

import ca.tristan.easycommands.commands.music.NowPlayingCmd;
import ca.tristan.easycommands.commands.music.PlayCmd;
import ca.tristan.easycommands.commands.music.SkipCmd;
import ca.tristan.easycommands.commands.music.StopCmd;
import ca.tristan.easycommands.commands.prefix.PrefixCommands;
import ca.tristan.easycommands.commands.slash.SlashExecutor;
import ca.tristan.easycommands.utils.LogType;
import ca.tristan.easycommands.utils.Logger;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

public class EasyCommands extends ListenerAdapter {

    public JDA jda;

    private final boolean usePrefixCommands;
    private final boolean useMusicBot;
    private final JDABuilder jdaBuilder;

    private static Connection connection;
    private Map<String, IExecutor> executorMap = new HashMap<>();

    private List<GatewayIntent> gatewayIntents = new ArrayList<>();
    private List<CacheFlag> enabledCacheFlags = new ArrayList<>();
    private List<CacheFlag> disabledCacheFlags = new ArrayList<>();

    private PrefixCommands prefixCommands;

    public EasyCommands(String token, boolean enablePrefixCommands, boolean enableMusicBot) {
        this.usePrefixCommands = enablePrefixCommands;
        this.useMusicBot = enableMusicBot;

        loadIntents();

        if(usePrefixCommands) {
            this.prefixCommands = new PrefixCommands(this);
            getGatewayIntents().add(GatewayIntent.MESSAGE_CONTENT);
        }

        jdaBuilder = JDABuilder.create(token, gatewayIntents);
        jdaBuilder.addEventListeners(this);
    }

    public JDA buildJDA() throws InterruptedException {

        jdaBuilder.setEnabledIntents(gatewayIntents);
        jdaBuilder.enableCache(enabledCacheFlags);
        jdaBuilder.disableCache(disabledCacheFlags);

        this.jda = jdaBuilder.build().awaitReady();

        if(this.useMusicBot) {
            enableMusicBot();
        }

        if(usePrefixCommands) {
            this.jda.addEventListener(prefixCommands);
        }

        updateCommands();
        logCurrentExecutors();

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

    public List<CacheFlag> getDisabledCacheFlags() {
        return disabledCacheFlags;
    }

    public PrefixCommands getPrefixCommands() {
        return prefixCommands;
    }

    /**
     * Connects a MySQL database to EasyCommands.
     * @param url Example format: "localhost:3306/database".
     * @param username Database username.
     * @param password Database password.
     */
    public void mysqlConnect(String url, String username, String password) {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + url, username, password);
            Logger.log(LogType.OK, "Database connection successful.");
        } catch (SQLException e) {
            Logger.log(LogType.ERROR, "Error while trying to connect to database.", Arrays.toString(e.getStackTrace()));
        }
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

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if(getExecutors().containsKey(event.getName()) && getExecutors().get(event.getName()) instanceof SlashExecutor executor) {
            Logger.log(LogType.SLASHCMD, "'" + executor.getName() + "' has been triggered.");
            if(executor.isOwnerOnly() && ! (Objects.requireNonNull(event.getMember())).isOwner()) {
                event.reply("This command can only be used by the server owner.").setEphemeral(true).queue();
                return;
            }

            if(!executor.getAuthorizedChannels(jda).isEmpty() && !executor.getAuthorizedChannels(jda).contains(event.getChannel())) {
                event.reply("This command cannot be used in this channel.").setEphemeral(true).queue();
                return;
            }

            if(executor.getAuthorizedRoles(jda) != null && !executor.getAuthorizedRoles(jda).isEmpty()) {
                for (Role authorizedRole : executor.getAuthorizedRoles(jda)) {
                    if(Objects.requireNonNull(event.getMember()).getRoles().contains(authorizedRole)) {
                        executor.execute(new EventData(event));
                        break;
                    }
                }
                return;
            }

            executor.execute(new EventData(event));
        }
    }

    /**
     * Used to debug executors. Serve to identify if the commands are registered to Discord correctly.
     */
    private void logCurrentExecutors() {
        Logger.log(LogType.OK, jda.retrieveCommands().complete().toString());
    }

    /**
     * Updates all executors/commands to Discord Guild.
     */
    public void updateCommands() {
        List<CommandData> commands = new ArrayList<>();
        getExecutors().forEach((name, executor) -> {
            if(executor instanceof SlashExecutor executor1) {
                commands.add(Commands.slash(executor1.getName(), executor1.getDescription()).addOptions(executor1.getOptions()));
            }
        });
        jda.updateCommands().addCommands(commands).queue();
    }

    public EasyCommands registerListeners(ListenerAdapter... listeners) {

        if(List.of(listeners).isEmpty()) {
            Logger.log(LogType.WARNING, "No listener are registered. Ignore this message if you're not supposed to have any.");
            return this;
        }

        for (Object listener : listeners) {
            jdaBuilder.addEventListeners(listener);
        }

        Logger.log(LogType.OK, List.of(listeners).toString());
        return this;
    }

    public void enableMusicBot() {
        this.addExecutor(new PlayCmd(), new StopCmd(), new NowPlayingCmd(), new SkipCmd());
        Logger.log(LogType.OK, "Music bot enabled.");
    }

}
