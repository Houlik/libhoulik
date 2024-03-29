package com.houlik.libhoulik.houlikapp.server;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author : houlikapp
 * @desription : 这是与服务端之间联系的服务类
 * @email : houlik@126.com
 * @since : 10/3/2023
 */
public class ServerUtils {

    /**
     * 使用 application/x-www-form-urlencoded 表单协议
     * 向服务端发送请求
     * @param formBody 构造请求参数 FormBody formBody = new FormBody.Builder().add("app_id", appId).add("account", account).add("password", password).build();
     * @param url "http://?.houlik.top:8168/folder/folder/folder?"
     */
    public static boolean uploadInfo(FormBody formBody, String url, String headerName, String headerValue, ServerImp serverImp){

        Request request = null;

        if(headerName.equals("") | headerName.equals(null) || headerValue.equals("") | headerValue.equals(null)){
            request = new Request.Builder().url(url).post(formBody).build();
        }else {
            // 构造请求对象
            request = new Request.Builder().url(url).post(formBody).header(headerName, headerValue).build();
        }

        // 构造 OkHttp3 客户端对象
        OkHttpClient client = new OkHttpClient.Builder().build();

        // 发送请求并处理响应
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String responseString = response.body().string();
                serverImp.responseServerData(responseString);
                return true;
                // 处理成功响应数据
            }else{
                serverImp.responseFailured(response);
                return false;
            }
        } catch (IOException e) {
            serverImp.responseException(e);
            // 处理网络异常
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 使用 application/x-www-form-urlencoded 表单协议
     * 无限制上传文件
     * @param type MultipartBody.FORM
     * @param formDataKey "file"
     * @param formDataValue "video.mp4"
     * @param headerKey "token"
     * @param headerValue
     * @param url "http://......"
     * @param progressRequestBody new ProgressRequestBody.ProgressListener(){}
     * @param serverImp
     */
    public static boolean uploadData(MediaType type, String formDataKey, String formDataValue, String headerKey, String headerValue, String url, ProgressRequestBody progressRequestBody, ServerImp serverImp) {

        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(type)
                .addFormDataPart(formDataKey, formDataValue, progressRequestBody).build();

        Request request = null;

        if(headerKey.equals("") | headerKey.equals(null) || headerValue.equals("") | headerValue.equals(null)){
            request = new Request.Builder()
                    .url(url)
                    .post(requestBody).build();
        }else{
            request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .header(headerKey, headerValue).build();
        }

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String responseString = response.body().string();
                // 处理成功响应数据 开始
                serverImp.responseServerData(responseString);
                return true;
            } else {
                // 处理失败响应数据
                serverImp.responseFailured(response);
                return false;
            }
        }catch (Exception e){
            serverImp.responseException(e);
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 与以上相同,只是当前函数返回的是 Call 对象
     * @param type
     * @param formDataKey
     * @param formDataValue
     * @param headerKey
     * @param headerValue
     * @param url
     * @param progressRequestBody
     * @param serverImp
     * @return
     */
    public static Call callUploadData(MediaType type, String formDataKey, String formDataValue, String headerKey, String headerValue, String url, ProgressRequestBody progressRequestBody, ServerImp serverImp) {

        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(type)
                .addFormDataPart(formDataKey, formDataValue, progressRequestBody).build();

        Request request = null;

        if (headerKey.equals("") | headerKey.equals(null) || headerValue.equals("") | headerValue.equals(null)) {
            request = new Request.Builder()
                    .url(url)
                    .post(requestBody).build();
        } else {
            request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .header(headerKey, headerValue).build();
        }

        Call call = client.newCall(request);

        try {
            Response response = call.execute();
            if (response.isSuccessful()) {
                String responseString = response.body().string();
                serverImp.responseServerData(responseString);
            } else {
                serverImp.responseFailured(response);
            }
        } catch (IOException e) {
            serverImp.responseException(e);
            e.printStackTrace();
        }
        return call;
    }

}
