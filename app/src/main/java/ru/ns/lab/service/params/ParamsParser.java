package ru.ns.lab.service.params;

public class ParamsParser {
    public ParamsParser() {}

    public double parseX(String xStr) throws ParamsException {
        try {
            Double.parseDouble(xStr);
            String x = xStr.substring(0, Math.min(xStr.length(), xStr.startsWith("-") ? 11 : 10));
            return Double.parseDouble(x);
        } catch (NumberFormatException e) {
            throw new ParamsException("Invalid X format: " + xStr);
        }
    }

    public double parseY(String yStr) throws ParamsException {
        try {
            Double.parseDouble(yStr);
            String y = yStr.substring(0, Math.min(yStr.length(), yStr.startsWith("-") ? 11 : 10));
            return Double.parseDouble(y);
        } catch (NumberFormatException e) {
            throw new ParamsException("Invalid Y format: " + yStr);
        }
    }

    public double parseR(String rStr) throws ParamsException {
        try {
            Double.parseDouble(rStr);
            String r = rStr.substring(0, Math.min(rStr.length(), rStr.startsWith("-") ? 11 : 10));
            return Double.parseDouble(r);
        } catch (NumberFormatException e) {
            throw new ParamsException("Invalid R format: " + rStr);
        }
    }
}
