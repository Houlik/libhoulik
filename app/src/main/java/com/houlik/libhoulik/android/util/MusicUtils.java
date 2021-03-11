package com.houlik.libhoulik.android.util;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

/**
 * 音乐播放工具类
 * Created by houlik on 2018/6/4.
 */

public class MusicUtils {

    private MediaPlayer oMediaPlayer;

    public MusicUtils(Context context, int resAudioID){
        oMediaPlayer = MediaPlayer.create(context, resAudioID);
        oMediaPlayer.start();
    }

    public MusicUtils(Context context, Uri uri){
        oMediaPlayer = MediaPlayer.create(context, uri);
        oMediaPlayer.start();
    }

    //停止播放
    public void stop(){
        oMediaPlayer.stop();
    }

    //释放
    public void release(){
        oMediaPlayer.release();
        oMediaPlayer = null;
    }

    //暂停播放
    public void pause(){
        oMediaPlayer.pause();
    }

    //开始播放 - 继续播放
    public void play(){
        oMediaPlayer.start();
    }
}
