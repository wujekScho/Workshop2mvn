package pl.piotrschodzinski;

import pl.piotrschodzinski.CodeSchool.ConnectionFactory;
import pl.piotrschodzinski.CodeSchool.Model.User;
import pl.piotrschodzinski.CodeSchool.Model.UserGroup;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        try (Connection connection = ConnectionFactory.getConnection("code_school", "root", "root")) {
            UserAdministration.menageUsers(connection);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}

class UserAdministration {
    static Scanner scanner = new Scanner(System.in);

    public static void menageUsers(Connection connection) throws SQLException {
//        User.printUsers(User.loadAll(connection));
//        addUser(connection);
//        User.printUsers(User.loadAll(connection));

    }

    public static User addUser(Connection connection) throws SQLException {
        System.out.print("Type username: ");
        String username = scanner.nextLine();
        System.out.print("Type email: ");
        String email = scanner.next();
        while (!User.checkMail(connection, email)) {
            System.out.print("Email already in use or wrong format, type proper one: ");
            email = scanner.next();
        }
        int userGroupId = getInt("Type user group id: ");
        while (!UserGroup.checkGroupId(connection, userGroupId)) {
            userGroupId = getInt("Group dont exist, type proper one: ");
        }
        System.out.print("Type password: ");
        String password = scanner.next();

        User user = new User(username, email, userGroupId, password);
        user.saveToDB(connection);
        return user;
    }

    public static User editUser(Connection connection) throws SQLException {
        long id = getLong("Type user id to edit user: ");
        while (!User.checkId(connection, id)) {
            id = getLong("User dont exist, type proper id: ");
        }
        User userToEdit = User.loadById(connection, id);
        //todo tu skończyłem


    }

    public static int getInt(String prompt) {
        System.out.println(prompt);
        while (!scanner.hasNextInt()) {
            System.out.print("Type valid integer value: ");
            scanner.next();
        }
        int result = scanner.nextInt();
        return result;
    }

    public static long getLong(String prompt) {
        System.out.println(prompt);
        while (!scanner.hasNextLong()) {
            System.out.print("Type valid integer value: ");
            scanner.next();
        }
        long result = scanner.nextLong();
        return result;
    }


}
