package com.example.productdatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Date {


    static Calendar currentTime = Calendar.getInstance();
    static SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMMM");

    public static String getDate() {

        return dateformat.format(Calendar.getInstance().getTime());
    }

    public static String getDays(String date) {

        try {
            return String.valueOf(TimeUnit.DAYS.convert(dateformat.parse("").getTime() - currentTime.getTimeInMillis(), TimeUnit.DAYS));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static long getHour() {

        return currentTime.get(Calendar.HOUR_OF_DAY);
    }
}