package com.db;

import java.sql.ResultSet;

/**
 * Created with IntelliJ IDEA.
 * User: lichsword
 * Date: 14-3-17
 * Time: 下午4:22
 * To change this res use File | Settings | File Templates.
 */
public interface ISqlStatement {

    public boolean insert(String sql);

    public boolean delete(String sql);

    public ResultSet query(String sql);

    public boolean update(String url);

}
