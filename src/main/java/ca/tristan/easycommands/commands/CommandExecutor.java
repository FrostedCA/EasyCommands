package ca.tristan.easycommands.commands;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
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

    public String getHelpMessage() {
        return null;
    }

    public boolean isOwnerOnly() {
        return false;
    }

    public List<OptionData> getOptions() {
        return options;
    }

    public void execute(EventData data) {

    }

}
