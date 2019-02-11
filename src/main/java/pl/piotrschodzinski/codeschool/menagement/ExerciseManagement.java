package pl.piotrschodzinski.codeschool.menagement;

import pl.piotrschodzinski.codeschool.model.Exercise;

import java.sql.Connection;
import java.sql.SQLException;

import static pl.piotrschodzinski.codeschool.tools.Utilities.getInt;
import static pl.piotrschodzinski.codeschool.tools.Utilities.getString;

public class ExerciseManagement {
    public static void menageExercises(Connection connection) throws SQLException {
        printMenu();
        int choice = getInt("Type your choice: ");
        while (choice != 0) {
            switch (choice) {
                case 1:
                    Exercise.printExercises(Exercise.loadAll(connection));
                    choice = getInt("Type your choice. \n" +
                            "5 - print menu: ");
                    break;
                case 2:
                    addExercise(connection);
                    choice = getInt("Type your choice. \n" +
                            "5 - print menu: ");
                    break;
                case 3:
                    editExercise(connection);
                    choice = getInt("Type your choice. \n" +
                            "5 - print menu: ");
                    break;
                case 4:
                    deleteExercise(connection);
                    choice = getInt("Type your choice. \n" +
                            "5 - print menu: ");
                    break;
                case 5:
                    printMenu();
                    choice = getInt("Type your choice. \n" +
                            "5 - print menu: ");
                    break;
                case 0:
                    break;
                default:
                    choice = getInt("Type valid number: ");
                    break;
            }
        }
    }

    public static void printMenu() {
        System.out.println("Choose from these choices");
        System.out.println("-------------------------");
        System.out.println("1 - Print exercises.");
        System.out.println("2 - Add exercise.");
        System.out.println("3 - Edit exercise.");
        System.out.println("4 - Delete exercise.");
        System.out.println("5 - Print this menu.");
        System.out.println("0 - Enter main menu.");
    }

    public static Exercise addExercise(Connection connection) throws SQLException {
        String title = getString("Type title: ");
        String description = getString("Type description: ");
        Exercise exercise = new Exercise(title, description);
        exercise.saveToDB(connection);
        System.out.println("Exercise succesfully saved to DB.");
        return exercise;
    }

    public static Exercise editExercise(Connection connection) throws SQLException {
        int id = getExerciseId(connection, "Type exercise id to edit exercise: ");
        String title = getString("Type new title: ");
        String description = getString("Type new description: ");
        Exercise editedExercise = Exercise.editExercise(connection, id, title, description);
        editedExercise.saveToDB(connection);
        System.out.println("Exercise succesfully edited.");
        return editedExercise;
    }

    public static void deleteExercise(Connection connection) throws SQLException {
        int id = getExerciseId(connection, "Type exercise id to delete exercise: ");
        Exercise exerciseToEdit = Exercise.loadById(connection, id);
        exerciseToEdit.delete(connection);
        System.out.println("Exercise succesfully deleted");
    }


    public static int getExerciseId(Connection connection, String prompt) throws SQLException {
        int id = getInt(prompt);
        while (!Exercise.checkId(connection, id)) {
            id = getInt("Exercise id desn't exist, type proper id: ");
        }
        return id;
    }
}
