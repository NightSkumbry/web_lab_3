package ru.ns.lab.service.areaLegacy.abs;

public interface IArea {
    boolean checkHit(double X, double Y, double R);
    boolean checkHit(double X, double Y, double R, int nestedLevel);

    String getLog();

    void enableInside();
    void disableInside();
    void enableBorder();
    void disableBorder();
    void inverseInside();
    void restoreInside();
    boolean isInverse();

    default void setInverse (boolean inverse) {
        if (inverse) inverseInside();
        else restoreInside();
    }
}
