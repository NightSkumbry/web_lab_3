package ru.ns.lab.service.area.variant.test;

import java.util.ArrayList;
import java.util.List;

import ru.ns.lab.service.area.abs.AbstractAreaComplex;
import ru.ns.lab.service.area.abs.IArea;
import ru.ns.lab.service.area.base.LineSegment;
import ru.ns.lab.service.area.base.PlaneArea;

public class LineSegmentTest extends AbstractAreaComplex {
    private IArea lineSegment1, lineSegment2, lineSegment3, lineSegment4, lineSegment5;


    public LineSegmentTest() {
        lineSegment2 = new LineSegment(0.5f, 0, 0, 0.5f);
        lineSegment3 = new LineSegment(1, 0, 0, 0.5f);
        lineSegment1 = new LineSegment(-1, 0, 0, 1);
        lineSegment4 = new LineSegment(-0.5f, -0.5f, 1, -1);
        lineSegment5 = new LineSegment(-1, -0.5f, -0.5f, -1);
        area = new PlaneArea(false, new ArrayList<>(List.of(LineSegmentTest.class.getSimpleName())));

        lineSegment1.disableInside();
        lineSegment4.disableBorder();
        lineSegment5.disableBorder();
        lineSegment5.disableInside();

        area.extend(lineSegment1);
        area.extend(lineSegment2);
        area.extend(lineSegment3);
        area.extend(lineSegment4);
        area.extend(lineSegment5);
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
