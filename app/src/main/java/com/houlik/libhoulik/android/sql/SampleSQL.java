package com.houlik.libhoulik.android.sql;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * SQL数据库 语句 - 例子
 * Created by Houlik on 18/11/2016.
 */
public class SampleSQL {

    private static SampleSQL sampleSql = new SampleSQL();

    private SampleSQL(){}

    public static SampleSQL getInstance(){
        if(sampleSql == null){
            new SampleSQL();
        }
        return sampleSql;
    }


    //==========检索数据==========

    /**
     * 检索单个列, 如果select是个星号* 字符,将检索所有列
     *
     * @param select 列名,以逗号区分
     * @param from 表名
     * @return
     */
    public String getSingleResult(String select, String from) {
        String tmpResult = ("SELECT " + select + " FROM " + from + " ;");
        return tmpResult;
    }

    /**
     * 检索所有列
     *
     * @param select 列名，存在数组
     * @param from 表名
     * @return
     */
    public String getMultiResult(String[] select, String from) {
        List<String[]> tmpList = new ArrayList<>();
        tmpList.add(select);
        String tmpResult = ("SELECT " + tmpList + " FROM " + from + " ;");
        return tmpResult;
    }

    //==========唯一数据 DISTINCT==========

    /**
     * 检索单个列,返回数据却是唯一的
     * DISTINCT 作用于所有列
     *
     * @param select
     * @param from
     * @return
     */
    public String getSingleResult_Distinct(String select, String from) {
        String tmpResult = ("SELECT DISTINCT " + select + " FROM " + from + " ;");
        return tmpResult;
    }

    /**
     * 检索所有列,返回数据却是唯一的
     *
     * @param select
     * @param from
     * @return
     */
    public String getMultiResult_Distinct(String[] select, String from) {
        List<String[]> tmpList = new ArrayList<>();
        tmpList.add(select);
        String tmpResult = ("SELECT DISTINCT " + tmpList + " FROM " + from + " ;");
        return tmpResult;
    }

    //==========指定列数据 LIMIT , OFFSET==========

    /**
     * 检索单个列指定的数据
     *
     * @param select
     * @param from
     * @param limit
     * @return
     */
    public String getSingleResult_Limit(String select, String from, String limit) {
        String tmpResult = ("SELECT " + select + " FROM " + from + " LIMIT " + limit + " ;");
        return tmpResult;
    }

    /**
     * 检索多个列指定的数据
     *
     * @param select
     * @param from
     * @param limit
     * @return
     */
    public String getMultiResult_Limit(String[] select, String from, String limit) {
        List<String[]> tmpList = new ArrayList<>();
        tmpList.add(select);
        String tmpResult = ("SELECT " + tmpList + " FROM " + from + " LIMIT " + limit + " ;");
        return tmpResult;
    }

    /**
     * 指定从哪儿开始以及检索的行数
     *
     * @param select
     * @param from
     * @param limit  多少行数
     * @param offset 开始的行数
     * @return
     */
    public String getSingleResult_Limit_Offset(String select, String from, String limit, String offset) {
        String tmpResult = ("SELECT " + select + " FROM " + from + " LIMIT " + limit + " OFFSET " + offset + " ;");
        return tmpResult;
    }

    //==========排序数据 ORDER BY==========
    //==========同时使用WHERE , ORDER BY 必须位于 WHERE 之后==========

    /**
     * 对单个检索按照顺序取得数据
     *
     * @param select
     * @param from
     * @param orderBy 列名称
     * @return
     */
    public String getSingleResult_OrderBy(String select, String from, String orderBy) {
        String tmpResult = ("SELECT " + select + " FROM " + from + " ORDER BY " + orderBy + " ;");
        return tmpResult;
    }

    /**
     * 按多个列排序
     *
     * @param select  列名称
     * @param from    哪个表
     * @param orderBy 按照指定的列排序的名称, 也可以使用数字来代表哪一个的列 如 : 2, 3 指的就是 select 中的第二个与第三个列, 用逗号区开
     * @return
     */
    public String getMultiResult_OrderBy(String[] select, String from, String[] orderBy) {
        List<String[]> tmpSelect = new ArrayList<>();
        tmpSelect.add(select);
        List<String[]> tmpOrderBy = new ArrayList<>();
        tmpOrderBy.add(orderBy);
        String tmpResult = ("SELECT " + tmpSelect + " FROM " + from + " ORDER BY " + tmpOrderBy + " ;");
        return tmpResult;
    }

    /**
     * 指定排序方向 - 降序
     *
     * @param select
     * @param from
     * @param orderBy
     * @return
     */
    public String getMultiResult_Desc(String[] select, String from, String orderBy) {
        List<String[]> tmpList = new ArrayList<>();
        tmpList.add(select);
        String tmpResult = ("SELECT " + tmpList + " FROM " + from + " ORDER BY " + orderBy + " DESC" + " ;");
        return tmpResult;
    }

    /**
     * 多个列排序,只对 DESC 前面的 ORDER BY 降序排列, 后面的列不指定
     *
     * @param select
     * @param from
     * @param orderBy
     * @param asc
     * @return
     */
    public String getMultiResult_Desc_Row(String[] select, String from, String orderBy, String asc) {
        List<String[]> tmpList = new ArrayList<>();
        tmpList.add(select);
        String tmpResult = ("SELECT " + tmpList + " FROM " + from + " ORDER BY " + orderBy + " DESC, " + asc + " ;");
        return tmpResult;
    }

    //==========过滤数据 WHERE==========

    /**
     * = 等于
     * <> | != 不等于
     * < 小于
     * <= 小于等于
     * !< 不小于
     * > 大于
     * >= 大于等于
     * !> 不大于
     * BETWEEN 在指定的两个值之间
     * IS NULL 为 NULL 值
     */

    enum FILTER {
        等于, 不等于, 小于, 小于等于, 不小于, 大于, 大于等于, 不大于, BETWEEN, ISNULL
    }

    ;

    /**
     * 字句操作符
     *
     * @param filter
     * @return
     */
    public String getFilter(FILTER filter) {
        switch (filter) {
            case 等于:
                return "=";
            case 不等于:
                return "!=";
            case 小于:
                return "<";
            case 小于等于:
                return "<=";
            case 不小于:
                return "!<";
            case 大于:
                return ">";
            case 大于等于:
                return ">=";
            case 不大于:
                return "!>";
            case BETWEEN:
                return "BETWEEN";
            case ISNULL:
                return "IS NULL";
        }
        return null;
    }

    /**
     * 使用 WHERE 过滤数据
     * 如果过滤的函数不是数字,而是字符,必须使用 ' ' 引号来标识
     *
     * @param select
     * @param from
     * @param where  "WHERE 列表名称 = 过滤的数据"
     * @return
     */
    public String getFilterResult(String[] select, String from, String where, FILTER filter, String valueFilter) {
        List<String[]> tmpList = new ArrayList<>();
        tmpList.add(select);
        String tmpResult = ("SELECT " + tmpList + " FROM " + from + " WHERE " + where + " " + getFilter(filter) + " " + valueFilter + " ;");
        return tmpResult;
    }

    //==========高级过滤数据 OPERATOR==========
    //==========在同时使用 AND 或 OR的 WHERE 字句, 都应该用括号 () 括起来, 括号具有比操作符更高的求值顺序

    enum OPERATOR {AND, OR, IN, NOT}

    public String getOperator(OPERATOR operator) {
        switch (operator) {
            //只返回满足所有给定条件的行
            case AND:
                return "AND";
            //返回匹配任意条件
            case OR:
                return "OR";
            //功能与OR类似 如 =>     IN('###','@@@')
            case IN:
                return "IN";
            //匹配NOT指定之外的数据
            case NOT:
                return "NOT";
        }
        return null;
    }

    /**
     * 使用操作符执行更高级数据过滤
     * @param select
     * @param from
     * @param orderBy
     * @param where
     * @param filter
     * @param valueFilter
     * @param operator
     * @param where2
     * @param filter2
     * @param valueFiler2
     * @return
     */
    public String getOperatorResult(String[] select, String from, String orderBy, String where, FILTER filter, String valueFilter,
                                    OPERATOR operator, String where2, FILTER filter2, String valueFiler2) {
        List<String[]> tmpList = new ArrayList<>();
        tmpList.add(select);
        String tmpResult = ("SELECT " + tmpList + " FROM " + from + " WHERE " + where + " " + getFilter(filter) + " " + valueFilter + " " +
                getOperator(operator) + " " + where2 + " " + getFilter(filter) + " " + valueFiler2 + " ;");
        return tmpResult;
    }

    //==========用通配符进行过滤 WILDCARD==========


    //==========自定义模板==========
    /**
     * 创建数据库或打开
     * @param path 数据库路径
     * @param factory 游标工厂
     * @param flags 标志,控制数据库访问模式
     * @return
     */
    //public abstract SQLiteDatabase openDatabase(String path, SQLiteDatabase.CursorFactory factory, int flags);

    //使用完数据库必须关闭
    //public abstract void close();

    /**
     * 插入数据
     * @param table 表名
     * @param nullColumnHack 设置 null
     * @param values 等待插入的数据
     * @return
     */
    //public abstract long insert(String table, String nullColumnHack, ContentValues values);

    /**
     * 更新数据
     * @param table 表名
     * @param values 等待更新内容
     * @param whereClause where字句内容
     * @param whereArgs where字句参数
     * @return
     */
    //public abstract int update(String table, ContentValues values,String whereClause,String[] whereArgs);

    /**
     * 删除数据
     * @param table 表名
     * @param whereClause where字句内容
     * @param whereArgs where字句参数
     * @return
     */
    //public abstract int delete(String table, String whereClause,String[] whereArgs);

    /**
     * 查询数据
     * @param table 表名
     * @param columns 查询的列
     * @param selection 过滤记录的字句
     * @param selectionArgs 过滤的参数值
     * @param groupBy 分组字句
     * @param having 过滤分组字句
     * @param orderBy 记录排列字句
     * @return
     */
    //public abstract Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy);

    /**
     * 执行非查询SQL
     * 高手使用
     * @param sql
     */
    //public abstract void execSQL(String sql);

    /**
     * 高手使用
     * @param sql
     * @param bindArgs
     */
    //public abstract void execSQL(String sql, Object[] bindArgs);

    /**
     * 执行查询
     * 高手使用
     * @param sql 要执行的SQL查询语句 - 可带参数
     * @param selectionArgs 查询参数的值
     * @return
     */
    //public abstract Cursor rawQuery(String sql, String[] selectionArgs);

}
