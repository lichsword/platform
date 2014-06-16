package com.lich.platform.service;

import com.db.SqliteDatabase;

import java.sql.ResultSet;

/**
 * Created with IntelliJ IDEA.
 * User: lichsword
 * Date: 14-6-16
 * Time: 下午2:08
 * <p/>
 * TODO
 */
public class DBService implements IDatabase {

    private SqliteDatabase mDatabase;

    public DBService() {
        super();
        mDatabase = new SqliteDatabase();
    }

    @Override
    public String handleMsg(String msg) {

        String result = "Database list:\n(1)brain.sqlite\n(2)bus.sqlite";
        // TODO
        return result;
    }

    @Override
    public ResultSet query(String table, String whereClause, String whereArgs) {
        return null;  // TODO
    }
}
