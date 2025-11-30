package ru.ns.lab.service.area.complex.quarter;

import java.util.ArrayList;
import java.util.List;

import ru.ns.lab.service.area.abs.AbstractAreaComplex;
import ru.ns.lab.service.area.abs.IArea;
import ru.ns.lab.service.area.base.LineArea;
import ru.ns.lab.service.area.base.PlaneArea;

public class FirstQuarter extends AbstractAreaComplex {
    private IArea horizontalLine, horizontalBorder;
    private IArea verticalLine, verticalBorder;


    public FirstQuarter() {
        this(0, 0, new ArrayList<>(List.of()));
    }

    public FirstQuarter(List<String> path) {
        this(0, 0, path);
    }
    
    public FirstQuarter(float x, float y) {
        this(x, y, new ArrayList<>(List.of()));
    }
    
    public FirstQuarter(float x, float y, List<String> path) {
        horizontalLine = LineArea.fromPoints(x, y, x+1, y);
        verticalLine = LineArea.fromPoints(x, y, x, y-1);
        horizontalBorder = LineArea.fromPoints(x, y, x+1, y);
        verticalBorder = LineArea.fromPoints(x, y, x, y-1);
        area = new PlaneArea(true, updatePath(path, FirstQuarter.class.getSimpleName()));

        enableBorder();
        horizontalBorder.disableInside();
        verticalBorder.disableInside();
        
        area.restrict(verticalLine);
        area.restrict(horizontalLine);
        area.extend(horizontalBorder);
        area.extend(verticalBorder);
    }


    @Override
    public void disableBorder() {
        horizontalLine.enableBorder();
        verticalLine.enableBorder();
    }
    
    @Override
    public void enableBorder() {
        horizontalLine.disableBorder();
        verticalLine.disableBorder();
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
