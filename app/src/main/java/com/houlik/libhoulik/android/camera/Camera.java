package com.houlik.libhoulik.android.camera;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.houlik.libhoulik.android.listener.OnBitmapListener;
import com.houlik.libhoulik.android.listener.OnSurfaceListener;
import com.houlik.libhoulik.android.util.AUtils;
import com.houlik.libhoulik.android.util.BitmapUtils;
import com.houlik.libhoulik.android.util.HLLog;
import com.houlik.libhoulik.android.util.IOUtils;
import com.houlik.libhoulik.houlik.utils.DateUtils;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义相机
 * @author Houlik
 * @since 2022/12/6
 * @description
 *
 */
public class Camera implements SurfaceHolder.Callback , android.hardware.Camera.PictureCallback {

    private final String TAG = "Camera";
    private SurfaceView oSurfaceView;
    private SurfaceHolder oSurfaceHolder;
    //相机
    private android.hardware.Camera oCamera;
    //文件文件夹
    private String mFolderPath;
    //文件名
    private String mFileName;
    //照片格式
    //private String mFileExtension = ".jpg";
    //得到照片的监听器
    private OnBitmapListener mOnBitmapListener;
    //照片存储路径
    private File oFile;
    //窗口宽高
    private int mScreenWidth;
    private int mScreenHeight;
    //文件中名称
    //private String mFileRecord = "_SE_";

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
        releaseCamera();

    }

    @Override
    public void onPictureTaken(byte[] data, android.hardware.Camera camera) {
        //文件名称 2022127_SE_154559.png
        //mFileName = DateUtils.getInstance().getDate(null)+ mFileRecord + DateUtils.getInstance().getTime() + mFileExtension;

        mFileName = DateUtils.getInstance().dateToLong(new Date()) + ".png";
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

    //拍完照, 需要重新预览
    public void cameraRePreview(){
        oCamera.startPreview();
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
            setOnSurfaceListener(null);
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

    private void getAllSupportSize(android.hardware.Camera camera){
        camera = android.hardware.Camera.open(0);
        List pictureSizes = camera.getParameters().getSupportedPictureSizes();
        List previewSizes = camera.getParameters().getSupportedPreviewSizes();
        for (int i=0; i <pictureSizes.size();i ++){
            android.hardware.Camera.Size pSize = (android.hardware.Camera.Size) pictureSizes.get(i);
            HLLog.i(TAG, "---------------------PictureSize.width = "+pSize.width+"-----------------PictureSize.height = "+pSize.height);
        }
        for (int i=0; i <previewSizes.size();i ++){
            android.hardware.Camera.Size pSize = (android.hardware.Camera.Size) previewSizes.get(i);
            HLLog.i(TAG, "--------------------previewSize.width = "+pSize.width+"-----------------previewSize.height = "+pSize.height);
        }
    }

    //回调设置
    private void getCaptureBitmap(){
        Bitmap bitmap = BitmapFactory.decodeFile(oFile.getAbsolutePath());
        if(mOnBitmapListener != null) {
            mOnBitmapListener.getBitmap(bitmap, oFile);
        }
    }

    //回调监听器
    public void setOnBitmapListener(OnBitmapListener mOnBitmapListener){
        this.mOnBitmapListener = mOnBitmapListener;
    }

    //回调SurfaceView 和 SurfaceHolder
    public void setOnSurfaceListener(OnSurfaceListener onSurfaceListener){
        if(onSurfaceListener != null) {
            onSurfaceListener.getSurface(oSurfaceView, oSurfaceHolder);
        }
    }

    /**
     * 修改位图尺寸, 获取比较中间的位图
     * <SurfaceView
     * android:layout_width="273dp"
     * android:layout_height="364dp" />
     *
     * <RelativeLayout
     * android:layout_width="273dp"
     * android:layout_height="50dp"
     * android:background="@color/white"/>
     *
     * <RelativeLayout
     * android:layout_width="273dp"
     * android:layout_height="50dp"
     * android:layout_alignBottom="@+id/camera_sv"
     * android:background="@color/white"/>
     *
     * @param tmp
     * @return
     */
    public Bitmap changeSize(Bitmap tmp){
        Bitmap bmp = BitmapUtils.rotatePic90(tmp,90);
        //获取原始宽高，并获取宽高中较大的
        int nWidth = bmp.getWidth();
        int nHeight = bmp.getHeight();
        Bitmap bitmap = BitmapUtils.bitmapCrop(bmp,0, (nHeight/4) - 300, nWidth - 100, (nHeight/2) + 600);
        Canvas canvas = new Canvas( bitmap );
        //生成正方形
        canvas.drawBitmap( bmp, 0 , (nHeight/2)+600, null );
        canvas = null;
        return bitmap;
    }

    /**
     * 以无格式方式保存拍摄的照片
     * 分类保存到SD卡
     * 将宽高减半后的位图固定质量后将Bitmap保存到SD卡
     * 将MB缩小到KB, 位图像素保持清晰
     * @param activity
     * @param file
     * @param folder
     * @param key item / locationDB / single_container / space_region / custom
     */
    public void classificationPreservation2SD(Activity activity, File file, String folder, String key){
        //按照静态变量临时保存图片路径
        Map<String, File> picPath = new HashMap<>();
        //图片名称
        Map<String, String> picname = new HashMap<>();
        //完整格式图片名称
        Map<String, String> picnameWithExtension = new HashMap<>();
        //获取图片名称
        int tmpLen = file.getName().length();
        //将当前拍摄的无格式照片的临时完整路径保存到字典中
        picPath.put(key,new File(activity.getExternalFilesDir(folder) + "/" + file.getName().substring(0,tmpLen-4)));
        //将当前的图片名称与格式保存到字典中
        picnameWithExtension.put(key,file.getName());
        //将图片的名称保存到字典中
        picname.put(key,file.getName().substring(0,tmpLen-4));
        //再次从临时保存路径decode到位图
        Bitmap tmpBitmap = com.houlik.libhoulik.android.util.BitmapUtils.getInstance().getSDBitmapDecodeFile(file.toString());
        //最终将经过压缩后的位图正式保存到SD卡
        com.houlik.libhoulik.android.util.BitmapUtils.getInstance().saveBitmap2SDCard(picPath.get(key),tmpBitmap, 80);
    }
}
