package com.log;

import com.util.TimeUtils;

/**
 * Created with IntelliJ IDEA.
 * User: lichsword
 * Date: 14-6-20
 * Time: 下午3:40
 * <p/>
 * TODO
 */
public class TimeInfo {

    //    private long start;
//    private long end;
    private String report;

    public TimeInfo(long start, long end) {
//        this.start = start;
//        this.end = end;

        report = TimeUtils.format(start, end);
    }

    public String getReport() {
        return report;
    }


}