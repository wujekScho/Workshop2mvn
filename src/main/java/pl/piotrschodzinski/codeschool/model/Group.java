package pl.piotrschodzinski.codeschool.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Group {
    private int id;
    private String name;

    public Group() {
    }

    public Group(String name) {
        this.name = name;
    }

    public static Group editUserGroup(Connection connection, int id, String name) throws SQLException {
        Group groupToEdit = loadById(connection, id);
        groupToEdit.name = name;
        return groupToEdit;
    }

    public static boolean checkGroupId(Connection connection, int id) throws SQLException {
        for (Group usergroup : loadAll(connection)) {
            if (usergroup.id == id) {
                return true;
            }
        }
        return false;
    }

    public static ArrayList<Group> loadAll(Connection connection) throws SQLException {
        ArrayList<Group> loadedGroups = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement("SELECT* FROM user_group ORDER BY id ASC;");
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            loadedGroups.add(loadSingleGroup(resultSet));
        }
        return loadedGroups;
    }

    public static void printUserGroups(ArrayList<Group> groups) {
        if (groups == null) {
            System.out.println("List is empty.");
            return;
        }
        System.out.println("List of user groups: ");
        System.out.println("+--------+---------------+");
        System.out.println(String.format("|%-8s|%-15s|", "Id", "Name"));
        System.out.println("+--------+---------------+");
        for (Group group : groups) {
            System.out.println(group);
        }
        System.out.println("+--------+---------------+");
    }

    private static Group loadSingleGroup(ResultSet resultSet) throws SQLException {
        Group loadedGroup = new Group();
        loadedGroup.id = resultSet.getInt("id");
        loadedGroup.name = resultSet.getString("name");
        return loadedGroup;
    }

    public static Group loadById(Connection connection, int id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT* FROM user_group WHERE id=?;");
        statement.setLong(1, id);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return loadSingleGroup(resultSet);
        }
        return null;
    }

    public void saveToDB(Connection connection) throws SQLException {
        if (this.id == 0) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO user_group (name) VALUES (?)",
                    PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, this.name);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                this.id = resultSet.getInt(1);
            }
        } else {
            PreparedStatement statement = connection.prepareStatement("UPDATE user_group SET name=? WHERE id=?;");
            statement.setString(1, this.name);
            statement.setInt(2, this.id);
            statement.executeUpdate();
        }
    }

    public void delete(Connection connection) throws SQLException {
        if (this.id != 0) {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM user_group WHERE id=?");
            statement.setInt(1, this.id);
            statement.executeUpdate();
            this.id = 0;
        } else {
            System.out.println("User group doesn't exist.");
        }
    }

    @Override
    public String toString() {
        return String.format("|%-8d|%-15s|", id, (name.length() > 15) ? name.substring(0, 12) + "..." : name);
    }
}
