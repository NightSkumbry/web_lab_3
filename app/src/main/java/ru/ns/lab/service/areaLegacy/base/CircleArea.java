package ru.ns.lab.service.areaLegacy.base;

import java.util.ArrayList;
import java.util.List;

import ru.ns.lab.service.areaLegacy.abs.AbstractAreaBase;

public class CircleArea extends AbstractAreaBase {
    private float x1;
    private float y1;
    private float r;


    public CircleArea(float x1, float y1, float r, List<String> path) {
        super(updatePath(path, CircleArea.class.getSimpleName()));
        this.x1 = x1;
        this.y1 = y1;
        this.r = r;
    }

    public CircleArea(float x1, float y1, float r) {
        this(x1, y1, r, new ArrayList<>(List.of()));
    }


    @Override
    protected boolean isInside(double X, double Y, double R) {
        return Math.pow(X - x1*R, 2) + Math.pow(Y - y1*R, 2) < Math.pow(r*R, 2);
    }

    @Override
    protected boolean isOnBorder(double X, double Y, double R) {
        return Math.pow(X - x1*R, 2) + Math.pow(Y - y1*R, 2) == Math.pow(r*R, 2);

    }
}
