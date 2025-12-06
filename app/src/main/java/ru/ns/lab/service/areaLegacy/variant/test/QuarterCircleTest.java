package ru.ns.lab.service.areaLegacy.variant.test;

import java.util.ArrayList;
import java.util.List;

import ru.ns.lab.service.areaLegacy.abs.AbstractAreaComplex;
import ru.ns.lab.service.areaLegacy.abs.IArea;
import ru.ns.lab.service.areaLegacy.base.PlaneArea;
import ru.ns.lab.service.areaLegacy.complex.QuarterCircle;

public class QuarterCircleTest extends AbstractAreaComplex {
    private IArea quarterCircle1, quarterCircle2, quarterCircle3, quarterCircle4;


    public QuarterCircleTest() {
        quarterCircle2 = new QuarterCircle(-0.5f, -0.5f, 0.5f, 2);
        quarterCircle3 = new QuarterCircle(0, 1, 0.5f, 3);
        quarterCircle1 = new QuarterCircle(0.5f, 0, 0.5f, 1);
        quarterCircle4 = new QuarterCircle(0, -0.5f, 0.5f, 4);
        area = new PlaneArea(false, new ArrayList<>(List.of(QuarterCircleTest.class.getSimpleName())));

        quarterCircle1.disableInside();
        quarterCircle2.disableBorder();
        quarterCircle4.disableBorder();
        quarterCircle4.disableInside();

        area.extend(quarterCircle1);
        area.extend(quarterCircle2);
        area.extend(quarterCircle3);
        area.extend(quarterCircle4);
    }

    
    @Override
    public void disableBorder() {
        
    }

    @Override
    public void enableBorder() {
        
    }

    @Override
    public void disableInside() {
        
    }

    @Override
    public void enableInside() {
        
    }
}
