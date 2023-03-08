package ca.tristan.easycommands.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {

    private final String host;
    private final int port;
    private final String database;
    private final String user;
    private final String password;
    private Connection connection;

    public MySQL(String host, int port, String database, String user, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getDatabase() {
        return database;
    }

    public Connection getConnection() {
        return connection;
    }

    public synchronized void connect() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql:://" + host + ":" + port + "/" + database + "?autoReconnect=true", user, password);
    }

    public synchronized void checkConnection(int timeout) throws SQLException {
        if(connection == null) {
            connect();
        }
        try {
            connection.isValid(timeout);
        } catch (SQLException e) {

        }
    }

}
