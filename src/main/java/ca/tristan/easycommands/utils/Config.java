package ca.tristan.easycommands.utils;

import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Properties;

public class Config {


    private final File file;
    private Properties properties;

    public Config() throws IOException {
        this.file = new File("config.properties");
        if(!file.exists()) {
            makeConfig();
            Logger.log(LogType.WARNING, "No config.properties file found. Created one. Please provide the token and start again.");
            System.exit(-1);
        }
        loadConfig();
    }


    public void loadConfig() throws IOException {
        properties = new Properties();
        properties.load(new FileInputStream(file));
        Arrays.stream(ConfigOptions.values()).forEach(this::initConfigOptionValue);
    }

    private void initConfigOptionValue(ConfigOptions option){
        String value = properties.getProperty(option.name().toLowerCase());
        if(value == null){
            value = "";
        }
        option.setValue(value);
    }

    private void makeConfig() throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("config.properties"), StandardCharsets.UTF_8));
        writer.write("# IF YOU DON'T WANT TO USE A FEATURE JUST LEAVE THE CONFIG LINES VALUE **EMPTY OR REMOVE** THEM AND THEY WILL GET IGNORED.\n");
        writer.write("token=\n\n");
        writer.write("### Database Settings ###\n\n");
        writer.write("use_mysql=false\n");
        writer.write("# db_host=\n");
        writer.write("# db_port=\n");
        writer.write("# db_name=\n");
        writer.write("# db_user=\n");
        writer.write("# db_password=\n\n");
        writer.write("### EasyCommands Settings ###\n\n");
        writer.write("use_music_bot=true\n");
        writer.write("use_prefixcommands=true\n\n");
        writer.write("### On join auto roles. (leave empty if not used) ###\n\n");
        writer.write("# member_role_id=\n");
        writer.write("# bot_role_id=\n\n");
        writer.write("# Enables log to channel feature.\n\n");
        writer.write("# log_channel_id=\n");
        writer.write("# Music bot config.\n");
        writer.write("music_embed_color=255:255:255\n");
        writer.close();
    }

    public String getToken() {
        return ConfigOptions.TOKEN.getValue();
    }

    public boolean getUseMysql() {
        return Boolean.parseBoolean(ConfigOptions.USE_MYSQL.getValue().isBlank() ? "false" : ConfigOptions.USE_MYSQL.getValue());
    }

    public String getDB_Host() {
        return ConfigOptions.DB_HOST.getValue();
    }

    public int getDB_Port() {
        int port = 0;
        String value = ConfigOptions.DB_PORT.getValue();
        try{
            port = Integer.parseInt(value);
        } catch (NumberFormatException e){
            if(getUseMysql()){
                Logger.log(LogType.ERROR, "Invalid or no port number for MySQL set in config.properties file.");
                System.exit(-1);
            }
        }
        return port;
    }

    public String getDB_Database() {
        return ConfigOptions.DB_NAME.getValue();
    }

    public String getDB_User() {
        return ConfigOptions.DB_USER.getValue();
    }

    public String getDB_Password() {
        return ConfigOptions.DB_PASSWORD.getValue();
    }

    public boolean getUseMusicBot() {
        String value = ConfigOptions.USE_MUSIC_BOT.getValue();
        return Boolean.parseBoolean(value.isBlank() ? "false" : value);
    }

    public boolean getUsePrefixCommands() {
        String value = ConfigOptions.USE_PREFIXCOMMANDS.getValue();
        return Boolean.parseBoolean(value.isBlank() ? "false" : value);
    }

    public String getMemberRoleID() {
        return ConfigOptions.MEMBER_ROLE_ID.getValue();
    }

    public String getBotRoleID() {
        return ConfigOptions.BOT_ROLE_ID.getValue();
    }

    public String getLogChannelID() {
        return ConfigOptions.LOG_CHANNEL_ID.getValue();
    }

    public Color getMusicEmbedColor() {
        Color color = null;
        if(ConfigOptions.MUSIC_EMBED_COLOR.getValue().isBlank()){
            ConfigOptions.MUSIC_EMBED_COLOR.setValue("255:255:255");
        }
        String[] rgbStrings = ConfigOptions.MUSIC_EMBED_COLOR.getValue().split(":");
        try {
            int r = Integer.parseInt(rgbStrings[0]);
            int g = Integer.parseInt(rgbStrings[1]);
            int b = Integer.parseInt(rgbStrings[2]);
            color = new Color(r,g,b);
        } catch (NumberFormatException e){
            Logger.log(LogType.ERROR, "Invalid or value for Music Embed Color in config.properties file provided. Use 255:0:0 e.g.");
            System.exit(-1);
        }
        return color;
    }

}
