package com.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: lichsword
 * Date: 14-3-17
 * Time: 下午4:31
 * <p/>
 * TODO
 */
class DatabaseBase {

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }


    protected DatabaseBase(IDriver driver) {
        connet(driver);
    }

    /**
     * 连接驱动
     *
     * @param driver
     */
    private void connet(IDriver driver) {
        if (driver.ok()) {
            try {
                String uriString = String.format(driver.FORMAT_SQL_URI, driver.getName(), driver.getPath());
                if (null == connection) {
                    connection = DriverManager.getConnection(uriString, null, null);
                }// end if
            } catch (SQLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        } else {
            // more
        }
    }

    /**
     * 关闭，断开驱动
     *
     * @throws java.sql.SQLException
     */
    private void disconnet() throws SQLException {
        if (null != connection) {
            connection.close();
            connection = null;
        }// end if
    }
}
