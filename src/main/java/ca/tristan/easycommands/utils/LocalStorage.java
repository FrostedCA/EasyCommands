package ca.tristan.easycommands.utils;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;

import java.io.*;
import java.util.Properties;

public class LocalStorage {

    BufferedWriter writer;
    BufferedReader reader;

    private final File savedDirectory;

    File[] saveFiles;

    public LocalStorage(File savedDir) {
        savedDirectory = savedDir;

        if(!savedDirectory.exists()) {
            savedDirectory.mkdirs();
        }
    }

    public void fetchGuildSaves(JDA jda) {
        saveFiles = new File[jda.getGuilds().size()];

        for(int i = 0; i < jda.getGuilds().size(); i++) {
            File guildFile = new File(savedDirectory, jda.getGuilds().get(i).getId() + ".properties");
            try {
                if (!guildFile.exists() && guildFile.createNewFile()) {
                    saveFiles[i] = guildFile;
                } else if (guildFile.exists()) {
                    saveFiles[i] = guildFile;
                }
            } catch (IOException e) {
                Logger.log(LogType.ERROR, "Couldn't create guild file.");
            }
        }
    }

    /**
     * Fetch the guild properties from local storage files. Provide a guild ID returns Java Properties;
     */
    public Properties fetchGuildProperties(String id) {
        File guildFile = new File(savedDirectory, id + ".properties");
        Properties properties = new Properties();

        if (!guildFile.exists()) {
            Logger.log(LogType.ERROR, "Guild properties file couldn't be found.");
            return null;
        }

        try {
            properties.load(new FileInputStream(guildFile));
        } catch (IOException e) {
            Logger.log(LogType.ERROR, "Couldn't fetch guild properties.");
        }
        return properties;
    }

    private void writeProperty(String key, String value, Guild guild) {
        /*if(writer == null) {
            Logger.log(LogType.ERROR, "Writer is null. Can't save to local storage.");
            return;
        }

        try {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            Logger.log(LogType.ERROR, "IOException Something went wrong while trying to write new line to local storage.");
        }*/
    }

    public File getSavedDirectory() {
        return savedDirectory;
    }

}
