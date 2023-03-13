package ca.tristan.easycommands.commands.music;

import ca.tristan.easycommands.commands.EasyCommands;
import ca.tristan.easycommands.commands.EventData;
import ca.tristan.easycommands.commands.slash.SlashExecutor;
import ca.tristan.easycommands.embeds.MusicEB;
import ca.tristan.easycommands.lavaplayer.GuildMusicManager;
import ca.tristan.easycommands.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.ArrayList;
import java.util.List;

public class PauseCmd extends SlashExecutor {

    @Override
    public String getName() {
        return "pause";
    }

    @Override
    public List<String> getAliases() {
        List<String> aliases = new ArrayList<>();
        aliases.add("unpause");
        return aliases;
    }

    @Override
    public String getDescription() {
        return "Pauses the current music.";
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

        GuildMusicManager guildMusicManager = PlayerManager.getInstance().getMusicManager(data.getGuild());

        if(guildMusicManager.scheduler.player.getPlayingTrack() == null) {
            MusicEB musicEB = new MusicEB();
            musicEB.getBuilder().setDescription("There is no music currently playing.");
            data.reply(musicEB.getBuilder().build(), false).setActionRow(
                    Button.link("https://github.com/FrostedCA/EasyCommands", "EasyCommands").withEmoji(Emoji.fromUnicode("✨"))
            ).queue();
            return;
        }

        guildMusicManager.scheduler.player.setPaused(!guildMusicManager.scheduler.player.isPaused());
        MusicEB musicEB = new MusicEB();
        musicEB.getBuilder().addField("Current Music", guildMusicManager.scheduler.player.getPlayingTrack().getInfo().title, false);
        if(guildMusicManager.scheduler.player.isPaused()) {
            musicEB.getBuilder().setDescription("Current music has been paused.");
            musicEB.getBuilder().addField("Paused by", data.getCommandSender().getAsMention(), false);
        }else {
            musicEB.getBuilder().setDescription("Current music has been unpause.");
            musicEB.getBuilder().addField("Unpause by", data.getCommandSender().getAsMention(), false);
        }
        data.reply(musicEB.getBuilder().build(), false).setActionRow(musicEB.getActionRow()).queue();

    }

}
