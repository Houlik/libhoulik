package com.houlik.libhoulik.android.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Houlik on 2018-03-16.
 */

public class AppWidget extends AppWidgetProvider {

    private static final String LOG_TAG = "AppWidget";

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Log.d(LOG_TAG,"onDeleted()");
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Log.d(LOG_TAG,"onDisabled()");
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.d(LOG_TAG,"onEnabled()");

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d(LOG_TAG,"onReceive()");
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.d(LOG_TAG,"onUpdate()");
    }

    /**
     *  AndroidManifest
     *  <receiver android:name=".AppWidget"
        android:label="@string/app_name">
        <intent-filter>
        <action android:name="android.appwidget.action.APPWIDGET_UPDATE" />
        </intent-filter>
        <meta-data android:name="android.appwidget.provider"
        android:resource="@xml/app_widget_config" />
        </receiver>
     *
     *  XML - app_widget_config
     *  <!--<appwidget-provider-->
        <!--xmlns:android="http://schemas.android.com/apk/res/android"-->
        <!--android:minWidth="40dp"-->
        <!--android:minHeight="40dp"-->
        <!--android:updatePeriodMillis="86400000"-->
        <!--android:previewImage="@drawable/preview"-->
        <!--android:initialLayout="@layout/example_appwidget"-->
        <!--android:configure="com.example.android.ExampleAppWidgetConfigure"-->
        <!--android:resizeMode="horizontal|vertical"-->
        <!--android:widgetCategory="home_screen"> home_screen|keyguard|searchbox
     </appwidget-provider>
     *
     * xml layout - app_widget
     *
     * Java - AppWidget extends AppWidgetProvider
     *
     */
}
