package ru.ns.stats.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnector {
    private final String URL = "jdbc:oracle:thin:@oracle-db:1521/FREEPDB1";
    private final String USER = System.getenv("DB_USER");
    private final String PASSWORD = System.getenv("DB_PASSWORD");

    public Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }
}
