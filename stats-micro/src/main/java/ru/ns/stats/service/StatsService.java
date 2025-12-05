package ru.ns.stats.service;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import ru.ns.stats.model.HitResult;
import ru.ns.stats.model.Stats;
import ru.ns.stats.repository.HitResultDAO;

import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.logging.Level;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class StatsService {
    private static final Logger logger = Logger.getLogger(StatsService.class.getName());
    private final Gson gson = new Gson();
    private Stats stats = new Stats();


    @PostConstruct
    public void init() {
        try {
            int count = 0;
            for (HitResult hitResult : new HitResultDAO().getAllResults()) {
                updateStatistics(hitResult);
                count++;
            }
            logger.info("StatsService initialized successfully with " + count + " records");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error loading hit results: " + e.getMessage(), e);
        }
    }


    public void recordStatistics(String data) {
        if (data != null && !data.isEmpty()) {
            try {
                HitResult hitResult = gson.fromJson(data, HitResult.class);
                if (hitResult != null) {
                    updateStatistics(hitResult);
                    logger.fine("Recorded statistics for hit result ID: " + hitResult.getId());
                }
            } catch (JsonSyntaxException e) {
                logger.log(Level.SEVERE, "Error parsing JSON: " + e.getMessage(), e);
            }
        }
    }

    private void updateStatistics(HitResult hitResult) {
        stats.setAverageXCoordinate(
            (stats.getAverageXCoordinate() *
            (stats.getHits() + stats.getMisses()) + hitResult.getX()) /
            ((stats.getHits() + stats.getMisses()) + 1)
        );

        stats.setAverageYCoordinate(
            (stats.getAverageYCoordinate() *
            (stats.getHits() + stats.getMisses()) + hitResult.getY()) /
            ((stats.getHits() + stats.getMisses()) + 1)
        );

        stats.setAverageRadius(
            (stats.getAverageRadius() *
            (stats.getHits() + stats.getMisses()) + hitResult.getR()) /
            ((stats.getHits() + stats.getMisses()) + 1)
        );

        if (hitResult.getMaxMissR() == 0) {
            stats.setAlwaysHitAmount(stats.getAlwaysHitAmount() + 1);
        }
        if (hitResult.getMaxMissR() >= 4) {
            stats.setAlwaysMissAmount(stats.getAlwaysMissAmount() + 1);
        }

        stats.setAverageDistanceFromCenter(
            (stats.getAverageDistanceFromCenter() *
            (stats.getHits() + stats.getMisses()) +
            Math.sqrt(
                Math.pow(hitResult.getX(), 2) +
                Math.pow(hitResult.getY(), 2)
            )) /
            ((stats.getHits() + stats.getMisses()) + 1)
        );

        if (hitResult.getHit()) {
            stats.setHits(stats.getHits() + 1);
        } else {
            stats.setMisses(stats.getMisses() + 1);
        }
    }

    public String getStatisticsJson() {
        logger.fine("Retrieving statistics JSON, total records: " + (stats.getHits() + stats.getMisses()));
        return gson.toJson(stats);
    }
}