package ca.tristan.easycommands.commands.music;

import ca.tristan.easycommands.commands.EasyCommands;
import ca.tristan.easycommands.commands.slash.SlashExecutor;
import ca.tristan.easycommands.commands.EventData;
import ca.tristan.easycommands.embeds.MusicEB;
import ca.tristan.easycommands.lavaplayer.GuildMusicManager;
import ca.tristan.easycommands.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    public List<Channel> getAuthorizedChannels(JDA jda) {
        List<Channel> channels = new ArrayList<>();
        EasyCommands.getMusicChannels().forEach((guild, channel) -> {
            channels.add(channel);
        });
        return channels;
    }

    @Override
    public void execute(EventData data) {
        final Member self = data.getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        MusicEB musicEB = new MusicEB();

        if(selfVoiceState == null || !selfVoiceState.inAudioChannel()){
            musicEB.getBuilder().setDescription("I need to be in a voice channel for this to work.");
            data.reply(musicEB.getBuilder().build(), true).queue();
            return;
        }

        final GuildVoiceState memberVoiceState = data.getMemberVoiceState();

        if(!memberVoiceState.inAudioChannel()){
            musicEB.getBuilder().setDescription("You need to be in a voice channel for this command to work.");
            data.reply(musicEB.getBuilder().build(), true).queue();
            return;
        }

        if(!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())){
            musicEB.getBuilder().setDescription("You need to be in the same voice channel as me for this to work.");
            data.reply(musicEB.getBuilder().build(), true).queue();
            return;
        }

        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(data.getGuild());
        final AudioPlayer audioPlayer = musicManager.audioPlayer;

        if(audioPlayer.getPlayingTrack() == null){
            musicEB.getBuilder().setDescription("There is no track playing currently.");
            data.reply(musicEB.getBuilder().build(), true).queue();
            return;
        }

        musicManager.scheduler.nextTrack();

        musicEB.getBuilder().setDescription("Skipped the current music.");
        musicEB.getBuilder().addField("Now playing", musicManager.audioPlayer.getPlayingTrack().getInfo().title, false);
        musicEB.getBuilder().addField("By", musicManager.audioPlayer.getPlayingTrack().getInfo().author, false);

        data.reply(musicEB.getBuilder().build(), false).setActionRow(musicEB.getActionRow()).queue();
    }

}
