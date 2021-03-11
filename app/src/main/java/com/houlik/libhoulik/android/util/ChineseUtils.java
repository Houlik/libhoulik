package com.houlik.libhoulik.android.util;

import android.content.Context;

import com.houlik.libhoulik.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * 查询汉字笔画数
 * Created by houlik on 2018/5/30.
 */

public class ChineseUtils extends Thread {

    private final String TAG = "Chinese Utils";
    private Hashtable<String, Integer> ht = new Hashtable<>();
    private Map<String, Integer> map = new HashMap<>();
    private List<Map> list = new ArrayList<>();
    private Context context;
    //总数是20967
    private static int count;

    public ChineseUtils(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        prepare(context);
    }

    private void prepare(Context context) {
        //得到文件
        InputStream is = context.getResources().openRawResource(R.raw.utf8);
        InputStreamReader isr = null;
        try {
            //读取文件 转成UTF-8格式
            isr = new InputStreamReader(is, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //使用缓冲读取
        BufferedReader br = new BufferedReader(isr);
        String buffer = null;
        String utf8 = null;
        Integer strokes = 0;
        try {
            while ((buffer = br.readLine()) != null) {
//                buffer = buffer.replaceAll("[\\u00A0]+", " ");
                //更换中文空格为英文空格
                buffer = buffer.replaceAll(String.valueOf((char) 160), " ");
                //断开空格存入数组
                String[] str = buffer.split(" ");
                utf8 = getUTF8Code(str[1].trim());
                strokes = Integer.parseInt(str[2].trim());
                //得到键值存入HashTable
                ht.put(utf8, strokes);
                count++;
//                Log.i(TAG,String.valueOf(count));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getUTF8Code(String chinese) {
        byte[] bytes = new byte[0];
        try {
            bytes = chinese.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String str = " ";
        for (int i = 0; i < bytes.length; i++) {
            String tmp = Integer.toHexString(bytes[i]);
            if (tmp.length() > 2) {
                tmp = tmp.substring(tmp.length() - 2);
                str = str + " " + tmp;
            }
        }
        return str.toUpperCase();
    }

    public interface OnResultListener {
        void getStrokeResult(int strokes);
        void getArrayStrokeResult(int[] strokes);
    }

    public void setOnResultListener(String chineseCharacter, OnResultListener onResultListener) {
        onResultListener.getStrokeResult(ht.get(getUTF8Code(chineseCharacter)));
    }

    public void setOnResultListener(String[] chineseCharacter, OnResultListener onResultListener) {
        int[] tmp = new int[chineseCharacter.length];
        for (int i = 0; i < chineseCharacter.length; i++) {
            tmp[i] = ht.get(getUTF8Code(chineseCharacter[i]));

        }
        onResultListener.getArrayStrokeResult(tmp);
    }
}
