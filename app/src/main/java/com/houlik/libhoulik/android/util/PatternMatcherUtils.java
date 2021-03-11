package com.houlik.libhoulik.android.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式工具类
 * Created by houlik on 2018/6/4.
 */

public class PatternMatcherUtils {

    /**
     * ^为限制开头
     * $为限制结尾
     * .  条件限制除/n以外任意一个单独字符
     * .. 任意两个字符
     * [] 特定限制条件
     * * 在限制条件为特定字符出现0次以上时，可以使用
     * + 在限制条件为特定字符出现1次以上时，可以使用
     * ？在限制条件为特定字符出现有0或1次以上时，可以使用
     * {} 限制为连续出现指定次数字符
     *
     */



    private Pattern oPattern;
    private Matcher oMatcher;
    public enum PatternType{
        NUMBER,
        CHAR_CAPITALS,
        CHAR_LOWERCASE,
        MANDARIN,
        NUM_CHAR,
        ID,
        MONTH,
        DAY,
        EMAIL,
        WEB,
        PHONE,
        PASSWORD,
        POSTCODE,
        ACCOUNT
    }

    private String number = "[0-9]";
    private String char_capitals = "[a-z]";
    private String char_lower_case = "[A-Z]";
    private String mandarin = "[\u4e00-\u9fa5]";
    private String num_char = "[0-9a-zA-Z]";
    //中国身份证
    private String id = "^d{15}|d{18}$";
    private String month = "^(0?[1-9]|1[0-2])$";
    private String day = "^((0?[1-9])|((1|2)[0-9])|30|31)$";
    private String email = "w+([-+.]w+)*@w+([-.]w+)*.w+([-.]w+)*$";
    private String web = "http://([w-]+.)+[w-]+(/[w-] ./?%&=]*)?$";
    //中国电话号码
    private String phoneNum = "^((d{3,4})|d{3,4}-)?d{7,8}$";
    private String password = "^[a-zA-Z]w{5,17}$";
    //中国邮政编码
    private String postcode = "[1-9]d{5}(?!d) ";
    private String account = "^[a-zA-Z][a-zA-Z0-9_]{4,15}$";


    public PatternMatcherUtils(PatternType type){
        init(type);

    }

    private void init(PatternType type){
        switch (type){
            case NUMBER:
                oPattern = Pattern.compile(number);
                break;
            case CHAR_CAPITALS:
                oPattern = Pattern.compile(char_capitals);
                break;
            case CHAR_LOWERCASE:
                oPattern = Pattern.compile(char_lower_case);
                break;
            case MANDARIN:
                oPattern = Pattern.compile(mandarin);
                break;
            case NUM_CHAR:
                oPattern = Pattern.compile(num_char);
                break;
            case ID:
                oPattern = Pattern.compile(id);
                break;
            case MONTH:
                oPattern = Pattern.compile(month);
                break;
            case DAY:
                oPattern = Pattern.compile(day);
                break;
            case EMAIL:
                oPattern = Pattern.compile(email);
                break;
            case WEB:
                oPattern = Pattern.compile(web);
                break;
            case PHONE:
                oPattern = Pattern.compile(phoneNum);
                break;
            case ACCOUNT:
                oPattern = Pattern.compile(account);
                break;
            case PASSWORD:
                oPattern = Pattern.compile(password);
                break;
            case POSTCODE:
                oPattern = Pattern.compile(postcode);
                break;
        }
    }

    public boolean isMatches(String matcher){
        oMatcher = oPattern.matcher(matcher);
        return oMatcher.matches();
    }


}
