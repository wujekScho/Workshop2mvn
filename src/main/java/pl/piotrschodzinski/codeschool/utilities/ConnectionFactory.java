package pl.piotrschodzinski.codeschool.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    public static Connection getConnection(String dbName, String username, String pass) throws SQLException {
        String connection = "jdbc:mysql://localhost:3306/" + dbName + "?useSSL=false&serverTimezone=UTC";
        return DriverManager.getConnection(connection, username, pass);
    }
}
