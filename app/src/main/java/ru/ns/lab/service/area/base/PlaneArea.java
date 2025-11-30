package ru.ns.lab.service.area.base;

import java.util.ArrayList;
import java.util.List;

import ru.ns.lab.service.area.abs.AbstractAreaBase;

public class PlaneArea extends AbstractAreaBase {

    public PlaneArea(boolean fill, List<String> path) {
        super(updatePath(path, PlaneArea.class.getSimpleName()));
        disableBorder();
        if (!fill) disableInside();
    }

    public PlaneArea(boolean fill) {
        this(fill, new ArrayList<>(List.of()));
    }


    @Override
    public boolean isInside(double X, double Y, double R) {
        return true;
    }

    @Override
    public boolean isOnBorder(double X, double Y, double R) {
        return false;
    }
}
