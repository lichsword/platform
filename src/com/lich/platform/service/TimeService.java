package com.lich.platform.service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: lichsword
 * Date: 14-6-16
 * Time: 下午12:44
 * <p/>
 * TODO
 */
public class TimeService implements ITime {

    public TimeService() {

    }

    @Override
    public String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = new Date();
        String dd = format.format(d);
        return dd;
    }
}
