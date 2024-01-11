package ca.tristan.easycommands.commands.defaults;

import ca.tristan.easycommands.EC;
import ca.tristan.easycommands.commands.EventData;
import ca.tristan.easycommands.commands.slash.SlashExecutor;
import ca.tristan.easycommands.guild.GuildOptions;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

/**
 * This command allows you to set an auto role for the guild the command has been executed from.
 * Last updated: v0.8
 */
public class SetLogChannel extends SlashExecutor {

    @Override
    public String getName() {
        return "setlogchannel";
    }

    @Override
    public boolean isOwnerOnly() {
        return true;
    }

    @Override
    public String getDescription() {
        return "Set a log channel for this guild";
    }

    @Override
    public void execute(EventData event) {
        if (EC.updateGuildProperty(GuildOptions.LOG_CHANNEL, event.getChannel().getId(), event.getGuild().getId())) {
            event.reply("Log channel updated.", true).queue();
        } else {
            event.reply("Log channel failed to updated.", true).queue();
        }
    }
}
