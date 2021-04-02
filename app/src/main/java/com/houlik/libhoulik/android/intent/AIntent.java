package com.houlik.libhoulik.android.intent;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public class AIntent {

    /**
     * 自定义打开浏览器
     * @param uri
     * @param activity
     */
    public void toBrowser(Uri uri, Activity activity){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_APP_BROWSER);
        intent.setData(uri);

        Intent chooser = Intent.createChooser(intent, "请选择一款浏览器");
        activity.startActivity(chooser);
    }
}
