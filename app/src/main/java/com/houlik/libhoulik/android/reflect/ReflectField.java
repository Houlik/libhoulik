package com.houlik.libhoulik.android.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * 属性反射
 */
public class ReflectField {

    /**
     * 获取私有属性, 但是不能访问父类的属性
     * @param cls
     * @param fieldName
     * @param fieldValue
     * @return
     */
    public Field getReflectDeclaredField(Class cls, String fieldName, String fieldValue){
        Field name = null;
        try {
            name = cls.getClass().getDeclaredField(fieldName);
            int modifiers = name.getModifiers();
            if(Modifier.isPrivate(modifiers)){
                name.setAccessible(true);//打开访问权限
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        if(fieldValue == null || fieldValue == "") {
            return name;
        }else{
            try {
                name.set(cls, fieldValue);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return name;
        }
    }

    /**
     * 通过父类的Class来获取父类的私有属性的值
     * @param cls
     * @param fieldName 属性名称
     * @param fieldValue 新值
     * @return
     */
    public Object getReflectSuperField(Class cls, String fieldName, String fieldValue){
        Class sc = cls.getSuperclass();
        Object obj = null;
        Field name = null;
        try {
            obj = sc.newInstance();
            name = sc.getDeclaredField(fieldName);
            name.setAccessible(true);
            if(fieldValue == null || fieldValue == ""){
                return name.get(obj);
            }else{
                name.set(obj, fieldValue);
                return name.get(obj);
            }
        } catch (NoSuchFieldException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        return name;
    }

    /**
     * 获取公开属性
     * @param cls
     * @param fieldName
     * @param fieldValue
     * @return
     */
    public Object getReflectField(Class cls, String fieldName, String fieldValue){
        Object obj = null;
        Field name = null;
        try {
            obj = cls.newInstance();
            name = cls.getClass().getField(fieldName);
            if(fieldValue == null || fieldValue == ""){
                return name.get(obj);
            }else{
                name.set(obj, fieldValue);
                return name.get(obj);
            }
        } catch (NoSuchFieldException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
