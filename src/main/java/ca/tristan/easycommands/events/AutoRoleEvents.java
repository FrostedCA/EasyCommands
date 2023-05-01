package ca.tristan.easycommands.events;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class AutoRoleEvents extends ListenerAdapter {

    Role memberRole;
    Role botRole;

    public AutoRoleEvents(Role memberRole, Role botRole) {
        this.memberRole = memberRole;
        this.botRole = botRole;
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        if(memberRole != null && !event.getMember().getUser().isBot()) {
            event.getGuild().addRoleToMember(event.getMember(), memberRole).queue();
            return;
        }
        if(botRole != null && event.getMember().getUser().isBot()) {
            event.getGuild().addRoleToMember(event.getMember(), botRole).queue();
        }
    }
}
