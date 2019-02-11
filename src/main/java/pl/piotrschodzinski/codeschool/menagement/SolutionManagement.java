package pl.piotrschodzinski.codeschool.menagement;

import pl.piotrschodzinski.codeschool.model.Exercise;
import pl.piotrschodzinski.codeschool.model.Solution;
import pl.piotrschodzinski.codeschool.model.User;

import java.sql.Connection;
import java.sql.SQLException;

import static pl.piotrschodzinski.codeschool.tools.Utilities.getInt;

public class SolutionManagement {
    public static void menageSolutions(Connection connection) throws SQLException {
        printMenu();
        int choice = getInt("Type your choice: ");
        while (choice != 0) {
            switch (choice) {
                case 1:
                    Solution.printSolutions(Solution.loadAll(connection));
                    choice = getInt("Type your choice. \n" +
                            "4 - print menu: ");
                    break;
                case 2:
                    addSolution(connection);
                    choice = getInt("Type your choice. \n" +
                            "4 - print menu: ");
                    break;
                case 3:
                    viewUserSolutions(connection);
                    choice = getInt("Type your choice. \n" +
                            "4 - print menu: ");
                    break;
                case 4:
                    printMenu();
                    choice = getInt("Type your choice. \n" +
                            "4 - print menu: ");
                    break;
                case 0:
                    break;
                default:
                    choice = getInt("Type valid number: ");
                    break;
            }
        }
    }

    public static Solution addSolution(Connection connection) throws SQLException {
        User.printUsers(User.loadAll(connection));
        long userId = UserManagement.getUserId(connection, "Type user id: ");
        Exercise.printExercises(Exercise.loadAll(connection));
        int exerciseId = ExerciseManagement.getExerciseId(connection, "Type solution id: ");
        Solution solution = new Solution(null, exerciseId, userId);
        solution.saveToDB(connection);
        System.out.println("Solution added succesfully.");
        return solution;
    }

    public static void viewUserSolutions(Connection connection) throws SQLException {
        long userId = UserManagement.getUserId(connection, "Type user id: ");
        Solution.printSolutions(Solution.loadAllByUserId(connection, userId));
    }

    public static void printMenu() {
        System.out.println("Choose from these choices");
        System.out.println("-------------------------");
        System.out.println("1 - Print all solutions.");
        System.out.println("2 - Add solution.");
        System.out.println("3 - Print user solutions.");
        System.out.println("4 - Print this menu.");
        System.out.println("0 - Enter main menu.");
    }
}
