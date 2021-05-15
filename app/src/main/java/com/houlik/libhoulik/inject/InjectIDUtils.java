package com.houlik.libhoulik.inject;

import android.app.Activity;
import android.util.Log;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 在使用映射前必须先初始化当前类才能使用捷径绑定
 * setContentView, findViewByID 捷径
 * Created by houlik on 2018/5/23.
 */
public class InjectIDUtils {

    private static final String METHOD_SET_CONTENTVIEW = "setContentView";
    private static final String METHOD_FIND_VIEW_BY_ID = "findViewById";
    private static final String METHOD_GET_RAW = "openRawResource";

    /**
     * activity界面ID
     * @param activity
     */
    private static void injectContentView(Activity activity){
        Class<? extends Activity> clazz = activity.getClass();
        // 查询类上是否存在ContentView注解
        ContentViewByID contentView = clazz.getAnnotation(ContentViewByID.class);
        if (contentView != null){
            int contentViewLayoutId = contentView.value();
            try {
                Method method = clazz.getMethod(METHOD_SET_CONTENTVIEW,
                        int.class);
                method.setAccessible(true);
                method.invoke(activity, contentViewLayoutId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * activity元件ID
     * @param activity
     */
    private static void injectViews(Activity activity) {
        //获取activity类class
        Class<? extends Activity> clazz = activity.getClass();
        //获取类中所有私有属性
        Field[] fields = clazz.getDeclaredFields();
        // 遍历所有成员变量
        for (Field field : fields) {
            //实例化注解 - 安装循环通过属性获取相对应的注解
            InjectViewByID viewInjectAnnotation = field
                    .getAnnotation(InjectViewByID.class);
            //如果实例化的注解不为空
            if (viewInjectAnnotation != null) {
                //获取注解中的值
                int viewId = viewInjectAnnotation.value();
                //如果注解值不是负一
                if (viewId != -1) {
                    Log.e("TAG", viewId + "");
                    // 初始化View
                    try {
                        //反射activity类中的findViewById方法
                        Method method = clazz.getMethod(METHOD_FIND_VIEW_BY_ID,
                                int.class);
                        //将对应的值注入该方法中
                        Object resView = method.invoke(activity, viewId);
                        //打开私有的属性
                        field.setAccessible(true);
                        //设置该私有属性的值
                        field.set(activity, resView);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void inject(Activity activity) {
        injectContentView(activity);
        injectViews(activity);
    }
}
