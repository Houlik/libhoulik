package com.houlik.libhoulik.android.util;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Interpolator;

/**
 * 动画工具类
 * 也可以直接使用元件的animate(). 方法操作动画
 * Created by houlik on 2018/6/7.
 */
public class AnimatorUtils {

    //ofInt(X,X) - 加速器 - Evaluator - 监听器
    private ValueAnimator valueAnimator;
    //ofFloat( view, X, X, X, X) - 加速器 - Evaluator - 调用实体类 set
    private ObjectAnimator objectAnimator;

    public AnimatorUtils(){}

    public interface IIntValueAnimator {
        //初始化动画
        ValueAnimator ofInt();
        //速度 值越小速度越快
        long setDuration(ValueAnimator valueAnimator);
        //循环次数 0 为一次 -1 为重复 其它为次数
        int setRepeatCount(ValueAnimator valueAnimator);
        //循环模式 REVERSE 到达目的地后再返回原路回去
        int setRepeatMode(ValueAnimator valueAnimator);
        //更新
        ValueAnimator.AnimatorUpdateListener addUpdateListener();
        //插值器 BounceInterpolator //t 为 0.0057 一跳 175次 //弹跳效果
        //LinearInterpolator 线性插值器 / 斜直线 //均速
        //AccelerateInterpolator 加速插值器 / u 往上 //逐渐加速
        //DecelerateInterpolator 减速插值器 / n 往下 //逐渐减速
        //AccelerateDecelerateInterpolator 加速减速插值器 / u 往上 n 往下 //先加速后减速
        //AnticipateInterpolator 回荡秋千插值器 //移动前先向后助跑
        //AnticipateOvershootInterpolator //组合效果
        //CycleInterpolator 正弦周期变化插值器 //对于指定的动画,正向反向各做一遍
        //OvershootInterpolator //超过目标后再返回到起始点
        //自定义Interpolator
        Interpolator setInterpolator();
        //取消
        void cancelAnimator(ValueAnimator valueAnimator);
    }

    /**
     * 整数 通过监听来执行
     * @param iIntValueAnimator
     */
    public void startIntValueAnimator(IIntValueAnimator iIntValueAnimator){
        valueAnimator = iIntValueAnimator.ofInt();
        valueAnimator.setDuration(iIntValueAnimator.setDuration(valueAnimator));
        valueAnimator.setRepeatCount(iIntValueAnimator.setRepeatCount(valueAnimator));
        valueAnimator.setRepeatMode(iIntValueAnimator.setRepeatMode(valueAnimator));
        valueAnimator.addUpdateListener(iIntValueAnimator.addUpdateListener());
        valueAnimator.setInterpolator(iIntValueAnimator.setInterpolator());
        valueAnimator.start();
        iIntValueAnimator.cancelAnimator(valueAnimator);
    }

    public interface IFloatValueAnimator{
        //初始化动画
        ValueAnimator ofFloat();
        //时间
        long setDuration(ValueAnimator valueAnimator);
        //循环次数
        int setRepeatCount(ValueAnimator valueAnimator);
        //循环模式
        int setRepeatMode(ValueAnimator valueAnimator);
        //更新
        ValueAnimator.AnimatorUpdateListener addUpdateListener();
        //插值器 BounceInterpolator
        Interpolator setInterpolator();
        //取消动画
        void cancelAnimator(ValueAnimator valueAnimator);
    }

    /**
     * 浮点数 通过监听来执行
     * @param iFloatValueAnimator
     */
    public void startFloatValueAnimator(IFloatValueAnimator iFloatValueAnimator){
        valueAnimator = iFloatValueAnimator.ofFloat();
        valueAnimator.setDuration(iFloatValueAnimator.setDuration(valueAnimator));
        valueAnimator.setRepeatCount(iFloatValueAnimator.setRepeatCount(valueAnimator));
        valueAnimator.setRepeatMode(iFloatValueAnimator.setRepeatMode(valueAnimator));
        valueAnimator.addUpdateListener(iFloatValueAnimator.addUpdateListener());
        valueAnimator.setInterpolator(iFloatValueAnimator.setInterpolator());
        valueAnimator.start();
        iFloatValueAnimator.cancelAnimator(valueAnimator);
    }

    public interface IFloatObjectAnimator{
        // "rotationX" = 翻转 华为手机 无效
        // rotationX 围绕X旋转度数 rotationY 围绕Y旋转度数 rotationZ 围绕Z旋转度数
        // "translationX"
        // translationX 向右正方向移动距离 translationY 向下正方向移动距离
        // "scaleX"
        // scaleX X轴缩放倍数 scaleY Y轴缩放倍数
        // alpha 透明度
        ObjectAnimator ofFloat();
        long setDuration();
        int setRepeatCount();
        //自定义Interpolator
        Interpolator setInterpolator();
    }

    /**
     * ValueAnimator 子类
     * 这是直接操作对象类的设置
     * @param iFloatObjectAnimator
     */
    public void startFloatObjectAnimator(IFloatObjectAnimator iFloatObjectAnimator){
        objectAnimator = iFloatObjectAnimator.ofFloat();
        objectAnimator.setDuration(iFloatObjectAnimator.setDuration());
        objectAnimator.setRepeatCount(iFloatObjectAnimator.setRepeatCount());
        objectAnimator.setInterpolator(iFloatObjectAnimator.setInterpolator());
        objectAnimator.start();
    }

    public interface IAnimation{
        AnimationSet setAnimationSet();
        Animation[] setAnimation();
    }

    /**
     * 通过四种子类来执行动画
     * 可以相互叠加重复不同值使用
     * AlphaAnimation / RotateAnimation / ScaleAnimation / TranslateAnimation
     * 当中 pivotType Animation.ABSOLUTE, Animation.RELATIVE_TO_SELF 自身范围, Animation.RELATIVE_TO_PARENT 父类范围
     * @param iAnimation
     * @param view
     */
    public void startAnimation(IAnimation iAnimation, View view){
        AnimationSet animationSet = iAnimation.setAnimationSet();
        Animation[] animation = iAnimation.setAnimation();
        for (int i = 0; i < animation.length ; i++) {
            animationSet.addAnimation(animation[i]);
        }
        view.startAnimation(animationSet);
    }
}
