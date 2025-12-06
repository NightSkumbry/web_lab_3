package ru.ns.lab.service.areaLegacy.abs;

import java.util.List;

public abstract class AbstractArea implements IArea {
    private boolean inverse = false;


    protected static List<String> updatePath(List<String> path, String name) {
        path.add(name);
        return path;
    }

    @Override
    public void inverseInside() { inverse = true; }

    @Override
    public void restoreInside() { inverse = false; }

    public boolean isInverse() { return inverse; }


    protected abstract String getAreaPath();
}
