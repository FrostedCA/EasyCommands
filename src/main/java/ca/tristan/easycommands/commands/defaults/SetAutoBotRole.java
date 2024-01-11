package ca.tristan.easycommands.commands.defaults;

import ca.tristan.easycommands.EC;
import ca.tristan.easycommands.commands.EventData;
import ca.tristan.easycommands.commands.slash.SlashExecutor;
import ca.tristan.easycommands.guild.GuildOptions;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

/**
 * This command allows you to set an auto role for the guild the command has been executed from.
 * Last updated: v0.8
 */
public class SetAutoBotRole extends SlashExecutor {

    @Override
    public String getName() {
        return "setbotrole";
    }

    /**
     * Override this function to add command aliases;
     */
    @Override
    public void updateAliases() {
        aliases.add("setabr");
    }

    @Override
    public String getDescription() {
        return "Set an auto role for this guild";
    }

    @Override
    public boolean isOwnerOnly() {
        return true;
    }

    /**
     * Use this function to add options to your commands.
     */
    @Override
    public void updateOptions() {
        options.add(new OptionData(OptionType.ROLE, "role", "this will be the bot auto role", true));
    }

    @Override
    public void execute(EventData event) {
        if (event.getCommand().getOptions().isEmpty()) {
            event.reply("You need to specify a certain role.", true).queue();
        }

        Role role = event.getCommand().getOptions().get(0).getAsRole();
        if (EC.updateGuildProperty(GuildOptions.BOT_ROLE, role.getId(), event.getGuild().getId())) {
            event.reply("Auto role updated.", true).queue();
        } else {
            event.reply("Auto role failed to updated. Please provide a valid role.", true).queue();
        }
    }
}
