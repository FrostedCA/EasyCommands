package ca.tristan.easycommands.commands.music;

import ca.tristan.easycommands.commands.CommandExecutor;
import ca.tristan.easycommands.commands.EventData;
import ca.tristan.easycommands.lavaplayer.GuildMusicManager;
import ca.tristan.easycommands.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

public class NowPlayingCmd extends CommandExecutor {

    @Override
    public String getName() {
        return "nowplaying";
    }

    @Override
    public String getDescription() {
        return "Shows the current playing track.";
    }

    @Override
    public void execute(EventData data) {
        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(data.getGuild());
        final AudioPlayer audioPlayer = musicManager.audioPlayer;

        if(audioPlayer.getPlayingTrack() == null){
            data.getEvent().reply("There is no track playing currently.").queue();
            return;
        }

        String title = audioPlayer.getPlayingTrack().getInfo().title;
        String author = audioPlayer.getPlayingTrack().getInfo().author;

        data.getEvent().reply("Now playing: **`" + title + "`** by **`" + author + "`**.").queue();
    }

}
