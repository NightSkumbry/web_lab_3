package ru.ns.common.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import ru.ns.common.model.HitResult;

public class HitResultDAO {
    private static final Logger logger = Logger.getLogger(HitResultDAO.class.getName());

    private final DataBaseConnector connector;
    private final DataBaseMigrator migrator;


    public HitResultDAO() {
        logger.fine("Initializing HitResultDAO");
        connector = new DataBaseConnector();
        migrator = new DataBaseMigrator(connector);

        migrator.migrate();
        logger.fine("HitResultDAO initialized");

    }

    public int addResult(HitResult result) throws SQLException {
        logger.fine("Adding hit result to database: " + result);
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
                    logger.severe("Creating hit result failed, no ID obtained.");
                    throw new SQLException("Creating hit result failed, no ID obtained.");
                }
            }
        }
    }

    public List<HitResult> getAllResults() throws SQLException {
        logger.fine("Retrieving all hit results from database");
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
        logger.info("Deleting all hit results from database");
        String updateSQL = "UPDATE hit_results SET deleted = 1";
        try (Connection connection = connector.getConnection();
             var statement = connection.createStatement()) {
            int affectedRows = statement.executeUpdate(updateSQL);
            logger.info("Soft deleted all hit results, affected rows: " + affectedRows);
        }
    }

    public void deleteResultById(int id) throws SQLException {
        logger.info("Deleting hit result with ID " + id + " from database");
        String updateSQL = "UPDATE hit_results SET deleted = 1 WHERE id = ?";
        try (Connection connection = connector.getConnection();
             var preparedStatement = connection.prepareStatement(updateSQL)) {
            preparedStatement.setInt(1, id);
            int affectedRows = preparedStatement.executeUpdate();
            logger.info("Soft deleted hit result with ID " + id + ", affected rows: " + affectedRows);
        }
    }


}
