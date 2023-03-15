package ca.tristan.easycommands.utils;

import java.io.*;

public class Config {

    private final File file;

    private String token, dbHost, dbName, dbUser, dbPassword;
    private int dbPort;
    private boolean musicBot, prefixCommands, mysql;

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
        bufferedWriter.flush();
        bufferedWriter.close();
        Logger.log(LogType.WARNING, "Launch stopped! Config file was not found so I created one. Now you can configure the new created 'config.ini' file. | See Config.class for more settings.");
    }

    public void loadConfig() throws IOException {
        String line;
        BufferedReader bufferedReader = new BufferedReader(new FileReader(this.file));
        while((line = bufferedReader.readLine()) != null) {
            String[] settings = line.split("=");
            switch (settings[0]) {
                case "token" -> token = settings[1];
                case "use_mysql" -> mysql = Boolean.parseBoolean(settings[1]);
                case "db_host" -> dbHost = settings[1];
                case "db_port" -> dbPort = Integer.parseInt(settings[1]);
                case "db_name" -> dbName = settings[1];
                case "db_user" -> dbUser = settings[1];
                case "db_password" -> dbPassword = settings[1];
                case "use_music_bot" -> musicBot = Boolean.parseBoolean(settings[1]);
                case "use_prefixcommands" -> prefixCommands = Boolean.parseBoolean(settings[1]);
            }
        }
        Logger.log(LogType.OK, "Config loaded.");
    }

    public String getToken() {
        return token;
    }

    public boolean getUseMysql() {
        return mysql;
    }

    public String getDB_Host() {
        return dbHost;
    }

    public int getDB_Port() {
        return dbPort;
    }

    public String getDB_Database() {
        return dbName;
    }

    public String getDB_User() {
        return dbUser;
    }

    public String getDB_Password() {
        return dbPassword;
    }

    public boolean getUseMusicBot() {
        return musicBot;
    }

    public boolean getUsePrefixCommands() {
        return prefixCommands;
    }

}
