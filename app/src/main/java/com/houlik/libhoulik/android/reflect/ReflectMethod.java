package com.houlik.libhoulik.android.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 方法反射
 */
public class ReflectMethod {

    /**
     * 调用无参有返回值的方法
     * @param obj
     * @param methodName "getName"
     * @return
     */
    public Object getVoidMethod(Object obj, String methodName){
        Method name = null;
        try {
            name = obj.getClass().getMethod(methodName, null);
            //返回值
            return name.invoke(obj);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 调用有参无返回值的方法
     * @param cls new Class("");
     * @param methodName 方法名称
     * @param methodParams 参数值
     * @param typeParams example : String.class
     * @return
     */
    public Object getParamMethod(Class cls, String methodName, String methodParams, Class typeParams){
        Object obj = null;
        try {
            obj = cls.newInstance();
            if(methodParams != null || methodParams != "") {
                obj.getClass().getMethod(methodName, typeParams).invoke(obj, methodParams);
            }
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 静态有参无返回值的方法
     * @param cls
     * @param methodName
     * @return
     */
    public void getStaticParamMethod(Class cls, String methodName){
        try {
            //静态方法可以不传递object
            cls.getClass().getMethod(methodName, null).invoke(null);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
