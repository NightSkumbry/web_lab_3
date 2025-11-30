package ru.ns.lab.service.params;

public class Params {
    private final double R;
    private final double Y;
    private final double X;

    public Params(double x, double y, double r) {
        this.X = x;
        this.Y = y;
        this.R = r;
    }

    public Params() {
        this.X = 0;
        this.Y = 0;
        this.R = 1;
    }


    public double getR() { return R; }
    public double getY() { return Y; }
    public double getX() { return X; }


    @Override
    public String toString() {
        return "Params{" +
            "R=" + R +
            ", Y=" + Y +
            ", X=" + X +
            '}';
    }

}