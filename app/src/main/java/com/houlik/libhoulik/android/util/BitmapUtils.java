package com.houlik.libhoulik.android.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.houlik.libhoulik.houlik.pixel.OnPixelUtilsListener;
import com.houlik.libhoulik.houlik.pixel.PixelUtils;
import com.houlik.libhoulik.houlik.pixel.PixelUtilsLayer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 位图工具
 * <p>
 * Created by Houlik on 2018-04-01.
 */

public class BitmapUtils {

    private String TAG = "BitmapUtils";

    /**
     * GC freed
     * Reason Concurrent----后台回收内存，不暂停用户线程
     * Alloc----当app要申请内存，而堆又快满了的时候，会阻塞用户线程
     * Explicit----调用Systemt.gc()等方法的时候触发，一般不建议使用
     * NativeAlloc----当native内存有压力的时候触发
     * Name Concurrent mark sweep----全部对象的检测回收
     * Concurrent partial mark sweep----部分的检测回收
     * Concurrent sticky mark sweep----仅检测上次回收后创 建的对象，速度快，卡顿少，比较频繁
     */

    private static BitmapUtils bitmapUtils = new BitmapUtils();

    private BitmapUtils() {
    }

    public static BitmapUtils getInstance() {
        if (bitmapUtils == null) {
            synchronized (BitmapUtils.class){
                if (bitmapUtils == null) {
                    new BitmapUtils();
                }
            }
        }
        return bitmapUtils;
    }

    /**
     * BitmapFactory.Options 如果出现内存不够或图片过大的问题可以使用Options来设置解决
     * BitmapFactory.decodeFile 使用Java CreateBitmap 完成 == 耗内存 == 慎用
     * BitmapFactory.decodeStream 直接调用JNI nativeDecodeAsset() 执行Decode
     * BitmapFactory.Options o = new BitmapFactory.Options();
     */


    /**
     * 用于Activity 或 Fragment 之间传递
     * 将 Bitmap 转换为字节
     * @param bitmap
     * @param format
     * @param quality
     * @return
     */
    public byte[] transferBitmap2Byte(Bitmap bitmap, Bitmap.CompressFormat format, int quality) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(format, quality, byteArrayOutputStream);
        byte[] toByte = byteArrayOutputStream.toByteArray();
        try {
            byteArrayOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return toByte;
    }

    /**
     * Bitmap位图转Pixel像素
     * @param imageView
     * @param density
     * @param shape
     * @param resolution
     * @param size
     */
    public void bitmap2Pixel(ImageView imageView, int density, PixelUtilsLayer.Shape shape, int resolution, int size) {
        PixelUtils p = new PixelUtils(imageView);
        p.setArea(0, 0, imageView.getWidth(), imageView.getHeight()).setDensity(density).setShape(shape).setResolution(resolution).setSize(size).make();
        p.setListener(new OnPixelUtilsListener() {
            @Override
            public void onPixel(ImageView imageView, Bitmap bitmap, final int density, PixelUtilsLayer.Shape shape, int resolution, int size) {
                Bitmap tmp = PixelUtils.fromBitmap(bitmap, new PixelUtilsLayer.Builder(shape).setResolution(resolution).setSize(size).build());
                imageView.setImageBitmap(tmp);
            }
        });
    }

    /**
     * 加载本地流图片
     * 注意事项: BitmapFactory.decode有从不同类型取得路径
     * 如果使用文件路径却使用decodeStream是得不到相关文件及信息,
     * 文件路径必须使用decodeFile
     * @param path
     * @return
     */
    public Bitmap getSDBitmapDecodeStream(String path) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(path);
            fileInputStream.read();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } 
        return BitmapFactory.decodeStream(fileInputStream);
    }

    /**
     * 从服务器获取图片
     * @param url
     * @return
     */
    public Bitmap getHttpBitmap(String url) {
        URL fileURL = null;
        Bitmap bitmap = null;
        HttpURLConnection httpURLConnection = null;
        try {
            fileURL = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            httpURLConnection = (HttpURLConnection) fileURL.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();

            bitmap = BitmapFactory.decodeStream(inputStream);
            BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 使用 matrix 改变图片宽高
     * @param bitmap
     * @param newWidth
     * @param newHeight
     * @return
     */
    public Bitmap changeBitmapSize(Bitmap bitmap, int newWidth, int newHeight) {
        if(bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            // 计算缩放比例
            float scaleWidth = ((float) newWidth) / width;
            float scaleHeight = ((float) newHeight) / height;
            // 取得想要缩放的matrix参数
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
            /**
             if (bitmap != null && !bitmap.equals(bmp) && !bitmap.isRecycled()) {
             bitmap.recycle();
             }**/
            //新的图片
            return bmp;
        }else{
            return null;
        }
    }

    /**
     * 将drawable转成位图bitmap
     * @param drawable
     * @param width
     * @param height
     * @return
     */
    public Bitmap drawableToBitmap(Drawable drawable, int width, int height) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 根据指定的宽度平铺图像
     * @param width
     * @param src
     * @return
     */
    public Bitmap createRepeater(int width, Bitmap src) {
        int count = (width + src.getWidth() - 1) / src.getWidth();
        Bitmap bitmap = Bitmap.createBitmap(width, src.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        for (int idx = 0; idx < count; ++idx) {
            canvas.drawBitmap(src, idx * src.getWidth(), 0, null);
        }
        return bitmap;
    }

    /**
     * 位图色彩
     * @param bitmap
     * @param hue
     * @param colorMidValue 颜色取中间值 127
     * @param config        Bitmap.Config_ARGB8888
     * @return
     */
    public Bitmap getBitmapHue(Bitmap bitmap, int hue, int colorMidValue, Bitmap.Config config) {
        float tmpHue = (hue - colorMidValue) * 1.0f / colorMidValue * 180;
        Bitmap tmpBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), config);
        Canvas canvas = new Canvas(tmpBitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setRotate(0, tmpHue);
        colorMatrix.setRotate(1, tmpHue);
        colorMatrix.setRotate(2, tmpHue);
        //融合
        ColorMatrix matrix = new ColorMatrix();
        matrix.postConcat(colorMatrix);
        paint.setColorFilter(new ColorMatrixColorFilter(matrix));
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return tmpBitmap;
    }

    /**
     * 饱和度
     * @param bitmap
     * @param saturation
     * @param colorMidValue
     * @param config        Bitmap.Config_ARGB8888
     * @return
     */
    public Bitmap getBitmapSaturation(Bitmap bitmap, int saturation, int colorMidValue, Bitmap.Config config) {
        float tmpSaturation = saturation * 1.0f / colorMidValue;
        Bitmap tmpBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), config);
        Canvas canvas = new Canvas(tmpBitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(tmpSaturation);
        //融合
        ColorMatrix matrix = new ColorMatrix();
        matrix.postConcat(colorMatrix);
        paint.setColorFilter(new ColorMatrixColorFilter(matrix));
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return tmpBitmap;
    }

    /**
     * 亮度
     * @param bitmap
     * @param brightness
     * @param colorMidValue
     * @param config        Bitmap.Config_ARGB8888
     * @return
     */
    public Bitmap getBitmapBrightness(Bitmap bitmap, int brightness, int colorMidValue, Bitmap.Config config) {
        float tmpBrightness = brightness * 1.0f / colorMidValue;
        Bitmap tmpBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), config);
        Canvas canvas = new Canvas(tmpBitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setScale(tmpBrightness, tmpBrightness, tmpBrightness, 1);
        //融合
        ColorMatrix matrix = new ColorMatrix();
        matrix.postConcat(colorMatrix);
        paint.setColorFilter(new ColorMatrixColorFilter(matrix));
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return tmpBitmap;
    }

    /**
     * 融合色彩，饱和度，亮度 为一体
     * @param bitmap
     * @param hue 色彩
     * @param saturation 饱和度
     * @param brightness 亮度
     * @param colorMidValue
     * @param config Bitmap.Config_ARGB8888
     * @return
     */
    public Bitmap getBitmapHSB(Bitmap bitmap, int hue, int saturation, int brightness, int colorMidValue, Bitmap.Config config) {
        float tmpHue = (hue - colorMidValue) * 1.0f / colorMidValue * 180;
        float tmpSaturation = saturation * 1.0f / colorMidValue;
        float tmpBrightness = brightness * 1.0f / colorMidValue;
        Bitmap tmpBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), config);
        Canvas canvas = new Canvas(tmpBitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        ColorMatrix matrixHue = new ColorMatrix();
        matrixHue.setRotate(0, tmpHue);
        matrixHue.setRotate(1, tmpHue);
        matrixHue.setRotate(2, tmpHue);

        ColorMatrix matrixSaturation = new ColorMatrix();
        matrixSaturation.setSaturation(tmpSaturation);

        ColorMatrix matrixBrightness = new ColorMatrix();
        matrixBrightness.setScale(tmpBrightness, tmpBrightness, tmpBrightness, 1);

        //融合
        ColorMatrix matrix = new ColorMatrix();
        matrix.postConcat(matrixHue);
        matrix.postConcat(matrixSaturation);
        matrix.postConcat(matrixBrightness);
        paint.setColorFilter(new ColorMatrixColorFilter(matrix));
        canvas.drawBitmap(bitmap, 0, 0, paint);

        return tmpBitmap;
    }

    /******************************
     *
     * 将宽高减半后的位图固定质量后将Bitmap保存到SD卡
     * 将MB缩小到KB, 位图像素保持清晰
     * @param file
     * @param bitmap
     * @param quality 最佳质量是80
     */
    public void saveBitmap2SDCard(File file, Bitmap bitmap, int quality){
        //如果该路径不存在文件就自动创建
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //保存
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            //将位图压缩后保存 //大PNG 中JPEG 小WEBP
            bitmap.compress(Bitmap.CompressFormat.WEBP, quality, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 质量压缩
     * @param bitmap
     * @return
     */
    private Bitmap compressQualityImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //JPEG
        int options = 100;
        //循环判断如果压缩后图片是否大于100kb,大于继续压缩
        while (baos.toByteArray().length / 1024 > 100) {
            //重置baos即清空baos
            baos.reset();
            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中 //JPEG
            options -= 10;//每次都减少10
        }
        //把压缩后的数据baos存放到ByteArrayInputStream中
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        //把ByteArrayInputStream数据生成图片
        Bitmap newBitmap = BitmapFactory.decodeStream(isBm, null, null);

        try {
            isBm.close();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newBitmap;
    }

    /**
     * 图片按比例大小压缩方法(根据Bitmap图片压缩)
     * @param bitmap
     * @return
     */
    public Bitmap getSDBitmapDecodeStream(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//JPEG
        if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();// 重置baos即清空baos
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中 //JPEG
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        options.inJustDecodeBounds = true;
        int oldWidth = options.outWidth;
        int oldHeight = options.outHeight;
        options.inSampleSize = calculateOptionsInSampleSize(options, oldWidth / 2, oldHeight / 2);
        options.inJustDecodeBounds = false;
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        Bitmap newBitmap = BitmapFactory.decodeStream(isBm, null, options);
        if (isBm != null) {
            try {
                isBm.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return compressQualityImage(newBitmap);
    }

    /**
     * 将位图宽高减半
     * @param filePath
     * @return
     */
    public Bitmap getSDBitmapDecodeFile(String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        //获取原宽高
        int oldWidth = options.outWidth;
        int oldHeight = options.outHeight;
        //采样率压缩
        options.inSampleSize = calculateOptionsInSampleSize(options, oldWidth / 2, oldHeight / 2);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 根据图片分辨率, 按照要求宽高, 计算压缩率 options.inSampleSize
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private int calculateOptionsInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight){
        int width = options.outWidth;
        int height = options.outHeight;
        // 现在主流手机比较多是800*480分辨率
        float normalHeight = reqHeight ;// 例子: 800f
        float normalWidth = reqWidth; // 例子: 480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (width > height && width > normalWidth) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (options.outWidth / normalWidth);
        } else if (width < height && height > normalHeight) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (options.outHeight / normalHeight);
        }
        if (be <= 0) {
            be = 1;
        }
        return be;
    }

    /**
     * 按照路径压缩图片返回位图
     * 采样率压缩
     * @param filePath 文件绝对路径
     * @param inSampleSize 1 = 不变 2 = 压缩
     * @return
     */
    public Bitmap getSDBitmapDecodeFile(String filePath, int inSampleSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        // Determine how much to scale down the image
        // int scaleFactor = Math.min(实际宽度/目标宽度, 实际高度/目标高度);
        // Decode the image file into a Bitmap sized to fill the View
        // options.inSampleSize = scaleFactor;
        // options.inPurgeable = true;
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        //避免出现内存溢出的情况，进行相应的属性设置。
        //options.inPreferredConfig = Bitmap.Config.RGB_565;
        //options.inMutable = true;
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * ImageView转Bitmap同时压缩后保存到SD卡
     * @param imageView
     * @param folder 文件夹
     * @param fileName 文件名称
     * @throws Exception
     */
    public void setBitmapCompress2SDCard(ImageView imageView, String folder, String fileName) throws Exception{
        //文件路径
        File file = new File(FileUtils.getInstance().getDirPath() + "/" + folder + fileName);
        //ImageView转Bitmap
        Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        //压缩
        bitmap = BitmapUtils.getInstance().getSDBitmapDecodeStream(bitmap);
        //保存
        FileOutputStream fos = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        fos.flush();
        fos.close();
    }

    /**
     * 当前ImageView转Bitmap后保存到SD卡
     * @param context
     * @param imageView
     * @param folder 文件夹中文件夹 "###/"
     * @param fileName 文件名
     * @param fileExtension ".文件格式"
     * @throws Exception
     */
    public void setBitmap2SDCard(Context context, ImageView imageView, String folder, String fileName, String fileExtension) throws Exception {
        //文件路径
        File file = new File(FileUtils.getInstance().getDirPath() + "/" + folder + fileName + fileExtension);
        Log.i(TAG,file.toString());
        if(!file.exists()){
            file.createNewFile();
        }else{
            Toast.makeText(context, "该文件名已存在，请输入新的文件名", Toast.LENGTH_LONG).show();
        }
        //ImageView转Bitmap
        Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        //保存
        FileOutputStream fos = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        fos.flush();
        fos.close();
    }

    /**
     * 按照路径得到图片同时压缩图片后保存到SD卡
     * 使用 流 读取文件必须执行读取 fis.read() 否则得到的是空值
     * @param uri
     */
    public void setCompressBitmap2SDCard(String uri) throws Exception {
        FileInputStream fis = new FileInputStream(uri);
        fis.read();
        Bitmap tmp_Bitmap = BitmapFactory.decodeStream(fis);
        tmp_Bitmap = BitmapUtils.getInstance().getSDBitmapDecodeStream(tmp_Bitmap);
        fis.close();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(uri);
            tmp_Bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载资源图
     * @param activity
     * @param fileName
     * @param transparency
     * @return
     */
    public Bitmap loadBitmap(Activity activity, String fileName, boolean transparency){
        InputStream inputStream = null;
        try {
            inputStream = activity.getAssets().open(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        if(transparency){
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        } else {
            options.inPreferredConfig = Bitmap.Config.RGB_565;
        }
        return BitmapFactory.decodeStream(inputStream, null, new BitmapFactory.Options());
    }


    public Bitmap pixelAction(Bitmap bitmap, Bitmap.Config config, int r, int g, int b){
        int tmpWidth = bitmap.getWidth();
        int tmpHeight = bitmap.getHeight();
        Bitmap tmpBitmap = Bitmap.createBitmap(tmpWidth, tmpHeight, config);
        int[] oldPixel = new int[tmpWidth * tmpHeight];
        bitmap.getPixels(oldPixel,0,tmpWidth, 0 ,0, tmpWidth, tmpHeight);
        int tmpColor;
        int tmpR,tmpG,tmpB,tmpA;
        int[] newPixel = new int[tmpWidth * tmpHeight];
        for (int i = 0; i < tmpWidth*tmpHeight; i++) {
            tmpColor = oldPixel[i];
            tmpR = Color.red(tmpColor);
            tmpG = Color.green(tmpColor);
            tmpB = Color.blue(tmpColor);
            tmpA = Color.alpha(tmpColor);

            tmpR = r - tmpR;
            tmpG = g - tmpG;
            tmpB = b - tmpB;

            if(tmpR > 255){
                tmpR = 255;
            }else if(tmpR < 0){
                tmpR = 0;
            }
            if(tmpG > 255){
                tmpG = 255;
            }else if(tmpG < 0){
                tmpG = 0;
            }
            if(tmpB > 255){
                tmpB = 255;
            }else if(tmpB < 0){
                tmpB = 0;
            }

            newPixel[i] = Color.argb(tmpA,tmpR, tmpG, tmpB);

        }
        tmpBitmap.setPixels(newPixel, 0, tmpWidth, 0, 0, tmpWidth, tmpHeight);

        return tmpBitmap;
    }

    /**
     * 移动图片到指定的文件夹下
     * @param context
     * @param newFolder
     * @param oldFolder
     * @param file
     */
    public void movePic(Context context, String newFolder, String oldFolder, String file){
        File fileTMP = new File(context.getExternalFilesDir(oldFolder) + "/" + file);
        fileTMP.renameTo(new File(context.getExternalFilesDir(newFolder) + "/" + file));
    }

    /**
     * 自定义裁剪图片
     * @param bitmap
     * @param firstX 第一个像素的x坐标
     * @param firstY 第一个像素的y坐标
     * @param width 每行中的像素数
     * @param height 行数
     * @return
     */
    public static Bitmap bitmapCrop(Bitmap bitmap, int firstX, int firstY, int width, int height) {
        return Bitmap.createBitmap(bitmap, firstX, firstY, width, height, null, false);
    }

    /**
     * 旋转90度
     * @param bitmap
     * @return
     */
    public static Bitmap rotatePic90(Bitmap bitmap, int degrees){
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.postRotate(degrees);
        Bitmap tmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
        return tmp;
    }


    /**
     * 加载大图或者一次性加载多张图片时,应该在异步线程中进行, 否则将引发卡顿
     * 类中参数为3种泛型类型
     * 整体作用：控制AsyncTask子类执行线程任务时各个阶段的返回类型
     * 具体说明：
     *     a. Params：开始异步任务执行时传入的参数类型，对应excute（）中传递的参数
     *     b. Progress：异步任务执行过程中，返回下载进度值的类型
     *     c. Result：异步任务执行完成后，返回的结果类型，与doInBackground()的返回值类型保持一致
     * 注：
     *     a. 使用时并不是所有类型都被使用
     *     b. 若无被使用，可用java.lang.Void类型代替
     *     c. 若有不同业务，需额外再写1个AsyncTask的子类
     *
     * 不能手动调用, 系统自动调用 onPreExecute()
     * 不能更改UI组件的信息
     * 执行过程中可调用publishProgress() 更新进度信息
     *
     * AsyncTask(传入参数,进度类型,返回结果类型)
     * 传入参数将显示在doInBackground参数里
     * 在doInBackground返回值的结果类型
     * */
    //public abstract class ProcessBitmapOnTask extends AsyncTask<Integer,Object, Bitmap> {}



    /**
     从获取方式分：
     （1）以文件流的方式
     假设在sdcard下有 test.jpg图片
     FileInputStream fis = new FileInputStream("/sdcard/test.jpg");
     Bitmap bitmap=BitmapFactory.decodeStream(fis);

     （2）以R文件的方式
     假设 res/drawable下有 test.jpg文件
     Bitmap bitmap =BitmapFactory.decodeResource(getResources(), R.drawable.test);
     或
     BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.test);
     Bitmap bitmap = bitmapDrawable.getBitmap();

     （3）以ResourceStream的方式，不用R文件
     Bitmap bitmap=BitmapFactory.decodeStream(getClass().getResourceAsStream(“/res/drawable/test.jpg”));

     （4）以文件流+R文件的方式
     InputStream in = getResources().openRawResource(R.drawable.test);
     Bitmap bitmap = BitmapFactory.decodeStream(in);
     或
     InputStream in = getResources().openRawResource(R.drawable.test);
     BitmapDrawable bitmapDrawable = new BitmapDrawable(in);
     Bitmap bitmap = bitmapDrawable.getBitmap();

     注意：openRawResource可以打开drawable, sound, 和raw资源，但不能是string和color。

     从资源存放路径分：
     （1）图片放在sdcard中
     Bitmap imageBitmap = BitmapFactory.decodeFile(path);// (path 是图片的路径，跟目录是/sdcard)

     （2）图片在项目的res文件夹下面
     ApplicationInfo appInfo = getApplicationInfo();
     //得到该图片的id(name 是该图片的名字，"drawable" 是该图片存放的目录，appInfo.packageName是应用程序的包)
     int resID = getResources().getIdentifier(fileName, "drawable", appInfo.packageName);
     Bitmap imageBitmap2 = BitmapFactory.decodeResource(getResources(), resID);

     （3）图片放在src目录下
     String path = "com/xiangmu/test.jpg"; //图片存放的路径
     InputStream in = getClassLoader().getResourceAsStream(path); //得到图片流
     Bitmap imageBitmap3 = BitmapFactory.decodeStream(in);

     （4）图片放在Assets目录
     InputStream in = getResources().getAssets().open(fileName);
     Bitmap imageBitmap4 = BitmapFactory.decodeStream(in);

     BitmapFactory.decode...() 为不可改变
     Bitmap.createBitmap(......) 为可改变
     **/


}
