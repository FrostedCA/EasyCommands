package ca.tristan.easycommands.events;

import ca.tristan.easycommands.commands.CommandExecutor;
import ca.tristan.easycommands.commands.EasyCommands;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class DevCommands extends ListenerAdapter {

    private final EasyCommands easyCommands;

    public DevCommands(EasyCommands easyCommands) {
        this.easyCommands = easyCommands;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        if(!Objects.requireNonNull(event.getMember()).isOwner()) {
            return;
        }

        String[] args = event.getMessage().getContentRaw().split(" ");
        String cmdName = args[0].replace("!", "");
        if(easyCommands.getExecutors().containsKey(cmdName)) {
            CommandExecutor executor = easyCommands.getExecutors().get(cmdName);
            executor.devExecute(event);
            event.getMessage().delete().queue();
        }
    }

}
