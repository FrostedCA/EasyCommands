package ca.tristan.easycommands.commands.music;

import ca.tristan.easycommands.commands.CommandExecutor;
import ca.tristan.easycommands.commands.EventData;
import ca.tristan.easycommands.lavaplayer.PlayerManager;

import java.util.Objects;

public class StopCmd extends CommandExecutor {

    @Override
    public String getName() {
        return "stop";
    }

    @Override
    public String getDescription() {
        return "Makes the bot leave your channel and clears the current music queue.";
    }

    @Override
    public void execute(EventData data) {
        data.deferReply();
        if(!data.getMemberVoiceState().inAudioChannel()){
            data.reply("You need to be in a voice channel for this command to work.", true).queue();
            return;
        }

        if(!data.getSelfVoiceState().inAudioChannel()){
            data.reply("I need to be in a voice channel or I need to be playing music for this command to work.", true).queue();
            return;
        }

        if(Objects.equals(data.getMemberVoiceState().getChannel(), data.getSelfVoiceState().getChannel())){
            PlayerManager.getInstance().getMusicManager(data.getGuild()).scheduler.player.stopTrack();
            PlayerManager.getInstance().getMusicManager(data.getGuild()).scheduler.queue.clear();
            data.getGuild().getAudioManager().closeAudioConnection();
            data.getHook().sendMessage("The player has been stopped and the queue has been cleared.").queue();
        }
    }

}
