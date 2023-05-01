package ca.tristan.easycommands.commands.defaults;

import ca.tristan.easycommands.EasyCommands;
import ca.tristan.easycommands.commands.EventData;
import ca.tristan.easycommands.commands.slash.SlashExecutor;
import ca.tristan.easycommands.database.MySQL;
import ca.tristan.easycommands.embeds.MusicEB;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SetMusicChannelCmd extends SlashExecutor {

    @Override
    public String getName() {
        return "setmusic";
    }

    @Override
    public String getDescription() {
        return "Updates the music channel for the current guild to the current channel.";
    }

    @Override
    public boolean isOwnerOnly() {
        return true;
    }

    @Override
    public void execute(EventData data, MySQL mySQL) {

        try {
            PreparedStatement ps = mySQL.getConnection().prepareStatement("UPDATE guilds set music_channel = ? WHERE guild_id = ?");
            ps.setString(1, data.getChannel().getId());
            ps.setString(2, data.getGuild().getId());
            ps.executeUpdate();

            MusicEB musicEB = new MusicEB();
            musicEB.getBuilder().setDescription("The music channel for the guild '**" + data.getGuild().getName() + "**' has been updated successfully.");
            data.reply(musicEB.getBuilder().build(), true).queue();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
