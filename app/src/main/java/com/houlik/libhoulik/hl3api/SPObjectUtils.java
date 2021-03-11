package com.houlik.libhoulik.hl3api;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.houlik.libhoulik.android.util.SharedPreferencesUtils;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 将普通的对象或者集合中的对象通过gson来存储或获取
 * @author houlik
 * @since 2020/10/26
 */
public class SPObjectUtils {

    private SharedPreferencesUtils sp;

    public SPObjectUtils(){}

    public void createSP(Context context, String fileName, int mode){
        sp = new SharedPreferencesUtils(context, fileName, mode);
    }

    //保存普通类对象
    public void saveSPListObject(Context context, String fileName, int mode, List<Object> objectList){
        createSP(context, fileName, mode);
        Gson gson = new Gson();
        String json = gson.toJson(objectList);
        sp.getEditor().putString("OBJECT", json).commit();
    }

    //获取普通类对象以及属性
    public Object getSPListObject(){
        Gson gson = new Gson();
        String json = sp.getSharedPreferences().getString("OBJECT", null);
        Type type = new TypeToken<List<Object>>(){}.getType();
        List<Object> list = gson.fromJson(json, type);
        return list;
    }

    //添加注解expose表示忽略该注释的属性
    Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    //保存普通类对象
    public void saveSPObject(Context context, String fileName, int mode, Object object){
        createSP(context, fileName, mode);
        //Gson gson = new Gson();
        String json = gson.toJson(object);
        sp.getEditor().putString("OBJECT", json).commit();
    }

    //获取普通类对象以及属性
    public Object getSPObject(){
        //Gson gson = new Gson();
        String json = sp.getSharedPreferences().getString("OBJECT", null);
        return gson.fromJson(json, Object.class);
    }
}
