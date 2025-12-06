package ru.ns.lab.service.areaLegacy.base;

import java.util.ArrayList;
import java.util.List;

import ru.ns.lab.service.areaLegacy.abs.AbstractAreaBase;

public class LineArea extends AbstractAreaBase {
    private float a;
    private float b;
    private float c;


    public LineArea(float a, float b, float c, List<String> path) {
        super(updatePath(path, LineArea.class.getSimpleName()));
        
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public LineArea(float a, float b, float c) {
        this(a, b, c, new ArrayList<>(List.of()));
    }

    public static LineArea fromPoints(float x1, float y1, float x2, float y2) {
        float a = y2 - y1;
        float b = x1 - x2;
        float c = x2*y1 - x1*y2;
        return new LineArea(a, b, c, new ArrayList<>(List.of()));
    }

    public static LineArea fromPoints(float x1, float y1, float x2, float y2, List<String> path) {
        float a = y2 - y1;
        float b = x1 - x2;
        float c = x2*y1 - x1*y2;
        return new LineArea(a, b, c, path);
    }


    @Override
    protected boolean isInside(double X, double Y, double R) {
        return a*X + b*Y + c*R > 0;
    }

    @Override
    protected boolean isOnBorder(double X, double Y, double R) {
        return a*X + b*Y + c*R == 0;
    }
}
