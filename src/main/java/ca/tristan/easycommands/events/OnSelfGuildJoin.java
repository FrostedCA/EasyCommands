package ca.tristan.easycommands.events;

import ca.tristan.easycommands.EasyCommands;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class OnSelfGuildJoin extends ListenerAdapter {

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        EasyCommands.loadGuildProperties();
    }
}
