package pl.piotrschodzinski.codeschool.menagement;

import pl.piotrschodzinski.codeschool.model.Solution;
import pl.piotrschodzinski.codeschool.model.User;
import pl.piotrschodzinski.codeschool.tools.Utilities;

import java.sql.Connection;
import java.sql.SQLException;

import static pl.piotrschodzinski.codeschool.tools.Utilities.getInt;


public class UserPanel {
    public static void userProgram(Connection connection, String[] userIds) throws SQLException {
        if (userIds == null || userIds.length < 1 || !tryParse(userIds[0]) || !User.checkId(connection, Long.valueOf(userIds[0]))) {
            System.out.println("Wrong input argument.");
            return;
        }
        long userId = Long.valueOf(userIds[0]);
        printMenu();
        int choice = getInt("Type your choice: ");
        while (choice != 0) {
            switch (choice) {
                case 1:
                    Solution.printSolutions(Solution.loadAllByUserId(connection, userId));
                    choice = getInt("Type your choice. \n" +
                            "3 - print menu: ");
                    break;
                case 2:
                    Solution.printSolutions(Solution.loadUncompltetedUserSolutions(connection, userId));
                    addSolution(connection, userId);
                    choice = getInt("Type your choice. \n" +
                            "3 - print menu: ");
                    break;
                case 3:
                    printMenu();
                    choice = getInt("Type your choice. \n" +
                            "3 - print menu: ");
                    break;
                case 0:
                    break;
                default:
                    choice = getInt("Type valid number: ");
                    break;
            }
        }
    }

    public static void addSolution(Connection connection, long userId) throws SQLException {
        int solutionId = getSolutionId(connection, userId);
        String solution = Utilities.getString("Type your solution.");
        Solution editedSolution = Solution.editSolution(connection, solutionId, solution);
        editedSolution.saveToDB(connection);
        System.out.println("Solution added succesfully.");
    }

    public static void printMenu() {
        System.out.println("Choose from these choices");
        System.out.println("-------------------------");
        System.out.println("1 - Print your solutions.");
        System.out.println("2 - Add solution.");
        System.out.println("3 - Print this menu.");
        System.out.println("0 - Enter main menu.");
    }

    private static boolean tryParse(String value) {
        try {
            Long.parseLong(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static int getSolutionId(Connection connection, long userId) throws SQLException {
        int solutionId = getInt("Type solution id: ");
        while (!Solution.checkUncompletedUserSolutionId(connection, solutionId, userId)) {
            solutionId = getInt("Type valid solution id: ");
        }
        return solutionId;
    }
}
