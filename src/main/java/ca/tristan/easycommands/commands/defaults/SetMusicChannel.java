package ca.tristan.easycommands.commands.defaults;

import ca.tristan.easycommands.EC;
import ca.tristan.easycommands.commands.EventData;
import ca.tristan.easycommands.commands.slash.SlashExecutor;
import ca.tristan.easycommands.guild.GuildOptions;

/**
 * This command allows you to set an auto role for the guild the command has been executed from.
 * Last updated: v0.8
 */
public class SetMusicChannel extends SlashExecutor {

    @Override
    public String getName() {
        return "setmusicchannel";
    }

    @Override
    public boolean isOwnerOnly() {
        return true;
    }

    @Override
    public String getDescription() {
        return "Set a music channel for this guild";
    }

    @Override
    public void execute(EventData event) {
        if (EC.updateGuildProperty(GuildOptions.MUSIC_CHANNEL, event.getChannel().getId(), event.getGuild().getId())) {
            event.reply("Music channel updated.", true).queue();
        } else {
            event.reply("Music channel failed to updated.", true).queue();
        }
    }
}
