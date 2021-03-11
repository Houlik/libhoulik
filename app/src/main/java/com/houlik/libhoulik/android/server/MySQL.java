package com.houlik.libhoulik.android.server;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author : houlik
 * @since : 2020/11/26
 * email : houlik@126.com
 * 注释 : 连接MySQL数据库
 */
public class MySQL implements Runnable{

    private final String TAG = "MySQL";
    //jdbc:mysql://ip:3306/testdb
    private String url;
    private String user;
    private String password;
    private MySQLAction action;
    //private String ssl = "?verifyServerCertificate=false&useSSL=false";
    //private String ssl = "?useSSL=false&serverTimeZone=UTC";

    public MySQL(String url, String user, String password, MySQLAction action){
        this.url = url;
        this.user = user;
        this.password = password;
        this.action = action;
    }

    @Override
    public void run() {
        connect2MySQL();
    }

    /**
     * insert, update, delete 都是使用以下提交数据方式除了 查询
     * st.execute(sql);提交数据
     *
     * 查询, 返回结果集
     * statement类的executeQuery()方法来下达select指令以查询数据库
     * ResultSet rs=st.executeQuery(sql);
     * while (rs.next()){
     *      String var=rs.getString("db_title");}
     */
    private void connect2MySQL(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url,user,password);
            Statement statement = conn.createStatement();
            action.connectSuccess(conn, statement);
            conn.close();
            Log.i(TAG, "Connecting Success");
        } catch (SQLException e) {
            e.printStackTrace();
            action.connectFailure(conn);
            Log.i(TAG, "Connecting Failure");
        }

    }

    public interface MySQLAction{
        void connectSuccess(Connection conn, Statement statement) throws SQLException;
        void connectFailure(Connection conn);
    }
}
