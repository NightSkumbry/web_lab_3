package ru.ns.common.model;

import java.io.Serializable;

public class HitResult implements Serializable {
    private int id;
    private final Double x;
    private final Double y;
    private final Double r;
    private final Boolean hit;
    private final Double maxMissR;

    public HitResult(int id, Double x, Double y, Double r, Boolean hit, Double maxMissR) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.r = r;
        this.hit = hit;
        this.maxMissR = maxMissR;
    }

    public HitResult(Double x, Double y, Double r, Boolean hit, Double maxMissR) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.hit = hit;
        this.maxMissR = maxMissR;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    public Double getR() {
        return r;
    }

    public Boolean getHit() {
        return hit;
    }

    public Double getMaxMissR() {
        return maxMissR;
    }
}
