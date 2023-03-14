package ca.tristan.easycommands.utils;

import java.io.*;

public class Config {

    private final File file;
    private BufferedReader bufferedReader;

    private String token, dbHost, dbName, dbUser, dbPassword;
    private int dbPort;
    private boolean musicBot, prefixCommands, mysql;

    public Config() throws IOException {
        this.file = new File("config.ini");
        if(!file.exists()) {
            file.createNewFile();
            Logger.log(LogType.WARNING, "Launch stopped! Config file was not found so I created one. Now add your bot token to the config file. ex: token=tokenhere | See Config.class for more settings.");
            System.exit(0);
        }
        loadConfig();
    }

    public void loadConfig() throws IOException {
        String line;
        bufferedReader = new BufferedReader(new FileReader(this.file));
        while((line = bufferedReader.readLine()) != null) {
            switch (line.split("=")[0]) {
                case "token":
                    token = line.split("=")[1];
                    break;
                case "use_mysql":
                    mysql = Boolean.parseBoolean(line.split("=")[1]);
                    break;
                case "db_host":
                    dbHost = line.split("=")[1];
                    break;
                case "db_port":
                    dbPort = Integer.parseInt(line.split("=")[1]);
                    break;
                case "db_name":
                    dbName = line.split("=")[1];
                    break;
                case "db_user":
                    dbUser = line.split("=")[1];
                    break;
                case "db_password":
                    dbPassword = line.split("=")[1];
                    break;
                case "use_music_bot":
                    musicBot = Boolean.parseBoolean(line.split("=")[1]);
                    break;
                case "use_prefixcommands":
                    prefixCommands = Boolean.parseBoolean(line.split("=")[1]);
                    break;
            }
        }
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
