package com.houlik.libhoulik.houlik.utils;

import java.lang.reflect.Method;

/**
 * 映射工具类
 * Created by houlik on 2018/11/17.
 */

public class Reflect {

    /**
     * 判断class与object是否相等,如果相等将返回object
     * @param cls
     * @param obj
     * @param <T>
     * @return
     */
    public <T> T class2Object(Class<T> cls, Object obj){
        if(cls.isInstance(obj)){
            return cls.cast(obj);
        }
        return null;
    }

    /**
     * 映射的方法需要传递参数
     * @param clsName 字符串 类名
     * @param methodName 字符串 方法名
     * @param cls 参数完整类型 new class[]{class,class}
     * @param objects 参数实际参数 new Object[]{par1,par2}
     */
    public void reflectMethodWithParameter(String clsName, String methodName, Class[] cls, Object... objects){
        Class<?> c = null;
        try {
            c = Class.forName(clsName);
            Object obj = c.newInstance();
            Method method = c.getMethod(methodName, cls);
            method.invoke(obj,objects);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 映射的方法无需传参数
     * @param clsName 字符串 类名
     * @param methodName 字符串 方法名
     */
    public void reflectMethodWithoutParameter(String clsName, String methodName){
        Class<?> c = null;
        try {
            //获取方法类
            c = Class.forName(clsName);
            //实例化该类
            Object obj = c.newInstance();
            //获取方法
            Method method = c.getMethod(methodName);
            //执行该方法
            method.invoke(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
