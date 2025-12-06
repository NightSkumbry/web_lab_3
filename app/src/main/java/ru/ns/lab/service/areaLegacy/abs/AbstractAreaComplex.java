package ru.ns.lab.service.areaLegacy.abs;

// import java.util.logging.Logger;

public abstract class AbstractAreaComplex extends AbstractArea {
    protected AbstractAreaBase area;
    // private final Logger logger = Logger.getLogger("FCGIApp");

    @Override
    public boolean checkHit(double X, double Y, double R) {
        return checkHit(X, Y, R, 0);
    }

    @Override
    public boolean checkHit(double X, double Y, double R, int nestedLevel) {
        return area.checkHit(X, Y, R, nestedLevel) ^ isInverse();
    }

    @Override
    public String getLog() {
        return area.getLog();
    }

    @Override
    public String getAreaPath() {
        return area.getAreaPath();
    }
}
