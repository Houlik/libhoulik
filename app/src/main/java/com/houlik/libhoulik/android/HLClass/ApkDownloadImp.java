package com.houlik.libhoulik.android.HLClass;

import android.app.Activity;
import android.widget.Toast;

import com.houlik.libhoulik.android.util.ApkUtil;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;

import java.io.File;

/**
 * @author : houlik
 * @since : 2020/12/25
 * email : houlik@126.com
 * 注释 : 实现下载类, 避免重复调用第三方API
 */
public class ApkDownloadImp implements Runnable {

    private Activity activity;
    private OnApkDownload onApkDownload;
    private String url;//http://store.houlik.top/download/app-debug.apk
    private File file;//File file = new File(activity.getExternalFilesDir(null) + "/app-debug.apk");

    public ApkDownloadImp(Activity activity, String url, File file, OnApkDownload onApkDownload){
        this.activity = activity;
        this.url = url;
        this.file = file;
        this.onApkDownload = onApkDownload;
    }

    @Override
    public void run() {
        //下载版本
        ApkUtil.downloadApk(url, file.toString(), new ApkUtil.MyCallBack() {
            @Override
            public void onSuccess(ResponseInfo<File> args) {
                Toast.makeText(activity, "下载成功", Toast.LENGTH_SHORT).show();
                //安装版本
                ApkUtil.installAPK(activity, file.toString());
                onApkDownload.onSuccess();
            }

            @Override
            public void onFailure(HttpException args, String args1) {
                Toast.makeText(activity, "下载失败", Toast.LENGTH_SHORT).show();
                onApkDownload.onFailure();
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                int percent = (int) ((current*100)/total);
                onApkDownload.onLoading(total, current, isUploading, percent);
            }
        });
    }

    public interface OnApkDownload{

        void onSuccess();

        void onFailure();

        void onLoading(long total, long current, boolean isUploading, int percent);
    }
}
