package pl.piotrschodzinski.CodeSchool;

import java.sql.Connection;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) {
        try (Connection connection = ConnectionFactory.getConnection("code_school", "root", "root")) {
            UserAdministration.menageUsers(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}

