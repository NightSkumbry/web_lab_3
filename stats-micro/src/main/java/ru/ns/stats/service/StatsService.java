package ru.ns.stats.service;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import ru.ns.stats.model.HitResult;
import ru.ns.stats.model.Stats;
import ru.ns.stats.repository.HitResultDAO;

import java.sql.SQLException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class StatsService {
    private final Gson gson = new Gson();
    private Stats stats = new Stats();


    @PostConstruct
    public void init() {
        try {
            for (HitResult hitResult : new HitResultDAO().getAllResults()) {
                updateStatistics(hitResult);
            }
        } catch (SQLException e) {
            System.err.println("Error loading hit results: " + e.getMessage());
        }
    }


    public void recordStatistics(String data) {
        if (data != null && !data.isEmpty()) {
            try {
                HitResult hitResult = gson.fromJson(data, HitResult.class);
                if (hitResult != null) {
                    updateStatistics(hitResult);
                }
            } catch (JsonSyntaxException e) {
                System.err.println("Error parsing JSON: " + e.getMessage());
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
        return gson.toJson(stats);
    }
}