package pl.piotrschodzinski.CodeSchool.Model;

import java.sql.*;
import java.time.LocalDateTime;

public class Solution {
    private int id;
    private LocalDateTime created;
    private LocalDateTime updated;
    private String description;
    private int exerciseId;
    private int userId;

    public Solution() {
    }

    public Solution(String description, int exerciseId, int userId) {
        this.created = LocalDateTime.now();
        this.description = description;
        this.exerciseId = exerciseId;
        this.userId = userId;
    }

    public void saveToDB(Connection connection) throws SQLException {
        if (this.id == 0) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO solution (created, updated, description, exercise_id, users_id) VALUES (?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setTimestamp(1, Timestamp.valueOf(created)); //todo tu skończyłem
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                this.id = resultSet.getInt(1);
            }
        } else {
            PreparedStatement statement = connection.prepareStatement("UPDATE exercise SET name=?, description=? WHERE id=?;");
            statement.setString(1, this.title);
            statement.setString(2, this.description);
            statement.setInt(3, this.id);
            statement.executeUpdate();
        }
    }
}
