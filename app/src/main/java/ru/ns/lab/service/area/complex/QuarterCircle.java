package ru.ns.lab.service.area.complex;

import java.util.ArrayList;
import java.util.List;

import ru.ns.lab.service.area.abs.AbstractAreaComplex;
import ru.ns.lab.service.area.abs.IArea;
import ru.ns.lab.service.area.base.CircleArea;
import ru.ns.lab.service.area.base.PlaneArea;
import ru.ns.lab.service.area.complex.quarter.Quarter;

public class QuarterCircle extends AbstractAreaComplex {
    private IArea circle;
    private IArea quarter;
    private IArea quarterBorder;
    private IArea circleBorder;


    public QuarterCircle(float x1, float y1, float r, int quarterNumber) {
        this(x1, y1, r, quarterNumber, new ArrayList<>(List.of()));
    }

    public QuarterCircle(float x1, float y1, float r, int quarterNumber, List<String> path) {
        quarter = new Quarter(x1, y1, quarterNumber);
        quarterBorder = new Quarter(x1, y1, quarterNumber);
        circle = new CircleArea(x1, y1, r);
        circleBorder = new CircleArea(x1, y1, r);
        area = new PlaneArea(true, updatePath(path, QuarterCircle.class.getSimpleName()));

        
        enableBorder();
        circle.inverseInside();
        quarter.inverseInside();
        quarterBorder.disableInside();
        circleBorder.disableInside();
        
        area.restrict(circle);
        area.restrict(quarter);
        area.extend(quarterBorder);
        area.extend(circleBorder);
    }

    
    @Override
    public void disableBorder() {
        quarter.disableBorder();
        area.enableBorder();
    }

    @Override
    public void enableBorder() {
        quarter.enableBorder();
        area.disableBorder();
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
