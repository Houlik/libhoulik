package com.houlik.libhoulik.android.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

/**
 * 音效工具类
 * Created by houlik on 2018/6/4.
 */

public class SoundUtils {

    /**
     * Stream Type
     *
     * STREAM_ALARM 警报
     * STREAM_MUSIC 媒体
     * STREAM_NOTIFICATION 窗口顶部状态栏
     * STREAM_RING 铃声
     * STREAM_SYSTEM 系统
     * STREAM_VOICE_CALL 通话
     * STREAM_DTMF 多频
     *
     * AudioManager am = getSystemService(Context.AUDIO_SERVICE) 获取AudioManager引用
     * float = am.getStreamVolume(AudioManager.STREAM_MUSIC) 获取当前音量
     * float = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC) 获取系统最大音量
     * float = 当前音量 除以 最大音量 得到播放音量
     * 之后赋予SoundPool.play 左右音量当中
     * MediaPlayer 通用
     */

    //声音池
    private SoundPool oSoundPool;
    //单序列
    private int mSoundID;
    //声音资源ID
    private int mResSoundID;
    //多序列
    private int[] arrSoundID;
    //声音资源ID数组
    private int[] arrResSoundID;

    /**
     * 单音效
     * @param context
     * @param resSoundID 单音效资源ID
     */
    public SoundUtils(Context context, int resSoundID){
        this.mResSoundID = resSoundID;
        initSingleSound(context);
    }

    /**
     * 多音效
     * @param context
     * @param resSoundID 多音效资源ID
     */
    public SoundUtils(Context context, int[] resSoundID){
        this.arrResSoundID = resSoundID;
        initMultiSound(context);
    }

    /**
     * 初始化单音效方法
     * 两种API不同初始化方法
     * @param context
     */
    private void initSingleSound(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            oSoundPool = new SoundPool.Builder().build();
        }else{
            oSoundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);

        }
        mSoundID = oSoundPool.load(context,mResSoundID,1);
    }

    private void initMultiSound(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            oSoundPool = new SoundPool.Builder().build();
        }else{
            oSoundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);

        }
        for (int i = 0; i < arrResSoundID.length; i++) {
            arrSoundID[i] = oSoundPool.load(context,arrResSoundID[i],1);
        }
    }

    /**
     * 单播放
     * @param soundID 播放第几首
     * @param loop 0／1 = 不循环 | -1 = 循环
     */
    public void playSingleSound(int soundID, int loop){
        oSoundPool.play(soundID,0.5f,0.5f,0,loop,1);
    }

    public void playMultiSound(int soundID, int loop){
        oSoundPool.play(arrSoundID[soundID],0.5f,0.5f,0,loop,1);
    }

    public void stopPlaySingleSound(int soundID){
        oSoundPool.stop(soundID);
    }

    public void stopPlayMultiSound(int soundID){
        oSoundPool.stop(arrSoundID[soundID]);
    }

    public void releaseSoundPool(){
        oSoundPool.release();
    }

    public void setOnLoadCompleteListener(SoundPool.OnLoadCompleteListener onLoadCompleteListener){
        oSoundPool.setOnLoadCompleteListener(onLoadCompleteListener);
    }


}
