package com.houlik.libhoulik.android.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.houlik.libhoulik.android.server.MySQL;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;

/**
 * 软件工具
 * Created by Houlik on 26/05/2017.
 */

public class ApkUtil {

    /**
     * 获取版本号
     * @param contect
     * @return
     */
    public static String getVersion(Context contect){
        PackageManager packageManager = contect.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(contect.getPackageName(), 0);
            return  packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 安装新版本
     * @param activity
     * @param urlApk "/mnt/sdcard/xxx.apk"
     */
    public static void installAPK(Activity activity, String urlApk){
        Intent intent = new Intent("android.intent.action.VIEW");
        //添加默认分类
        intent.addCategory("android.intent.category.DEFAULT");
        //设置数据和类型
        intent.setDataAndType(Uri.fromFile(new File(urlApk)),"application/vnd.android.package-archive");
        //如果开启的Activity退出时会调用当前Activity的onActivityResult
        activity.startActivityForResult(intent, 0);
    }

    /**
     * 下载最新APK
     * @param url
     * @param targetFile
     * @param myCallBack
     */
    public static void downloadApk(String url, String targetFile, final ApkUtil.MyCallBack myCallBack){
        HttpUtils httpUtils = new HttpUtils();
        //下载指定的文件方法
        httpUtils.download(url, targetFile, new RequestCallBack<File>() {
            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {
                myCallBack.onSuccess(responseInfo);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                myCallBack.onFailure(e, s);
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                myCallBack.onLoading(total, current, isUploading);
            }
        });
    }

    /**
     * 用于监听下载的接口
     */
    public interface MyCallBack{
        //下载成功是调用
        void onSuccess(ResponseInfo<File> args);
        //下载失败时调用
        void onFailure(HttpException args, String args1);
        //正在下载中调用
        void onLoading(long total, long current, boolean isUploading);
    }
}
