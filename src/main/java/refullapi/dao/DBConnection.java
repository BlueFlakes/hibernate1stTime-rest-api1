package refullapi.dao;

import refullapi.exceptions.DAOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBConnection {
    private static Connection connection;

    private DBConnection() {}

    public static Connection getConnection() throws DAOException {
        if (connection == null) {
            synchronized (DBConnection.class) {
                if (connection == null) {
                    connectWithDatabase();
                }
            }
        }

        return connection;
    }

    private static void connectWithDatabase() throws DAOException {
        try {
            String controllerType = "jdbc:postgresql";
            String address = "localhost";
            String port = "5432";
            String databaseName = "canteen__database";

            String userLogin = "read_write_access";
            String userPasswd = "read_write";
            String url = String.format("%s://%s:%s/%s", controllerType, address, port, databaseName);

            connection = DriverManager.getConnection(url, userLogin, userPasswd);
        } catch (SQLException e) {
            throw new DAOException("Could not connect with database.");
        }
    }
}