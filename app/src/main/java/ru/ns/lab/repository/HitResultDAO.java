package ru.ns.lab.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import ru.ns.lab.model.HitResult;

public class HitResultDAO {
    private final DataBaseConnector connector;


    public HitResultDAO() {
        connector = new DataBaseConnector();

        createTableIfNotExists();

    }

    private void createTableIfNotExists() {
        String checkTableSQL = """
            SELECT 1 FROM user_tables WHERE table_name = 'HIT_RESULTS'
        """;

        try (Connection connection = connector.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(checkTableSQL)) {

            if (!rs.next()) {
                // Таблица не существует, создаём
                String createTableSQL = """
                    CREATE TABLE hit_results (
                        id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                        x NUMBER(10, 8) NOT NULL,
                        y NUMBER(10, 8) NOT NULL,
                        r NUMBER(10, 8) NOT NULL,
                        is_hit NUMBER(1) NOT NULL,
                        max_miss_r NUMBER(10, 8) NOT NULL
                    )
                """;
                stmt.execute(createTableSQL);
                System.out.println("Таблица hit_results создана.");
            } else {
                System.out.println("Таблица hit_results уже существует.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int addResult(HitResult result) throws SQLException {
        String insertSQL = "INSERT INTO hit_results (x, y, r, is_hit, max_miss_r) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = connector.getConnection();
             var preparedStatement = connection.prepareStatement(insertSQL, new String[]{"id"})) {
            connection.setAutoCommit(false);

            preparedStatement.setDouble(1, result.getX());
            preparedStatement.setDouble(2, result.getY());
            preparedStatement.setDouble(3, result.getR());
            preparedStatement.setInt(4, result.getHit() ? 1 : 0);
            preparedStatement.setDouble(5, result.getMaxMissR());

            preparedStatement.executeUpdate();
            connection.commit();

            try (var generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating hit result failed, no ID obtained.");
                }
            }
        }
    }

    public List<HitResult> getAllResults() throws SQLException {
        String selectSQL = "SELECT * FROM hit_results";
        try (Connection connection = connector.getConnection();
             var statement = connection.createStatement();
             var resultSet = statement.executeQuery(selectSQL)) {

            List<HitResult> results = new java.util.ArrayList<>();

            while (resultSet.next()) {
                HitResult result = new HitResult(
                    resultSet.getInt("id"),
                    resultSet.getDouble("x"),
                    resultSet.getDouble("y"),
                    resultSet.getDouble("r"),
                    resultSet.getInt("is_hit") == 1,
                    resultSet.getDouble("max_miss_r")
                );
                results.add(result);
            }

            return results;
        }
    }

    public void deleteAllResults() throws SQLException {
        String deleteSQL = "DELETE FROM hit_results";
        try (Connection connection = connector.getConnection();
             var statement = connection.createStatement()) {
            statement.executeUpdate(deleteSQL);
        }
    }

    public void deleteResultById(int id) throws SQLException {
        String deleteSQL = "DELETE FROM hit_results WHERE id = ?";
        try (Connection connection = connector.getConnection();
             var preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }


}
