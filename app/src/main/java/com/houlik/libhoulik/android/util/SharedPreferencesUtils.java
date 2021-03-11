package com.houlik.libhoulik.android.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 轻量级数据存储方式
 * Created by Houlik on 17/1/2017.
 */

public class SharedPreferencesUtils {

    /**
     * MODE_PRIVATE 只有应用自身有读写XML文件的权限
     * MODE_WORLD_READABLE 其它应用有读XML文件的权限
     * MODE_WORLD_WRITEABLE 其它应用有写XML文件的权限
     * this.getSharedPreferences(String key,int mode)
     * SharedPreferencesUtils.Editor = sharedPreferences.edit();
     * SharedPreferencesUtils 对象.edit().put... 的一些方法来保存数据到XML文件, 最后使用 commit() 方法来提交数据保存到XML文件里
     * SharedPreferencesUtils 对象 get... 的一些方法是读取到XML文件里的内容
     */

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SharedPreferencesUtils(Context context, String fileName, int mode) {
        sharedPreferences = context.getSharedPreferences(fileName, mode);
        editor = sharedPreferences.edit();
    }

    public SharedPreferences getSharedPreferences(){
        return sharedPreferences;
    }

    public SharedPreferences.Editor getEditor(){
        return editor;
    }

    /**
     * 判断preferences文件内容是否为空
     *
     * @return
     */
    public boolean isPreferencesEmpty() {
        if (sharedPreferences.getAll().isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * 先写入内存, 再异步写入磁盘。将最后修改内容写入磁盘
     * commit 是直接写入磁盘。立刻获取存储操作的结果,以此结果做其他操作,当使用commit
     * @param spe
     */
    public void apply(SharedPreferences.Editor spe){
        spe.apply();
    }
}
