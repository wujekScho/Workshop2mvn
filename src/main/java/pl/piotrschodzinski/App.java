package pl.piotrschodzinski;

import pl.piotrschodzinski.CodeSchool.ConnectionFactory;
import pl.piotrschodzinski.CodeSchool.Model.User;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        try (Connection connection = ConnectionFactory.getConnection("code_school", "root", "root")) {
            User.printUsers(User.loadAll(connection));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
