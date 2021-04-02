package com.houlik.libhoulik.android.util;

/**
 * 反射
 * 详细使用在com.houlik.libhoulik.android.reflect包中
 * Created by houlik on 2018/7/7.
 */

public class ReflectUtils {

    /**
     * 
     * Class.forName();
     * clazz.getClass();
     * Class.class;
     *
     * 使用newInstance()必须确保当前类已经被加载以及连接
     *
     *
     * 先找到摇操作的对象类
     * Class<?> clazz = Class.forName("com.houlik.class");
     * 有构造方法必须找出,有构造参数必须给出参数类型
     * clazz.getDeclaredConstructor(类型.class, 类型.class); Declared私有使用
     * Constructor constructor = clazz.getConstructor(new class[]{}); 没有Declared,公开使用
     *
     * 要创建必须匹配
     * Object obj = constructor.newInstance(" "," ");
     *
     * 公开
     *
     * 找到对象后调用如下
     * clazz.getMethod("方法名称");
     * Method method = clazz.getMethod("");
     *
     * 私有
     * clazz.getDeclaredMethod("方法名称");
     * Method method = clazz.getDeclaredMethod(" ");
     *
     * method.setAccessible(true); 所有私有都必须使用setAccessible(true)打开 不是私有的必须设置为false
     * 有返回值
     * String field = (Object)method.invoke(obj);
     *
     * 修改属性
     * Field field = clazz.getDeclaredField("属性名称");
     * field.setAccessible(true);
     * field.set类型(obj, 输入修改的值);
     *
     */




}
