package jforgame.admin.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    /**
     * 一毫秒
     */
    public static final long ONE_MILLSECOND = 1L;
    /**
     * 一秒的毫秒数
     */
    public static final long ONE_SECOND = ONE_MILLSECOND * 1000;
    /**
     * 一分的毫秒数
     */
    public static final long ONE_MINUTE = ONE_SECOND * 60;
    /**
     * 一时的毫秒数
     */
    public static final long ONE_HOUR = ONE_MINUTE * 60;
    /**
     * 一天的毫秒数
     */
    public static final long ONE_DAY = ONE_HOUR * 24;
    /**
     * 一天总共有{@value}小时
     */
    public static final int HOURS_OF_DAY = 24;
    private static final String baseFmt = "yyyy-MM-dd HH:mm:ss";
    private static final ThreadLocal<SimpleDateFormat> dateFormat = new ThreadLocal() {
        @Override
        public DateFormat initialValue() {
            return new SimpleDateFormat(baseFmt);
        }
    };

    public static String format(Date date) {
        return dateFormat.get().format(date);
    }
}
