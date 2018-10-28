package com.base.util.comm;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {

    public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat DATE_FORMAT_DATE    = new SimpleDateFormat("yyyy-MM-dd");

    private TimeUtils() {
        throw new AssertionError();
    }

    /**
     * long time to string
     * 
     * @param timeInMillis
     * @param dateFormat
     * @return
     */
    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis));
    }

    /**
     * long time to string, format is {@link #DEFAULT_DATE_FORMAT}
     * 
     * @param timeInMillis
     * @return
     */
    public static String getTime(long timeInMillis) {
        return getTime(timeInMillis, DEFAULT_DATE_FORMAT);
    }

    /**
     * get current time in milliseconds
     * 
     * @return
     */
    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    /**
     * get current time in milliseconds, format is {@link #DEFAULT_DATE_FORMAT}
     * 
     * @return
     */
    public static String getCurrentTimeInString() {
        return getTime(getCurrentTimeInLong());
    }

    /**
     * get current time in milliseconds
     * 
     * use getNow(String) instead
     * 
     * @return
     */
    @Deprecated
    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
        return getTime(getCurrentTimeInLong(), dateFormat);
    }
    /**
     * get current time
     * 
     * @param format
     * @return
     */
    public static String getNow(String format) {
    	return getTime(getCurrentTimeInLong(), new SimpleDateFormat(format));
    }
    
    /**
     * 获得昨天的日期
     * @param format
     * @return
     */
    public static String getYesterday(String format) {
    	return getTime(getCurrentTimeInLong()-3600000*24, new SimpleDateFormat(format));
    }
    
    /**
     * 获得一个星期前的日期
     * @param format
     * @return
     */
    public static String getAWeekAgo(String format) {
    	return getTime(getCurrentTimeInLong()-3600000*24*7, new SimpleDateFormat(format));
    }
    
}
