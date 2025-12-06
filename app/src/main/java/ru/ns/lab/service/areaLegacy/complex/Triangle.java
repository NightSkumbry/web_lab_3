package ru.ns.lab.service.areaLegacy.complex;

import java.util.ArrayList;
import java.util.List;

import ru.ns.lab.service.areaLegacy.abs.AbstractAreaComplex;
import ru.ns.lab.service.areaLegacy.abs.IArea;
import ru.ns.lab.service.areaLegacy.base.LineArea;
import ru.ns.lab.service.areaLegacy.base.PlaneArea;

public class Triangle extends AbstractAreaComplex {
    private IArea lineA, borderA;
    private IArea lineB, borderB;
    private IArea lineC, borderC;


    public Triangle(float x1, float y1, float x2, float y2, float x3, float y3) {
        this(x1, y1, x2, y2, x3, y3, new ArrayList<>(List.of()));
    }

    public Triangle(float x1, float y1, float x2, float y2, float x3, float y3, List<String> path) {
        float dotProduct = (x2 - x1) * (y3 - y1) - (y2 - y1) * (x3 - x1);
        if (dotProduct < 0) {
            float tmp;
            tmp = x2; x2 = x3; x3 = tmp;
            tmp = y2; y2 = y3; y3 = tmp;
        }

        area = new PlaneArea(true, updatePath(path, Triangle.class.getSimpleName()));
        lineA = LineArea.fromPoints(x1, y1, x2, y2);
        lineB = LineArea.fromPoints(x2, y2, x3, y3);
        lineC = LineArea.fromPoints(x3, y3, x1, y1);
        borderA = LineArea.fromPoints(x1, y1, x2, y2);
        borderB = LineArea.fromPoints(x2, y2, x3, y3);
        borderC = LineArea.fromPoints(x3, y3, x1, y1);

        enableBorder();
        borderA.disableInside();
        borderB.disableInside();
        borderC.disableInside();

        area.restrict(lineA);
        area.restrict(lineB);
        area.restrict(lineC);
        area.extend(borderA);
        area.extend(borderB);
        area.extend(borderC);
    }

    
    @Override
    public void disableBorder() {
        lineA.enableBorder();
        lineB.enableBorder();
        lineC.enableBorder();
    }

    @Override
    public void enableBorder() {
        lineA.disableBorder();
        lineB.disableBorder();
        lineC.disableBorder();
    }

    @Override
    public void disableInside() {
        area.disableInside();
    }

    @Override
    public void enableInside() {
        area.enableInside();
    }
}
