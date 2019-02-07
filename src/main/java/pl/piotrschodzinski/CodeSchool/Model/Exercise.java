package pl.piotrschodzinski.CodeSchool.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Exercise {
    private int id;
    private String title;
    private String description;

    public Exercise() {
    }

    public Exercise(String title, String description) {
        this.title = title;
        this.description = description;
    }

    private static Exercise loadSingleExercise(ResultSet resultSet) throws SQLException {
        Exercise loadedExercise = new Exercise();
        loadedExercise.id = resultSet.getInt("id");
        loadedExercise.title = resultSet.getString("title");
        loadedExercise.description = resultSet.getString("description");
        return loadedExercise;
    }

    public static Exercise loadById(Connection connection, int id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT* FROM exercise WHERE id=?;");
        statement.setLong(1, id);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return loadSingleExercise(resultSet);
        }
        return null;
    }

    public static ArrayList<Exercise> loadAll(Connection connection) throws SQLException {
        ArrayList<Exercise> loadedExercise = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement("SELECT* FROM exercise;");
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            loadedExercise.add(loadSingleExercise(resultSet));
        }
        return loadedExercise;
    }

    public static void printExercises(ArrayList<Exercise> exercises) {
        if (exercises == null) {
            System.out.println("List is empty.");
            return;
        }
        for (Exercise exercise : exercises) {
            System.out.println(exercise);
        }
    }

    public void saveToDB(Connection connection) throws SQLException {
        if (this.id == 0) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO exercise (title, description) VALUES (?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, this.title);
            statement.setString(2, this.description);
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

    public void delete(Connection connection) throws SQLException {
        if (this.id != 0) {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM exercise WHERE id=?");
            statement.setInt(1, this.id);
            statement.executeUpdate();
            this.id = 0;
        } else {
            System.out.println("Exercise doesn't exist.");
        }
    }

    @Override
    public String toString() {
        return "id:" + id +
                "\ttitle:" + title +
                "\tdescription:" + description;
    }
}
