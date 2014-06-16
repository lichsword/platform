package com.lich.platform.service;

import java.sql.ResultSet;

/**
 * Created with IntelliJ IDEA.
 * User: lichsword
 * Date: 14-6-16
 * Time: 下午4:03
 * <p/>
 * TODO
 */
public interface IDatabase {

    public String handleMsg(String msg);

    public ResultSet query(String table, String whereClause, String whereArgs);

}
