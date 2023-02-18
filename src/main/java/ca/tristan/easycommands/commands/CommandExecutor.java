package ca.tristan.easycommands.commands;

import net.dv8tion.jda.api.entities.Role;

import java.util.List;

public abstract class CommandExecutor {

    public CommandExecutor() {
        EasyCommands.jda.upsertCommand(getName(), getDescription()).queue();
    }

    public String getName() {
        return null;
    }

    public String getDescription() {
        return null;
    }

    public String getHelpMessage() {
        return null;
    }

    public List<Role> authorizedRoles() {
        return null;
    }

    public boolean isOwnerOnly() {
        return false;
    }

    public void execute(EventData executor) {

    }

}
