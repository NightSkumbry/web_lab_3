package ru.ns.lab.service.area.abs;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
// import java.util.logging.Logger;

public abstract class AbstractAreaBase extends AbstractArea {
    // private final Logger logger = Logger.getLogger("FCGIApp");
    private HashSet<IArea> restrictions = new HashSet<>();
    private HashSet<IArea> extensions = new HashSet<>();
    private String areaPath;
    private String logString = "";
    private boolean border = true;
    private boolean inside = true;


    protected AbstractAreaBase(List<String> areaPath) {
        Collections.reverse(areaPath);
        this.areaPath = String.join(" : ", areaPath);
        // logger.info(this.areaPath);
    }


    public void restrict(IArea area) {
        restrictions.add(area);
    }

    public void extend(IArea area) {
        extensions.add(area);
    }
    

    @Override
    public String getAreaPath() {
        return areaPath;
    }

    @Override
    public String getLog() {
        return logString;
    }

    @Override
    public boolean checkHit(double X, double Y, double R) {
        return checkHit(X, Y, R, 0);
    }

    @Override
    public boolean checkHit(double X, double Y, double R, int nestedLevel) {

        for (IArea area : restrictions) {
            if (area.checkHit(X, Y, R, nestedLevel + 1)) {
                logString = "✕  " + "_ ".repeat(nestedLevel) + getAreaPath() + " - " + "restriction hit\n" + area.getLog();
                return false;
            }
        }
        for (IArea area : extensions) {
            if (area.checkHit(X, Y, R, nestedLevel + 1)) {
                logString = "✓  " + "_ ".repeat(nestedLevel) + getAreaPath() + " - " + "extension hit\n" + area.getLog();
                return true;
            }
        }

        boolean onBorder = isOnBorder(X, Y, R);
        boolean hit = inside && (isInside(X, Y, R) ^ (isInverse() && !onBorder)) || border && onBorder;
        logString = hit ? "✓  " + "_ ".repeat(nestedLevel) + getAreaPath() + " - " + "self hit\n" : "✕  " + "_ ".repeat(nestedLevel) + getAreaPath() + " - " + "self miss\n";
        return hit;
    }

    @Override
    public void disableBorder() { border = false; }
    
    @Override
    public void enableBorder() { border = true; }

    @Override
    public void disableInside() { inside = false; }

    @Override
    public void enableInside() { inside = true; }


    protected abstract boolean isOnBorder(double X, double Y, double R);
    protected abstract boolean isInside(double X, double Y, double R);
}
