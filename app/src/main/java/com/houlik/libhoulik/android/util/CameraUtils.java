package com.houlik.libhoulik.android.util;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.houlik.libhoulik.houlik.utils.DateUtils;

import java.io.File;
import java.util.Date;

/**
 * Created by Houlik on 2018-01-08.
 */

public class CameraUtils {

    /**
     * 关于FileProvider
     * 请参考UriUtils7
     */

    private static CameraUtils cameraUtils = new CameraUtils();

    private final String TAG = "CameraUtils";

    private CameraUtils(){}

    public static CameraUtils getInstance(){
        if(cameraUtils == null){
            new CameraUtils();
        }
        return cameraUtils;
    }

    /**
     * 正常打开相机, 不做任何操作
     * 通过 onActivityResult 获取数据
     * Bundle bundle = data.getExtras();
     * bundle.get("data") 强制转换何种类型
     * 返回的数据是小而低像素
     * @param activity
     */
    public void openCamera(Activity activity){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        activity.startActivityForResult(intent, 1);
    }

    /**
     * 针对10.0以上版本 context.getExternalFilesDir(null).toString()
     * 自动创建文件名
     * 将照片保存到 getExternalFilesDir(Environment.DIRECTORY_PICTURES)
     * 在该路径下另行创建Pictures文件夹
     * 安装当前日期取文件名 格式是 png
     * @param activity
     * @param requestCode
     * @return 返回文件完整路径名称
     */
    public File saveTheCapturedPic(Activity activity, int requestCode){
        File file = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String filename = DateUtils.getInstance().dateToLong(new Date()) + ".png";
        File fullpath = new File(file.toString() + "/" + filename);
        Log.i(TAG, fullpath.toString());
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        ContentValues contentValues = new ContentValues(1);
        contentValues.put(MediaStore.Images.Media.DATA, fullpath.toString());
        Uri uri = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        activity.startActivityForResult(intent, requestCode);
        return fullpath;
    }

    /**
     * 针对7.0以上版本
     * @param activity
     * @param file
     * @param requestCode
     */
    private void openCameraSavePicture(Activity activity, File file, int requestCode){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        ContentValues contentValues = new ContentValues(1);
        contentValues.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
        activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, contentValues);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 创建图片地址uri,用于保存拍照后的照片 Android 10以后使用这种方法
     */
    private Uri createImageUri(Context context) {
        String status = Environment.getExternalStorageState();
        // 判断是否有SD卡,优先使用SD卡存储,当没有SD卡时使用手机存储
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return context.getApplicationContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
        } else {
            return context.getApplicationContext().getContentResolver().insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, new ContentValues());
        }
    }

}


