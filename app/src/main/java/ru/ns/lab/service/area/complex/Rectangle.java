package ru.ns.lab.service.area.complex;

import java.util.ArrayList;
import java.util.List;

import ru.ns.lab.service.area.abs.AbstractAreaComplex;
import ru.ns.lab.service.area.abs.IArea;
import ru.ns.lab.service.area.base.LineSegment;
import ru.ns.lab.service.area.base.PlaneArea;

public class Rectangle extends AbstractAreaComplex {
    private IArea triangle1, triangle2;
    private IArea connector, antiConnector;


    public Rectangle(float x1, float y1, float x2, float y2) {
        this(x1, y1, x2, y2, new ArrayList<>(List.of()));
    }

    public Rectangle(float x1, float y1, float x2, float y2, List<String> path) {
        triangle1 = new Triangle(x1, y1, x1, y2, x2, y1);
        triangle2 = new Triangle(x2, y2, x1, y2, x2, y1);
        connector = new LineSegment(x1, y2, x2, y1);
        antiConnector = new LineSegment(x1, y2, x2, y1);
        area = new PlaneArea(false, updatePath(path, Rectangle.class.getSimpleName()));

        antiConnector.disableBorder();
        enableInside();

        area.extend(triangle1);
        area.extend(triangle2);
        area.extend(connector);
        area.restrict(antiConnector);
    }


    @Override
    public void disableBorder() {
        triangle1.disableBorder();
        triangle2.disableBorder();
        connector.disableBorder();
    }

    @Override
    public void enableBorder() {
        triangle1.enableBorder();
        triangle2.enableBorder();
        connector.enableBorder();
    }

    @Override
    public void disableInside() {
        triangle1.disableInside();
        triangle2.disableInside();
        connector.disableInside();
        antiConnector.enableInside();
    }

    @Override
    public void enableInside() {
        triangle1.enableInside();
        triangle2.enableInside();
        connector.enableInside();
        antiConnector.disableInside();
    }
}
