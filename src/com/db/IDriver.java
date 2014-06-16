package com.db;

/**
 * Created with IntelliJ IDEA.
 * User: lichsword
 * Date: 14-3-17
 * Time: 下午4:24
 * To change this res use File | Settings | File Templates.
 */
public interface IDriver {

    /**
     * jdbc:{DRIVER_NAME}:{DB_PATH}:{DB_NAME}
     * eg: "jdbc:driver:path"
     */
    public static final String FORMAT_SQL_URI = "jdbc:%1$s:%2$s";

    public boolean ok();

    public String getName();

    public String getPath();

}
