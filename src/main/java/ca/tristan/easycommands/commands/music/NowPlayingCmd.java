package ca.tristan.easycommands.commands.music;

import ca.tristan.easycommands.commands.EasyCommands;
import ca.tristan.easycommands.commands.slash.SlashExecutor;
import ca.tristan.easycommands.commands.EventData;
import ca.tristan.easycommands.lavaplayer.GuildMusicManager;
import ca.tristan.easycommands.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.Channel;

import java.util.ArrayList;
import java.util.List;

public class NowPlayingCmd extends SlashExecutor {

    @Override
    public String getName() {
        return "nowplaying";
    }

    @Override
    public String getDescription() {
        return "Shows the current playing track.";
    }

    @Override
    public List<Channel> getAuthorizedChannels(JDA jda) {
        List<Channel> channels = new ArrayList<>();
        if(EasyCommands.getMusicChannel() != null) {
            channels.add(EasyCommands.getMusicChannel());
        }
        return channels;
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
