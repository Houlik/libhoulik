package com.houlik.libhoulik.houlik.pixel;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.widget.ImageView;

import com.houlik.libhoulik.android.util.ImageUtils;
import com.houlik.libhoulik.houlik.pixel.PixelUtilsLayer.Shape;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * 图片转像素点
 * Created by Houlik on 2018-04-02.
 */

public class PixelUtils implements OnPixelUtilsListener{

    private final String TAG = "PIXEL UTILS : ";

    private int density = 12;
    private OnPixelUtilsListener onPixelUtilsListener;
    private PixelUtilsThread pixelUtilsThread;

    //坐标 X,Y 宽高 Width, Height
    private int x, y, width, height = -1;
    //最终位图
    private Bitmap destBitmap;
    //目标图像
    private ImageView targetImageView;
    private boolean loadIntoImageView = true;

    //形状
    private PixelUtilsLayer.Shape shape;
    //分辨率
    private int resolution;
    //方块大小
    private int size;

    //位图
    public PixelUtils(Bitmap bitmap) {
        pixelUtilsThread = new PixelUtilsThread();
        setBitmap(bitmap);
    }

    //图像
    public PixelUtils(ImageView imageView) {
        pixelUtilsThread = new PixelUtilsThread();
        this.targetImageView = imageView;
        setBitmap(ImageUtils.getImageUtils().transferImageView2Bitmap(imageView));

    }

    public PixelUtils setShouldHandleImageView(boolean loadIntoImageView) {
        this.loadIntoImageView = loadIntoImageView;
        return this;
    }

    public PixelUtils setDensity(int density) {
        this.density = density;
        return this;
    }

    public PixelUtils setShape(Shape shape){
        this.shape = shape;
        return this;
    }

    public PixelUtils setResolution(int resolution){
        this.resolution = resolution;
        return this;
    }

    public PixelUtils setSize(int size){
        this.size = size;
        return this;
    }

    public PixelUtils setListener(OnPixelUtilsListener listener) {
        this.onPixelUtilsListener = listener;
        return this;
    }

    public PixelUtils setArea(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        return this;
    }

    public PixelUtils setBitmap(Bitmap bitmap) {
        this.destBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        return this;
    }

    public void make() {
        if(pixelUtilsThread == null || pixelUtilsThread.isRendering()) {
            // Create a new thread when the old one is busy
            pixelUtilsThread = new PixelUtilsThread();
        }
        pixelUtilsThread.setBitmap(this.destBitmap);
        pixelUtilsThread.pixelate(density);
        pixelUtilsThread.getShape(shape);
        pixelUtilsThread.getResolution(resolution);
        pixelUtilsThread.getSize(size);
        if(x != -1 && y != -1) {
            pixelUtilsThread.setArea(x, y, width, height);
        }
        // Let this class handle the callback and not the thread
        pixelUtilsThread.setPixelateListener(this);
        pixelUtilsThread.start();
    }

    public void onPixel(ImageView imageView, Bitmap bitmap, int density, Shape shape, int resolution, int size) {

        if(loadIntoImageView && targetImageView != null){
            targetImageView.setImageBitmap(bitmap);
        }
        if(onPixelUtilsListener != null) {
            onPixelUtilsListener.onPixel(targetImageView, bitmap, density, shape,resolution,size);
        }
    }

    public ImageView getImageView(ImageView imageView){
        return imageView;
    }

    private final static float SQRT2 = (float) Math.sqrt(2);

    public static Bitmap fromAsset(@NonNull AssetManager assetManager, @NonNull String path, @NonNull PixelUtilsLayer... layers) throws IOException {
        return fromInputStream(assetManager.open(path), layers);
    }

    public static Bitmap fromInputStream(@NonNull InputStream inputStream, @NonNull PixelUtilsLayer... layers) throws IOException {
        Bitmap in = BitmapFactory.decodeStream(inputStream);
        inputStream.close();
        Bitmap out = fromBitmap(in, layers);
        in.recycle();
        return out;
    }

    public static Bitmap fromBitmap(@NonNull Bitmap in, @NonNull PixelUtilsLayer... layers) {
        Bitmap out = Bitmap.createBitmap(in.getWidth(), in.getHeight(), Bitmap.Config.ARGB_8888);
        render(in, out, layers);
        return out;
    }

    public static void render(@NonNull Bitmap in, @NonNull Bitmap out, @NonNull PixelUtilsLayer... layers) {
        render(in, null, out, layers);
    }

    public static void render(@NonNull Bitmap in, @Nullable Rect inBounds, @NonNull Bitmap out, @NonNull PixelUtilsLayer... layers) {
        render(in, inBounds, out, null, layers);
    }

    public static void render(@NonNull Bitmap in, @Nullable Rect inBounds, @NonNull Bitmap out, @Nullable Rect outBounds, @NonNull PixelUtilsLayer... layers) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.FILTER_BITMAP_FLAG);
        if (outBounds == null) {
            outBounds = new Rect(0, 0, out.getWidth(), out.getHeight());
        }
        render(in, inBounds, new Canvas(out), outBounds, paint, layers);
    }

    public static void render(@NonNull Bitmap in, @Nullable Rect inBounds, @NonNull Canvas canvas, @NonNull Rect outBounds, @NonNull Paint paint, @NonNull PixelUtilsLayer... layers) {
        int inWidth = inBounds == null ? in.getWidth() : inBounds.width();
        int inHeight = inBounds == null ? in.getHeight() : inBounds.height();
        int inX = inBounds == null ? 0 : inBounds.left;
        int inY = inBounds == null ? 0 : inBounds.top;
        float scaleX = ((float) outBounds.width()) / inWidth;
        float scaleY = ((float) outBounds.height()) / inHeight;

        canvas.save();
        canvas.clipRect(outBounds);
        canvas.translate(outBounds.left, outBounds.top);
        canvas.scale(scaleX, scaleY);

        for (PixelUtilsLayer layer : layers) {
            // option defaults
            float size = layer.size == null ? layer.resolution : layer.size;
            int cols = (int) (inWidth / layer.resolution + 1);
            int rows = (int) (inHeight / layer.resolution + 1);
            float halfSize = size / 2f;
            float diamondSize = size / SQRT2;
            float halfDiamondSize = diamondSize / 2f;

            for (int row = 0; row <= rows; row++ ) {
                float y = (row - 0.5f) * layer.resolution + layer.offsetY;
                // normalize y so shapes around edges get color
                float pixelY = inY + Math.max(Math.min(y, inHeight - 1), 0);

                for (int col = 0; col <= cols; col++ ) {
                    float x = (col - 0.5f) * layer.resolution + layer.offsetX;
                    // normalize y so shapes around edges get color
                    float pixelX = inX + Math.max(Math.min(x, inWidth - 1), 0);

                    paint.setColor(getPixelColor(in, (int) pixelX, (int) pixelY, layer));

                    switch (layer.shape) {
                        case Circle:
                            canvas.drawCircle(x, y, halfSize, paint);
                            break;
                        case Diamond:
                            canvas.save();
                            canvas.translate(x, y);
                            canvas.rotate(45);
                            canvas.drawRect(-halfDiamondSize, -halfDiamondSize, halfDiamondSize, halfDiamondSize, paint);
                            canvas.restore();
                            break;
                        case Square:
                            canvas.drawRect(x - halfSize, y - halfSize, x + halfSize, y + halfSize, paint);
                            break;
                    } // switch
                } // col
            } // row
        }
        canvas.restore();
    }

    /**
     * Returns the color of the cluster. If options.enableDominantColor is true, return the
     * dominant color around the provided point. Return the color of the point itself otherwise.
     * The dominant color algorithm is based on simple counting search, so use with caution.
     *
     * @param pixels the bitmap
     * @param pixelX the x coordinate of the reference point
     * @param pixelY the y coordinate of the reference point
     * @param opts additional options
     * @return the color of the cluster
     */
    private static int getPixelColor(@NonNull Bitmap pixels, int pixelX, int pixelY, @NonNull PixelUtilsLayer opts) {
        int pixel = pixels.getPixel(pixelX, pixelY);
        if (opts.enableDominantColor) {
            Map<Integer, Integer> colorCounter = new HashMap<>(100);
            for (int x = (int) Math.max(0, pixelX - opts.resolution); x < Math.min(pixels.getWidth(), pixelX + opts.resolution); x++) {
                for (int y = (int) Math.max(0, pixelY - opts.resolution); y < Math.min(pixels.getHeight(), pixelY + opts.resolution); y++) {
                    int currentRGB = pixels.getPixel(x, y);
                    int count = colorCounter.containsKey(currentRGB) ? colorCounter.get(currentRGB) : 0;
                    colorCounter.put(currentRGB, count + 1);
                }
            }
            Integer max = null;
            Integer dominantRGB = null;
            for (Map.Entry<Integer, Integer> entry : colorCounter.entrySet()) {
                if (max == null || entry.getValue() > max) {
                    max = entry.getValue();
                    dominantRGB = entry.getKey();
                }
            }

            pixel = dominantRGB;
        }
        int red = Color.red(pixel);
        int green = Color.green(pixel);
        int blue = Color.blue(pixel);
        int alpha = (int) (opts.alpha * Color.alpha(pixel));
        return Color.argb(alpha, red, green, blue);
    }
}
