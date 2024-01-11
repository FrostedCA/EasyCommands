package ca.tristan.easycommands.guild;

import ca.tristan.easycommands.EasyCommands;
import ca.tristan.easycommands.utils.LogType;
import ca.tristan.easycommands.utils.Logger;

import java.io.*;
import java.util.Arrays;
import java.util.Properties;

public class ECGuild {

    public final String id;
    public Properties guildProperties;

    private final File guildFile;
    private BufferedWriter writer;

    public ECGuild(String id) {
        this.id = id;
        this.guildFile = new File(EasyCommands.savedDir, id + ".properties");

        fetchProperties();
    }

    public void fetchProperties() {
        guildProperties = new Properties();

        if (EasyCommands.getConfig().getUseMySQL()) {
            Logger.log(LogType.ERROR, "Couldn't load guild properties from MySQL; MySQL isn't currently supported.");
        } else {
            try {
                guildProperties.load(new FileInputStream(guildFile));
                Logger.log(LogType.OK, "Loaded properties for guild: " + id);
            } catch (IOException e) {
                Logger.log(LogType.WARNING, "Guild properties for guild: " + id + " couldn't be found. Creating properties for guild...");
                saveProperties();
            }
        }
    }

    public void saveProperties() {
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
