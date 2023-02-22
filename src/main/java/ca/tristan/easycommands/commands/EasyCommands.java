package ca.tristan.easycommands.commands;

import ca.tristan.easycommands.commands.music.NowPlayingCmd;
import ca.tristan.easycommands.commands.music.PlayCmd;
import ca.tristan.easycommands.commands.music.SkipCmd;
import ca.tristan.easycommands.commands.music.StopCmd;
import ca.tristan.easycommands.events.DevCommands;
import ca.tristan.easycommands.utils.LogType;
import ca.tristan.easycommands.utils.Logger;
import net.dv8tion.jda.api.JDA;
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

    public static final GatewayIntent[] gatewayIntents = { GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS };
    public static final CacheFlag[] cacheFlags = { CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE };

    public EasyCommands(JDA jda, boolean useDevCommands) {
        this.jda = jda;
        this.jda.addEventListener(this);

        if(useDevCommands) {
            this.jda.addEventListener(new DevCommands(this));
        }

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

    public void setExecutors(List<CommandExecutor> executors) {
        this.executors = executors;
        updateCommands();
    }

    public void addExecutor(CommandExecutor... executors) {
        this.executors.addAll(List.of(executors));
        updateCommands();
    }

    public void clearExecutors() { this.executors.clear(); }

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
    private void updateCommands() {
        List<CommandData> commands = new ArrayList<>();
        executors.forEach(commandExecutor -> {
            if(!commandExecutor.isDevOnly()) {
                commands.add(Commands.slash(commandExecutor.getName(), commandExecutor.getDescription()).addOptions(commandExecutor.getOptions()));
            }
        });
        jda.updateCommands().addCommands(commands).queue();
        logCurrentExecutors();
    }

    public void registerListeners(@NotNull ListenerAdapter... listeners) {
        for (Object listener : listeners) {
            jda.addEventListener(listener);
        }

        Logger.log(LogType.OK, List.of(listeners).toString());
    }

    public void enableMusicBot() {
        addExecutor(new PlayCmd(), new StopCmd(), new NowPlayingCmd(), new SkipCmd());
    }

}
