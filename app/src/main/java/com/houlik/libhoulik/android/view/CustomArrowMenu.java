package com.houlik.libhoulik.android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.houlik.libhoulik.R;
import com.houlik.libhoulik.android.util.BitmapUtils;

import androidx.annotation.Nullable;

/**
 * 自定义的带箭头简单菜单控件
 * 搭配popupWindow使用
 * @author houlik
 * @since 2020/10/20
 */
public class CustomArrowMenu extends View {

    private int count = 1;
    private Drawable[] drawables;
    private String[] titles;
    private Bitmap[] bitmaps;

    private int icon_size;
    private int text_size;
    private int text_color;
    private int background_color;

    private Paint text_paint;
    private Paint icon_paint;
    private Paint background_paint;
    private Paint line_paint;

    private TypedArray typedArray;

    private int arrowSize = 20, paddingRight = 20, paddingLeft = 20, lineSize = 1, paddingTop = 10;
    private int totalWidth;
    private int totalHeight;
    private OnArrowMenuEvent onArrowMenuEvent;

    private boolean isRight = false;

    public CustomArrowMenu(Context context) {
        super(context, null);
    }

    public CustomArrowMenu(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);

        typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomArrowMenu);
        icon_size = (int) typedArray.getDimension(R.styleable.CustomArrowMenu_icon_size, icon_size);
        text_size = (int) typedArray.getDimension(R.styleable.CustomArrowMenu_text_size, text_size);
        text_color = typedArray.getColor(R.styleable.CustomArrowMenu_text_color, text_color);
        background_color = typedArray.getColor(R.styleable.CustomArrowMenu_background_color, background_color);

        icon_paint = new Paint();
        icon_paint.setAntiAlias(true);

        background_paint= new Paint();
        background_paint.setStyle(Paint.Style.FILL);
        background_paint.setColor(background_color);

        line_paint = new Paint();
        line_paint.setAntiAlias(true);
        line_paint.setStrokeWidth(1);
        line_paint.setColor(Color.BLACK);
        line_paint.setStyle(Paint.Style.STROKE);

        text_paint = new Paint();
        text_paint.setAntiAlias(true);
        text_paint.setColor(text_color);
        text_paint.setTextSize(text_size);
    }

    public CustomArrowMenu(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if(widthMode == View.MeasureSpec.AT_MOST | widthMode == View.MeasureSpec.EXACTLY | widthMode == View.MeasureSpec.UNSPECIFIED){
            width = totalWidth;
        }

        if(heightMode == View.MeasureSpec.AT_MOST | heightMode == View.MeasureSpec.EXACTLY | heightMode == MeasureSpec.UNSPECIFIED){
            height = totalHeight;
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(!isRight){
            toLeft(canvas);
            invalidate();
        }else{
            toRight(canvas);
            invalidate();
        }
    }

    private void toLeft(Canvas canvas){
        //背景颜色
        Path path = new Path();
        path.moveTo(20, 0);
        path.lineTo(totalWidth, 0);
        path.lineTo(totalWidth, totalHeight);
        path.lineTo(20,totalHeight);
        path.lineTo(20, (totalHeight / 3)*2);
        path.lineTo(0, totalHeight / 2);
        path.lineTo(20, totalHeight / 3);
        path.close();
        canvas.drawPath(path, background_paint);
        canvas.drawPath(path, line_paint);

        for (int i = 0; i < count; i++) {
            canvas.drawBitmap(BitmapUtils.getInstance().changeBitmapSize(bitmaps[i], icon_size, icon_size), ((20 + 1 + 20)*(i+1)) + (icon_size * i), 10, icon_paint);
        }

        for (int i = 0; i < (count - 1); i++) {
            canvas.drawLine(20+(20+icon_size+20)*(i+1), 10, 20+(20+icon_size+20)*(i+1), totalHeight - 10, line_paint);
        }

        for (int i = 0; i < count; i++) {
            Rect bounds = new Rect();
            text_paint.getTextBounds(titles[i], 0 , titles[i].length(), bounds);
            int front = (paddingLeft + icon_size + paddingRight);
            if(i < 1){
                canvas.drawText(titles[i], arrowSize + ((paddingLeft + icon_size + paddingRight + (lineSize * 2))/2) - (bounds.width()/2), totalHeight - 10, text_paint);
            }else{
                canvas.drawText(titles[i], arrowSize + ((paddingLeft + icon_size + paddingRight + (lineSize * 2))/2) - (bounds.width()/2) + (front * i), totalHeight - 10, text_paint);
            }
        }
    }

    private void toRight(Canvas canvas){
        //背景颜色
        Path path = new Path();
        path.moveTo(0, 0);
        path.lineTo(totalWidth - 20, 0);
        path.lineTo(totalWidth - 20, totalHeight / 3);
        path.lineTo(totalWidth, totalHeight / 2);
        path.lineTo(totalWidth - 20, (totalHeight / 3)*2);
        path.lineTo(totalWidth - 20, totalHeight);
        path.lineTo(0,totalHeight);
        path.close();
        canvas.drawPath(path, background_paint);
        canvas.drawPath(path, line_paint);

        for (int i = 0; i < count; i++) {
            if(i < 1) {
                canvas.drawBitmap(BitmapUtils.getInstance().changeBitmapSize(bitmaps[i], icon_size, icon_size), 1 + 20, 10, icon_paint);
            }else{
                canvas.drawBitmap(BitmapUtils.getInstance().changeBitmapSize(bitmaps[i], icon_size, icon_size), ((icon_size + paddingRight + paddingLeft) * i) + 20, 10, icon_paint);
            }
        }

        for (int i = 0; i < (count - 1); i++) {
            canvas.drawLine((20+icon_size+20)*(i+1), 10, (20+icon_size+20)*(i+1), totalHeight - 10, line_paint);
        }

        for (int i = 0; i < count; i++) {
            Rect bounds = new Rect();
            text_paint.getTextBounds(titles[i], 0 , titles[i].length(), bounds);
            canvas.drawText(titles[i], ((paddingLeft + icon_size + paddingRight + lineSize)/2) - (bounds.width()/2) + ((paddingLeft + icon_size + paddingRight + lineSize) * i), totalHeight - 10, text_paint);
        }
    }

    //用于判断箭头显示方向, 默认向左
    public void setIsRight(boolean isRight){
        this.isRight = isRight;
    }

    /**
     *
     * @param adapter 设置数量，图标，标题
     * @param onArrowMenuEvent 自定义实现点击触发交互事件
     * 例子:
     *     if (x > 0 & x < (20 + 20 + icon_size + 20)) {
     *          do something
     *     } else if (x > (20 + (20 + icon_size + 20)) & x < (20 + (20 + icon_size + 20) * 2)) {
     *          do something
     *     }
     */
    public void setAdapter(ArrowMenuAdapter adapter, OnArrowMenuEvent onArrowMenuEvent) {
        this.count = adapter.getCount();
        this.drawables = adapter.getDrawables();
        this.titles = adapter.getTitles();

        //初始化位图数量
        this.bitmaps = new Bitmap[this.count];
        //将drawables转成位图用于绘制
        for (int i = 0; i < this.count; i++) {
            bitmaps[i] = BitmapUtils.getInstance().drawableToBitmap(drawables[i], icon_size, icon_size);
        }

        //通过画笔将获取到的测量赋值给新建立的 Rect, 通过Rect可获取到当前测量的字体宽高
        Rect bounds = new Rect();
        text_paint.getTextBounds(titles[0], 0 , titles[0].length(), bounds);

        //获取总宽度用于测量使用
        totalWidth = arrowSize + ((paddingLeft + paddingRight + icon_size + lineSize) * count);
        //获取总高度用于测量使用
        totalHeight = icon_size + bounds.height() + (lineSize*2) + (paddingTop * 3);

        this.onArrowMenuEvent = onArrowMenuEvent;

        invalidate();
    }

    public interface ArrowMenuAdapter{
        //子view数量
        int getCount();
        //图标图片
        Drawable[] getDrawables();
        //图标标题
        String[] getTitles();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.onArrowMenuEvent.onArrowMenuEvent(event, icon_size);
    }

    public interface OnArrowMenuEvent{
        //自定义实现点击触发交互事件
        boolean onArrowMenuEvent(MotionEvent event, int icon_width);
    }

    public void setIcon_Size(int icon_size){
        this.icon_size = icon_size;
        invalidate();
    }
}
