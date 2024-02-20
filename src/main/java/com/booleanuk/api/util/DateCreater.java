package com.booleanuk.api.util;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateCreater {

    public static String getCurrentDate() {
        ZonedDateTime zonedDateTimeNow = ZonedDateTime.now(ZoneId.of("UTC"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy - HH:mm:ss Z");

        return zonedDateTimeNow.format(formatter);
    }


}