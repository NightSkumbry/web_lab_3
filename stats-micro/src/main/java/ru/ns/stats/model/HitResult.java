package ru.ns.stats.model;

import java.io.Serializable;

public class HitResult implements Serializable {
    private int id;
    private Double x;
    private Double y;
    private Double r;
    private Boolean hit;
    private Double maxMissR;


    public HitResult() {}

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

    public void setX(Double x) {
        this.x = x;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public void setR(Double r) {
        this.r = r;
    }

    public void setHit(Boolean hit) {
        this.hit = hit;
    }

    public void setMaxMissR(Double maxMissR) {
        this.maxMissR = maxMissR;
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
