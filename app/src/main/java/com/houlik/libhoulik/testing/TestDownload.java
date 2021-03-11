package com.houlik.libhoulik.testing;

import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by houlik on 2018/11/9.
 */

public class TestDownload {

    private final String TAG = "TestDownload : ";

    //网址
    private String downloadURL;
    //路径
    private String dirPath;
    //文件名
    private String fileName;
    //数据大小
    private long dataSize;

    public TestDownload(String downloadURL, String dirPath, String fileName){
        this.downloadURL = downloadURL;
        this.dirPath = dirPath;
        this.fileName = fileName;
        getDataSize();
        initFileSize();
    }

    public long getDownloadDataSize(){
        return this.dataSize;
    }

    /**
     * 获取数据大小
     */
    public void getDataSize(){
        URL url = null;
        HttpURLConnection httpURLConnection = null;
        try {
            url = new URL(downloadURL);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            if(httpURLConnection.getResponseCode() == 200){
                //获取数据大小
                dataSize = httpURLConnection.getContentLength();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        httpURLConnection.disconnect();
    }

    /**
     * 初始化文件容量大小
     */
    private void initFileSize(){
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(dirPath+fileName, "rw");
            randomAccessFile.setLength(dataSize);
            randomAccessFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startDownload(long skip, long endSkip, long seek) throws Exception{


        URL url = new URL(downloadURL);
        InputStream inputStream = url.openStream();
        inputStream.skip(skip);

        RandomAccessFile randomAccessFile = new RandomAccessFile(dirPath+fileName, "rw");
        randomAccessFile.seek(seek);

        byte[] b = new byte[1024];
        int len = 0;
        int length = 0;
        while ((len = inputStream.read(b)) < endSkip){
            randomAccessFile.write(b, 0, len);
            System.out.println(length += len);
        }

        randomAccessFile.close();
        inputStream.close();

    }
}
