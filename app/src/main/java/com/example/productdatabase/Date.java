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
    static SimpleDateFormat dateformat = new SimpleDateFormat("ss/mm/HH/dd/MM/yyyy<Z");

    public static String getDate() {

        return dateformat.format(Calendar.getInstance().getTime());
    }

    public static java.util.Date toDate(String s) {
        try {
            return dateformat.parse(s);

        }catch(Exception e){
            e.printStackTrace();
            return Calendar.getInstance().getTime();
        }
    }
    public static String toString(java.util.Date d){
        return dateformat.format(d);
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