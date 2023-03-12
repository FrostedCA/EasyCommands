package ca.tristan.easycommands.commands.prefix;

import net.dv8tion.jda.api.interactions.commands.OptionType;

public class PrefixOptions {

    private String name;
    private String description;
    private String stringValue;

    public PrefixOptions(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }
}
