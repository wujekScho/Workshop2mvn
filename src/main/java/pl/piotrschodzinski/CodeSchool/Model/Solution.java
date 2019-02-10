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
    private long userId;

    public Solution() {
    }

    public Solution(String description, int exerciseId, long userId) {
        this.created = LocalDateTime.now();
        this.description = description;
        this.exerciseId = exerciseId;
        this.userId = userId;
    }

    public static Solution editSolution(Connection connection, int id, String description) throws SQLException {
        Solution solutionToEdit = loadById(connection, id);
        solutionToEdit.updated = LocalDateTime.now();
        solutionToEdit.description = description;
        return solutionToEdit;
    }

    public static boolean checkUncompletedUserSolutionId(Connection connection, int solutionId, long userId) throws SQLException {
        for (Solution solution : loadUncompltetedUserSolutions(connection, userId)) {
            if (solution.id == solutionId) {
                return true;
            }
        }
        return false;
    }

    private static Solution loadSingleSolution(ResultSet resultSet) throws SQLException {
        Solution loadedSolution = new Solution();
        loadedSolution.id = resultSet.getInt("id");
        loadedSolution.created = resultSet.getTimestamp("created").toLocalDateTime();
        if (resultSet.getTimestamp("updated") != null) {
            loadedSolution.updated = resultSet.getTimestamp("updated").toLocalDateTime();
        } else {
            loadedSolution.updated = null;
        }
        loadedSolution.description = resultSet.getString("description");
        loadedSolution.exerciseId = resultSet.getInt("exercise_id");
        loadedSolution.userId = resultSet.getLong("users_id");
        return loadedSolution;
    }

    public static ArrayList<Solution> loadUncompltetedUserSolutions(Connection connection, long userId) throws SQLException {
        ArrayList<Solution> loadedSolutions = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement(
                "SELECT* FROM solution WHERE users_id=? AND description IS NULL ORDER BY id ASC;");
        statement.setLong(1, userId);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            loadedSolutions.add(loadSingleSolution(resultSet));
        }
        return loadedSolutions;
    }

    public static ArrayList<Solution> loadAll(Connection connection) throws SQLException {
        ArrayList<Solution> loadedSolutions = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement("SELECT* FROM solution ORDER BY id ASC;");
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            loadedSolutions.add(loadSingleSolution(resultSet));
        }
        return loadedSolutions;
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

    public static ArrayList<Solution> loadAllByUserId(Connection connection, long userId) throws SQLException {
        ArrayList<Solution> loadedSolutions = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement(
                "SELECT* FROM solution WHERE users_id=? ORDER BY id ASC;");
        statement.setLong(1, userId);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            loadedSolutions.add(loadSingleSolution(resultSet));
        }
        return loadedSolutions;
    }

    public static ArrayList<Solution> loadAllByExerciseId(Connection connection, int exerciseId) throws SQLException {
        ArrayList<Solution> loadedSolutions = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement(
                "SELECT* FROM solution WHERE exercise_id=? ORDER BY updated ASC;");
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
        System.out.println("List of solutions: ");
        System.out.println("+-----+--------+------------+-------------------------+--------------------+--------------------+");
        System.out.println(String.format("|%-5s|%-8s|%-12s|%-25s|%-20s|%-20s|", "Id", "User Id", "Exercise Id", "Description", "Created", "Updated"));
        System.out.println("+-----+--------+------------+-------------------------+--------------------+--------------------+");
        for (Solution solution : solutions) {
            System.out.println(solution);
        }
        System.out.println("+-----+--------+------------+-------------------------+--------------------+--------------------+");
    }

    public void saveToDB(Connection connection) throws SQLException {
        if (this.id == 0) {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO solution (created, updated, description, exercise_id, users_id) VALUES (?,?,?,?,?)",
                    PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setTimestamp(1, Timestamp.valueOf(this.created));
            if (updated != null) {
                statement.setTimestamp(2, Timestamp.valueOf(this.updated));
            } else {
                statement.setTimestamp(2, null);
            }
            statement.setString(3, this.description);
            statement.setInt(4, this.exerciseId);
            statement.setLong(5, this.userId);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                this.id = resultSet.getInt(1);
            }
        } else {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE solution SET updated=? ,description=?,exercise_id=?,users_id=? WHERE id=?;");
            statement.setTimestamp(1, Timestamp.valueOf(this.updated));
            statement.setString(2, this.description);
            statement.setInt(3, this.exerciseId);
            statement.setLong(4, this.userId);
            statement.setInt(5, this.id);
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

        String dateCreated = "";
        if (created != null) {
            dateCreated = created.format(formatter);
        }
        String dateUpdated = "";
        if (updated != null) {
            dateUpdated = updated.format(formatter);
        }
        String solutionDescription = "";
        if (description != null) {
            solutionDescription = description;
        }
        if (solutionDescription.length() > 25) {
            solutionDescription = solutionDescription.substring(0, 22) + "...";
        }
        return String.format("|%-5s|%-8s|%-12s|%-25s|%-20s|%-20s|", id, userId, exerciseId,
                solutionDescription, dateCreated, dateUpdated);
    }
}
