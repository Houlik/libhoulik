package com.houlik.libhoulik.android.reflect;

/**
 * 类反射
 */
public class ReflectClass {

    public Class getReflectClass(Object o){
        Class cls = o.getClass();
        return cls;
    }

    /**
     * 获取普通类
     * @param publicClass Example:"java.lang.String"
     * @return
     */
    public Class getPublicClass(String publicClass){
        Class cls = null;
        try {
            cls = Class.forName(publicClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return cls;
    }

    /**
     * 获取内部类
     * @param innerClass example:"android.view.View$OnClickListener"
     * @return
     */
    public Class getInnerClass(String innerClass){
        Class cls = null;
        try {
            cls = Class.forName(innerClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return cls;
    }

    /**
     *
     * @param publicClass Example:"java.lang.String"
     * @param initialize true = 被load后,就初始化了
     * @param clsLoader 类
     * @return
     */
    public Class getLoaderClass(String publicClass, boolean initialize, Class clsLoader){
        Class cls = null;
        ClassLoader loader = clsLoader.getClassLoader();
        try {
            cls = Class.forName(publicClass, initialize, loader);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return cls;
    }
}
