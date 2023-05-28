package ca.tristan.testbot;

import ca.tristan.easycommands.commands.prefix.PrefixExecutor;
import ca.tristan.easycommands.commands.prefix.PrefixOption;
import ca.tristan.easycommands.database.MySQL;
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

    @Override
    public List<String> getAliases() {
        return List.of("s");
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
