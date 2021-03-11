package com.houlik.libhoulik.houlik.lifeApp;

import android.content.Context;

import com.houlik.libhoulik.R;
import com.houlik.libhoulik.android.util.FileUtils;
import com.houlik.libhoulik.houlik.utils.HLUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by houlik on 2018/10/31.
 */

public class Result {

    private Context context;
    private List<Character> listResult;
    private int rawFile = R.raw.life;

    public Result(Context context){
        this.context = context;
    }

    /**
     *
     * @param numToCheck
     * @return
     */
    private List prepared(final int... numToCheck) {
        listResult = new ArrayList<>();
        FileUtils.getInstance().readRawFile(context, rawFile, "UTF-8", new FileUtils.OnActionReadFile() {
            @Override
            public void action(BufferedReader bufferedReaderReadLine) {
                String buffer = null;
                StringBuffer sBuffer = new StringBuffer();
                try {
                    while ((buffer = bufferedReaderReadLine.readLine()) != null) {
                        //把读取到的数据全部连在一起
                        sBuffer.append(buffer);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //按照数组中数据读取相关解析后存入集合
                for (int i = 0; i < numToCheck.length; i++) {
                    listResult.add(sBuffer.charAt(numToCheck[i]-1));
                }
            }
        });
        return listResult;
    }

    /**
     *
     * @param num
     * @param onResultListener
     */
    public void setOnResultListener(int[] num, OnResultListener onResultListener){
        List<Integer> tmpList;
        if(num.length <= 1) {
            //得到连锁数据
            tmpList = HLUtils.getInstance().addition(384, 12700, num[0]);
        }else{
            //编辑得到的输入值
            int digitNum = HLUtils.getInstance().jointUnitsDigit(num);
            //得到连锁数据
            tmpList = HLUtils.getInstance().addition(384,12700,digitNum);
        }
        //把连锁数据存入数组
        int[] arrTmp = new int[tmpList.size()];
        for (int i = 0; i < arrTmp.length; i++) {
            arrTmp[i] = tmpList.get(i);
        }
        //通过解析得到答案的集合
        List result = prepared(arrTmp);
        //移除集合中不必要的数据
        List<Object> removeX = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            if(!result.get(i).equals('X')){
                removeX.add(result.get(i));
            }
        }
        //再把数据重新整理后存入新的集合
        String strTmp = "";
        for (int i = 0; i < removeX.size(); i++) {
            strTmp += removeX.get(i);
        }
        //得到最新的数据
        onResultListener.getResult(tmpList.get(0), strTmp);
    }

    public interface OnResultListener{
        void getResult(int number, String result);
    }

}
