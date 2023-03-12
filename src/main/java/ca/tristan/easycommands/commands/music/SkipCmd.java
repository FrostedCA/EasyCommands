package ca.tristan.easycommands.commands.music;

import ca.tristan.easycommands.commands.slash.SlashExecutor;
import ca.tristan.easycommands.commands.EventData;
import ca.tristan.easycommands.lavaplayer.GuildMusicManager;
import ca.tristan.easycommands.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;

public class SkipCmd extends SlashExecutor {

    @Override
    public String getName() {
        return "skip";
    }

    @Override
    public String getDescription() {
        return "Skips the current track to the next one.";
    }

    @Override
    public void execute(EventData data) {
        final Member self = data.getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        data.deferReply();

        if(selfVoiceState == null || !selfVoiceState.inAudioChannel()){
            data.getHook().sendMessage("I need to be in a voice channel for this to work.").queue();
            return;
        }

        final Member member = data.getCommandSender();
        final GuildVoiceState memberVoiceState = data.getMemberVoiceState();

        if(!memberVoiceState.inAudioChannel()){
            data.getHook().sendMessage("You need to be in a voice channel for this command to work.").queue();
            return;
        }

        if(!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())){
            data.getHook().sendMessage("You need to be in the same voice channel as me for this to work.").queue();
            return;
        }

        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(data.getGuild());
        final AudioPlayer audioPlayer = musicManager.audioPlayer;

        if(audioPlayer.getPlayingTrack() == null){
            data.getHook().sendMessage("There is no track playing currently.").queue();
            return;
        }

        musicManager.scheduler.nextTrack();
        data.getHook().sendMessage("Skipped the current track. Now playing **`" + musicManager.audioPlayer.getPlayingTrack().getInfo().title + "`** by **`" + musicManager.audioPlayer.getPlayingTrack().getInfo().author + "`**.").queue();
    }

}
