package ca.tristan.easycommands.utils;

import java.io.*;

public class Config {

    private final File file;
    private final BufferedReader bufferedReader;

    public Config() throws IOException {
        this.file = new File("config.ini");
        if(!file.exists()) {
            file.createNewFile();
            Logger.log(LogType.WARNING, "Launch stopped! Config file was not found so I created one. Now add your bot token to the config file. ex: token=tokenhere | See Config.class for more settings.");
            System.exit(0);
        }
        bufferedReader = new BufferedReader(new FileReader(this.file));
    }

    public String getToken() throws IOException {
        String line;
        while((line = bufferedReader.readLine()) != null) {
            if(line.startsWith("token")) {
                String[] str = line.split("=");
                return str[1];
            }
        }
        Logger.log(LogType.WARNING, "Launch stopped! Bot Token was not found inside the Config file. ex: token=tokenhere | See Config.class for more settings.");
        System.exit(0);
        return null;
    }

    public String getDB_URL() throws IOException {
        String line;
        while((line = bufferedReader.readLine()) != null) {
            if(line.startsWith("db_url")) {
                String[] str = line.split("=");
                return str[1];
            }
        }
        return null;
    }

    public String getDB_User() throws IOException {
        String line;
        while((line = bufferedReader.readLine()) != null) {
            if(line.startsWith("db_user")) {
                String[] str = line.split("=");
                return str[1];
            }
        }
        return null;
    }

    public String getDB_Password() throws IOException {
        String line;
        while((line = bufferedReader.readLine()) != null) {
            if(line.startsWith("db_password")) {
                String[] str = line.split("=");
                return str[1];
            }
        }
        return null;
    }

}
