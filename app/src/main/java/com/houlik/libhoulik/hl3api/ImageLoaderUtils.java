package com.houlik.libhoulik.hl3api;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;

/**
 * Manifest-Permission Internet/Write/Read
 * 初始化与封装第三方API　UniversalImageLoader 并加载网络图片
 * 本地/网络 图片 多线程异步加载和缓存处理
 * 监听回调
 * 根据控件大小对Bitmap裁剪减少内存使用
 * 控制加载过程,暂停加载 - 重新加载
 *
 * 显示图片所使用的uri：
 * String imageUri = “http://site.com/image.png“; // from Web
 * String imageUri = “file:///mnt/sdcard/image.png”; // from SD card
 * String imageUri = “content://media/external/audio/albumart/13”; // from content provider
 * String imageUri = “assets://image.png”; // from assets
 * String imageUri = “drawable://” + R.drawable.image; // from drawables (only images, non - 9patch)
 * 注意：使用drawable://注意使用本地图片加载方法：setImageResource带代替ImageLoader。
 *
 * Created by Houlik on 2017/6/26.
 */

public class ImageLoaderUtils {

    private Context context;
    //有多少条UniversalImageLoader线程 1到5之间
    private final int THREAD_COUNT = 4;
    //图片加载优先级
    private final int PRIORITY = 2;
    //缓存多少张图片
    private final int DISK_CACHE_SIZE = 100 * 1024;
    //连接超时
    private final int CONNECTION_TIMEOUT = 5 * 1000;
    //读取超时
    private final int READ_TIMEOUT = 30 * 1000;

    //第三方类对象
    private ImageLoader mImageLoader = null;

    //本类对象
    private static ImageLoaderUtils mInstance = null;

    //无效地址显示的图片
    private Drawable mImageEmptyURL = null;
    //下载错误显示的图片
    private Drawable mImageOnFailDownload = null;

    //单例模式方法
    public static ImageLoaderUtils getInstance(Context context, Drawable mImageEmptyURL){
        if(mInstance == null){
            synchronized (ImageLoaderUtils.class){
                if(mInstance == null){
                    mInstance = new ImageLoaderUtils(context, mImageEmptyURL);
                }
            }
        }
        return mInstance;
    }


    //单例模式的私有构造方法
    private ImageLoaderUtils(Context context, Drawable mImageEmptyURL){
        this.context = context;
        ImageLoaderConfiguration imageLoaderConfiguration = new ImageLoaderConfiguration.Builder(context)
                .threadPoolSize(THREAD_COUNT) //线程条数
                .threadPriority(Thread.NORM_PRIORITY - PRIORITY) //线程池优先级
                .denyCacheImageMultipleSizesInMemory() //防止缓存多套的尺寸图片到内存中
                .memoryCache(new WeakMemoryCache()) //使用弱引用内存缓存
                .diskCacheSize(DISK_CACHE_SIZE) //分配硬盘缓存大小
                .diskCacheFileNameGenerator(new Md5FileNameGenerator()) //使用MD5 命名文件
                .tasksProcessingOrder(QueueProcessingType.LIFO) //图片下载顺序
                .defaultDisplayImageOptions(getDefaultOptions(mImageEmptyURL)) //默认的图片加载Options
                .imageDownloader(new BaseImageDownloader(context, CONNECTION_TIMEOUT, READ_TIMEOUT)) //设置图片下载器
                .writeDebugLogs() //debug输出日志
                .build();

        ImageLoader.getInstance().init(imageLoaderConfiguration);
        mImageLoader = ImageLoader.getInstance();
    }

    //实现默认的Options
    private DisplayImageOptions getDefaultOptions(Drawable mImageEmptyURL) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(mImageEmptyURL) //图片地址为空时
                .showImageOnFail(mImageOnFailDownload) //图片下载失败时
                .cacheInMemory(true) //设置图片可以缓存在内存
                .cacheOnDisk(false) //设置图片可以缓存在硬盘 //设置true -> java.io.IOException: read failed: ENOENT (No such file or directory) 需要相应的写入权限 //Caused by: android.system.ErrnoException: read failed: ENOENT (No such file or directory) //会出现显示不全的情况
                .bitmapConfig(Bitmap.Config.RGB_565) //使用的图片解码类型
                .decodingOptions(new BitmapFactory.Options()) //图片解码配置
                .build();
        return options;
    }

    //1.file:// 2.drawable:// 3.content://..../...jpg 4.assets:// 5.http://

    /**
     * 加载图片API
     * @param url
     * @param imageView
     * @param options 默认已经设置
     * @param listener
     */
    public void displayImage(String url, ImageView imageView,
                             DisplayImageOptions options,
                             ImageLoadingListener listener){
        if(mImageLoader != null){
            mImageLoader.displayImage(url,imageView,options,listener);

        }
    }

    public void displayImage(String url, ImageView imageView, ImageLoadingListener listener){
        displayImage(url, imageView, null, listener);
    }

    /**
     * 从路径获取位图添加到ImageView
     * @param url
     * @param imageView
     */
    public void displayImage(String url, ImageView imageView){
        displayImage(url, imageView, null, null);
    }

    public Bitmap getBitmap(String uri){
        return mImageLoader.loadImageSync(uri);
    }

    //清除内存缓存
    public void clearMemoryCache(){
        mImageLoader.clearMemoryCache();
    }

    //清楚硬盘缓存
    public void clearDiskCache(){
        mImageLoader.clearDiskCache();
    }

    public void saveBitmap(final String uri){
        mImageLoader.loadImage(uri, null);
    }

    public interface OnImageEmpty{
        Drawable setImageEmpty();
    }

    /**
     * 使用ImageLoader, 必须使用此方法获取文件, 否则获取不到
     * 按照ImageLoader路径开头方法获取文件
     * @param dir
     * @param filename
     * @return 返回字符串
     */
    public String useImageLoaderTypeObtainExternalFiles(String dir, String filename){
        File path = new File(context.getExternalFilesDir(dir) + "/" + filename);
        String headImageLoader = "file:/";
        return headImageLoader + path.toString();
    }
}
