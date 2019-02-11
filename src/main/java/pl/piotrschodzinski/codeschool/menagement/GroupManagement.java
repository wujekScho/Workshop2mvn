package pl.piotrschodzinski.codeschool.menagement;

import pl.piotrschodzinski.codeschool.model.Group;

import java.sql.Connection;
import java.sql.SQLException;

import static pl.piotrschodzinski.codeschool.tools.Utilities.getInt;
import static pl.piotrschodzinski.codeschool.tools.Utilities.getString;

public class GroupManagement {
    public static void menageUserGroups(Connection connection) throws SQLException {
        printMenu();
        int choice = getInt("Type your choice: ");
        while (choice != 0) {
            switch (choice) {
                case 1:
                    Group.printUserGroups(Group.loadAll(connection));
                    choice = getInt("Type your choice. \n" +
                            "5 - print menu: ");
                    break;
                case 2:
                    addUserGroup(connection);
                    choice = getInt("Type your choice. \n" +
                            "5 - print menu: ");
                    break;
                case 3:
                    editUserGroup(connection);
                    choice = getInt("Type your choice. \n" +
                            "5 - print menu: ");
                    break;
                case 4:
                    deleteUserGroup(connection);
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
        System.out.println("1 - Print user groups.");
        System.out.println("2 - Add user group.");
        System.out.println("3 - Edit user group.");
        System.out.println("4 - Delete user group.");
        System.out.println("5 - Print this menu.");
        System.out.println("0 - Enter main menu.");
    }

    public static Group addUserGroup(Connection connection) throws SQLException {
        String name = getString("Type name: ");
        Group group = new Group(name);
        group.saveToDB(connection);
        System.out.println("User group succesfully saved to DB.");
        return group;
    }

    public static Group editUserGroup(Connection connection) throws SQLException {
        int id = getUserGroupId(connection, "Type group id to edit user group: ");
        String name = getString("Type new name: ");
        Group editedGroup = Group.editUserGroup(connection, id, name);
        editedGroup.saveToDB(connection);
        System.out.println("User Group succesfully edited.");
        return editedGroup;
    }

    public static void deleteUserGroup(Connection connection) throws SQLException {
        int id = getUserGroupId(connection, "Type group id to delete user group: ");
        Group groupToEdit = Group.loadById(connection, id);
        groupToEdit.delete(connection);
        System.out.println("User group succesfully deleted");
    }

    public static int getUserGroupId(Connection connection, String prompt) throws SQLException {
        int id = getInt(prompt);
        while (!Group.checkGroupId(connection, id)) {
            id = getInt("Group id desn't exist, type proper id: ");
        }
        return id;
    }
}
