package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBHelper {
    /**
     * 链接地址
     */
    public static final String url = "jdbc:mysql://127.0.0.1/SignerZKP?characterEncoding=utf8&useSSL=true&autoReconnect=true&failOverReadOnly=false&maxReconnects=2000";
    /**
     * 连接名称
     */
    public static final String name = "com.mysql.jdbc.Driver";
    /**
     * 数据库用户名
     */
    public static final String user = "root";
    /**
     * 数据库用户名对应的密码
     */
    public static final String password = "123456";

    public Connection conn = null;
    public PreparedStatement pst = null;

    public DBHelper(String sql) {
        try {
            Class.forName(name);
            conn = DriverManager.getConnection(url, user, password);
            pst = conn.prepareStatement(sql);

            System.out.println("数据库连接成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("数据库连接失败" + e.toString());
        }
    }

    /**
     * 关闭数据库连接
     */
    public void close() {
        try {
            this.conn.close();
            this.pst.close();

            System.out.println("关闭数据库连接成功");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("数据库连接关闭失败" + e.toString());
        }
    }
}
