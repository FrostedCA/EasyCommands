package ca.tristan.easycommands.guild;

import ca.tristan.easycommands.EasyCommands;
import ca.tristan.easycommands.utils.LogType;
import ca.tristan.easycommands.utils.Logger;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Properties;

public class ECGuild {

    public final String id;
    public Properties guildProperties;

    private File guildFile;
    private BufferedWriter writer;

    public ECGuild(String id) {
        this.id = id;

        if (EasyCommands.getConfig().getUseMySQL()) {
            fetchPropertiesFromMySQL();
        } else {
            this.guildFile = new File(EasyCommands.savedDir, id + ".properties");
            fetchPropertiesFromLocal();
        }

    }

    public void fetchPropertiesFromMySQL() {
        guildProperties = new Properties();

        String query = "SELECT * FROM guildproperties WHERE guildId = ?";
        String query2 = "INSERT guildproperties VALUE (?, ?, ?, ?, ?)";

        try {
            PreparedStatement ps = EasyCommands.getMySQL().getConnection().prepareStatement(query);
            ps.setString(1, id);

            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                String member_role = rs.getString("member_role");
                String bot_role = rs.getString("bot_role");
                String music_channel = rs.getString("music_channel");
                String log_channel = rs.getString("log_channel");

                guildProperties.put("MEMBER_ROLE", member_role != null ? member_role : "");
                guildProperties.put("BOT_ROLE", bot_role != null ? bot_role : "");
                guildProperties.put("MUSIC_CHANNEL", music_channel != null ? music_channel : "");
                guildProperties.put("LOG_CHANNEL", log_channel != null ? log_channel : "");

                Logger.log(LogType.OK, "Loaded properties for guild: " + id + "; from MySQL");
                return;
            }

            PreparedStatement ps2 = EasyCommands.getMySQL().getConnection().prepareStatement(query2);
            ps2.setString(1, id);
            ps2.setString(2, "");
            ps2.setString(3, "");
            ps2.setString(4, "");
            ps2.setString(5, "");

            if (ps2.executeUpdate() > 0) {
                Logger.log(LogType.OK, "Created new properties for guild: " + id + "; from MySQL");
                guildProperties.setProperty("MEMBER_ROLE", "");
                guildProperties.setProperty("BOT_ROLE", "");
                guildProperties.setProperty("MUSIC_ROLE", "");
                guildProperties.setProperty("LOG_CHANNEL", "");
            } else {
                Logger.log(LogType.ERROR, "Couldn't created new properties for guild: " + id + "; from MySQL");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void fetchPropertiesFromLocal() {
        guildProperties = new Properties();

        try {
            guildProperties.load(new FileInputStream(guildFile));
            Logger.log(LogType.OK, "Loaded properties for guild: " + id);
        } catch (IOException e) {
            Logger.log(LogType.WARNING, "Guild properties for guild: " + id + " couldn't be found. Creating properties for guild...");
            savePropertiesLocal();
        }
    }

    public void saveProperties() {
        if (EasyCommands.getConfig().getUseMySQL()) {
            savePropertiesMySQL();
        } else {
            savePropertiesLocal();
        }
    }

    private void savePropertiesMySQL() {
        try {
            String query = "UPDATE guildproperties SET bot_role = ?, log_channel = ?, member_role = ?, music_channel = ? where guildId = ?";

            PreparedStatement ps = EasyCommands.getMySQL().getConnection().prepareStatement(query);
            ps.setString(1, guildProperties.getProperty(GuildOptions.BOT_ROLE.name()));
            ps.setString(2, guildProperties.getProperty(GuildOptions.LOG_CHANNEL.name()));
            ps.setString(3, guildProperties.getProperty(GuildOptions.MEMBER_ROLE.name()));
            ps.setString(4, guildProperties.getProperty(GuildOptions.MUSIC_CHANNEL.name()));
            ps.setString(5, id);

            if (ps.executeUpdate() > 0) {
                Logger.log(LogType.OK, "Guild properties successfully saved with mysql.");
            } else {
                Logger.log(LogType.ERROR, "Couldn't save guild properties with mysql.");
            }
        } catch (SQLException e) {
            Logger.log(LogType.ERROR, "Couldn't save guild properties with mysql.");
        }
    }

    private void savePropertiesLocal() {
        try {
            if (!guildFile.exists()) {
                guildFile.createNewFile();
                Logger.log(LogType.OK, "Properties file created for guild: " + id);
            }
            writer = new BufferedWriter(new FileWriter(guildFile));
            Arrays.stream(GuildOptions.values()).forEach(guildOptions -> {
                if (guildProperties.containsKey(guildOptions.name())) {
                    writeProperty(guildOptions.name(), guildProperties.getProperty(guildOptions.name()));
                } else {
                    writeProperty(guildOptions.name(), guildOptions.getValue());
                }
            });
            writer.flush();
            writer.close();
            Logger.log(LogType.OK, "Saved properties for guild: " + id);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeProperty(String key, String value) {
        if (writer == null) {
            return;
        }

        String line = ((value.isEmpty() || value.isBlank()) ? "#" : "") + key + "=" + value;
        try {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            Logger.log(LogType.ERROR, "Couldn't write property.");
        }
    }
}
