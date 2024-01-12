package ca.tristan.easycommands.utils;

import java.awt.*;
import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;

public class ConfigFile {

    private final File configFile;
    private final Properties properties;

    BufferedWriter writer;

    HashMap<String, String> configProperties = new HashMap<>();

    public ConfigFile(File configDir) throws IOException {

        if(!configDir.exists()) {
            configDir.mkdirs();
        }

        this.configFile = new File(configDir, "config.properties");
        this.properties = new Properties();

        if (!configFile.exists() && configFile.createNewFile()) {
            writeDefaultConfig();
        }

        loadConfig();
        checkConfig();
        writeConfig();
    }

    private void writeDefaultConfig() throws IOException {
        writer = new BufferedWriter(new FileWriter(this.configFile));

        writeLine("### EasyCommands Config 2.0 | This config from now on will be updated automatically with each updates ###");

        Arrays.stream(ConfigOptions.values()).forEach(configOptions -> {
            try {
                writeProperty(configOptions.name(), configOptions.getValue());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        writer.flush();
        writer.close();
    }

    public void writeConfig() throws IOException {
        writer = new BufferedWriter(new FileWriter(this.configFile));
        writeLine("### EasyCommands Config 2.0 | This config will now be updated with each update, showing you the newest settings available and cleaning up the unused ones. Do NOT leave important comments here they will get removed. ###");
        Arrays.stream(ConfigOptions.values()).forEach(configOptions -> {
            if(configProperties.containsKey(configOptions.name())) {
                try {
                    writeProperty(configOptions.name(), configProperties.get(configOptions.name()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    writeProperty(configOptions.name(), configOptions.getValue());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        writer.flush();
        writer.close();
    }

    private void writeLine(String line) throws IOException {
        if (writer == null) {
            return;
        }
        writer.write(line);
        writer.newLine();
    }

    private void writeProperty(String key, String value) throws IOException {
        if (writer == null) {
            return;
        }

        String line = ((value.isEmpty() || value.isBlank()) ? "#" : "") + key + "=" + value;
        writer.write(line);
        writer.newLine();
    }

    private void loadConfig() throws IOException {
        configProperties.clear();
        properties.clear();

        properties.load(new FileInputStream(configFile));
        properties.forEach((key, value) -> {
            configProperties.put((String) key, (String) value);
        });
    }

    private void checkConfig() {
        Arrays.stream(ConfigOptions.values()).forEach(configOptions -> {
            if(!configProperties.containsKey(configOptions.name())) {
                configProperties.put(configOptions.name(), configOptions.getValue());
            }
        });
    }

    public String getToken() {
        return configProperties.get(ConfigOptions.TOKEN.name());
    }

    public boolean getUsePrefixCommands() {
        return Boolean.parseBoolean(configProperties.get(ConfigOptions.USE_PREFIXCOMMANDS.name()));
    }

    public boolean getUseMusicBot() {
        return Boolean.parseBoolean(configProperties.get(ConfigOptions.USE_MUSIC_BOT.name()));
    }

    public boolean getUseMySQL() {
        return Boolean.parseBoolean(configProperties.get(ConfigOptions.USE_MYSQL.name()));
    }

    public String getDBHost() {
        return configProperties.get(ConfigOptions.DB_HOST.name());
    }

    public int getDBPort() {
        return Integer.parseInt(configProperties.get(ConfigOptions.DB_PORT.name()));
    }

    public String getDBName() {
        return configProperties.get(ConfigOptions.DB_NAME.name());
    }

    public String getDBUser() {
        return configProperties.get(ConfigOptions.DB_USER.name());
    }

    public String getDBPassword() {
        return configProperties.get(ConfigOptions.DB_PASSWORD.name());
    }

    public Color getMusicEmbedColor() {
        Color color = null;
        String[] rgbStrings = configProperties.get(ConfigOptions.MUSIC_EMBED_COLOR.name()).split(":");
        try {
            int r = Integer.parseInt(rgbStrings[0]);
            int g = Integer.parseInt(rgbStrings[1]);
            int b = Integer.parseInt(rgbStrings[2]);
            color = new Color(r,g,b);
        } catch (NumberFormatException e){
            Logger.log(LogType.ERROR, "Invalid format or value for Music Embed Color in config.properties file provided. RGB format ex: '255:255:255'");
            color = new Color(255,255,255);
        }
        return color;
    }

}
