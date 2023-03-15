package ca.tristan.easycommands.commands.defaults;

import ca.tristan.easycommands.EasyCommands;
import ca.tristan.easycommands.commands.EventData;
import ca.tristan.easycommands.commands.slash.SlashExecutor;

import java.io.IOException;

public class ReloadConfigCmd extends SlashExecutor {

    private EasyCommands easyCommands;

    public ReloadConfigCmd(EasyCommands easyCommands) {
        this.easyCommands = easyCommands;
    }

    @Override
    public String getName() {
        return "reloadconfig";
    }

    @Override
    public String getDescription() {
        return "Reloads the EasyCommands config.";
    }

    @Override
    public boolean isOwnerOnly() {
        return true;
    }

    @Override
    public void execute(EventData data) {
        try {
            this.easyCommands.getConfig().loadConfig();
            data.reply("Config has been reload successfully.", true).queue();
        } catch (IOException e) {
            data.reply("Error while reloading the config file. Try restarting the bot. If the problem persist contact support on Discord.", true).queue();
            throw new RuntimeException(e);
        }
    }

}
