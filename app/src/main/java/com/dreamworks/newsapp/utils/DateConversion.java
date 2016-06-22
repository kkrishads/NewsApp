package com.dreamworks.newsapp.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateConversion {

    public static String getFromDate(Calendar calendar) {
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(calendar.getTimeInMillis());
    }

    public static String getToDate(Calendar calendar) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date lastDayOfMonth = calendar.getTime();
        return dateFormat.format(lastDayOfMonth);
    }

    public static String getMonth(Calendar calendar) {
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMM", Locale.getDefault());
        Date lastDayOfMonth = calendar.getTime();
        return monthFormat.format(lastDayOfMonth);
    }

    public static String getYear(Calendar calendar) {
        SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
        Date lastDayOfMonth = calendar.getTime();
        return monthFormat.format(lastDayOfMonth);
    }

    public static String getDayShortForm(Calendar calendar){
        SimpleDateFormat dayShortFormat = new SimpleDateFormat("EEE",Locale.getDefault());
        Date dayFromDate = calendar.getTime();
        return dayShortFormat.format(dayFromDate);
    }

    public static Date stringToDate(String strDate, String parseFormat) {
        DateFormat formatter;
        Date date = null;
        formatter = new SimpleDateFormat(parseFormat, Locale.getDefault());
        try {
            date = (Date) formatter.parse(strDate);
        } catch (ParseException e) {
            date = new Date(strDate);
        }
        return date;
    }

    public static String getFullDate(long time) {
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL);
        return dateFormat.format(new Date(time));
    }

    public static long getTimeFromString(String dateTime, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        try {
            Date date = sdf.parse(dateTime);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getTimeFromDate(Date date, String format) {
        return getTimeFromDate(date, format, TimeZone.getDefault());
    }

    public static String getTimeFromLong(long time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        return sdf.format(new Date(time));
    }

    public static String getTimeFromDate(Date date, String format, TimeZone timeZone) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        if (timeZone != null) {
            sdf.setTimeZone(timeZone);
        }
        return sdf.format(date);
    }

    public static long getCurrentTime(String timeZone) {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
        return sdf.getCalendar().getTimeInMillis();
    }

    /**
     * The time(long) value is seconds not millis
     *
     * @param timeZone String representation of time format
     * @param time     time as long value in seconds
     * @return time time as long in seconds
     */
    // GMT0
    public static long getLocalizedTime(String timeZone, String time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
        long millist = 0;
        try {
            Date date = sdf.parse(time);
            millist = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return millist;
    }

    /**
     * This is for GLOBAL
     */
    public static long getTimeForGMT0(String time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("GMT0"));
        long millist = 0;
        try {
            Date date = sdf.parse(time);
            millist = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return millist;
    }

    /**
     * This is for ony Mingl App 2014-05-19 11:38:03
     */
    /*
     * public static String getTimeForApp(String time, String requiredFormat) {
     * SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
     * Locale.US); sdf.setTimeZone(TimeZone.getTimeZone("GMT0")); Date date =
     * null; try { date = sdf.parse(time); } catch (ParseException e) {
     * e.printStackTrace(); } return new SimpleDateFormat(requiredFormat,
     * Locale.getDefault()).format(date); }
     */
    public static long getDateDiff(long time, long current) {
        long diff = Math.max(time, current) - Math.min(time, current);
        long days = diff / (1000 * 60 * 60 * 24);
        return days;
    }

    /**
     * This is for ony Mingl App 2014-05-19 11:38:03
     */
    public static long convertToMillis(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        long millis = 0l;
        try {
            Date date = sdf.parse(time);
            millis = date.getTime();
        } catch (ParseException e) {
            //e.printStackTrace();
        }
        return millis;
    }

    /**
     * It converts to GMT-0
     */
    public static long getTimeMillisForApp(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT0"));
        long millis = 0l;
        try {
            Date date = sdf.parse(time);
            millis = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return millis;
    }

    public static long getLocalizedTime(String timeZone, long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss aaa", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
        String dateS = sdf.format(new Date(time));
        return DateConversion.stringToDate(dateS, "dd-MM-yyyy hh:mm:ss aaa").getTime();
    }

    public static long getLocalizedTime(String time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        sdf.setTimeZone(TimeZone.getDefault());
        long millist = 0;
        try {
            Date date = sdf.parse(time);
            millist = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return millist;
    }

    public static String getDateFromString(String dateInString, String actualformat, String exceptedFormat) {
        SimpleDateFormat form = new SimpleDateFormat(actualformat, Locale.getDefault());
        form.setTimeZone(TimeZone.getTimeZone("GMT"));
        String formatedDate = null;
        Date date;
        try {
            date = form.parse(dateInString);
            SimpleDateFormat postFormater = new SimpleDateFormat(exceptedFormat, Locale.getDefault());
            formatedDate = postFormater.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formatedDate;
    }
}
