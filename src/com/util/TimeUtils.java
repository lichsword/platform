package com.util;

/**
 * Created with IntelliJ IDEA.
 * User: lichsword
 * Date: 14-6-20
 * Time: 下午3:26
 * <p/>
 * TODO
 */
public class TimeUtils {

    public static final long UNIT_MILLISECOND = 1000;
    public static final long UNIT_SECOND = 1 * UNIT_MILLISECOND;
    public static final long UNIT_MINUTE = 60 * UNIT_SECOND;
    public static final long UNIT_HOUR = 60 * UNIT_MINUTE;
    public static final long UNIT_DAY = 24 * UNIT_HOUR;

    private static final String FORMAT_TIME_1 = "%d min %d sec";
    private static final String FORMAT_TIME_2 = "%d sec";

    public static String format(long start, long end) {
        return format(end - start);
    }

    /**
     * @param delta
     * @return
     */
    public static String format(long delta) {
        long second = delta / UNIT_SECOND;
        long minute = delta / UNIT_MINUTE;
        if (0 != minute) {
            return String.format(FORMAT_TIME_1, minute, second);
        } else {
            return String.format(FORMAT_TIME_2, second);
        }

    }

}
