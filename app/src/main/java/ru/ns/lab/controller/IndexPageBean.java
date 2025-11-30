package ru.ns.lab.controller;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class IndexPageBean {
    private String fullName = "Осипов Вячеслав Витальевич";
    private String group = "P3219";
    private String variant = "6386";


    public String getFullName() {
        return fullName;
    }

    public String getGroup() {
        return group;
    }

    public String getVariant() {
        return variant;
    }

    public String getTimestamp() {
        return ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
    }

}
