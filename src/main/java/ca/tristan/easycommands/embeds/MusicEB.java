package ca.tristan.easycommands.embeds;

import ca.tristan.easycommands.EasyCommands;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.util.List;

public class MusicEB {

    private EmbedBuilder builder;
    private List<Button> actionRow;

    public MusicEB() {
        builder = new EmbedBuilder();
        builder.setTitle("Music Player");
        builder.setColor(EasyCommands.getConfig().getMusicEmbedColor());
        builder.setFooter("This music bot was created using EasyCommands.", "https://raw.githubusercontent.com/FrostedCA/EasyCommands/master/ECLogo_new.png");
        this.actionRow = List.of(
                Button.link("https://github.com/FrostedCA/EasyCommands", "EasyCommands").withEmoji(Emoji.fromUnicode("✨")),
                Button.primary("pause", "Pause").withEmoji(Emoji.fromUnicode("⏸")),
                Button.primary("skip", "Skip").withEmoji(Emoji.fromUnicode("⏭")),
                Button.primary("lyrics", "Lyrics").withEmoji(Emoji.fromUnicode("📄")),
                Button.danger("stop", "Stop").withEmoji(Emoji.fromUnicode("⏹"))
        );
    }

    public EmbedBuilder getBuilder() {
        return builder;
    }

    public List<Button> getActionRow() {
        return actionRow;
    }
}
