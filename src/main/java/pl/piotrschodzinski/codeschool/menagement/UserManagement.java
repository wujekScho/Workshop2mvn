package pl.piotrschodzinski.codeschool.menagement;

import pl.piotrschodzinski.codeschool.model.Group;
import pl.piotrschodzinski.codeschool.model.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import static pl.piotrschodzinski.codeschool.utilities.Tools.*;

public class UserManagement {
    static Scanner scanner = new Scanner(System.in);

    public static void menageUsers(Connection connection) throws SQLException {
        printMenu();
        int choice = getInt("Type your choice: ");
        while (choice != 0) {
            switch (choice) {
                case 1:
                    User.printUsers(User.loadAll(connection));
                    choice = getInt("Type your choice. \n" +
                            "5 - print menu: ");
                    break;
                case 2:
                    addUser(connection);
                    choice = getInt("Type your choice. \n" +
                            "5 - print menu: ");
                    break;
                case 3:
                    editUser(connection);
                    choice = getInt("Type your choice. \n" +
                            "5 - print menu: ");
                    break;
                case 4:
                    deleteUser(connection);
                    choice = getInt("Type your choice. \n" +
                            "5 - to print menu: ");
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
        System.out.println("Program is being shut down...");
    }

    public static void printMenu() {
        System.out.println("Choose from these choices");
        System.out.println("-------------------------");
        System.out.println("1 - Print users.");
        System.out.println("2 - Add user.");
        System.out.println("3 - Edit user.");
        System.out.println("4 - Delete user.");
        System.out.println("5 - Print this menu.");
        System.out.println("0 - Quit");
    }

    public static User addUser(Connection connection) throws SQLException {
        String username = getString("Type username: ");
        String email = getUserMail(connection, "Type email: ");
        int userGroupId = getUserGroupId(connection, "Type user group id: ");
        String password = getString("Type password: ");
        User user = new User(username, email, userGroupId, password);
        user.saveToDB(connection);
        System.out.println("User succesfully saved to DB.");
        return user;
    }

    public static User editUser(Connection connection) throws SQLException {
        long id = getUserId(connection, "Type user id to edit user: ");
        String username = getString("Type new username: ");
        String email = getUserMail(connection, "Type new email: ");
        int userGroupId = getUserGroupId(connection, "Type new user group id: ");
        String password = getString("Type new password: ");
        User editedUser = User.editUser(connection, id, username, email, password, userGroupId);
        editedUser.saveToDB(connection);
        System.out.println("User succesfully edited.");
        return editedUser;
    }

    public static void deleteUser(Connection connection) throws SQLException {
        long id = getUserId(connection, "Type user id to delete user: ");
        User userToEdit = User.loadById(connection, id);
        userToEdit.delete(connection);
        System.out.println("User succesfully deleted");
    }

    public static String getUserMail(Connection connection, String prompt) throws SQLException {
        System.out.print(prompt);
        String email = scanner.nextLine();
        while (!User.checkMail(connection, email)) {
            System.out.print("Email already in use or wrong format, type proper one: ");
            email = scanner.nextLine();
        }
        return email;
    }

    public static int getUserGroupId(Connection connection, String prompt) throws SQLException {
        int userGroupId = getInt(prompt);
        while (!Group.checkGroupId(connection, userGroupId)) {
            userGroupId = getInt("Group dont exist, type proper one: ");
        }

        return userGroupId;
    }


    public static long getUserId(Connection connection, String prompt) throws SQLException {
        long id = getLong(prompt);
        while (!User.checkId(connection, id)) {
            id = getLong("User id desn't exist, type proper id: ");
        }
        return id;
    }
}
