package pl.piotrschodzinski.CodeSchool.Model;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class User {
    private long id;
    private String username;
    private String email;
    private String password;
    private int userGroupId;

    public User() {
    }

    public User(String username, String email, int userGroupId, String password) {
        this.username = username;
        this.email = email;
        this.userGroupId = userGroupId;
        setPassword(password);
    }

    private void setPassword(String password) {
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }


    public void saveToDB(Connection connection) throws SQLException {
        if (this.id == 0) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO users (username,email,password,user_group_id) VALUES (?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, this.username);
            statement.setString(2, this.email);
            statement.setString(3, this.password);
            statement.setInt(4, this.userGroupId);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                this.id = resultSet.getInt(1);
            }
        } else {
            PreparedStatement statement = connection.prepareStatement("UPDATE users SET username=?,email=?,password=?,user_group_id=? WHERE id=?;");
            statement.setString(1, this.username);
            statement.setString(2, this.email);
            statement.setString(3, this.password);
            statement.setInt(4, this.userGroupId);
            statement.setLong(5, this.id);
            statement.executeUpdate();
        }
    }

    public static User editUser(Connection connection, long id, String username, String email, String password, int userGroupId) throws SQLException {
        User userToEdit = loadById(connection, id);
        userToEdit.username = username;
        userToEdit.email = email;
        userToEdit.password = getHashedPassword(password);
        userToEdit.userGroupId = userGroupId;
        return userToEdit;
    }

    public void delete(Connection connection) throws SQLException {
        if (this.id != 0) {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM users WHERE id=?");
            statement.setLong(1, this.id);
            statement.executeUpdate();
            this.id = 0;
        } else {
            System.out.println("User doesn't exist.");
        }
    }

    private static String getHashedPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean checkId(Connection connection, long id) throws SQLException {
        for (User user : loadAll(connection)) {
            if (user.id == id) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkMail(Connection connection, String email) throws SQLException {
        for (User user : loadAll(connection)) {
            if (user.email.equalsIgnoreCase(email)) {
                return false;
            }
        }
        return Pattern.matches("^([\\w-]+(?:\\.[\\w-]+)*)@((?:[\\w-]+\\.)*\\w[\\w-]{0,66})\\.([a-z]{2,6}(?:\\.[a-z]{2})?)$", email);
    }

    private static User loadSingleUser(ResultSet resultSet) throws SQLException {
        User loadedUser = new User();
        loadedUser.id = resultSet.getLong("id");
        loadedUser.username = resultSet.getString("username");
        loadedUser.userGroupId = resultSet.getInt("user_group_id");
        loadedUser.password = resultSet.getString("password");
        loadedUser.email = resultSet.getString("email");
        return loadedUser;
    }

    public static User loadById(Connection connection, long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT* FROM users WHERE id=?;");
        statement.setLong(1, id);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return loadSingleUser(resultSet);
        }
        return null;
    }

    public static ArrayList<User> loadAll(Connection connection) throws SQLException {
        ArrayList<User> loadedUsers = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement("SELECT* FROM users ORDER BY id ASC;");
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            loadedUsers.add(loadSingleUser(resultSet));
        }
        return loadedUsers;
    }

    public static void printUsers(ArrayList<User> users) {
        if (users == null) {
            System.out.println("List is empty.");
            return;
        }
        System.out.println("List of users: ");
        System.out.println("+--------+---------------+-------------------------+----------+");
        System.out.println(String.format("|%-8s|%-15s|%-25s|%-10s|", "Id", "Username", "Email", "User Group"));
        System.out.println("+--------+---------------+-------------------------+----------+");
        for (User user : users) {
            System.out.println(user);
        }
        System.out.println("+--------+---------------+-------------------------+----------+");
    }

    @Override
    public String toString() {
        return String.format("|%-8d|%-15s|%-25s|%-10d|", id,
                (username.length() > 15) ? username.substring(0, 12) + "..." : username,
                (email.length() > 25) ? email.substring(0, 22) + "..." : email, userGroupId);
    }
}
