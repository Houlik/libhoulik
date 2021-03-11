package com.houlik.libhoulik.android.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片工具类
 * Created by Houlik on 2017-12-10.
 */

public class ImageUtils {

    private static ImageUtils imageUtils = new ImageUtils();

    private ImageUtils(){}

    public static ImageUtils getImageUtils(){
        if(imageUtils == null){
            return new ImageUtils();
        }
        return  imageUtils;
    }

    /**
     *
     * @param context
     * @param arrImageID
     * @return 得到数组资源存入集合中后返回集合
     */
    public List<View> setImageViewToList(Context context, int[] arrImageID){
        List<View> list_tmp = new ArrayList<>();
        for (int i = 0; i < arrImageID.length; i++) {
            ImageView iv_tmp = new ImageView(context);
            iv_tmp.setImageResource(arrImageID[i]);
            list_tmp.add(iv_tmp);
        }
        return list_tmp;
    }

    /**
     * ImageView 资源集合
     * @param context
     * @param typedArrayResource
     * @return 得到XML中数组资源存入集合中后返回集合
     */
    public List<View> initImageView(Context context,int typedArrayResource){
        TypedArray typedArray = context.getResources().obtainTypedArray(typedArrayResource);
        int[] image = new int[typedArray.length()];
        List<View> imageViewList = new ArrayList<>();
        ImageView imageView;
        for (int i = 0; i < image.length; i++) {
            image[i] = typedArray.getResourceId(i, 0);
            imageView = new ImageView(context);
            imageView.setImageResource(image[i]);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageViewList.add(imageView);
        }
        return imageViewList;
    }

    /**
     * 这是把得到的图片经过ImageView设置保存到集合返回
     * @param context
     * @param imageResource
     * @return
     */
    public List<ImageView> imageToList(Context context, int...imageResource){
        List<ImageView> imageViewList = new ArrayList<>();
        for (int i = 0; i < imageResource.length; i++) {
            ImageView imageView = new ImageView(context);
            imageView.setImageResource(imageResource[i]);
            imageViewList.add(imageView);
        }
        return imageViewList;
    }

    /**
     * ImageView 从缓冲区获取 Bitmap
     * @param imageView
     * @return
     */
    public Bitmap transferImageView2Bitmap(ImageView imageView){
        //得到Bitmap后必须清空画图缓冲区,否则该图像一直留在缓冲区,二次获取还是最初图像
        imageView.setDrawingCacheEnabled(false);
        imageView.setDrawingCacheEnabled(true);
        Bitmap bitmap = imageView.getDrawingCache();
        return bitmap;
    }

    /**
     * 启动线程下载网络图片
     *
     * @param imageView
     * @param imageURL
     * @return
     */
    public ImageView threadDownloadImage(final ImageView imageView, final String imageURL) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final Bitmap bitmap = BitmapUtils.getInstance().getHttpBitmap(imageURL);

                imageView.post(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(bitmap);
                    }
                });
            }
        });

        thread.start();
        return imageView;
    }

    /**
     * 获取Bitmap宽高
     * 设置ImageView宽高
     * @param activity
     * @param bitmap
     * @param imageView
     */
    public void setImageViewParam(Activity activity, Bitmap bitmap, ImageView imageView){
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int desityDpi = metrics.densityDpi;
        int width = bitmap.getWidth() * 160 / desityDpi;
        int height = bitmap.getHeight() * 160 / desityDpi;
        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        params.width = width;
        params.height = height;
        imageView.setLayoutParams(params);
    }

    /**
     * 自定义ImageView宽高
     * @param activity
     * @param imageView
     * @param gapSize 尺寸差距
     */
    public void setImageViewParam(Activity activity,ImageView imageView, int gapSize){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels - gapSize;
        //调整边框时保持可绘制对象的比例
        imageView.setAdjustViewBounds(true);
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = width;
        imageView.setLayoutParams(layoutParams);
    }

    /** ImageView 尺寸 */
    private int size;
    private void setSize(int size){
        this.size = size;
    }
    public int getSize(){
        return size;
    }

    /**
     * 在程序启动过程中获取自定义后ImageView的宽高
     * @param imageView
     * @return
     */
    public int getSizeOnCreate(final ImageView imageView){
        imageView.post(new Runnable() {
            @Override
            public void run() {
                setSize(imageView.getWidth());
            }
        });
        return  getSize();
    }

    /**
     * 在获取到ImageView的ID后想要修改ImageView的大小,通过scale可快速更改
     * @param context
     * @param imageView
     * @param widthDPValue
     * @param heightDPValue
     */
    public void changeImageViewSizeByLinearLayout(Context context, ImageView imageView, int widthDPValue, int heightDPValue, float scaleX, float scaleY){
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(AUtils.dp2px(context, widthDPValue), AUtils.dp2px(context, heightDPValue));
        imageView.setLayoutParams(layoutParams);
        imageView.setScaleX(scaleX);
        imageView.setScaleY(scaleY);
    }

    /**
     * 在获取到ImageView的ID后想要修改ImageView的大小,通过scale可快速更改
     * @param context
     * @param imageView
     * @param widthDPValue
     * @param heightDPValue
     */
    public void changeImageViewSizeByRelativateLayout(Context context, ImageView imageView, int widthDPValue, int heightDPValue, float scaleX, float scaleY){
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(AUtils.dp2px(context, widthDPValue), AUtils.dp2px(context, heightDPValue));
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        imageView.setLayoutParams(layoutParams);
        imageView.setScaleX(scaleX);
        imageView.setScaleY(scaleY);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    /**
     * 在获取到ImageView的ID后想要修改ImageView的大小,通过scale可快速更改
     * 设置ImageView处于位置
     * 通过addRule()设置居中 或者 其它布局
     * @param context
     * @param imageView
     * @param widthDPValue
     * @param heightDPValue
     */
    public void changeImageViewSizeByRelativateLayoutRules(Context context, ImageView imageView, int widthDPValue, int heightDPValue, float scaleX, float scaleY){
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(AUtils.dp2px(context, widthDPValue), AUtils.dp2px(context, heightDPValue));
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        imageView.setLayoutParams(layoutParams);
        imageView.setScaleX(scaleX);
        imageView.setScaleY(scaleY);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
    }
}
