package ca.tristan.easycommands.commands;

import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.List;

public class CommandData {

    Long id;
    String commandString;
    net.dv8tion.jda.api.interactions.commands.Command.Type type;
    String name;
    String fullName;
    List<OptionMapping> options;

    public CommandData(Long id, String commandString, Command.Type type, String name, String fullName, List<OptionMapping> options) {
        this.id = id;
        this.commandString = commandString;
        this.type = type;
        this.name = name;
        this.fullName = fullName;
        this.options = options;
    }

    public Long getId() {
        return id;
    }

    public String getCommandString() {
        return commandString;
    }

    public Command.Type getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        return fullName;
    }

    public List<OptionMapping> getOptions() {
        return options;
    }
}
