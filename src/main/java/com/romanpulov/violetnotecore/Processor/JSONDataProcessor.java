package com.romanpulov.violetnotecore.Processor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class JSONDataProcessor {
    protected static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    public static String formatDate(Date date) {
        return date == null ? null : df.format(date);
    }

    public static Date parseDate(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            return null;
        } else {
            try {
                return df.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
