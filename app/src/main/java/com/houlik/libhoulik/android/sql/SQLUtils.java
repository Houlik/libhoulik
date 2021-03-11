package com.houlik.libhoulik.android.sql;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.ListAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;

/**
 *
 * sqLiteDatabase.beginTransaction();
 * 用于 INSERT, UPDATE, DELETE 事务处理, 当中处理数据过程出现一条错误,将全部取消回退
 * sqLiteDatabase.setTransactionSuccessful();
 * sqLiteDatabase.endTransaction();
 *
 * 创建表
 * primary key:主键
 * autoincrement：自增型变量
 * if not exists ：如果创建的表存在就不在创建
 *
 * WHERE column IS NULL 表示该列的空值
 *
 * 删除表
 * drop table if exists 表名
 *
 * 清空表
 * delete from if exists 表名
 *
 * 修改列名方式顺序
 * 1. renameTableName 修改旧数据表名 - 临时数据表
 * 2. createTable 创建新的数据表名 - 和旧数据表名一致
 * 3. copyOldDB2NewDB 将临时数据表中的数据导入到新创建的数据表内
 * 4. deleteTable 将临时数据表删除
 * 5. 提交就可以了
 *
 * Created by Houlik on 2018-01-07.
 * 数据库工具类
 */
public class SQLUtils {

    private Context context;

    private final String TAG = "SQLUtils";

    public SQLUtils(Context context){
        this.context = context;
    }

    /**
     * 使用DBOpenHelper创建数据库，数据库将建立在软件内路径
     * data.data.databases.data.db | data.data.databases.data.db-journal
     * @param createTable
     */
    public SQLiteDatabase createTable(String createTable){
        //SQL语句无需添加if not exists条件判断
        DBOpenHelper dbOpenHelper = new DBOpenHelper(this.context, createTable);
        return dbOpenHelper.getWritableDatabase();
    }

    /**
     * 创建数据库建立在自定义的文件夹内，需要在AndroidManifest.xml文件内 application里打开
     * android:requestLegacyExternalStorage="true" 此打开后将按照旧版本方式访问storage中文件
     * /storage/emulated/0/Android/data/com.项目文件夹.项目/files
     * @param tableName 数据库表名称
     * @param args 数据库列名称 "列1 样式, 列2 样式, 列3 样式"
     *             例子: "name CHAR(长度), age Integer(长度),"
     * @param dbPath 创建路径 getExternalFilesDir(null) + "/数据库名称.db"
     * @param cursorFactory
     */
    public SQLiteDatabase createSQLiteTable(String tableName, String args, String dbPath, SQLiteDatabase.CursorFactory cursorFactory){
        //必须添加if not exists否则出现重复创建异常
        String createTableSQL = "CREATE TABLE if not exists " + tableName + "(" + args + ")";
        SQLiteDatabase sqLiteDatabase = this.context.openOrCreateDatabase(dbPath,Context.MODE_PRIVATE,cursorFactory);
        sqLiteDatabase.execSQL(createTableSQL);
        Log.i(this.TAG,"CUSTOMER CREATE " + createTableSQL + " SUCCESS");
        return sqLiteDatabase;
    }

    /**
     * 创建新数据表
     * @param sqLiteDatabase
     * @param tableName
     * @param subject
     */
    public void createTable(SQLiteDatabase sqLiteDatabase, String tableName, String subject){
        sqLiteDatabase.beginTransaction();
        //例子: String createItemTable = "subject CHAR(?), subject CHAR(?), ..."
        //例子: String createTableSQL = 原表名 + "(" + createItemTable + ")";
        String sql = "CREATE TABLE " + tableName + "(" + subject + ")";
        exeSQL(sqLiteDatabase, sql);
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        Log.i(TAG,"CREATE SUCCESS");
    }

    /**
     * 打开SQLiteDatabase
     * @param sqLiteDatabase
     * @param dbPath UriUtils7.getInstance().getUriPath("new folder/dbName.db")
     * @param factory null
     * @param flags 0
     */
    public void open(SQLiteDatabase sqLiteDatabase, String dbPath, SQLiteDatabase.CursorFactory factory, int flags){
        sqLiteDatabase = SQLiteDatabase.openDatabase(dbPath, factory, flags);
        boolean isOpen = dbIsOpen(sqLiteDatabase);
        if(isOpen){
            Log.i(TAG,"READY OPEN DATABASE");
        }
    }

    /**
     * 关闭SQLiteDatabase
     */
    public void close(SQLiteDatabase sqLiteDatabase){
        sqLiteDatabase.close();
    }

    /**
     * 检查是否已经打开SQLite数据库
     * @param sqLiteDatabase
     */
    public boolean dbIsOpen(SQLiteDatabase sqLiteDatabase){
        if(sqLiteDatabase.isOpen()){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 自定义SQL语句
     * @param sqLiteDatabase
     * @param sql
     */
    public void exeSQL(SQLiteDatabase sqLiteDatabase, String sql){
        sqLiteDatabase.beginTransaction();
        sqLiteDatabase.execSQL(sql);
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        Log.i(TAG,"COMMIT SUCCESS");
    }

    /**
     * 保存数据到数据库 tableName表名 totalValues多少个数据将存入,如果是三个就填三个,例如:(?,?,?)这样就可以
     * object在new Object(){里面存入要存入的数据,几条都行必须根据前面totalValues}
     * 例子: save("表名(欲查询的列名)","几个列,如果是两个列就用 ？,? 来标识",new Object[]{输入列的值});
     * 例子: insert into 表名(列名1,列名2,列名3) values(?,?,?) , new Object[]{列名1的值, 列名2的值, 列名3的值}
     * @param sqLiteDatabase
     * @param tableName
     * @param totalValues
     * @param objects
     */
    public void save(SQLiteDatabase sqLiteDatabase, String tableName, String totalValues, Object... objects){
        sqLiteDatabase.beginTransaction();
        sqLiteDatabase.execSQL("INSERT INTO " + tableName + " VALUES(" + totalValues + ")", objects);
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        Log.i(tableName, tableName + " : SAVE SUCCESS");
    }

    /**
     * 根据ID删除数据
     *
     * @param id
     * @param tableName
     * @param title
     */
    public void deleteByID(SQLiteDatabase sqLiteDatabase, int id, String tableName, String title) {
        sqLiteDatabase.beginTransaction();
        sqLiteDatabase.execSQL("DELETE FROM " + tableName + " WHERE " + title + "=?", new Object[]{Integer.toString(id)});
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        Log.i(TAG, id + " DELETE SUCCESS");
    }

    /**
     * 删除指定的数据
     * @param sqLiteDatabase
     * @param tableName
     * @param columnSubject
     * @param columnValue
     */
    public void deleteAll(SQLiteDatabase sqLiteDatabase, String tableName, String columnSubject, String[] columnValue){
        sqLiteDatabase.beginTransaction();
        sqLiteDatabase.execSQL("DELETE FROM " + tableName + " WHERE " + columnSubject + "=?", columnValue);
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        Log.i(TAG, "DELETE SUCCESS");
    }

    /**
     * 删除指定的数据
     * @param sqLiteDatabase
     * @param tableName
     * @param selection
     */
    public void delete(SQLiteDatabase sqLiteDatabase, String tableName, String selection){
        sqLiteDatabase.beginTransaction();
        sqLiteDatabase.execSQL("DELETE FROM " + tableName + " WHERE " + selection);
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        Log.i(TAG, "DELETE SUCCESS");
    }

    /**
     * 删除数据表
     * @param sqLiteDatabase
     * @param tableName
     */
    public void deleteTable(SQLiteDatabase sqLiteDatabase, String tableName){
        sqLiteDatabase.beginTransaction();
        String deleteOldTable = "DROP TABLE " + tableName;
        exeSQL(sqLiteDatabase, deleteOldTable);
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        Log.i(TAG, " DELETE SUCCESS");
    }

    /**
     * 更新指定的哪一列哪一行的值
     * 例子: update person set 修改的列名 = ? where 修改的列名 = ?, new String[]{原来的值, 修改的值}
     * @param sqLiteDatabase
     * @param tableName
     * @param columnName 修改哪一列 (修改多列 一条SET命令 每个"列=值,"之间用逗号分割)
     * @param value 值
     * @param selection 在哪一行
     * @param selectionArgs
     */
    public void update(SQLiteDatabase sqLiteDatabase, String tableName, String columnName, String value, String selection, String selectionArgs){
        String sql = "UPDATE " + tableName + " SET " + columnName + "='" + value + "' WHERE " + selection + "='" + selectionArgs+"'";
        Log.i(TAG, sql);
        //sqLiteDatabase.beginTransaction();
        sqLiteDatabase.execSQL(sql);
        //sqLiteDatabase.setTransactionSuccessful();
        //sqLiteDatabase.endTransaction();
        Log.i(TAG,"UPDATE SUCCESS");
    }

    /**
     * 更新多列数据
     * @param sqLiteDatabase
     * @param tableName
     * @param set SET column1 = value1, column2 = value2...., columnN = valueN
     * @param condition
     */
    public void multiUpdate(SQLiteDatabase sqLiteDatabase, String tableName, String set, String condition){
        String sql = "UPDATE " + tableName + " SET " + set + " WHERE " + condition;
        Log.i(TAG, sql);
        sqLiteDatabase.execSQL(sql);
        Log.i(TAG,"UPDATE SUCCESS");
    }

    /**
     * 添加新的列，使用 ALTER 只能一列一列的添加
     * @param tableName 表的名称
     * @param columnName 列的名称
     * @param type 格式 = TEXT | CHAR[10] | INTEGER DEFAULT 0 ......
     */
    public void updateAlterTable(SQLiteDatabase sqLiteDatabase, String tableName, String columnName, String type){
        String sql = "ALTER TABLE " + tableName + " ADD COLUMN " + columnName + " " + type;
        sqLiteDatabase.beginTransaction();
        sqLiteDatabase.execSQL(sql);
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        Log.i(this.TAG,"CUSTOMER ADDING " + sql + " SUCCESS");
    }

    /**
     * 修改数据表名称
     * @param sqLiteDatabase
     * @param oldTableName
     * @param newTableName
     */
    public void renameTableName(SQLiteDatabase sqLiteDatabase, String oldTableName, String newTableName){
        sqLiteDatabase.beginTransaction();
        String changeTableNameSQL = "ALTER TABLE " + oldTableName + " RENAME TO " + newTableName;
        exeSQL(sqLiteDatabase, changeTableNameSQL);
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        Log.i(this.TAG,"RENAME TABLE NAME SUCCESS");
    }

    /**
     * 复制旧数据表的数据到新的数据表
     * @param sqLiteDatabase
     * @param newTableNameANDColumnName
     * @param oldTableColumnName
     * @param oldTableName
     */
    public void copyOldDB2NewDB(SQLiteDatabase sqLiteDatabase, String newTableNameANDColumnName, String oldTableColumnName, String oldTableName){
        sqLiteDatabase.beginTransaction();
        //例子: String newTableColumnName = "?, ?, ?, ..."; 新数据库列名
        //例子: String newTableANDColumnName = 原表名 + "(" + newTableColumnName + ")";
        //例子: String oldColumname = "?, ?, ?, ..."; 旧数据库列名
        String insertDataSQL = "INSERT INTO " + newTableNameANDColumnName + " SELECT " + oldTableColumnName + " FROM " + oldTableName;
        exeSQL(sqLiteDatabase, insertDataSQL);
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        Log.i(this.TAG,"COPY DATA SUCCESS");
    }

    /**
     * 得到检索到的数据集合
     * Cursor cursor = getQuery(false,"表名",new String[]{"列名,列名"},"生时 = ? ",new String[]{"行名,行名"});
     * @param sqLiteDatabase
     * @param distinct 是否是唯一,不重复的数据
     * @param table 数据库里的哪一张表
     * @param columns 表里的哪一列 例子: new String[]{"列名"}
     * @param selection 这是 where 字句, 是表里的哪一列哪一行 例子: "哪一列 = '哪一行' " | 例子: "列名='"+ object +"'"
     * @param selectionArgs 如果 selection 是 " 哪一列 = ? " , 那么在selectionArgs 例子: new String[]{"哪一行字句"} , 不能超出被查询列的数量,不是表中列的数量
     * @return
     */
    public Cursor getQuery(SQLiteDatabase sqLiteDatabase, boolean distinct,String table,String[] columns, String selection,String[] selectionArgs){
        return sqLiteDatabase.query(distinct,table,columns,selection,selectionArgs,null,null,null,null);
    }

    /**
     * 完整检索数据方式
     * @param sqLiteDatabase
     * @param distinct
     * @param table
     * @param columns
     * @param selection
     * @param selectionArgs
     * @param groupBy
     * @param having
     * @param orderBy
     * @param limit
     * @return
     */
    public Cursor getQuery(SQLiteDatabase sqLiteDatabase, boolean distinct, String table,String[] columns, String selection,String[] selectionArgs, String groupBy,
                           String having, String orderBy, String limit){
        return sqLiteDatabase.query(distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    /**
     * 查询数据
     * @param sqLiteDatabase
     * @param sql
     * @param columnName
     * @param selectionArgs
     * @return 返回List集合
     */
    public Object queryList(SQLiteDatabase sqLiteDatabase, String sql,String columnName,String[] selectionArgs){
        Cursor cursor = sqLiteDatabase.rawQuery(sql, selectionArgs);
        List<String> list = new ArrayList<>();
        while(cursor.moveToNext()){
            String result = cursor.getString(cursor.getColumnIndex(columnName));
            list.add(result);
        }
        if(cursor != null) {
            cursor.close();
        }
        return list;
    }

    /**
     * 查询单条数据
     * @param cursor 数据集
     * @param columnIndex 单条数据是根据列来计算 - 数组中有两列 第一列是 0 第二列是 1
     * @return 返回字符串
     */
    public String getResult(Cursor cursor,int columnIndex){
        if(cursor.moveToFirst()){
            return cursor.getString(columnIndex);
        }
        return null;
    }

    /**
     * 查询整列的数据
     * @param cursor 数据集
     * @param column 多少列
     * @param row 多少行
     * @return
     */
    public String[][] getResult(Cursor cursor, int column, int row){
        String[][] tmp = new String[column][row];
        for (int i = 0; i < column; i++) {
            for (int j = 0; j < row; j++) {
                cursor.moveToPosition(j);
                tmp[i][j] = cursor.getString(i);
            }
        }
        return tmp;
    }

    /**
     * 获取整列数据后从遍历中得到数据,遍历之前必须先初始化回调函数接口 OnResult
     * @param result
     */
    public void onnResult(String[][] result, OnResult onResult){
        for (String[] tmp: result) {
            for (Object obj:tmp) {
                onResult.getResult(obj);
            }
        }
    }

    public interface OnResult{
        void getResult(Object obj);
    }

    /**
     * 使用DBOpenHelper 创建的数据库路径
     * SQL数据库绝对路径
     * @param SQLCompanyName "com.文件夹.项目名称"
     * @param SQLName "database.db"
     * @param SQLJournal "database.db-journal"
     * @param isJournal
     * @return 如果是journal 文件就返回 true，不是就返回 false
     */
    public String getSQLAbsolutePath(String SQLCompanyName, String SQLName, String SQLJournal, boolean isJournal) {
        String SQLAbsolutePath = "/data/data/" + SQLCompanyName + "/databases/" + SQLName;
        String SQLJournalAbsolutePath = "/data/data/" + SQLCompanyName + "/databases/" + SQLJournal;
        return isJournal ? SQLJournalAbsolutePath : SQLAbsolutePath;
    }

    /**
     * 删除数据库文件方法
     *
     * @param file
     */
    public void deleteFile(File file) {
        //文件是否存在
        if (file.exists()) {
            //是否是文件
            if (file.isFile()) {
                //设置属性:可执行,可读,可写
                file.setExecutable(true, false);
                file.setReadable(true, false);
                file.setWritable(true, false);
                file.delete();
                //如果是一个目录
            } else if (file.isDirectory()) {
                //声明目录下所有文件
                File files[] = file.listFiles();
                //遍历目录下所有文件
                for (int i = 0; i < files.length; i++) {
                    //把每个文件用此方法进行遍历
                    this.deleteFile(files[i]);
                }
            }
            file.setExecutable(true, false);
            file.setReadable(true, false);
            file.setWritable(true, false);
            file.delete();
            Log.i(TAG, file.getName() + "删除成功");
        } else {
            Log.i(TAG, file.getName() + "不存在！！！");
        }
    }

    /**
     * 查询当前数据列数与数据库列数是否一致，用于判断是否添加新的列
     * @param sqLiteDatabase
     * @param sql 查询数据库
     * @param args 列表数组
     * @param dataNumberOfColumn 当前数据列数
     * @return
     */
    public boolean checkNumberOfColumnsIsConsistent(SQLiteDatabase sqLiteDatabase, String sql, String[] args, int dataNumberOfColumn){
        Cursor cursor = sqLiteDatabase.rawQuery(sql, args);
        if(cursor.getColumnCount() == dataNumberOfColumn){
            return true;
        }
        return false;
    }

    private IProcessDBOnThread iProcessDBOnThread;

    /**
     * 多线程操作写入数据库时,需要使用事务,以免出现同步问题
     * @param sqLiteDatabase
     * @param cv cv.put(String key, String value) ==> db.insert(?,null,cv)
     * @param onProcess
     */
    public void save2DBOnThread(SQLiteDatabase sqLiteDatabase, ContentValues cv, IProcessDBOnThread onProcess){
        sqLiteDatabase.beginTransaction();
        try{
            iProcessDBOnThread.process(sqLiteDatabase, cv);
            sqLiteDatabase.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            sqLiteDatabase.endTransaction();
        }
    }

    public interface IProcessDBOnThread{
        void process(SQLiteDatabase sqLiteDatabase, ContentValues cv);
    }

    /**
     * 乃是AsyncTaskLoader子类
     * 1. 让加载管理器创建一个加载器
     * 2. 在加载管理器请求时, 给予一个加载器
     * 3. 接收加载器返回的游标
     *
     * 初始化加载器
     * onCreate中实现
     * SimpleCursorAdapter adapter = new SimpleCursorAdapter(activity, R.layout.???, null, FROM, TO, 0);
     * setListAdapter(adapter)
     * Bundle args = getIntent().getExtras();
     * getLoaderManager().initLoader(DATA_LOADER, args, activity);
     * @param loader 加载器
     * @param adapter 集合适配器
     * @param callbacks 回调 loadFinish中 .swapCursor(cursor) loaderReset中 .swapCursor(null)
     */
    public void processLoaderCallBack(CursorLoader loader, ListAdapter adapter, LoaderManager.LoaderCallbacks callbacks){}

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        Log.i(TAG, "finaliza()");
    }
}
