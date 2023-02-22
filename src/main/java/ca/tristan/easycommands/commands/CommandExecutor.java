package ca.tristan.easycommands.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

public abstract class CommandExecutor {

    public List<OptionData> options = new ArrayList<>();

    public String getName() {
        return null;
    }

    public String getDescription() {
        return null;
    }

    public boolean isOwnerOnly() {
        return false;
    }

    public List<OptionData> getOptions() {
        return options;
    }

    /**
     * Specifies if the command should be Developer mode only. If true the command won't be registered as a slash command.
     * The function 'execute(EventData)' will then be ignored, and you should only use 'devExecute(MessageReceivedEvent)'.
     * Only server owners can use Dev commands. For now.
     */
    public boolean isDevOnly() { return false; }

    public void execute(EventData data) {

    }

    public void devExecute(MessageReceivedEvent event) {

    }

}
