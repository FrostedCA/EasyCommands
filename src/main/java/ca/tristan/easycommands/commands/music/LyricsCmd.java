package ca.tristan.easycommands.commands.music;

import ca.tristan.easycommands.EasyCommands;
import ca.tristan.easycommands.commands.EventData;
import ca.tristan.easycommands.commands.slash.SlashExecutor;
import ca.tristan.easycommands.database.MySQL;
import ca.tristan.easycommands.embeds.MusicEB;
import ca.tristan.easycommands.lavaplayer.GuildMusicManager;
import ca.tristan.easycommands.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import core.GLA;
import genius.SongSearch;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.Channel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LyricsCmd extends SlashExecutor {

    public final EasyCommands easyCommands;

    public LyricsCmd(EasyCommands easyCommands) {
        this.easyCommands = easyCommands;
    }

    @Override
    public String getName() {
        return "lyrics";
    }

    @Override
    public String getDescription() {
        return "Retrieves the lyrics of the current playing music.";
    }

    @Override
    public List<Channel> getAuthorizedChannels(JDA jda) {
        List<Channel> channels = new ArrayList<>();
        if(easyCommands.getGuildsMusicChannel().isEmpty()) {
            return channels;
        }
        easyCommands.getGuildsMusicChannel().forEach((guild, channel) -> {
            channels.add(channel);
        });
        return channels;
    }

    @Override
    public void execute(EventData data, MySQL mySQL) {
        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(data.getGuild());
        final AudioPlayer audioPlayer = musicManager.audioPlayer;

        MusicEB musicEB = new MusicEB();

        if(audioPlayer.getPlayingTrack() == null){
            musicEB.getBuilder().setDescription("There is no music currently playing.");
            data.reply(musicEB.getBuilder().build(), true).queue();
            return;
        }

        String title = audioPlayer.getPlayingTrack().getInfo().title;

        GLA gla = new GLA();
        try {

            SongSearch search = gla.search(title.toLowerCase().replaceAll("official", "").replaceAll("music", "").replaceAll("video", "").replaceAll("Audio", ""));

            String url = search.getHits().isEmpty() ? "" : search.getHits().getFirst().getUrl();
            if(url.isBlank()){
                musicEB.getBuilder().setDescription("Sorry, I haven't found any lyrics for that song.");
                data.reply(musicEB.getBuilder().build(), true).queue();
                return;
            }
            musicEB.getBuilder().setDescription("Here's what I've found: " + url);
            data.reply(musicEB.getBuilder().build(), true).queue();
        } catch (IOException e) {
            musicEB.getBuilder().setDescription("Sorry, I haven't found any lyrics for that song.");
            data.reply(musicEB.getBuilder().build(), true).queue();
            e.printStackTrace();
        }
    }
}
