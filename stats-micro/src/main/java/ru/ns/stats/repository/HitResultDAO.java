package ru.ns.stats.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import ru.ns.stats.model.HitResult;

public class HitResultDAO {
    private final DataBaseConnector connector;


    public HitResultDAO() {
        connector = new DataBaseConnector();
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
}
