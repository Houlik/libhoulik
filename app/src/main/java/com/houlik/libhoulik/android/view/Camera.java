package com.houlik.libhoulik.android.view;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.houlik.libhoulik.android.listener.OnSurfaceListener;
import com.houlik.libhoulik.android.util.AUtils;
import com.houlik.libhoulik.houlik.utils.DateUtils;
import com.houlik.libhoulik.android.util.IOUtils;
import com.houlik.libhoulik.android.listener.OnBitmapListener;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 自定义相机
 * Created by houlik on 2018/5/15.
 */

public class Camera implements SurfaceHolder.Callback , android.hardware.Camera.PictureCallback {


    private final String TAG = "HL_CAMERA : ";
    private SurfaceView oSurfaceView;
    private SurfaceHolder oSurfaceHolder;
    //相机
    private android.hardware.Camera oCamera;
    //文件文件夹
    private String mFolderPath;
    //文件名
    private String mFileName;
    //照片格式
    private String mFileExtension = ".jpg";
    //得到照片的监听器
    private OnBitmapListener mOnBitmapListener;
    //照片存储路径
    private File oFile;
    //窗口宽高
    private int mScreenWidth;
    private int mScreenHeight;
    //文件中名称
    private String mFileRecord = "_SE_";

    public Camera(android.hardware.Camera camera, Activity activity, int surfaceViewID, String folderPath) {
        this.oCamera = camera;
        mScreenWidth = AUtils.getInstance().getScreenWidth(activity);
        mScreenHeight = AUtils.getInstance().getScreenHeight(activity);
        oSurfaceView = activity.findViewById(surfaceViewID);
        this.mFolderPath = folderPath;
        oSurfaceHolder = oSurfaceView.getHolder();
        oSurfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
//        setStartPreview(oCamera, holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        setStartPreview(oCamera, holder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //必须在调用当前类中使用释放方法//在此处使用如果跳向其它界面,当前相机将被销毁
//        releaseCamera();
    }

    @Override
    public void onPictureTaken(byte[] data, android.hardware.Camera camera) {
        //文件名称
        mFileName = DateUtils.getInstance().getDate(null)+ mFileRecord + DateUtils.getInstance().getTime() + mFileExtension;
        //文件保存路径
        oFile = new File(mFolderPath + mFileName);
        //自动保存
        IOUtils.getInstance().setByte2SD(oFile, data);
        //回调得到位图以及路径
        getCaptureBitmap();

        surfaceCreated(oSurfaceHolder);
    }

    //设置开始浏览
    public void setStartPreview(android.hardware.Camera camera, SurfaceHolder surfaceHolder){
        try {
            //设置浏览
            camera.setPreviewDisplay(surfaceHolder);
            //开始浏览
            camera.startPreview();
            //相机旋转90度
            camera.setDisplayOrientation(90);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //释放相机
    public void releaseCamera(){
        if(oCamera != null){
            //停止浏览
            oCamera.stopPreview();
            //浏览回调清空
            oCamera.setPreviewCallback(null);
            //释放相机
            oCamera.release();
            //清空相机数据
            oCamera = null;
            //图片回调清空
            setOnBitmapListener(null);
        }
    }

    //开始启动相机
    public void capture(){
        // 获取相机参数集
        android.hardware.Camera.Parameters oCameraParameters = oCamera.getParameters();
        // 设置预览照片的大小 // 获取支持保存图片的尺寸
        List<android.hardware.Camera.Size> listPreviewSizes = oCameraParameters.getSupportedPreviewSizes();

        //遍历集合中保存的浏览尺寸
        int previewIndex = 0;
        for (int i = 0; i < listPreviewSizes.size(); i++) {
            //如果得到的尺寸大于本机就取得编号
            if(listPreviewSizes.get(i).width > mScreenWidth){
                previewIndex = i;
                break;
            }
        }
        android.hardware.Camera.Size previewSize = listPreviewSizes.get(previewIndex);
        oCameraParameters.setPreviewSize(previewSize.width, previewSize.height);

        // 设置照片的大小
        List<android.hardware.Camera.Size> listPictureSizes = oCameraParameters.getSupportedPictureSizes();
        //遍历集合中保存的图片尺寸
        int photoIndex = 0;
        for (int i = 0; i < listPictureSizes.size(); i++) {
            //如果得到的尺寸大于本机就取得编号
            if(listPictureSizes.get(i).width > mScreenHeight){
                photoIndex = i;
                break ;
            }
        }
        android.hardware.Camera.Size pictureSize = listPictureSizes.get(photoIndex);
        oCameraParameters.setPictureSize(pictureSize.width, pictureSize.height);
        //设置聚焦
        oCameraParameters.setFocusMode(android.hardware.Camera.Parameters.FOCUS_MODE_AUTO);
        //绑定当前所有的设置到自定义的相机
        oCamera.setParameters(oCameraParameters);
        oCamera.autoFocus(new android.hardware.Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, android.hardware.Camera camera) {
                //成功聚焦取得照片
                if(success){
                    camera.takePicture(null,null, Camera.this);
                }
            }
        });
    }

    //回调设置
    private void getCaptureBitmap(){
        Bitmap bitmap = BitmapFactory.decodeFile(oFile.getAbsolutePath());
        mOnBitmapListener.getBitmap(bitmap, oFile);
    }

    //回调监听器
    public void setOnBitmapListener(OnBitmapListener mOnBitmapListener){
        this.mOnBitmapListener = mOnBitmapListener;
    }

    //回调SurfaceView 和 SurfaceHolder
    public void setOnSurfaceListener(OnSurfaceListener onSurfaceListener){
        onSurfaceListener.getSurface(oSurfaceView, oSurfaceHolder);
    }
}
