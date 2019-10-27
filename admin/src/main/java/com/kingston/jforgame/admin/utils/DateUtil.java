package com.kingston.jforgame.admin.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    private static ThreadLocal<SimpleDateFormat> dateFormat = new ThreadLocal() {
        @Override
        public DateFormat initialValue() {
            return new SimpleDateFormat(baseFmt);
        }
    };


    private static String baseFmt = "yyyy-MM-dd hh:MM:ss";

    public static String format(Date date) {
        return dateFormat.get().format(date);
    }
}
