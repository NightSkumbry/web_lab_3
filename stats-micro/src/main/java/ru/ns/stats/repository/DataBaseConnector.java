package ru.ns.stats.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.logging.Level;

public class DataBaseConnector {
    private static final Logger logger = Logger.getLogger(DataBaseConnector.class.getName());

    private final String URL = "jdbc:oracle:thin:@oracle-db:1521/FREEPDB1";
    private final String USER = System.getenv("DB_USER");
    private final String PASSWORD = System.getenv("DB_PASSWORD");

    public Connection getConnection() throws SQLException {
        int attempts = 0;
        int maxAttempts = 10;
        long delayMs = 3000;

        while (attempts < maxAttempts) {
            try {
                return DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (SQLException e) {
                attempts++;
                logger.log(Level.SEVERE, "Error getting database connection", e);
                if (attempts < maxAttempts) {
                    try {
                        Thread.sleep(delayMs);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new SQLException("Connection interrupted", ie);
                    }
                } else {
                    throw new SQLException("Failed to connect to database after " + maxAttempts + " attempts", e);
                }
            }
        }

        throw new SQLException("Failed to connect to database after " + maxAttempts + " attempts");
    }
}
