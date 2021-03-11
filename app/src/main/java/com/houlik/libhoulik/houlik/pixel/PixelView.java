package com.houlik.libhoulik.houlik.pixel;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
//import android.support.annotation.NonNull;
import androidx.annotation.NonNull;
import android.view.MotionEvent;

import com.houlik.libhoulik.android.listener.OnViewListener;

/**
 * 绘画像素图工具类
 * Created by houlik on 2018/5/5.
 */

public class PixelView{

    /**
     * 不允许直接修改, 必须在该后面添加 copy(Bitmap.Config.ARGB_8888, true);
     * java.lang.IllegalStateException: Immutable bitmap passed to Canvas constructor
     */


    private final String TAG = "Pixel View : ";

    //画布
    public Canvas canvas;
    //画笔
    public Paint paint;
    //位图
    private Bitmap bitmap;
    private BitmapFactory.Options options;
    //单元格尺寸，默认为20像素
    private int blockSize;
    //面板的宽高，表示有多少个单元格，默认为16×16
    private int blockWidthQty;
    private int blockHeightQty;
    //传递值回调
    private OnViewListener onViewListener;
    //是否由外传人的位图
    private boolean isBitmap;
    private int paintWidthStroke;
    private Shape shape;

    //空格形状
    public enum Shape {
        DEFAULT, RECT, CIRCLE, SQUARE, DIAMOND, OCTAGON, HEXAGON, PENTAGON, STAR6, STAR8
    }

    public enum Square {
        DEFAULT, UPRight, UPLeft, DOWNRight, DOWNLeft, SUPRight, SUPLeft, SDOWNRight, SDOWNLeft, UPCenter, RIGHTCenter, DOWNCenter, LEFTCenter
    }

    private Path path;
    public final int baseLineColor = Color.rgb(204, 204, 204);

    //默认位图
    protected PixelView(int blockSize, int blockWidthQty, int blockHeightQty, Shape shape) {
        this.blockSize = blockSize;
        this.blockWidthQty = blockWidthQty;
        this.blockHeightQty = blockHeightQty;
        this.shape = shape;
        isBitmap = false;
        initBitmap();
        setPaintWidthStroke(blockSize, blockWidthQty, blockHeightQty);
        initBasePaint();
        initCanvas();
        drawBackgroundLine(shape);
    }

    //自定义位图
    protected PixelView(Bitmap bmp, int blockSize) {
        this.blockSize = blockSize;
        this.blockWidthQty = bmp.getWidth() / blockSize;
        this.blockHeightQty = bmp.getHeight() / blockSize;
        this.bitmap = bmp;
        isBitmap = true;
        initBitmap();
        initCustomPaint();
        initCanvas();
    }

    /**
     * 初始化画布,设置背景为白色
     * canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR) 绘画前清空
     */
    private void initCanvas() {
        canvas = new Canvas(bitmap);
        if (!isBitmap) {
            setCanvasBackgroundColor(Color.WHITE);
        }
    }

    /**
     * 初始化画笔,设置默认画笔颜色为灰色,用于初始化绘画单元格线条
     */
    private void initBasePaint() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(paintWidthStroke);
        setPaintColor(baseLineColor);
        paint.setStyle(Paint.Style.STROKE);
    }

    /**
     * 自定义位图的画笔
     */
    private void initCustomPaint() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
    }

    private void setPaintWidthStroke(int blockSize, int blockWidthQty, int blockHeightQty){
        if(blockSize > 0 && blockSize <= 16){
            paintWidthStroke = 1;
        }else if(blockSize > 16 && blockSize <= 32){
            paintWidthStroke = 3;
        }else if(blockSize > 32 && blockSize <= 64){
            paintWidthStroke = 5;
        }else{
            paintWidthStroke = 7;
        }
    }

    /**
     * 得到传人画笔的颜色
     *
     * @param color
     */
    public void setPaintColor(int color) {
        paint.setColor(color);
    }

    /**
     * 设置画笔绘画形状
     * Paint.Style.STROKE 只描边不填充
     * Paint.Style.FILL 只填充不描边
     * Paint.Style.FILL_AND_STROKE 同时描边和填充
     *
     * @param style
     */
    public void setPaintStyle(Paint.Style style) {
        paint.setStyle(style);
    }

    /**
     * 设置画布颜色
     *
     * @param color
     */
    public void setCanvasBackgroundColor(int color) {
        canvas.drawColor(color);
    }

    /**
     * 初始化位图
     */
    private void initBitmap() {
        if (bitmap == null) {
            options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            bitmap = Bitmap.createBitmap(blockSize * blockWidthQty, blockSize * blockHeightQty, Bitmap.Config.ARGB_8888);
        }
    }

    /**
     * 全区域绘画
     *
     * @param shape
     */
    private void drawBackgroundLine(Shape shape){

        switch (shape) {
            //默认全区域矩形绘制
            case RECT:
                for (int i = 0; i <= blockWidthQty; i++) {
                    for (int j = 0; j <= blockHeightQty; j++) {
                        canvas.drawLine(0, j * blockSize, blockWidthQty * blockSize, j * blockSize, paint);
                        canvas.drawLine(i * blockSize, 0, i * blockSize, blockHeightQty * blockSize, paint);
                    }
                }

                break;
            //默认全区域圆形绘制
            case CIRCLE:
                for (int i = 0; i <= blockWidthQty; i++) {
                    for (int j = 0; j <= blockHeightQty; j++) {
                        if (i == 0 && j == 0) {
                            canvas.drawRect(0, 0, blockSize * blockWidthQty, blockSize * blockWidthQty, paint);
                        }
                        canvas.drawCircle(j * blockSize + (blockSize / 2), i * blockSize + (blockSize / 2), blockSize / 2, paint);
                    }
                }
                break;
        }
    }

    /**
     * 暂时不使用
     */
    private void drawBackgroundLine() {
        for (int i = 0; i <= blockWidthQty; i++) {
            for (int j = 0; j <= blockHeightQty; j++) {
                canvas.drawRect(j * blockSize, i * blockSize, j * blockSize + blockSize, i * blockSize + blockSize, paint);
            }
        }
    }

    /**
     * 公式计算点击X的具体坐标,以及缩放与移动后点击转换成具体的坐标
     *
     * @param posX
     * @param matrix
     * @return
     */
    private float calCoordinateX(float posX, Matrix matrix) {
        //矩阵存放数组
        final float[] values = new float[9];
        //获取矩阵信息
        matrix.getValues(values);
        //得到缩放的倍数
        float zoomMultiples = values[Matrix.MSCALE_X];
        //得到平移的距离
        float translateX = values[Matrix.MTRANS_X];
        // (先把当前各个坐标减去平移距离再除以缩放倍数) 后再减去 (当前坐标减去平移距离再除以缩放倍数) 取 方块尺寸余数
        float coordinateX = (((posX - translateX) / zoomMultiples) - ((posX - translateX) / zoomMultiples) % blockSize);
        return coordinateX;
    }

    /**
     * 公式计算点击Y的具体坐标,以及缩放与移动后点击转换成具体的坐标
     *
     * @param posY
     * @param matrix
     * @return
     */
    private float calCoordinateY(float posY, Matrix matrix) {
        //矩阵存放数组
        final float[] values = new float[9];
        //获取矩阵信息
        matrix.getValues(values);
        //得到缩放的倍数
        float zoomMultiples = values[Matrix.MSCALE_Y];
        //得到平移的距离
        float translateY = values[Matrix.MTRANS_Y];
        // (先把当前各个坐标减去平移距离再除以缩放倍数) 后再减去 (当前坐标减去平移距离再除以缩放倍数) 取 方块尺寸余数
        float coordinateY = (((posY - translateY) / zoomMultiples) - ((posY - translateY) / zoomMultiples) % blockSize);
        return coordinateY;
    }

    /**
     * 单个圆形绘制
     * 根据画笔的设置来绘制当前坐标圆形的填充或描边
     * 缩放或平移都能准确绘制
     *
     * @param event
     * @param posX
     * @param posY
     * @param canvas
     * @param paint
     * @param matrix
     * @param shape
     * @param square
     */
    public void drawSingleShape(MotionEvent event, float posX, float posY, @NonNull Canvas canvas, @NonNull Paint paint, @NonNull Matrix matrix, @NonNull Shape shape, Square square) {
        float pointX;
        float pointY;
        if (posX == 0 && posY == 0) {
            pointX = calCoordinateX(event.getX(), matrix);
            pointY = calCoordinateY(event.getY(), matrix);
        } else {
            pointX = calCoordinateX(posX, matrix);
            pointY = calCoordinateY(posY, matrix);
        }
        switch (shape) {
            //四方形-矩形
            case RECT:
                if (isBitmap) {
                    canvas.drawRect(new RectF(pointX, pointY, pointX + blockSize, pointY + blockSize), paint);
                } else {
                    if (paintWidthStroke >= 0 && paintWidthStroke <= 1) {
                        canvas.drawRect(new RectF(pointX + 1, pointY + 1,
                                pointX + blockSize - 1, pointY + blockSize - 1), paint);
                    } else {
                        canvas.drawRect(new RectF(pointX + (paintWidthStroke - 1), pointY + (paintWidthStroke - 1),
                                pointX + blockSize - paintWidthStroke + 1, pointY + blockSize - paintWidthStroke + 1), paint);
                    }

                }
                break;
            //圆形
            case CIRCLE:
                canvas.drawCircle(pointX + (blockSize / 2), pointY + (blockSize / 2), (blockSize / 2), paint);
                break;
            //三角形
            case SQUARE:
                //三角形方向
                switch (square) {
                    //上右
                    case UPRight:
                        squareUpRight(pointX, pointY);
                        break;
                    //上左
                    case UPLeft:
                        squareUpLeft(pointX, pointY);
                        break;
                    //下右
                    case DOWNRight:
                        squareDownRight(pointX, pointY);
                        break;
                    //下左
                    case DOWNLeft:
                        squareDownLeft(pointX, pointY);
                        break;
                    //小上右
                    case SUPRight:
                        sSquareUPRight(pointX, pointY);
                        break;
                    //小上左
                    case SUPLeft:
                        sSquareUPLeft(pointX, pointY);
                        break;
                    //小下右
                    case SDOWNRight:
                        sSquareDOWNRight(pointX, pointY);
                        break;
                    //小下左
                    case SDOWNLeft:
                        sSquareDOWNLeft(pointX, pointY);
                        break;
                    //上中
                    case UPCenter:
                        squareUPCenter(pointX, pointY);
                        break;
                    //右中
                    case RIGHTCenter:
                        squareRIGHTCenter(pointX, pointY);
                        break;
                    //下中
                    case DOWNCenter:
                        squareDOWNCenter(pointX, pointY);
                        break;
                    //左中
                    case LEFTCenter:
                        squareLEFTCenter(pointX, pointY);
                        break;
                    //默认
                    case DEFAULT:
                        break;
                }
                break;
            case DIAMOND:
                diamond(pointX, pointY);
                break;
            case OCTAGON:
                octagon(pointX, pointY);
                break;
            case STAR6:
                star6(pointX, pointY);
                break;
            case PENTAGON:
                pentagon(pointX, pointY);
                break;
            case HEXAGON:
                hexagon(pointX, pointY);
                break;

        }
    }

    /**
     * 三角形 - 下左
     *
     * @param pointX
     * @param pointY
     */
    private void squareDownLeft(float pointX, float pointY) {
        path = new Path();
        path.moveTo(pointX, pointY);
        path.lineTo(pointX + blockSize, pointY + blockSize);
        path.lineTo(pointX, pointY + blockSize);
        path.close();
        canvas.drawPath(path, paint);
    }

    /**
     * 三角形 - 下右
     *
     * @param pointX
     * @param pointY
     */
    private void squareDownRight(float pointX, float pointY) {
        path = new Path();
        path.moveTo(pointX, pointY + blockSize);
        path.lineTo(pointX + blockSize, pointY + blockSize);
        path.lineTo(pointX + blockSize, pointY);
        path.close();
        canvas.drawPath(path, paint);
    }

    /**
     * 三角形 - 上左
     *
     * @param pointX
     * @param pointY
     */
    private void squareUpLeft(float pointX, float pointY) {
        path = new Path();
        path.moveTo(pointX, pointY + blockSize);
        path.lineTo(pointX + blockSize, pointY);
        path.lineTo(pointX, pointY);
        path.close();
        canvas.drawPath(path, paint);
    }

    /**
     * 三角形 - 上右
     *
     * @param pointX
     * @param pointY
     */
    private void squareUpRight(float pointX, float pointY) {
        path = new Path();
        path.moveTo(pointX, pointY);
        path.lineTo(pointX + blockSize, pointY + blockSize);
        path.lineTo(pointX + blockSize, pointY);
        path.close();
        canvas.drawPath(path, paint);
    }

    /**
     * 钻石 - 倒立四方形
     *
     * @param pointX
     * @param pointY
     */
    private void diamond(float pointX, float pointY) {
        path = new Path();
        path.moveTo(pointX + (blockSize / 2), pointY);
        path.lineTo(pointX + blockSize, pointY + (blockSize / 2));
        path.lineTo(pointX + (blockSize / 2), pointY + blockSize);
        path.lineTo(pointX, pointY + (blockSize / 2));
        path.close();
        canvas.drawPath(path, paint);
    }


    /**
     * 小三角形-上右
     *
     * @param pointX
     * @param pointY
     */
    private void sSquareUPRight(float pointX, float pointY) {
        path = new Path();
        path.moveTo(pointX + (blockSize / 2), pointY);
        path.lineTo(pointX + blockSize, pointY);
        path.lineTo(pointX + blockSize, pointY + (blockSize / 2));
        path.close();
        canvas.drawPath(path, paint);
    }

    /**
     * 小三角形-下右
     *
     * @param pointX
     * @param pointY
     */
    private void sSquareDOWNRight(float pointX, float pointY) {
        path = new Path();
        path.moveTo(pointX + blockSize, pointY + (blockSize / 2));
        path.lineTo(pointX + blockSize, pointY + blockSize);
        path.lineTo(pointX + (blockSize / 2), pointY + blockSize);
        path.close();
        canvas.drawPath(path, paint);
    }

    /**
     * 小三角形-下左
     *
     * @param pointX
     * @param pointY
     */
    private void sSquareDOWNLeft(float pointX, float pointY) {
        path = new Path();
        path.moveTo(pointX + (blockSize / 2), pointY + blockSize);
        path.lineTo(pointX, pointY + blockSize);
        path.lineTo(pointX, pointY + (blockSize / 2));
        path.close();
        canvas.drawPath(path, paint);
    }

    /**
     * 小三角形-上左
     *
     * @param pointX
     * @param pointY
     */
    private void sSquareUPLeft(float pointX, float pointY) {
        path = new Path();
        path.moveTo(pointX, pointY + (blockSize / 2));
        path.lineTo(pointX, pointY);
        path.lineTo(pointX + (blockSize / 2), pointY);
        path.close();
        canvas.drawPath(path, paint);
    }

    /**
     * 三角形-上中
     *
     * @param pointX
     * @param pointY
     */
    private void squareUPCenter(float pointX, float pointY) {
        path = new Path();
        path.moveTo(pointX, pointY);
        path.lineTo(pointX + blockSize, pointY);
        path.lineTo(pointX + (blockSize / 2), pointY + (blockSize / 2));
        path.close();
        canvas.drawPath(path, paint);
    }

    /**
     * 三角形-右中
     *
     * @param pointX
     * @param pointY
     */
    private void squareRIGHTCenter(float pointX, float pointY) {
        path = new Path();
        path.moveTo(pointX + blockSize, pointY);
        path.lineTo(pointX + blockSize, pointY + blockSize);
        path.lineTo(pointX + (blockSize / 2), pointY + (blockSize / 2));
        path.close();
        canvas.drawPath(path, paint);
    }

    /**
     * 三角形-下中
     *
     * @param pointX
     * @param pointY
     */
    private void squareDOWNCenter(float pointX, float pointY) {
        path = new Path();
        path.moveTo(pointX + blockSize, pointY + blockSize);
        path.lineTo(pointX, pointY + blockSize);
        path.lineTo(pointX + (blockSize / 2), pointY + (blockSize / 2));
        path.close();
        canvas.drawPath(path, paint);
    }

    /**
     * 三角形-左中
     *
     * @param pointX
     * @param pointY
     */
    private void squareLEFTCenter(float pointX, float pointY) {
        path = new Path();
        path.moveTo(pointX, pointY + blockSize);
        path.lineTo(pointX, pointY);
        path.lineTo(pointX + (blockSize / 2), pointY + (blockSize / 2));
        path.close();
        canvas.drawPath(path, paint);
    }

    /**
     * 八角形
     *
     * @param pointX
     * @param pointY
     */
    private void octagon(float pointX, float pointY) {
        path = new Path();
        path.moveTo(pointX + (blockSize / 3), pointY);
        path.lineTo(pointX + (blockSize / 3) + (blockSize / 3), pointY);
        path.lineTo(pointX + blockSize, pointY + (blockSize / 3));
        path.lineTo(pointX + blockSize, pointY + (blockSize / 3) + (blockSize / 3));
        path.lineTo(pointX + (blockSize / 3) + (blockSize / 3), pointY + blockSize);
        path.lineTo(pointX + (blockSize / 3), pointY + blockSize);
        path.lineTo(pointX, pointY + (blockSize / 3) + (blockSize / 3));
        path.lineTo(pointX, pointY + (blockSize / 3));
        path.close();
        canvas.drawPath(path, paint);
    }

    /**
     * 六角星
     *
     * @param pointX
     * @param pointY
     */
    private void star6(float pointX, float pointY) {
        path = new Path();
        path.moveTo(pointX + (blockSize / 2), pointY);
        path.lineTo(pointX + (blockSize / 6) + (blockSize / 6) + (blockSize / 6) + (blockSize / 6), pointY + (blockSize / 4));
        path.lineTo(pointX + blockSize, pointY + (blockSize / 4));
        path.lineTo(pointX + (blockSize / 2) + ((blockSize / 6) * 2), pointY + (blockSize / 2));
        path.lineTo(pointX + blockSize, pointY + (blockSize / 2) + (blockSize / 4));
        path.lineTo(pointX + (blockSize / 2) + (blockSize / 6), pointY + (blockSize / 2) + (blockSize / 4));
        path.lineTo(pointX + (blockSize / 2), pointY + blockSize);
        path.lineTo(pointX + (blockSize / 6) + (blockSize / 6), pointY + (blockSize / 2) + (blockSize / 4));
        path.lineTo(pointX, pointY + (blockSize / 2) + (blockSize / 4));
        path.lineTo(pointX + (blockSize / 6), pointY + (blockSize / 2));
        path.lineTo(pointX, pointY + (blockSize / 4));
        path.lineTo(pointX + (blockSize / 6) + (blockSize / 6), pointY + (blockSize / 4));
        path.close();
        canvas.drawPath(path, paint);
    }

    /**
     * 六角星
     * 这是可以见到实线
     *
     * @param pointX
     * @param pointY
     */
    private void star6line(float pointX, float pointY) {
        path = new Path();
        path.moveTo(pointX + (blockSize / 2), pointY);
        path.lineTo(pointX + blockSize, pointY + (blockSize - (blockSize / 4)));
        path.lineTo(pointX, pointY + (blockSize - (blockSize / 4)));
        path.close();
        path.moveTo(pointX + (blockSize / 2), pointY + blockSize);
        path.lineTo(pointX, pointY + (blockSize / 4));
        path.lineTo(pointX + blockSize, pointY + (blockSize / 4));
        path.close();
        canvas.drawPath(path, paint);
    }

    /**
     * 八角星
     *
     * @param pointX
     * @param pointY
     */
    public void star8(float pointX, float pointY) {
        path = new Path();

        //do something

        path.close();
        canvas.drawPath(path, paint);
    }

    /**
     * 六角型
     *
     * @param pointX
     * @param pointY
     */
    public void hexagon(float pointX, float pointY) {
        path = new Path();
        path.moveTo(pointX + (blockSize / 2), pointY);
        path.lineTo(pointX + blockSize, pointY + (blockSize / 4));
        path.lineTo(pointX + blockSize, pointY + (blockSize / 2) + (blockSize / 4));
        path.lineTo(pointX + (blockSize / 2), pointY + blockSize);
        path.lineTo(pointX, pointY + (blockSize / 2) + (blockSize / 4));
        path.lineTo(pointX, pointY + (blockSize / 4));
        path.close();
        canvas.drawPath(path, paint);
    }

    /**
     * 五角形
     *
     * @param pointX
     * @param pointY
     */
    public void pentagon(float pointX, float pointY) {
        path = new Path();
        path.moveTo(pointX + (blockSize / 2), pointY);
        path.lineTo(pointX + blockSize, pointY + (blockSize / 2));
        path.lineTo(pointX + (blockSize / 2) + (blockSize / 4), pointY + blockSize);
        path.lineTo(pointX + (blockSize / 4), pointY + blockSize);
        path.lineTo(pointX, pointY + (blockSize / 2));
        path.close();
        canvas.drawPath(path, paint);
    }

    /**
     * 调用当前画布,画笔,位图
     *
     * @param onViewListener
     * @return
     */
    public OnViewListener setOnViewListener(OnViewListener onViewListener) {
        onViewListener.getViewElement(canvas, paint, bitmap);
        return onViewListener;
    }
}