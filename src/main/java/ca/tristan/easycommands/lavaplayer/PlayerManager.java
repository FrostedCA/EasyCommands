package ca.tristan.easycommands.lavaplayer;

import ca.tristan.easycommands.commands.EventData;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.awt.*;
import java.util.Calendar;
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
        data.deferReply().queue();
        this.audioPlayerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                musicManager.scheduler.queue(audioTrack);

                EmbedBuilder builder = new EmbedBuilder();
                builder.setTitle("Music");
                builder.setDescription("A new music has been added to queue.");
                builder.addField("Music:", audioTrack.getInfo().title, false);
                builder.addField("Author:", audioTrack.getInfo().author, false);
                builder.addField("Added by:", data.getCommandSender().getAsMention(), false);
                builder.setFooter("This music bot was made with EasyCommands.", data.getGuild().getIconUrl());
                builder.setColor(new Color(95, 86, 188));

                data.getEvent().getHook().sendMessageEmbeds(builder.build()).queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                final List<AudioTrack> tracks = audioPlaylist.getTracks();
                if(!tracks.isEmpty()){
                    musicManager.scheduler.queue(tracks.get(0));
                    EmbedBuilder builder = new EmbedBuilder();
                    builder.setTitle("Music");
                    builder.setDescription("A new music has been added to queue.");
                    builder.addField("Music:", tracks.get(0).getInfo().title, false);
                    builder.addField("Author:", tracks.get(0).getInfo().author, false);
                    builder.addField("Added by:", data.getCommandSender().getAsMention(), false);
                    builder.setFooter("This music bot was made with EasyCommands.", data.getGuild().getIconUrl());
                    builder.setColor(new Color(95, 86, 188));

                    data.getEvent().getHook().sendMessageEmbeds(builder.build()).queue();
                }
            }

            @Override
            public void noMatches() {

            }

            @Override
            public void loadFailed(FriendlyException e) {

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
