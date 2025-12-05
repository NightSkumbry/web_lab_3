package ru.ns.lab.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

import ru.ns.lab.model.HitResult;

public class HitResultDAO {
    private static final Logger logger = Logger.getLogger(HitResultDAO.class.getName());

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
                        max_miss_r NUMBER(10, 8) NOT NULL,
                        deleted NUMBER(1) DEFAULT 0 NOT NULL
                    )
                """;
                stmt.execute(createTableSQL);
                logger.info("Table hit_results created.");
            } else {
                // Check if deleted column exists, if not add it
                String checkColumnSQL = """
                    SELECT 1 FROM user_tab_cols WHERE table_name = 'HIT_RESULTS' AND column_name = 'DELETED'
                """;
                try (ResultSet columnRs = stmt.executeQuery(checkColumnSQL)) {
                    if (!columnRs.next()) {
                        // Add the deleted column to existing table
                        stmt.execute("ALTER TABLE hit_results ADD (deleted NUMBER(1) DEFAULT 0 NOT NULL)");
                        logger.info("Added deleted column to hit_results table.");
                    }
                }
                logger.info("Table hit_results already exists.");
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error creating table if not exists", e);
        }
    }

    public int addResult(HitResult result) throws SQLException {
        String insertSQL = "INSERT INTO hit_results (x, y, r, is_hit, max_miss_r, deleted) VALUES (?, ?, ?, ?, ?, 0)";
        try (Connection connection = connector.getConnection();
             var preparedStatement = connection.prepareStatement(insertSQL, new String[]{"id"})) {
            connection.setAutoCommit(false);

            preparedStatement.setDouble(1, result.getX());
            preparedStatement.setDouble(2, result.getY());
            preparedStatement.setDouble(3, result.getR());
            preparedStatement.setInt(4, result.getHit() ? 1 : 0);
            preparedStatement.setDouble(5, result.getMaxMissR());

            int affectedRows = preparedStatement.executeUpdate();
            connection.commit();

            logger.fine("Added hit result to database, affected rows: " + affectedRows);

            try (var generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    logger.fine("Generated ID for new hit result: " + id);
                    return id;
                } else {
                    throw new SQLException("Creating hit result failed, no ID obtained.");
                }
            }
        }
    }

    public List<HitResult> getAllResults() throws SQLException {
        String selectSQL = "SELECT * FROM hit_results WHERE deleted = 0";
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

            logger.fine("Retrieved " + results.size() + " hit results from database");
            return results;
        }
    }


    public void deleteAllResults() throws SQLException {
        String updateSQL = "UPDATE hit_results SET deleted = 1";
        try (Connection connection = connector.getConnection();
             var statement = connection.createStatement()) {
            int affectedRows = statement.executeUpdate(updateSQL);
            logger.info("Soft deleted all hit results, affected rows: " + affectedRows);
        }
    }

    public void deleteResultById(int id) throws SQLException {
        String updateSQL = "UPDATE hit_results SET deleted = 1 WHERE id = ?";
        try (Connection connection = connector.getConnection();
             var preparedStatement = connection.prepareStatement(updateSQL)) {
            preparedStatement.setInt(1, id);
            int affectedRows = preparedStatement.executeUpdate();
            logger.info("Soft deleted hit result with ID " + id + ", affected rows: " + affectedRows);
        }
    }


}
