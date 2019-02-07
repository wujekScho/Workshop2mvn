package pl.piotrschodzinski;

import pl.piotrschodzinski.CodeSchool.ConnectionFactory;
import pl.piotrschodzinski.CodeSchool.Model.Exercise;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        try (Connection connection = ConnectionFactory.getConnection("code_school", "root", "root")) {
//            Exercise exercise1 = new Exercise("Task 2", "some coding");
            Exercise exercise1 = new Exercise("Task 2", "some coding");
            exercise1.saveToDB(connection);
//            exercise1.saveToDB(connection);
//            exercise1.delete(connection);
            Exercise.printExercises(Exercise.loadAll(connection));


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
