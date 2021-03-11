package com.houlik.libhoulik.houlik.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * @author houlik
 * @since 2020/10/16
 */
public class HLView {

    //第一步: 创建 attrs 文件
    /**
     * 例子:
     * <?xml version="1.0" encoding="utf-8"?>
     * <resources>
     *     <declare-styleable name="样式名称">
     *         <attr name="color" format="color"></attr> //颜色
     *         <attr name="icon" format="reference"></attr> //图标
     *         <attr name="text" format="string"></attr> //文字
     *         <attr name="text_size" format="dimension"></attr> //文字尺寸
     *         <attr name="border_width" format="dimension" /> //宽度
     *
     *         <attr name="showText" format="boolean" />
     *  *        <attr name="labelPosition" format="enum">
     *  *            <enum name="left" value="0"/>
     *  *            <enum name="right" value="1"/>
     *  *        </attr>
     *
     *     </declare-styleable>
     *
     * </resources>
     */

    //第二步: 创建自定义绘制类
    /**
     * extends View
     * HLView(Context context) {super(context);}
     * HLView(Context context, @Nullable AttributeSet attrs) {super(context, attrs);
     * HLView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {super(context, attrs, defStyleAttr);}
     *
     * @Override
     *     protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {super.onMeasure(widthMeasureSpec, heightMeasureSpec);}
     *
     * @Override
     *     protected void onDraw(Canvas canvas) {super.onDraw(canvas);}
     */

    //第三步: 在XML布局中调用
    /**
     * <com.houlik.HLView
     *             android:layout_width="100dp"
     *             android:layout_height="100dp"
     *             app:color="@color/颜色"
     *             app:border_width="宽度dp"
     *             app:icon="@drawable/图标"
     *             app:text_size="字体大小sp"
     *             app:text="文字"/>
     */

    //第四步: 类中开始绘制
    /**
     * 在构造方法中初始化所定义的属性
     * 通过TypedArray 获取XML中定义的属性值
     * TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.样式名称);
     * 获取相关属性值
     * 例子:
     * Color view_color = ta.getColor(R.styleable.样式名称_color, 默认值);
     * int text_size = (int)ta.getDimension(R.styleable.样式名称_border_width, 默认值);
     *
     * 获取完相关属性值后, 回收 TypedArray
     * ta.recycle()
     *
     *
     * 初始化绘制中所需的画笔
     * Paint paint = new paint()
     * 消除锯齿
     * paint.setAntiAlias(true)
     * 设置颜色
     * paint.setColor(颜色)
     * 画笔宽度
     * paint.setStrokeWidth(尺寸)
     * 空心还是实心
     * paint.setStyle(Paint.Style.样式)
     * 开始结束的帽盖
     * paint.setStrokeCap(Paint.Cap.样式)
     * 字体大小
     * paint.setTextSize(字体大小)
     *
     */

    //第五步: 测量

    /**
     * @Override
     *     protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
     *         super.onMeasure(widthMeasureSpec, heightMeasureSpec);
     *
     *         //指定宽高 AT_MOST(wrap_content) | EXACTLY(match_parent) | 确定值无需计算
     *         int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
     *         int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
     *
     *         //获取宽度尺寸
     *         int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
     *         //获取高度尺寸
     *         int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);
     *
     *         //进行判断获取到的尺寸是固定 | 最大值match_parent | 自动wrap_content
     *         if(widthMode == View.MeasureSpec.AT_MOST){ //最多不超过某个值
     *             //计算宽度
     *             //画笔测量
     *             Rect bounds = new Rect();
     *             //文本, 从0开始, 文字长度, 画笔测量
     *             mTextPaint.getTextBounds(mText, 0 , mText.length(), bounds);
     *             widthSize = bounds.width() + getPaddingRight() + getPaddingLeft();
     *         }
     *
     *         if(heightMode == View.MeasureSpec.AT_MOST){ //最多不超过某个值 WrapContent
     *             //计算宽度
     *             //画笔测量
     *             Rect bounds = new Rect();
     *             //文本, 从0开始, 文字长度, 画笔测量
     *             mTextPaint.getTextBounds(mText, 0 , mText.length(), bounds);
     *             //result = Math.min(result, size);
     *             heightSize = bounds.height() + getPaddingTop() + getPaddingBottom();
     *         }
     *
     *         //这是最重要的一点, 必须将正确尺寸提交
     *         setMeasuredDimension(widthSize, heightSize);
     *
     *
     *
     *         //如果不是绘制文字, 如下设置即可。
     *         int width = View.MeasureSpec.getSize(widthMeasureSpec);
     *         int height = View.MeasureSpec.getSize(heightMeasureSpec);
     *
     *         setMeasuredDimension(width>height?height:width, width>height?height:width );
     *
     *         //按照所需情况设置
     *
     * }
     */

    //第六步: 开始绘制
    /**
     * @Override
     *     protected void onDraw(Canvas canvas) {
     *         super.onDraw(canvas);
     *
     *         //这是绘制文字
     *         //top 负值 bottom 正值
     *         //获取画笔的测量
     *         Paint.FontMetrics fm = mTextPaint.getFontMetrics();
     *         //获取文字高度
     *         int dy = (int) ((fm.bottom - fm.top) / 2 - fm.bottom);
     *         //获取文字基线
     *         int baseline = getHeight() / 2 + dy;
     *         //获取宽度的开始
     *         int x = getPaddingLeft();
     *         canvas.drawText(文字, x, baseline, 画笔);
     *
     *         //这是绘图案
     *         例子:
     *         绘制表盘
     *         int center = getWidth() / 2;
     *         int radius = getWidth() / 2 - border_width / 2;
     *         RectF rectF = new RectF(center - radius, center - radius, center + radius, center + radius);
     *
     *         //useCenter是否包括中间
     *         canvas.drawArc(rectF, 135, 270, false, mNormalSpeedPaint);
     *
     *         if(maxSpeed == 0)return;
     *
     *         float sweepAngle = (float)currrentSpeed/maxSpeed;
     *         //从外传入
     *         canvas.drawArc(rectF, 135, sweepAngle * 270, false, mOverSpeedPaint);
     *
     *         //绘制表盘中的文字 - 操作如上绘制文字相同
     *         //top 负值 bottom 正值
     *         Paint.FontMetrics fm = mTextPaint.getFontMetrics();
     *         int dy = (int) ((fm.bottom - fm.top) / 2 - fm.bottom);
     *         int baseline = getHeight() / 2 + dy;
     *
     *         Rect bounds = new Rect();
     *         //文本, 从0开始, 文字长度, 画笔测量
     *         String spd = currrentSpeed + "";
     *         mTextPaint.getTextBounds(spd, 0 , spd.length(), bounds);
     *
     *         //这一处需要改成文字显示在正中间 - 宽度减半 再减去 文字长度减半
     *         int x = (getWidth() / 2) - (bounds.width()/2);
     *
     *         canvas.drawText(String.valueOf(currrentSpeed), x, baseline, mTextPaint);
     *
     * }
     */

    //第七步: 继承 viewGroup 的 onLayout操作

    //第八步: 动态表盘运行
    /**
     * 在类中添加两个方法
     * 1。设置最大值
     * public synchronized void setMaxSpeed(int maxSpeed){
     *         this.maxSpeed = maxSpeed;
     *     }
     *
     * 2。设置当前值
     * public synchronized void setCurrentSpeedNum(int currentSpeedNum){
     *         this.currrentSpeed = currentSpeedNum;
     *         界面刷新
     *         invalidate();
     *     }
     */

    //第九步:
    /**
     * 在XML中为HLView设置ID用于Activity中调用
     */

    //第十步: 最终设置
    /**
     * 在activity中
     *
     * 使用Animator来实现动画效果
     * final HLView hlView = findViewById(R.id.hlview);
     *         hlView.setMaxSpeed(4000);
     *
     *         ValueAnimator valueAnimator = ObjectAnimator.ofFloat(0, 3000);
     *         valueAnimator.setDuration(1000);
     *         设置运行到最后减慢速度
     *         valueAnimator.setInterpolator(new DecelerateInterpolator());
     *         valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
     *             @Override
     *             public void onAnimationUpdate(ValueAnimator animation) {
     *                 float currentSpeed = (float) animation.getAnimatedValue();
     *                 hlView.setCurrentSpeedNum((int) currentSpeed);
     *             }
     *         });
     *         启动动画效果
     *         valueAnimator.start();
     */

    /**
     * Color 颜色
     * 不透明度-对应的值
     *     100% — FF  (不透明)
     *     99% — FC
     *     98% — FA
     *     97% — F7
     *     96% — F5
     *     95% — F2
     *     94% — F0
     *     93% — ED
     *     92% — EB
     *     91% — E8
     *     90% — E6
     *     -----------------------------------90%
     *     89% — E3
     *     88% — E0
     *     87% — DE
     *     86% — DB
     *     85% — D9
     *     84% — D6
     *     83% — D4
     *     82% — D1
     *     81% — CF
     *     80% — CC
     *     -----------------------------------80%
     *     79% — C9
     *     78% — C7
     *     77% — C4
     *     76% — C2
     *     75% — BF
     *     74% — BD
     *     73% — BA
     *     72% — B8
     *     71% — B5
     *     70% — B3
     *      -----------------------------------70%
     *     69% — B0
     *     68% — AD
     *     67% — AB
     *     66% — A8
     *     65% — A6
     *     64% — A3
     *     63% — A1
     *     62% — 9E
     *     61% — 9C
     *     60% — 99
     *     -----------------------------------60%
     *     59% — 96
     *     58% — 94
     *     57% — 91
     *     56% — 8F
     *     55% — 8C
     *     54% — 8A
     *     53% — 87
     *     52% — 85
     *     51% — 82
     *     50% — 80  (半透明)
     *     -----------------------------------50%
     *     49% — 7D
     *     48% — 7A
     *     47% — 78
     *     46% — 75
     *     45% — 73
     *     44% — 70
     *     43% — 6E
     *     42% — 6B
     *     41% — 69
     *     40% — 66
     *     -----------------------------------40%
     *     39% — 63
     *     38% — 61
     *     37% — 5E
     *     36% — 5C
     *     35% — 59
     *     34% — 57
     *     33% — 54
     *     32% — 52
     *     31% — 4F
     *     30% — 4D
     *     -----------------------------------30%
     *     29% — 4A
     *     28% — 47
     *     27% — 45
     *     26% — 42
     *     25% — 40
     *     24% — 3D
     *     23% — 3B
     *     22% — 38
     *     21% — 36
     *     20% — 33
     *     -----------------------------------20%
     *     19% — 30
     *     18% — 2E
     *     17% — 2B
     *     16% — 29
     *     15% — 26
     *     14% — 24
     *     13% — 21
     *     12% — 1F
     *     11% — 1C
     *     10% — 1A
     *     -----------------------------------10%
     *     9% — 17
     *     8% — 14
     *     7% — 12
     *     6% — 0F
     *     5% — 0D
     *     4% — 0A
     *     3% — 08
     *     2% — 05
     *     1% — 03
     *     0% — 00  (完全透明)
     */


}
