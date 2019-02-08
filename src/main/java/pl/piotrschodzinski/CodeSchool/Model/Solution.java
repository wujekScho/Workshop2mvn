package pl.piotrschodzinski.CodeSchool.Model;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

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
        this.updated = LocalDateTime.now();
        this.description = description;
        this.exerciseId = exerciseId;
        this.userId = userId;
    }

    private static Solution loadSingleSolution(ResultSet resultSet) throws SQLException {
        Solution loadedSolution = new Solution();
        loadedSolution.id = resultSet.getInt("id");
        loadedSolution.created = resultSet.getTimestamp("created").toLocalDateTime();
        loadedSolution.updated = resultSet.getTimestamp("updated").toLocalDateTime();
        loadedSolution.description = resultSet.getString("description");
        loadedSolution.exerciseId = resultSet.getInt("exercise_id");
        loadedSolution.userId = resultSet.getInt("users_id");
        return loadedSolution;
    }

    public static Solution loadById(Connection connection, int id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT* FROM solution WHERE id=?;");
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return loadSingleSolution(resultSet);
        }
        return null;
    }

    public static ArrayList<Solution> loadAll(Connection connection) throws SQLException {
        ArrayList<Solution> loadedSolutions = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement("SELECT* FROM solution;");
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            loadedSolutions.add(loadSingleSolution(resultSet));
        }
        return loadedSolutions;
    }

    public static ArrayList<Solution> loadAllByUserId(Connection connection, int userId) throws SQLException {
        ArrayList<Solution> loadedSolutions = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement("SELECT* FROM solution WHERE users_id=?;");
        statement.setInt(1, userId);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            loadedSolutions.add(loadSingleSolution(resultSet));
        }
        return loadedSolutions;
    }

    public static ArrayList<Solution> loadAllByExerciseId(Connection connection, int exerciseId) throws SQLException {
        ArrayList<Solution> loadedSolutions = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement("SELECT* FROM solution WHERE exercise_id=? ORDER BY updated ASC;");
        statement.setInt(1, exerciseId);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            loadedSolutions.add(loadSingleSolution(resultSet));
        }
        return loadedSolutions;
    }

    public static void printSolutions(ArrayList<Solution> solutions) {
        if (solutions == null) {
            System.out.println("List is empty.");
            return;
        }
        for (Solution solution : solutions) {
            System.out.println(solution);
        }
    }


    public void saveToDB(Connection connection) throws SQLException {
        if (this.id == 0) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO solution (created, updated, description, exercise_id, users_id) VALUES (?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setTimestamp(1, Timestamp.valueOf(this.created));
            statement.setTimestamp(2, Timestamp.valueOf(this.updated));
            statement.setString(3, this.description);
            statement.setInt(4, this.exerciseId);
            statement.setInt(5, this.userId);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                this.id = resultSet.getInt(1);
            }
        } else {
            PreparedStatement statement = connection.prepareStatement("UPDATE solution SET updated=CURRENT_TIMESTAMP ,description=?,exercise_id=?,users_id? WHERE id=?;");
            statement.setString(1, this.description);
            statement.setInt(2, this.exerciseId);
            statement.setInt(3, this.userId);
            statement.setInt(4, this.id);
            statement.executeUpdate();
        }
    }

    public void delete(Connection connection) throws SQLException {
        if (this.id != 0) {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM solution WHERE id=?");
            statement.setInt(1, this.id);
            statement.executeUpdate();
            this.id = 0;
        } else {
            System.out.println("Solution doesn't exist.");
        }
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return "id:" + id +
                "\tcreated:" + created.format(formatter) +
                "\tupdated:" + updated.format(formatter) +
                "\tdescription:" + description +
                "\texerciseId:" + exerciseId +
                "\tuserId:" + userId;
    }
}
