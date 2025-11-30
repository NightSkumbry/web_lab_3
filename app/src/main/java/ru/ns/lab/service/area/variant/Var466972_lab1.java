package ru.ns.lab.service.area.variant;

import java.util.ArrayList;
import java.util.List;

import ru.ns.lab.service.area.abs.AbstractAreaComplex;
import ru.ns.lab.service.area.abs.IArea;
import ru.ns.lab.service.area.base.LineSegment;
import ru.ns.lab.service.area.base.PlaneArea;
import ru.ns.lab.service.area.complex.QuarterCircle;
import ru.ns.lab.service.area.complex.Rectangle;
import ru.ns.lab.service.area.complex.Triangle;

public class Var466972_lab1 extends AbstractAreaComplex {
    private IArea rectangle, triangle, quarterCircle;
    private IArea connector1, connector2;
    private IArea antiConnector1, antiConnector2;


    public Var466972_lab1() {
        rectangle = new Rectangle(0, 0, 1, 0.5f, new ArrayList<>(List.of("466972 rectangle")));
        triangle = new Triangle(0, 0, 0, 1, -0.5f, 0, new ArrayList<>(List.of("466972 triangle")));
        quarterCircle = new QuarterCircle(0, 0, 0.5f, 3, new ArrayList<>(List.of("466972 quarterCircle")));
        connector1 = new LineSegment(-0.5f, 0, 0, 0, new ArrayList<>(List.of("466972 connector triangle-quarterCircle")));
        antiConnector1 = new LineSegment(-0.5f, 0, 0, 0, new ArrayList<>(List.of("466972 connector triangle-quarterCircle")));
        connector2 = new LineSegment(0, 0.5f, 0, 0, new ArrayList<>(List.of("466972 connector rectangle-triangle")));
        antiConnector2 = new LineSegment(0, 0.5f, 0, 0, new ArrayList<>(List.of("466972 connector rectangle-triangle")));
        area = new PlaneArea(false, new ArrayList<>(List.of(Var466972_lab1.class.getSimpleName())));

        antiConnector1.disableBorder();
        antiConnector2.disableBorder();
        enableInside();

        area.extend(rectangle);
        area.extend(triangle);
        area.extend(quarterCircle);
        area.extend(connector1);
        area.extend(connector2);
        area.restrict(antiConnector1);
        area.restrict(antiConnector2);
    }

    
    @Override
    public void disableBorder() {
        rectangle.disableBorder();
        triangle.disableBorder();
        quarterCircle.disableBorder();
        connector1.disableBorder();
        connector2.disableBorder();
    }

    @Override
    public void enableBorder() {
        rectangle.enableBorder();
        triangle.enableBorder();
        quarterCircle.enableBorder();
        connector1.enableBorder();
        connector2.enableBorder();
    }

    @Override
    public void disableInside() {
        rectangle.disableInside();
        triangle.disableInside();
        quarterCircle.disableInside();
        connector1.disableInside();
        connector2.disableInside();
        antiConnector1.enableInside();
        antiConnector2.enableInside();
    }

    @Override
    public void enableInside() {
        rectangle.enableInside();
        triangle.enableInside();
        quarterCircle.enableInside();
        connector1.enableInside();
        connector2.enableInside();
        antiConnector1.disableInside();
        antiConnector2.disableInside();
    }
}
