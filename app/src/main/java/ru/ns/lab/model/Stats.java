package ru.ns.lab.model;

import java.io.Serializable;

public class Stats implements Serializable {
    private int hits;
    private int misses;
    private double averageDistanceFromCenter;
    private double averageXCoordinate;
    private double averageYCoordinate;
    private double averageRadius;
    private int alwaysHitAmount;
    private int alwaysMissAmount;

    public Stats() {}

    public Stats(int hits, int misses, double averageDistanceFromCenter,
            double averageXCoordinate, double averageYCoordinate,
            double averageRadius, int alwaysHitAmount, int alwaysMissAmount) {
        this.hits = hits;
        this.misses = misses;
        this.averageDistanceFromCenter = averageDistanceFromCenter;
        this.averageXCoordinate = averageXCoordinate;
        this.averageYCoordinate = averageYCoordinate;
        this.averageRadius = averageRadius;
        this.alwaysHitAmount = alwaysHitAmount;
        this.alwaysMissAmount = alwaysMissAmount;
    }


    public int getHits() {
        return hits;
    }


    public void setHits(int hits) {
        this.hits = hits;
    }


    public int getMisses() {
        return misses;
    }


    public void setMisses(int misses) {
        this.misses = misses;
    }


    public double getAverageDistanceFromCenter() {
        return averageDistanceFromCenter;
    }


    public void setAverageDistanceFromCenter(double averageDistanceFromCenter) {
        this.averageDistanceFromCenter = averageDistanceFromCenter;
    }


    public double getAverageXCoordinate() {
        return averageXCoordinate;
    }


    public void setAverageXCoordinate(double averageXCoordinate) {
        this.averageXCoordinate = averageXCoordinate;
    }


    public double getAverageYCoordinate() {
        return averageYCoordinate;
    }


    public void setAverageYCoordinate(double averageYCoordinate) {
        this.averageYCoordinate = averageYCoordinate;
    }


    public double getAverageRadius() {
        return averageRadius;
    }


    public void setAverageRadius(double averageRadius) {
        this.averageRadius = averageRadius;
    }

    public int getAlwaysHitAmount() {
        return alwaysHitAmount;
    }

    public void setAlwaysHitAmount(int alwaysHitAmount) {
        this.alwaysHitAmount = alwaysHitAmount;
    }

    public int getAlwaysMissAmount() {
        return alwaysMissAmount;
    }

    public void setAlwaysMissAmount(int alwaysMissAmount) {
        this.alwaysMissAmount = alwaysMissAmount;
    }

    
}
