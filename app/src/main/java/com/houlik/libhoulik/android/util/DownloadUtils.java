package com.houlik.libhoulik.android.util;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;

/**
 * 文件下载工具
 * Created by Houlik on 21/05/2021
 */
public class DownloadUtils {

    /**
     * 下载文件
     * @param url 下载
     * @param targetFile 保存
     * @param myCallBack 回调
     */
    public static void downloadFile(String url, String targetFile, final DownloadUtils.MyCallBack myCallBack){
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
