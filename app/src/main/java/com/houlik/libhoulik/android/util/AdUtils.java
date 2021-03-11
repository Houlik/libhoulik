package com.houlik.libhoulik.android.util;

import android.app.Activity;
import android.os.Build;

import androidx.annotation.RequiresApi;

/**
 * Created by houlik on 2018/5/19.
 */

public class AdUtils {

    private final String TAG = "AdUtils : ";
    private NetworkUtils networkUtils;

    public AdUtils(Activity activity){
        networkUtils = new NetworkUtils(activity);
    }

    /**
     * 计时器,检查网络是否连接
     * 服务端不允许播放或者无网络则直接进入主页
     * 如果有网络及允许播放以及超出播放时间则进行显示
     * 向服务器请求是否要播放广告
     * 发布者在后台线程中运行
     * 订阅者在Android主线程中运行
     * 添加一个是否已经下载的判断，如果已经下载，则不再进行下载
     * 获取要展示的广告图片
     * 判断是否存在缓存
     * 保存图片到本地
     * 查询广告信息
     * 获取最新的一条广告信息
     * 判断文件存在，并且没有过期
     *
     * 根据是否保存有 token 判断去登录界面还是主界面
     *
     */

    //下载广告
    //在欢迎页面中, 启动一个异步线程, 加载广告信息, 提高启动速度, 防止网速过慢导致切换卡顿.
    // 异步广告信息
    public void AsyncCheckInfo() {
        // 异步线程处理监听, 在新线程上监听, 发送到主线程
        System.out.println("启动异步线程");
    }

    //判断网络, 在有网的时候, 加载广告信息; 在无网的时候, 直接略过
    // 加载广告信息
    @RequiresApi(api = Build.VERSION_CODES.M)
    public String checkInfo() {
        if (networkUtils.checkIsConnect2NetworkCapabilities()) {
            return "Begin to load info.";
        } else {
            return "Stop to load info";
        }
    }

    //启动线程删除保存的过期广告
    public void removeExpiredAD(){
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();
    }
}
