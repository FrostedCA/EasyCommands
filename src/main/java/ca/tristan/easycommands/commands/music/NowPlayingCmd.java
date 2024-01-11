package ca.tristan.easycommands.commands.music;

import ca.tristan.easycommands.EC;
import ca.tristan.easycommands.EasyCommands;
import ca.tristan.easycommands.commands.slash.SlashExecutor;
import ca.tristan.easycommands.commands.EventData;
import ca.tristan.easycommands.database.MySQL;
import ca.tristan.easycommands.lavaplayer.GuildMusicManager;
import ca.tristan.easycommands.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.Channel;

import java.util.ArrayList;
import java.util.List;

public class NowPlayingCmd extends SlashExecutor {

    public final EasyCommands easyCommands;

    public NowPlayingCmd(EasyCommands easyCommands) {
        this.easyCommands = easyCommands;
    }

    @Override
    public String getName() {
        return "nowplaying";
    }

    @Override
    public String getDescription() {
        return "Shows the current playing track.";
    }

    @Override
    public void execute(EventData data, MySQL mySQL) {
        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(data.getGuild());
        final AudioPlayer audioPlayer = musicManager.audioPlayer;

        if(!EC.canSendMusicCommand(data.getGuild().getId(), data.getChannel().getId())) {
            data.getEvent().reply("You can't use this command in this channel.").setEphemeral(true).queue();
            return;
        }

        if(audioPlayer.getPlayingTrack() == null){
            data.reply("There is currently no music playing.", true).queue();
            return;
        }

        String title = audioPlayer.getPlayingTrack().getInfo().title;
        String author = audioPlayer.getPlayingTrack().getInfo().author;

        data.reply("Now playing: **`" + title + "`** by **`" + author + "`**.", true).queue();
    }

}
