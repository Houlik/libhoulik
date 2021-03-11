package com.houlik.libhoulik.android.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Houlik on 14/11/2016.
 * 数据库类框架
 */
public class DBOpenHelper extends SQLiteOpenHelper {

    private final String TAG = "DBOpenHelper";
    //数据库名称
    public static final String DATABASE_NAME = "Houlik.db";
    //数据库版本
    public static final int DATABASE_VERSION = 1;

    /**"CREATE TABLE UserInformation(
     *  userID INTEGER PRIMARY KEY AUTOINCREMENT,
     *  userCode CHAR(10),
     *  userType CHAR(10),
     *  userName CHAR(20),
     *  userPassword CHAR(20))
     */
    private String vCreateTable;

    //context上下文引用 name数据库名称 factory游标,访问数据库数据,迭代,查询,一般采用默认即可 version数据库版本-必须大于0
    public DBOpenHelper(Context context, String createTable) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.vCreateTable = createTable;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //执行可更改的sql语句
        //创建person表,具有主键,名称
        //primary key主键
        //autoincrement 自动叠加
        //varchar字符
        //integer整数
        db.execSQL(vCreateTable);
        Log.i(this.TAG,"用户表创建 " + this.vCreateTable + " 成功");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
