package ca.tristan.easycommands.commands.music;

import ca.tristan.easycommands.EC;
import ca.tristan.easycommands.EasyCommands;
import ca.tristan.easycommands.commands.slash.SlashExecutor;
import ca.tristan.easycommands.commands.EventData;
import ca.tristan.easycommands.database.MySQL;
import ca.tristan.easycommands.embeds.MusicEB;
import ca.tristan.easycommands.lavaplayer.GuildMusicManager;
import ca.tristan.easycommands.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.Channel;

import java.util.ArrayList;
import java.util.List;

public class SkipCmd extends SlashExecutor {

    public final EasyCommands easyCommands;

    public SkipCmd(EasyCommands easyCommands) {
        this.easyCommands = easyCommands;
    }

    @Override
    public String getName() {
        return "skip";
    }

    @Override
    public String getDescription() {
        return "Skips the current track to the next one.";
    }

    @Override
    public void execute(EventData data, MySQL mySQL) {
        final Member self = data.getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        MusicEB musicEB = new MusicEB();

        if(!EC.canSendMusicCommand(data.getGuild().getId(), data.getChannel().getId())) {
            data.getEvent().reply("You can't use this command in this channel.").setEphemeral(true).queue();
            return;
        }

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
        musicEB.getBuilder().addField("Skipped by", data.getCommandSender().getAsMention(), false);

        data.reply(musicEB.getBuilder().build(), false).setActionRow(musicEB.getActionRow()).queue();
    }

}
