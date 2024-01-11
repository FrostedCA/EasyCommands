package ca.tristan.testbot;

import ca.tristan.easycommands.commands.prefix.PrefixExecutor;
import ca.tristan.easycommands.commands.prefix.PrefixOption;
import ca.tristan.easycommands.database.MySQL;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class ExampleCommand extends PrefixExecutor {

    public ExampleCommand() {
        super();
        options.add(new PrefixOption("someOption", "first option"));
    }

    @Override
    public String getName() {
        return "example";
    }

    /**
     * Override this function to add command aliases;
     */
    @Override
    public void updateAliases() {

    }

    /**
     * Override this function to add your specific authorized roles ex:
     * authorizedRoles.add(jda.getGuildById().getRoleById("myRoleID"));
     */
    @Override
    public void updateAuthorizedRoles(JDA jda) {

    }

    /**
     * Override this function to add your specific authorized channels ex:
     * authorizedChannels.add(jda.getChannelByID("myChannelID"));
     */
    @Override
    public void updateAuthorizedChannels(JDA jda) {

    }

    @Override
    public String getDescription() {
        return "!example [options] - The bot will say all the options.";
    }

    @Override
    public boolean isOwnerOnly() {
        return false;
    }


    public void execute(MessageReceivedEvent event, MySQL mySQL) {
        // the bot simply prints all the options of the !example command
        String reply = options.stream().map(PrefixOption::getStringValue).reduce("", (r,u) -> r + " " + u);
        event.getChannel().sendMessage(reply).queue();
    }


}
