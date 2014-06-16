package com.db;

import com.log.Log;

/**
 * Created with IntelliJ IDEA.
 * User: lichsword
 * Date: 14-3-17
 * Time: 下午4:26
 * To change this res use File | Settings | File Templates.
 */
public class SqliteDriver implements IDriver {

    private String DB_LIB_CLS_NAME = "org.sqlite.JDBC";

    @Override
    public boolean ok() {
        boolean driverOK = false;
        try {
            Class.forName(DB_LIB_CLS_NAME);
            Log.e("INFO", "com.lichsword.nextbrain.db driver(Ok)");
            driverOK = true;
        } catch (ClassNotFoundException e) {
            Log.e("INFO", "com.lichsword.nextbrain.db driver(No found)");
        }
        return driverOK;
    }

    @Override
    public String getName() {
        return "sqlite";
    }

    @Override
    public String getPath() {
        return "brain.sqlite";
    }
}
