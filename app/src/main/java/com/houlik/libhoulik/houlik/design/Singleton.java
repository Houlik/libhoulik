package com.houlik.libhoulik.houlik.design;

/**
 * 单例模式
 * Created by houlik on 2018/11/16.
 */

public class Singleton {

    //创建静态类对象且实例化
    private static Singleton singleton = new Singleton();

    //私有构造方法便于不能随意实例化
    private Singleton(){}

    //通过静态方法获取当前类实例化对象,如果为空将重新实例化当前类对象
    public static Singleton getInstance(){
        if(singleton == null){
            singleton = new Singleton();
        }
        return singleton;
    }

    /**
     * 防止过多使用new一片内存
     * 常用于工具类或者该类属性不变动情况
     */
}
