package com.houlik.libhoulik.houlik.date;

import android.content.Context;

import com.houlik.libhoulik.android.util.AssetsUtils;

/**
 * Created by houlik on 2018/11/14.
 */

public class SubLunarCalendar extends LunarCalendar {

    public SubLunarCalendar(String[] config_calendar) {
        super(config_calendar);
    }

    public static class Builder{
        private Context context;
        //这个文件已经保存在asset文件夹中
        private String assetsFile = "config_calendar.txt";

        public Builder setContext(Context context){
            this.context = context;
            return this;
        }

        public Builder setAssetsFile(String assetsFile){
            this.assetsFile = assetsFile;
            return this;
        }

        public SubLunarCalendar process(){
            String[] config_calendar = AssetsUtils.getInstance().readAssets(context,assetsFile,"\n");
            return new SubLunarCalendar(config_calendar);
        }
    }
}
