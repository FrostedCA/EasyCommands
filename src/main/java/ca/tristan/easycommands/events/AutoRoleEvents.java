package ca.tristan.easycommands.events;

import ca.tristan.easycommands.EasyCommands;
import ca.tristan.easycommands.guild.ECGuild;
import ca.tristan.easycommands.guild.GuildOptions;
import ca.tristan.easycommands.utils.LogType;
import ca.tristan.easycommands.utils.Logger;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class AutoRoleEvents extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {

        Optional<ECGuild> opt = Arrays.stream(EasyCommands.getEcGuilds()).filter(ecGuild -> ecGuild.id.equals(event.getGuild().getId())).findAny();

        if (opt.isEmpty()) {
            Logger.log(LogType.ERROR, "Guild properties couldn't be found.");
            return;
        }

        ECGuild guild = opt.get();

        if (event.getUser().isBot()) {
            if (guild.guildProperties.containsKey(GuildOptions.BOT_ROLE.name()) && !guild.guildProperties.getProperty(GuildOptions.BOT_ROLE.name()).isEmpty()) {
                event.getGuild().addRoleToMember(event.getMember(), Objects.requireNonNull(event.getGuild().getRoleById(guild.guildProperties.getProperty(GuildOptions.BOT_ROLE.name())))).queue();
                Logger.log(LogType.OK, "Auto bot role has been added to new bot.");
            }
            return;
        }

        if (guild.guildProperties.containsKey(GuildOptions.MEMBER_ROLE.name()) && !guild.guildProperties.getProperty(GuildOptions.MEMBER_ROLE.name()).isEmpty()) {
            event.getGuild().addRoleToMember(event.getMember(), Objects.requireNonNull(event.getGuild().getRoleById(guild.guildProperties.getProperty(GuildOptions.MEMBER_ROLE.name())))).queue();
            Logger.log(LogType.OK, "Auto member role has been added to new member.");
        } else {
            Logger.log(LogType.ERROR, "Specified guild properties doesn't not contain auto role. Event has been ignored. Please use the command '/setmemberrole <role>' on discord to setup a role.");
        }
    }

}
