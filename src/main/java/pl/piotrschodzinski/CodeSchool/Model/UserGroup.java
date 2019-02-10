package pl.piotrschodzinski.CodeSchool.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserGroup {
    private int id;
    private String name;

    public UserGroup() {
    }

    public UserGroup(String name) {
        this.name = name;
    }

    public static UserGroup editUserGroup(Connection connection, int id, String name) throws SQLException {
        UserGroup userGroupToEdit = loadById(connection, id);
        userGroupToEdit.name = name;
        return userGroupToEdit;
    }

    public static boolean checkGroupId(Connection connection, int id) throws SQLException {
        for (UserGroup usergroup : loadAll(connection)) {
            if (usergroup.id == id) {
                return true;
            }
        }
        return false;
    }

    public static ArrayList<UserGroup> loadAll(Connection connection) throws SQLException {
        ArrayList<UserGroup> loadedGroups = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement("SELECT* FROM user_group ORDER BY id ASC;");
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            loadedGroups.add(loadSingleGroup(resultSet));
        }
        return loadedGroups;
    }

    public static void printUserGroups(ArrayList<UserGroup> userGroups) {
        if (userGroups == null) {
            System.out.println("List is empty.");
            return;
        }
        System.out.println("List of user groups: ");
        System.out.println("+--------+---------------+");
        System.out.println(String.format("|%-8s|%-15s|", "Id", "Name"));
        System.out.println("+--------+---------------+");
        for (UserGroup userGroup : userGroups) {
            System.out.println(userGroup);
        }
        System.out.println("+--------+---------------+");
    }

    private static UserGroup loadSingleGroup(ResultSet resultSet) throws SQLException {
        UserGroup loadedGroup = new UserGroup();
        loadedGroup.id = resultSet.getInt("id");
        loadedGroup.name = resultSet.getString("name");
        return loadedGroup;
    }

    public static UserGroup loadById(Connection connection, int id) throws SQLException {
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
