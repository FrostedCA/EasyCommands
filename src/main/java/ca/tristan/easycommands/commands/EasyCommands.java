package ca.tristan.easycommands.commands;

import ca.tristan.easycommands.commands.music.NowPlayingCmd;
import ca.tristan.easycommands.commands.music.PlayCmd;
import ca.tristan.easycommands.commands.music.SkipCmd;
import ca.tristan.easycommands.commands.music.StopCmd;
import ca.tristan.easycommands.events.DevCommands;
import ca.tristan.easycommands.utils.LogType;
import ca.tristan.easycommands.utils.Logger;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class EasyCommands extends ListenerAdapter {

    protected JDA jda;
    private static Connection connection;
    private List<CommandExecutor> executors = new ArrayList<>();
    private final boolean useDevCommands;
    private final boolean useMusicBot;

    public static GatewayIntent[] gatewayIntents = { GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS };
    public static final CacheFlag[] cacheFlags = { CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE };
    private final JDABuilder jdaBuilder;

    public EasyCommands(String token, boolean enableDevCommands, boolean enableMusicBot) {
        this.useDevCommands = enableDevCommands;
        this.useMusicBot = enableMusicBot;

        if(!useDevCommands) {
            gatewayIntents = new GatewayIntent[]{GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS};
        }

        jdaBuilder = JDABuilder.create(token, Arrays.asList(gatewayIntents));
        jdaBuilder.enableCache(Arrays.asList(cacheFlags));
        jdaBuilder.addEventListeners(this);
    }

    public JDA buildJDA() throws InterruptedException {

        this.jda = jdaBuilder.build().awaitReady();

        if(this.useMusicBot) {
            enableMusicBot();
        }

        if(useDevCommands) {
            this.jda.addEventListener(new DevCommands(this));
        }

        updateCommands();
        logCurrentExecutors();

        return jda;
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

    public List<CommandExecutor> getExecutors() { return executors; }

    public EasyCommands setExecutors(List<CommandExecutor> executors) {
        this.executors = executors;
        return this;
    }

    public EasyCommands addExecutor(CommandExecutor... executors) {
        this.executors.addAll(List.of(executors));
        return this;
    }

    public EasyCommands clearExecutors() {
        this.executors.clear();
        return this;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        for (CommandExecutor executor : this.executors) {
            if(event.getName().equals(executor.getName())) {
                if(executor.isOwnerOnly() && !(Objects.requireNonNull(event.getMember())).isOwner()) {
                    event.reply("This command can only be used by the server owner.").setEphemeral(true).queue();
                    break;
                }
                executor.execute(new EventData(event));
                break;
            }
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
        executors.forEach(commandExecutor -> {
            if(!commandExecutor.isDevOnly()) {
                commands.add(Commands.slash(commandExecutor.getName(), commandExecutor.getDescription()).addOptions(commandExecutor.getOptions()));
            }
        });
        jda.updateCommands().addCommands(commands).queue();
    }

    public EasyCommands registerListeners(ListenerAdapter... listeners) {
        for (Object listener : listeners) {
            jdaBuilder.addEventListeners(listener);
        }

        Logger.log(LogType.OK, List.of(listeners).toString());
        return this;
    }

    public void enableMusicBot() {
        this.executors.addAll(List.of(new PlayCmd(), new StopCmd(), new NowPlayingCmd(), new SkipCmd()));
        Logger.log(LogType.OK, "Music bot enabled.");
    }

}
