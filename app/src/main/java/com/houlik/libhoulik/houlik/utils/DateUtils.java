package com.houlik.libhoulik.houlik.utils;

import android.icu.text.SimpleDateFormat;
import android.os.Build;

import java.util.Calendar;
import java.util.Date;

import androidx.annotation.RequiresApi;

/**
 * 日期时间工具类
 * Created by Houlik on 2018-01-08.
 */

public class DateUtils {

    private static DateUtils dateUtils = new DateUtils();

    public static DateUtils getInstance(){
        if(dateUtils == null){
            return new DateUtils();
        }
        return dateUtils;
    }

    private DateUtils(){}

    /**
     * 获取转换后的长整型日期
     * @param date
     * @return
     */
    public Long dateToLong(Date date){
        return date.getTime();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String longDateBack2Str(String longdate){
        long date = Long.parseLong(longdate);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    /**
     * 得到系统时间
     * @return 返回字符串
     */
    public String getTime(){
        Calendar calendar = Calendar.getInstance();
        //获取系统时间
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        return hour+""+minute+""+second;
    }

    /**
     * 得到系统时间,详细分割[时][分][秒]存入数组返回
     * @return 返回 0 = 时 1 = 分 2 = 秒
     */
    public String[] getArrayTime(){
        String[] tmp = new String[3];
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < 3; i++) {
            if(i == 0){
                tmp[i] = calendar.get(Calendar.HOUR_OF_DAY)+"";
            }else if(i == 1){
                tmp[i] = calendar.get(Calendar.MINUTE)+"";
            }else{
                tmp[i] = calendar.get(Calendar.SECOND)+"";
            }
        }
        return tmp;
    }

    /**
     * 得到系统日期返回字符串
     * @return
     */
    public String getDate(String specificSymbol){
        Calendar calendar = Calendar.getInstance();
        //获取系统的日期
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        if(specificSymbol == null){
            specificSymbol = "";
        }
        return year + specificSymbol + month + specificSymbol + day;
    }

    /**
     * 得到系统日期,详细分割[年][月][日]存入数组返回
     * @return 返回 0 = 年 1 = 月 2 = 日
     */
    public String[] getArrayDate(){
        String[] tmp = new String[3];
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < 3; i++) {
            if(i == 0){
                tmp[i] = calendar.get(Calendar.YEAR)+"";
            }else if(i == 1){
                tmp[i] = calendar.get(Calendar.MONTH)+1+"";
            }else{
                tmp[i] = calendar.get(Calendar.DAY_OF_MONTH)+"";
            }
        }
        return tmp;
    }

    /**
     * 获取当前时间明天的日期
     * 例子：当前时间是 Mon Dec 1 00:00:00 CST 20..
     * 获取：明天时间是 Tue Dec 2 00:00:00 CST 20..
     * 60*60*12*1000 = 86400000
     * @param nowDate 现在时间日期
     * @return
     */
    public Date getNextDateSameTime(Date nowDate){
        Long tmp = dateToLong(nowDate) * 86400000;
        return new Date(tmp);
    }

    /**
     * 查询距离第二天还有几小时几分钟几秒
     */
    public String[] checkHowManyHour2NextDay(){
        String hour = getArrayTime()[0];
        String minute = getArrayTime()[1];
        String second = getArrayTime()[2];
        String limit = "240000";
        String[] next = new String[3];
        if(!(hour+minute+second).equals(limit)){
            next[0] = (24 - Integer.parseInt(hour))+"";
            next[1] = (60 - Integer.parseInt(minute))+"";
            next[2] = (60 - Integer.parseInt(second))+"";
        }
        return next;
    }

}
