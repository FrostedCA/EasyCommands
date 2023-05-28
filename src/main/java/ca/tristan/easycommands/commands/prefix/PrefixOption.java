package ca.tristan.easycommands.commands.prefix;

public class PrefixOption {

    private String name;
    private String description;
    private String stringValue;

    public PrefixOption(String name, String description) {
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
