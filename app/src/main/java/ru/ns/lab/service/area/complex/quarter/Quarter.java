package ru.ns.lab.service.area.complex.quarter;

import java.util.ArrayList;
import java.util.List;

import ru.ns.lab.service.area.abs.AbstractAreaComplex;
import ru.ns.lab.service.area.abs.IArea;
import ru.ns.lab.service.area.base.PlaneArea;

public class Quarter extends AbstractAreaComplex {
    private IArea quarter;


    public Quarter(int quarterNumber) {
        this(0, 0, quarterNumber, new ArrayList<>(List.of()));
    }

    public Quarter(int quarterNumber, List<String> path) {
        this(0, 0, quarterNumber, path);
    }

    public Quarter(float x, float y, int quarterNumber) {
        this(x, y, quarterNumber, new ArrayList<>(List.of()));
    }

    public Quarter(float x, float y, int quarterNumber, List<String> path) {
        area = new PlaneArea(false, path);
        switch (quarterNumber) {
            case 1:
                quarter = new FirstQuarter(x, y);
                break;
            case 2:
                quarter = new SecondQuarter(x, y);
                break;
            case 3:
                quarter = new ThirdQuarter(x, y);
                break;
            case 4:
                quarter = new FourthQuarter(x, y);
                break;
            default:
                throw new IllegalArgumentException("Quarter must be in range [1;4]");
        }

        area.extend(quarter);
    }

    
    @Override
    public void disableBorder() {
        quarter.disableBorder();
    }

    @Override
    public void enableBorder() {
        quarter.enableBorder();
    }

    @Override
    public void disableInside() {
        quarter.disableInside();
    }

    @Override
    public void enableInside() {
        quarter.enableInside();
    }
}
