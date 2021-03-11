package com.houlik.libhoulik.android.util;

import android.util.Log;

import org.apache.http.HttpResponse;//过时
import org.apache.http.client.methods.HttpGet;//过时
import org.apache.http.impl.client.DefaultHttpClient;//过时
import org.apache.http.util.EntityUtils;//过时
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.UUID;

/**
 * @author houlik
 * @since 2020/5/31
 */
public class BaiduScan {

    private static final String TAG = "BaiduScan";
    private static final int TIME_OUT = 10 * 1000; // 超时时间
    private static final String CHARSET = "utf-8"; // 设置编码

    public static String resolvePostResponse(String postResponseUrl){
        String requestUri = "http://image.baidu.com" + postResponseUrl;
        HttpGet httpRequest = new HttpGet(requestUri);
        String strResult = null;
        try {

            HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);

            if (httpResponse.getStatusLine().getStatusCode() == 200) {

                strResult = EntityUtils.toString(httpResponse.getEntity());

                Document document = Jsoup.parseBodyFragment(strResult);
                strResult = document.getElementsByClass("guess-info-not-found-title").text();
                if (strResult == ""){
                    strResult = document.getElementsByClass("guess-info-text").text();
                    if(strResult == ""){
                        strResult = document.getElementsByClass("error-msg").text();
                    }
                }
                return strResult;
            } else {
                strResult = "Error Response";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strResult;
    }

    public static String postFile(String filePath, String requestURL) {
        int res=0;
        String result = null;
        String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
        String PREFIX = "--", LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data"; // 内容类型
        File file = new File(filePath);
        try {

            URL url = new URL(requestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setReadTimeout(TIME_OUT);
            conn.setConnectTimeout(TIME_OUT);
            conn.setChunkedStreamingMode(1024 * 1024);// 1024K
            conn.setDoInput(true); // 允许输入流
            conn.setDoOutput(true); // 允许输出流
            conn.setUseCaches(false); // 不允许使用缓存
            conn.setRequestMethod("POST"); // 请求方式
            conn.setRequestProperty("Charset", CHARSET); // 设置编码
            conn.setRequestProperty("Connection", "keep-alive");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="+ BOUNDARY);
            if (file != null) {
                /**
                 * 当文件不为空时执行上传
                 */
                OutputStream outputSteam=conn.getOutputStream();
                DataOutputStream dos = new DataOutputStream(outputSteam);

                StringBuffer sb = new StringBuffer();
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);
                /**
                 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
                 * filename是文件的名字，包含后缀名
                 */

                sb.append("Content-Disposition: form-data; name=\"filedata\"; filename=\""
                        + file.getName() + "\"" + LINE_END);
                sb.append("Content-Type: application/octet-stream; charset="
                        + CHARSET + LINE_END);
                sb.append(LINE_END);
                dos.write(sb.toString().getBytes());
                InputStream is = new FileInputStream(file);

                byte[] bytes = new byte[1024];
                int len = 0;
                while ((len = is.read(bytes)) != -1) {
                    dos.write(bytes, 0, len);
                }
                is.close();
                dos.write(LINE_END.getBytes());
                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
                        .getBytes();
                dos.write(end_data);
                dos.flush();
                /**
                 * 获取响应码 200=成功 当响应成功，获取响应的流
                 */

                res = conn.getResponseCode();

                Log.e(TAG, "response code:" + res);
                if (res == 200) {
                    Log.e(TAG, "request success");
                    InputStream input = conn.getInputStream();
                    StringBuffer sb1 = new StringBuffer();
                    int ss;
                    while ((ss = input.read()) != -1) {
                        sb1.append((char) ss);
                    }
                    result = sb1.toString();
                    Log.e(TAG, "result : " + result);
                } else {
                    Log.e(TAG, "request error");
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    public static String baiduShitu(String imageUrl){
        String uriAPI = null;
        try {
            uriAPI = "http://image.baidu.com/n/pc_search?queryImageUrl="+ URLEncoder.encode(imageUrl,"utf-8")+"&fm=result&pos=&uptype=paste";
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        HttpGet httpRequest = new HttpGet(uriAPI);
        String strResult = null;
        try {

            HttpResponse httpResponse = new DefaultHttpClient()
                    .execute(httpRequest);

            if (httpResponse.getStatusLine().getStatusCode() == 200) {

                strResult = EntityUtils.toString(httpResponse.getEntity());

                Document document = Jsoup.parseBodyFragment(strResult);
                strResult = document.getElementsByClass("guess-info-not-found-title").text();
                if (strResult == ""){
                    strResult = document.getElementsByClass("guess-info-text").text();
                    if(strResult == ""){
                        strResult = document.getElementsByClass("error-msg").text();
                    }
                }
                return strResult;
            } else {
                strResult = "Error Response";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strResult;
    }
}
