package com.houlik.libhoulik.android.annotation;

import android.app.Activity;

import com.houlik.libhoulik.inject.InjectField;

import java.lang.reflect.Field;

public class InjectUtils {

    public static Object getIField(Activity activity){
        //获取activity类class
        Class<? extends Activity> clazz = activity.getClass();
        //获取类中所有私有属性
        Field[] fields = clazz.getDeclaredFields();
        // 遍历所有成员变量
        for (Field field : fields) {
            //实例化注解 - 安装循环通过属性获取相对应的注解
            InjectField injectField = field
                    .getAnnotation(InjectField.class);
            //如果实例化的注解不为空
            if (injectField != null) {
                //获取注解中的值
                return injectField.strValue();
            }
        }
        return null;
    }
}
