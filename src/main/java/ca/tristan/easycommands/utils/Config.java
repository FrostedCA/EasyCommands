package ca.tristan.easycommands.utils;

import com.mysql.cj.log.Log;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Config {

    private final File file;

    private Map<ConfigSettings, String> settingsValues = new HashMap<>();

    public Config() throws IOException {
        this.file = new File("config.ini");
        if(!file.exists()) {
            makeConfig();
            System.exit(0);
        }
        loadConfig();
    }

    public void makeConfig() throws IOException {
        if(!file.createNewFile()) {
            Logger.log(LogType.ERROR, "Unexpected error while trying to create the Config file...");
            return;
        }
        PrintWriter bufferedWriter = new PrintWriter(new FileWriter(this.file));
        bufferedWriter.println("# IF YOU DON'T WANT TO USE A FEATURE JUST LEAVE THE CONFIG LINES VALUE **EMPTY OR REMOVE** THEM AND THEY WILL GET IGNORED.");
        bufferedWriter.println(ConfigSettings.TOKEN.label);
        bufferedWriter.println("# Database Settings");
        bufferedWriter.println(ConfigSettings.USE_MYSQL.label);
        bufferedWriter.println(ConfigSettings.DB_HOST.label);
        bufferedWriter.println(ConfigSettings.DB_PORT.label);
        bufferedWriter.println(ConfigSettings.DB_NAME.label);
        bufferedWriter.println(ConfigSettings.DB_USER.label);
        bufferedWriter.println(ConfigSettings.DB_PASSWORD.label);
        bufferedWriter.println("# EasyCommands Settings");
        bufferedWriter.println(ConfigSettings.USE_MUSIC_BOT.label);
        bufferedWriter.println(ConfigSettings.USE_PREFIXCOMMANDS.label);
        bufferedWriter.println("# On join auto roles. (leave empty if not used)");
        bufferedWriter.println(ConfigSettings.MEMBER_ROLE_ID.label);
        bufferedWriter.println(ConfigSettings.BOT_ROLE_ID.label);
        bufferedWriter.println("# Enables log to channel feature.");
        bufferedWriter.println(ConfigSettings.LOG_CHANNEL_ID.label);
        bufferedWriter.flush();
        bufferedWriter.close();
        Logger.log(LogType.WARNING, "Launch stopped! Config file was not found so I created one. You can now configure the new created 'config.ini' file. | See https://github.com/FrostedCA/EasyCommands/blob/master/src/main/java/ca/tristan/easycommands/utils/Config.java for more settings.");
    }

    public void loadConfig() throws IOException {
        String line;
        BufferedReader bufferedReader = new BufferedReader(new FileReader(this.file));
        while((line = bufferedReader.readLine()) != null) {
            String[] settings = line.split("=");
            if(!settings[0].startsWith("#")) {
                ConfigSettings configSettings = ConfigSettings.valueOf(settings[0].toUpperCase());
                settingsValues.put(configSettings, settings.length == 2 ? settings[1] : "");
                switch (configSettings) {
                    case TOKEN:
                        String token = settingsValues.get(ConfigSettings.TOKEN);
                        if(token.isEmpty() || token.isBlank()){
                            Logger.log(LogType.ERROR, "Empty token in config file.");
                            System.exit(1);
                        }
                        break;
                    case USE_MYSQL:
                    case DB_HOST:
                    case DB_PORT:
                    case DB_NAME:
                    case DB_USER:
                    case DB_PASSWORD:
                    case USE_MUSIC_BOT:
                    case USE_PREFIXCOMMANDS:
                    case MEMBER_ROLE_ID:
                    case BOT_ROLE_ID:
                    case LOG_CHANNEL_ID:
                }
            }
        }
        Logger.log(LogType.OK, "Config loaded.");
    }

    public String getToken() {
        return settingsValues.get(ConfigSettings.TOKEN);
    }

    public boolean getUseMysql() {
        return Boolean.parseBoolean(settingsValues.get(ConfigSettings.USE_MYSQL).isBlank() ? "false" : settingsValues.get(ConfigSettings.USE_MYSQL));
    }

    public String getDB_Host() {
        return settingsValues.get(ConfigSettings.DB_HOST);
    }

    public int getDB_Port() {
        int port = Integer.parseInt(settingsValues.get(ConfigSettings.DB_PORT).isBlank() ? "9999999" : settingsValues.get(ConfigSettings.DB_PORT));
        if(getUseMysql()){
            if(port == 9999999) {
                Logger.log(LogType.ERROR, "Empty port number for MySQL. If you don't want to use MySQL make sure to set `use_mysql=false` in the config file.");
                System.exit(1);
            }
        }
        return Integer.parseInt(settingsValues.get(ConfigSettings.DB_PORT).isBlank() ? "0" : settingsValues.get(ConfigSettings.DB_PORT));
    }

    public String getDB_Database() {
        return settingsValues.get(ConfigSettings.DB_NAME);
    }

    public String getDB_User() {
        return settingsValues.get(ConfigSettings.DB_USER);
    }

    public String getDB_Password() {
        return settingsValues.get(ConfigSettings.DB_PASSWORD);
    }

    public boolean getUseMusicBot() {
        return Boolean.parseBoolean(settingsValues.get(ConfigSettings.USE_MUSIC_BOT).isBlank() ? "false" : settingsValues.get(ConfigSettings.USE_MUSIC_BOT));
    }

    public boolean getUsePrefixCommands() {
        return Boolean.parseBoolean(settingsValues.get(ConfigSettings.USE_PREFIXCOMMANDS).isBlank() ? "false" : settingsValues.get(ConfigSettings.USE_PREFIXCOMMANDS));
    }

    public String getMemberRoleID() {
        String id = settingsValues.get(ConfigSettings.MEMBER_ROLE_ID);
        if(id.isBlank()) {
            Logger.log(LogType.WARNING, "No auto Member Role ID in the config was detected. Ignore if disable.");
        }
        return id;
    }

    public String getBotRoleID() {
        String id = settingsValues.get(ConfigSettings.BOT_ROLE_ID);
        if(id.isBlank()) {
            Logger.log(LogType.WARNING, "No auto Bot Role ID in the config was detected. Ignore if disable.");
        }
        return id;
    }

    public String getLogChannelID() {
        String id = settingsValues.get(ConfigSettings.LOG_CHANNEL_ID);
        if(id.isBlank()) {
            Logger.log(LogType.WARNING, "No log channel ID in the config was detected. Ignore if normal, note: You will only receive logs in bot console.");
        }

        return id;
    }
}
