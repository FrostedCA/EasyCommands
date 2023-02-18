package ca.tristan.easycommands.commands;

import ca.tristan.easycommands.utils.LogType;
import ca.tristan.easycommands.utils.Logger;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class EasyCommands extends ListenerAdapter {

    protected static List<CommandExecutor> executors = new ArrayList<>();
    protected static JDA jda;
    private static Connection connection;

    public EasyCommands(JDA jda) {
        EasyCommands.jda = jda;
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

    public void setExecutors(List<CommandExecutor> executors) { EasyCommands.executors = executors; }

    public void addExecutor(CommandExecutor... executors) { EasyCommands.executors.addAll(List.of(executors)); }

    public void addExecutor(CommandExecutor executor) { EasyCommands.executors.add(executor); }

    public void clearExecutors() { EasyCommands.executors.clear(); }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        for (CommandExecutor executor : EasyCommands.executors) {
            if(event.getName().equals(executor.getName())) {
                if(executor.isOwnerOnly() && !(Objects.requireNonNull(event.getMember())).isOwner()) {
                    event.deferReply().queue();
                    event.getHook().setEphemeral(true);
                    event.getHook().sendMessage("This command can only be used by the server owner.").queue();
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
    public void logCurrentExecutors() {
        Logger.log(LogType.OK, jda.retrieveCommands().complete().toString());
    }

    /**
     * Updates all executors/commands to Discord Server.
     */
    public void updateCommands() {
        List<CommandData> commands = new ArrayList<>();
        executors.forEach(commandExecutor -> {
            commands.add(Commands.slash(commandExecutor.getName(), commandExecutor.getDescription()));
        });
        jda.updateCommands().addCommands(commands).queue();
    }

}