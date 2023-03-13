package ca.tristan.easycommands.events;

import ca.tristan.easycommands.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Objects;

public class AutoDisconnectEvent extends ListenerAdapter {

    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
        if(Objects.requireNonNull(event.getGuild().getSelfMember().getVoiceState()).inAudioChannel()) {
            if(Objects.requireNonNull(event.getGuild().getSelfMember().getVoiceState().getChannel()).getMembers().size() == 1) {
                PlayerManager.getInstance().getMusicManager(event.getGuild()).scheduler.player.stopTrack();
                PlayerManager.getInstance().getMusicManager(event.getGuild()).scheduler.queue.clear();
                event.getGuild().getAudioManager().closeAudioConnection();
            }
        }
    }
}
