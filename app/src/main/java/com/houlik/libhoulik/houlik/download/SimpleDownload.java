package com.houlik.libhoulik.houlik.download;

//import android.support.annotation.NonNull;
import androidx.annotation.NonNull;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;

//import io.reactivex.annotations.NonNull;

/**
 * 服务器下载文件
 * 轻量级数据下载 几十KB到几百KB，3MB以内
 * 视服务器速度而论，目前已经测试100MB下载没问题
 * Created by houlik on 2018/11/9.
 */

public class SimpleDownload implements Runnable {

    private final String TAG = "SIMPLE DOWNLOAD";

    /**
     * 服务器ip异常
     * 网络缓慢异常
     */

    /**
     * 1MB = 1024kb, 1kb = 1024b
     */

    private String downloadURL;
    private String dirPath;
    private String fileName;
    private URL url = null;
    /**
     * 连接情况
     * HTTP/1.0 200 OK
     * HTTP/1.0 401 Unauthorized
     * -1 无相关代号
     */
    private HttpURLConnection httpURLConnection = null;
    private DownloadCallback downloadCallback;
    private Builder builder;
    private OnCustomProcess onCustomProcess;
    //用于记录已经下载的文件大小
    private double totalDownloadSize = 0;

    private SimpleDownload(Builder builder){
        this.builder = builder;
        this.downloadURL = builder.downloadURL;
        this.dirPath = builder.dirPath;
        this.fileName = builder.fileName;
        this.downloadCallback = builder.downloadCallback;
        this.onCustomProcess = builder.onCustomProcess;
    }

    /**
     * 获取网络数据流以及文件大小
     * @return
     */
    private InputStream getURLInputStream(){
        InputStream inputStream = null;
        try {
            url = new URL(downloadURL);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            //必须启动线程运行, 否则抛异常
            httpURLConnection.setRequestProperty("Accept-Encoding", "identity");
            downloadCallback.getHttpURLConnectioninstance(httpURLConnection);
            if (httpURLConnection.getResponseCode() == 200) {
                if (downloadCallback != null) {
                    downloadCallback.getDataSize(httpURLConnection.getContentLength());
                }
                inputStream = url.openStream();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            try {
                throw new Exception("当前数据流发生错误");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        return inputStream;
    }

    /**
     * 开始下载
     */
    private void startDownload(){
        switch (builder.process){
            case -1:
                try {
                    throw new Exception("请输入 process 正确值");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 0:
                //如果设置为0, 就必须实现OnCustomProcess接口
                if(builder.process == 0 && onCustomProcess == null){
                    try {
                        throw new Exception("process值为零, onCustomProcess 不能为空");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    //自定义流操作
                    onCustomProcess.customProcessInputStream(getURLInputStream(), builder, downloadCallback, totalDownloadSize);
                }
                break;
            case 1:
                useRandomAccessFile();
                break;
            case 2:
                // doSomething
                break;
        }

    }

    /**
     * 使用RandomAccessFile加载数据
     * RandomAccessFile 没有文件将自动创建, 对于已经存在文件不会更改文件已有的大小
     */
    private void useRandomAccessFile(){
        BufferedInputStream inputStream = new BufferedInputStream(getURLInputStream());
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(dirPath+fileName, "rw");
            byte[] bytes = new byte[1024];
            int len;

            while ((len = inputStream.read(bytes)) != -1){
                randomAccessFile.write(bytes, 0 ,len);
                if(downloadCallback != null) {
                    downloadCallback.getDownloadProgress(len, totalDownloadSize += len);
                }
            }
            randomAccessFile.close();
            inputStream.close();
            httpURLConnection.disconnect();
        } catch (FileNotFoundException e) {
            if(downloadCallback != null) {
                //回调下载不成功
                downloadCallback.getDownloadResult(1);
            }
            e.printStackTrace();
        } catch (IOException e) {
            if(downloadCallback != null) {
                //回调下载不成功
                downloadCallback.getDownloadResult(2);
            }
            e.printStackTrace();
        }
        if(downloadCallback != null) {
            //回调下载成功
            downloadCallback.getDownloadResult(0);
        }
    }

    @Override
    public void run() {
        //开始启动线程下载
        startDownload();
    }

    public interface OnCustomProcess{
        /**
         * 通过url.openStream()得到的数据流来自定义操作
         * 该inputStream已经存在数据流
         * 可以通过builder来获取文件夹路径以及文件名
         *
         * 如 :
         * BufferedOutputStream bufferedOutputStream = null;
         *      try{
         *          bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(new FileBuilder( ??? )));
         *          byte[] b = new byte[1024];
         *          int len = 0;
         *          while((len = inputStream.read(b))!= -1){
         *              bufferedOutputStream.write(b, 0 ,len);
         *              //此处用于进度条使用 - 可省略 - 但同时无进度可取
         *              if(downloadCallback != null) {
         *                     downloadCallback.getDownloadProgress(len, totalDownloadSize += len);
         *                 }
         *          }
         *          bufferedOutputStream.close();
         *          inputStream.close();
         *      }catch (Exception e){
         *          e.printStackTrace();
         *      }
         *
         * @param inputStream
         * @param builder
         */
        void customProcessInputStream(InputStream inputStream, Builder builder, DownloadCallback downloadCallback, double totalDownloadSize);
    }

    public interface DownloadCallback{
        /**
         * 0 = 下载成功
         * 1 = 找不到文件链接
         * 2 = 数据流出现问题
         * @param result
         */
        void getDownloadResult(int result);

        void getHttpURLConnectioninstance(HttpURLConnection httpURLConnection) throws IOException;

        /**
         * 获取文件大小
         * @param dataSize
         */
        void getDataSize(int dataSize);

        /**
         * 获取下载进度
         * DecimalFormat decimalFormat = new DecimalFormat("##0.00"); 四舍五入方法
         * 最后使用 decimalFormat.format(???) 来转换
         * @param progress
         */
        void getDownloadProgress(int progress, double totalDownloadSize);
    }

    public static class Builder{
        //网址
        private String downloadURL;
        //文件夹
        private String dirPath;
        //文件名
        private String fileName;
        /**
         * 0 = custom;
         * 1 = randomAccessFile
         * 2 = InputStream
         */
        private int process = -1;
        //下载过程结果的回调
        private DownloadCallback downloadCallback;
        //自定义实现流的操作接口
        private OnCustomProcess onCustomProcess = null;

        public Builder setDownloadHttpURL(@NonNull String downloadURL){
            this.downloadURL = downloadURL;
            return this;
        }

        public Builder setFileDirPath(@NonNull String dirPath){
            this.dirPath = dirPath;
            return this;
        }

        public String getFileDirPath(){
            return dirPath;
        }

        public Builder setFileName(@NonNull String fileName){
            this.fileName = fileName;
            return this;
        }

        public String getFileName(){
            return fileName;
        }

        public Builder setProcess(@NonNull int process){
            this.process = process;
            return this;
        }

        public Builder setCustomProcess(OnCustomProcess onCustomProcess){
            this.onCustomProcess = onCustomProcess;
            return this;
        }

        /**
         * 便于查询 byte - kb - mb
         * 如果输入的byte为 123456 按照判断除于 1024 将获知 是多少kb
         * @param bytes
         * @return
         */
        public String transferByte2KB2MB(int bytes){
            DecimalFormat decimalFormat;
            if(bytes < 1024) {
                decimalFormat = new DecimalFormat("##0");
                return decimalFormat.format(bytes / 1024) + " B";
            }else if(bytes > 1024 && bytes < 1048576){
                decimalFormat = new DecimalFormat("##0");
                return decimalFormat.format(bytes / 1024) + " KB";
            }else{
                decimalFormat = new DecimalFormat("##0.00");
                return decimalFormat.format((bytes / 1024) / 1024) + " MB";
            }
        }

        public Builder setDownloadCallback(@NonNull DownloadCallback downloadCallback){
            this.downloadCallback = downloadCallback;
            return this;
        }

        public SimpleDownload startDownload(){
            SimpleDownload simpleDownload = new SimpleDownload(this);
            new Thread(simpleDownload).start();
            return simpleDownload;
        }
    }
}
