package com.houlik.libhoulik.android.util;

/**
 * 字符串工具
 * Created by Houlik on 2017-11-04.
 */

public class StringUtils {

    private static StringUtils stringUtils = new StringUtils();

    private StringUtils(){}

    public static StringUtils getInstance(){
        if(stringUtils == null){
            new StringUtils();
        }
        return stringUtils;
    }

    public boolean isNumber(String value){
        return isInteger(value);
    }

    private boolean isInteger(String value){
        try {
            Integer.parseInt(value);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    /**
     * 用于把不统一的中英文标点符号转换成统一中文或英文标点符号,再按照标点符号所处,分割字符串保存于数组中
     * @param str 字符串
     * @param oldStr 旧字符
     * @param newStr 新字符
     * @param regex 分割字符,如果拥有多个分割字符可以使用 "|" 区分开, 如 : (":|,|。") FASTSPLIT_METACHARACTERS = "\\?*+[](){}^$.|"
     * @return 返回区分开后保存的数组,返回的数组中不存在分割字符
     */
    public String[] replace(String str, String[] oldStr, String[] newStr, String regex){
        for (int i = 0; i < oldStr.length; i++) {
            str = str.replace(oldStr[i],newStr[i]);
        }
        String[] arrSubject = str.split(regex);
        return arrSubject;
    }



}
