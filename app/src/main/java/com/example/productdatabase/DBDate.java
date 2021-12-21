package com.example.productdatabase;

import static java.util.Calendar.getInstance;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DBDate {
    static Calendar currentTime = getInstance();
    static SimpleDateFormat dateformat = new SimpleDateFormat("ss/mm/HH/dd/MM/yyyy<Z");

    public static String getDateS() {
        //currentTime=getInstance();
        //Date Calendar.getInstance().getTime();
        //java.util.Date date=getInstance().getTime();
        return dateformat.format(Calendar.getInstance().getTime());
    }
    public static Date getDate(){
        return Calendar.getInstance().getTime();
    }
    public static Date toDate(String s) {
        try {
            return dateformat.parse(s);

        }catch(Exception e){
            e.printStackTrace();
            return getInstance().getTime();
        }
    }
    public static String toString(Date d){
        return dateformat.format(d);
    }
}