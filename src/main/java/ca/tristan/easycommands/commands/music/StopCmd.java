package ca.tristan.easycommands.commands.music;

import ca.tristan.easycommands.commands.EasyCommands;
import ca.tristan.easycommands.commands.slash.SlashExecutor;
import ca.tristan.easycommands.commands.EventData;
import ca.tristan.easycommands.embeds.MusicEB;
import ca.tristan.easycommands.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StopCmd extends SlashExecutor {

    @Override
    public String getName() {
        return "stop";
    }

    @Override
    public String getDescription() {
        return "Makes the bot leave your channel and clears the current music queue.";
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

        MusicEB musicEB = new MusicEB();
        if(!data.getMemberVoiceState().inAudioChannel()){
            musicEB.getBuilder().setDescription("You need to be in a voice channel for this command to work.");
            data.reply(musicEB.getBuilder().build(), true).queue();
            return;
        }

        if(!data.getSelfVoiceState().inAudioChannel()){
            musicEB.getBuilder().setDescription("I need to be in a voice channel or I need to be playing music for this command to work.");
            data.reply(musicEB.getBuilder().build(), true).queue();
            return;
        }

        if(Objects.equals(data.getMemberVoiceState().getChannel(), data.getSelfVoiceState().getChannel())){
            PlayerManager.getInstance().getMusicManager(data.getGuild()).scheduler.player.stopTrack();
            PlayerManager.getInstance().getMusicManager(data.getGuild()).scheduler.queue.clear();
            data.getGuild().getAudioManager().closeAudioConnection();
            musicEB.getBuilder().setDescription("The player has been stopped and the queue has been cleared.");
            musicEB.getBuilder().addField("Stopped by", data.getCommandSender().getAsMention(), false);
            data.reply(musicEB.getBuilder().build(), false).setActionRow(
                    Button.link("https://github.com/FrostedCA/EasyCommands", "EasyCommands").withEmoji(Emoji.fromUnicode("✨"))
            ).queue();
        }
    }

}
