package com.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created with IntelliJ IDEA.
 * User: lichsword
 * Date: 14-3-17
 * Time: 下午4:30
 * <p/>
 * TODO
 */
public final class SqliteDatabase extends DatabaseBase implements ISqlStatement {

    private static final String TAG = SqliteDatabase.class.getSimpleName();

    private SqliteDatabase(IDriver driver) {
        super(driver);
    }

    public SqliteDatabase() {
        super(new SqliteDriver());
    }

    @Override
    public Connection getConnection() {
        return super.getConnection();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public boolean insert(String sql) {
        try {
            Statement statement = getConnection().createStatement();
            return statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(String sql) {
        try {
            Statement statement = getConnection().createStatement();
            return statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public ResultSet query(String sql) {
        ResultSet cursor = null;
        try {
            Statement statement = getConnection().createStatement();
            cursor = statement.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cursor;
    }

    @Override
    public boolean update(String url) {
//        String safeSql = formatSafeSql(sql);
//        Log.d(TAG, "safe sql=" + safeSql);
        // TODO
        return false;
    }
}
