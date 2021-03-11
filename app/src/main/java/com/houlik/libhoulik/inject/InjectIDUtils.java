package com.houlik.libhoulik.inject;

import android.app.Activity;
import android.util.Log;

import java.io.InputStream;
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
        Class<? extends Activity> clazz = activity.getClass();
        Field[] fields = clazz.getDeclaredFields();
        // 遍历所有成员变量
        for (Field field : fields) {
            InjectViewByID viewInjectAnnotation = field
                    .getAnnotation(InjectViewByID.class);
            if (viewInjectAnnotation != null) {
                int viewId = viewInjectAnnotation.value();
                if (viewId != -1) {
                    Log.e("TAG", viewId + "");
                    // 初始化View
                    try {
                        Method method = clazz.getMethod(METHOD_FIND_VIEW_BY_ID,
                                int.class);
                        Object resView = method.invoke(activity, viewId);
                        field.setAccessible(true);
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
