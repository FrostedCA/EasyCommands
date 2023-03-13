package ca.tristan.easycommands.lavaplayer;

import ca.tristan.easycommands.commands.EventData;
import ca.tristan.easycommands.embeds.MusicEB;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerManager {

    private static PlayerManager INSTANCE;
    private final Map<Long, GuildMusicManager> musicManagers;
    private final AudioPlayerManager audioPlayerManager;

    public PlayerManager(){
        this.musicManagers = new HashMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();

        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
    }

    public GuildMusicManager getMusicManager(Guild guild){
        return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            final GuildMusicManager guildMusicManager = new GuildMusicManager(this.audioPlayerManager);
            guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());
            return guildMusicManager;
        });
    }

    public void loadAndPlay(TextChannel textChannel, EventData data, String trackUrl){
        final GuildMusicManager musicManager = this.getMusicManager(textChannel.getGuild());
        this.audioPlayerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                musicManager.scheduler.queue(audioTrack);
                MusicEB musicEB = new MusicEB();
                musicEB.getBuilder().setDescription("A new music has been added to queue.");
                musicEB.getBuilder().addField("Music", audioTrack.getInfo().title, false);
                musicEB.getBuilder().addField("Author", audioTrack.getInfo().author, false);
                musicEB.getBuilder().addField("Added by", data.getCommandSender().getAsMention(), false);
                data.getEvent().getHook().sendMessageEmbeds(musicEB.getBuilder().build()).setActionRow(musicEB.getActionRow()).queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                final List<AudioTrack> tracks = audioPlaylist.getTracks();
                if(!tracks.isEmpty()){
                    musicManager.scheduler.queue(tracks.get(0));
                    MusicEB musicEB = new MusicEB();
                    musicEB.getBuilder().setDescription("A new music has been added to queue.");
                    musicEB.getBuilder().addField("Music", tracks.get(0).getInfo().title, false);
                    musicEB.getBuilder().addField("Author", tracks.get(0).getInfo().author, false);
                    musicEB.getBuilder().addField("Added by", data.getCommandSender().getAsMention(), false);
                    data.getEvent().getHook().sendMessageEmbeds(musicEB.getBuilder().build()).setActionRow(musicEB.getActionRow()).queue();
                }
            }

            @Override
            public void noMatches() {
                MusicEB musicEB = new MusicEB();
                musicEB.getBuilder().setDescription("Couldn't find the specified music.");
                data.getEvent().getHook().sendMessageEmbeds(musicEB.getBuilder().build()).setActionRow(
                        Button.link("https://github.com/FrostedCA/EasyCommands", "EasyCommands").withEmoji(Emoji.fromUnicode("✨"))
                ).queue();
            }

            @Override
            public void loadFailed(FriendlyException e) {
                MusicEB musicEB = new MusicEB();
                musicEB.getBuilder().setDescription("Failed to load new music.");
                data.getEvent().getHook().sendMessageEmbeds(musicEB.getBuilder().build()).setActionRow(
                        Button.link("https://github.com/FrostedCA/EasyCommands", "EasyCommands").withEmoji(Emoji.fromUnicode("✨"))
                ).queue();
            }

        });
    }

    public static PlayerManager getInstance(){
        if(INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }
        return INSTANCE;
    }

}
