package pl.piotrschodzinski.codeschool;

import pl.piotrschodzinski.codeschool.menagement.*;
import pl.piotrschodzinski.codeschool.utilities.ConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;

import static pl.piotrschodzinski.codeschool.utilities.Tools.getInt;

public class App {
    public static void main(String[] args) {
        try (Connection connection = ConnectionFactory.getConnection("code_school", "root", "root")) {
            printMenu();
            int choice = getInt("Type your choice: ");
            while (choice != 0) {
                switch (choice) {
                    case 1:
                        UserManagement.menageUsers(connection);
                        printMenu();
                        choice = getInt("Type your choice.");
                        break;
                    case 2:
                        GroupManagement.menageUserGroups(connection);
                        printMenu();
                        choice = getInt("Type your choice.");
                        break;
                    case 3:
                        ExerciseManagement.menageExercises(connection);
                        printMenu();
                        choice = getInt("Type your choice.");
                        break;
                    case 4:
                        SolutionManagement.menageSolutions(connection);
                        printMenu();
                        choice = getInt("Type your choice.");
                        break;
                    case 5:
                        UserPanel.userProgram(connection, args);
                        printMenu();
                        choice = getInt("Type your choice.");
                        break;
                    case 0:
                        break;
                    default:
                        choice = getInt("Type valid number: ");
                        break;
                }
            }
            System.out.println("Program is being shut down...");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void printMenu() {
        System.out.println("Choose from these choices");
        System.out.println("-------------------------");
        System.out.println("1 - Menage users.");
        System.out.println("2 - Menage user groups.");
        System.out.println("3 - Menage exercises.");
        System.out.println("4 - Menage solutions.");
        System.out.println("5 - Enter user panel.");
        System.out.println("0 - Exit program.");
    }
}

