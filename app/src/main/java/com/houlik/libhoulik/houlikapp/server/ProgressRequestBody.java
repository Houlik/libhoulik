package com.houlik.libhoulik.houlikapp.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

/**
 * 案例如下 :
 * public static void uploadVideoServer(String filePath) {
 *         OkHttpClient client = new OkHttpClient();
 *         RequestBody requestBody = new MultipartBody.Builder()
 *                 .setType(MultipartBody.FORM)
 *                 .addFormDataPart("file", "video.mp4",
 *                         new ProgressRequestBody(new File(filePath), new ProgressRequestBody.ProgressListener(){
 *                             @Override
 *                             public void onProgress(long bytesWritten, long contentLength, boolean done) {
 *                                 // update progress UI
 *                                 int progress = (int) (100 * bytesWritten / contentLength);
 *                                 if(done){
 *                                      //done
 *                                 }
 *                             }
 *                         })).build();
 *
 *         Request request = new Request.Builder()
 *                 .url("http://cloud.houlik.com:0000/houlikapp/api/user/upload?")
 *                 .post(requestBody)
 *                 .header("token", StaticValue.LOGIN_TOKEN) .build();
 *
 *         try {
 *             Response response = client.newCall(request).execute();
 *             if (response.isSuccessful()) {
 *                 String responseString = response.body().string();
 *                 // 处理成功响应数据 开始
 *             } else {
 *                 // 处理失败响应数据
 *             }
 *         }catch (Exception e){
 *             e.printStackTrace();
 *         }
 *     }
 * @author : houlikapp
 * @desription : 自定义视频上传进度回调类
 * @email : houlik@126.com
 * @since : 9/28/2022
 */
public class ProgressRequestBody extends RequestBody {

    private ProgressListener mListener;

    private static final int DEFAULT_BUFFER_SIZE = 2048;

    private File file;

    public ProgressRequestBody(File file, ProgressListener listener) {
        this.file = file;
        mListener = listener;
    }

    @Override
    public MediaType contentType() {
        return MediaType.parse("video/mp4");
    }

    @Override
    public long contentLength() throws IOException {
        return file.length();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        long fileLength = file.length();
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        long uploadedBytes = 0;

        try (InputStream inputStream = new FileInputStream(file)) {
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                uploadedBytes += read;
                sink.write(buffer, 0, read);
                mListener.onProgress(uploadedBytes, fileLength, uploadedBytes == contentLength());
            }
        }

        sink.flush();
    }

    public interface ProgressListener {
        //int progress = (int) (100 * bytesWritten / contentLength);
        //(contentLength/1024/1024) + "MB"
        void onProgress(long bytesWritten, long contentLength, boolean done);
    }
}
