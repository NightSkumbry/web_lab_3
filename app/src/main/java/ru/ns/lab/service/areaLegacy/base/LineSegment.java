package ru.ns.lab.service.areaLegacy.base;

import java.util.ArrayList;
import java.util.List;

import ru.ns.lab.service.areaLegacy.abs.AbstractAreaBase;

public class LineSegment extends AbstractAreaBase {
    float x1, y1;
    float x2, y2;
    float a;
    float b;
    float c;


    public LineSegment(float x1, float y1, float x2, float y2, List<String> path) {
        super(updatePath(path, LineSegment.class.getSimpleName()));

        this.a = y2 - y1;
        this.b = x1 - x2;
        this.c = x2*y1 - x1*y2;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public LineSegment(float x1, float y1, float x2, float y2) {
        this(x1, y1, x2, y2, new ArrayList<>(List.of()));
    }


    @Override
    protected boolean isInside(double X, double Y, double R) {
        boolean onLine = a*X + b*Y + c*R == 0f;
        boolean xBounds = X > Math.min(x1, x2)*R && X < Math.max(x1, x2)*R;
        boolean yBounds = Y > Math.min(y1, y2)*R && Y < Math.max(y1, y2)*R;
        return onLine && (xBounds || yBounds);
    }

    @Override
    protected boolean isOnBorder(double X, double Y, double R) {
        boolean isFirstPoint = X == x1*R && Y == y1*R;
        boolean isSecondPoint = X == x2*R && Y == y2*R;
        return isFirstPoint || isSecondPoint;
    }
}
