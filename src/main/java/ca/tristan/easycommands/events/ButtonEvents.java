package ca.tristan.easycommands.events;

import ca.tristan.easycommands.embeds.MusicEB;
import ca.tristan.easycommands.lavaplayer.GuildMusicManager;
import ca.tristan.easycommands.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import core.GLA;
import genius.SongSearch;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.io.IOException;
import java.util.Objects;

public class ButtonEvents extends ListenerAdapter {

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if(event.getComponentId().equals("pause")) {
            GuildMusicManager guildMusicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());

            if(guildMusicManager.scheduler.player.getPlayingTrack() == null) {
                MusicEB musicEB = new MusicEB();
                musicEB.getBuilder().setDescription("There is no music currently playing.");
                event.replyEmbeds(musicEB.getBuilder().build()).setActionRow(
                        Button.link("https://github.com/FrostedCA/EasyCommands", "EasyCommands").withEmoji(Emoji.fromUnicode("✨"))
                ).queue();
                return;
            }

            guildMusicManager.scheduler.player.setPaused(!guildMusicManager.scheduler.player.isPaused());
            MusicEB musicEB = new MusicEB();
            musicEB.getBuilder().addField("Current Music", guildMusicManager.scheduler.player.getPlayingTrack().getInfo().title, false);
            if(guildMusicManager.scheduler.player.isPaused()) {
                musicEB.getBuilder().setDescription("Current music has been paused.");
                musicEB.getBuilder().addField("Paused by", event.getMember().getAsMention(), false);
            }else {
                musicEB.getBuilder().setDescription("Current music has been unpause.");
                musicEB.getBuilder().addField("Unpause by", event.getMember().getAsMention(), false);
            }
            event.replyEmbeds(musicEB.getBuilder().build()).setActionRow(musicEB.getActionRow()).queue();
        } else if (event.getComponentId().equals("stop")) {
            MusicEB musicEB = new MusicEB();
            if(!event.getMember().getVoiceState().inAudioChannel()){
                musicEB.getBuilder().setDescription("You need to be in a voice channel for this command to work.");
                event.replyEmbeds(musicEB.getBuilder().build()).setEphemeral(true).queue();
                return;
            }

            if(!event.getGuild().getSelfMember().getVoiceState().inAudioChannel()){
                musicEB.getBuilder().setDescription("I need to be in a voice channel or I need to be playing music for this command to work.");
                event.replyEmbeds(musicEB.getBuilder().build()).setEphemeral(true).queue();
                return;
            }

            if(Objects.equals(event.getMember().getVoiceState().getChannel(), event.getGuild().getSelfMember().getVoiceState().getChannel())){
                PlayerManager.getInstance().getMusicManager(event.getGuild()).scheduler.player.stopTrack();
                PlayerManager.getInstance().getMusicManager(event.getGuild()).scheduler.queue.clear();
                event.getGuild().getAudioManager().closeAudioConnection();
                musicEB.getBuilder().setDescription("The player has been stopped and the queue has been cleared.");
                musicEB.getBuilder().addField("Stopped by", event.getMember().getAsMention(), false);
                event.replyEmbeds(musicEB.getBuilder().build()).setActionRow(
                        Button.link("https://github.com/FrostedCA/EasyCommands", "EasyCommands").withEmoji(Emoji.fromUnicode("✨"))
                ).queue();
            }
        } else if (event.getComponentId().equals("skip")) {
            final Member self = event.getGuild().getSelfMember();
            final GuildVoiceState selfVoiceState = self.getVoiceState();

            MusicEB musicEB = new MusicEB();

            if(selfVoiceState == null || !selfVoiceState.inAudioChannel()){
                musicEB.getBuilder().setDescription("I need to be in a voice channel for this to work.");
                event.replyEmbeds(musicEB.getBuilder().build()).setEphemeral(true).queue();
                return;
            }

            final GuildVoiceState memberVoiceState = event.getMember().getVoiceState();

            if(!memberVoiceState.inAudioChannel()){
                musicEB.getBuilder().setDescription("You need to be in a voice channel for this command to work.");
                event.replyEmbeds(musicEB.getBuilder().build()).setEphemeral(true).queue();
                return;
            }

            if(!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())){
                musicEB.getBuilder().setDescription("You need to be in the same voice channel as me for this to work.");
                event.replyEmbeds(musicEB.getBuilder().build()).setEphemeral(true).queue();
                return;
            }

            final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
            final AudioPlayer audioPlayer = musicManager.audioPlayer;

            if(audioPlayer.getPlayingTrack() == null){
                musicEB.getBuilder().setDescription("There is no track playing currently.");
                event.replyEmbeds(musicEB.getBuilder().build()).setEphemeral(true).queue();
                return;
            }

            musicManager.scheduler.nextTrack();

            musicEB.getBuilder().setDescription("Skipped the current music.");
            musicEB.getBuilder().addField("Now playing", musicManager.audioPlayer.getPlayingTrack().getInfo().title, false);
            musicEB.getBuilder().addField("By", musicManager.audioPlayer.getPlayingTrack().getInfo().author, false);

            event.replyEmbeds(musicEB.getBuilder().build()).setActionRow(musicEB.getActionRow()).queue();
        } else if (event.getComponentId().equals("lyrics")) {
            final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
            final AudioPlayer audioPlayer = musicManager.audioPlayer;

            MusicEB musicEB = new MusicEB();

            if(audioPlayer.getPlayingTrack() == null){
                musicEB.getBuilder().setDescription("There is no music currently playing.");
                event.replyEmbeds(musicEB.getBuilder().build()).setEphemeral(true).queue();
                return;
            }

            String title = audioPlayer.getPlayingTrack().getInfo().title;

            GLA gla = new GLA();
            try {

                SongSearch search = gla.search(title.toLowerCase().replaceAll("official", "").replaceAll("music", "").replaceAll("video", "").replaceAll("audio", ""));

                String url = search.getHits().isEmpty() ? "" : search.getHits().getFirst().getUrl();
                if(url.isBlank()){
                    musicEB.getBuilder().setDescription("Sorry, I haven't found any lyrics for that song.");
                    event.replyEmbeds(musicEB.getBuilder().build()).setEphemeral(true).queue();
                    return;
                }
                musicEB.getBuilder().setDescription("Here's what I've found: " + url);
                event.replyEmbeds(musicEB.getBuilder().build()).setEphemeral(true).queue();
            } catch (IOException e) {
                musicEB.getBuilder().setDescription("Sorry, I haven't found any lyrics for that song.");
                event.replyEmbeds(musicEB.getBuilder().build()).setEphemeral(true).queue();
                e.printStackTrace();
            }
        }
    }
}
