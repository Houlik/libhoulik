package com.houlik.libhoulik.android.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
//import android.support.annotation.NonNull;
//import android.support.annotation.StyleRes;
//import android.support.v7.app.AlertDialog;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AlertDialog;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;



import com.houlik.libhoulik.R;
import com.houlik.libhoulik.android.util.ColorUtils;
import com.houlik.libhoulik.android.listener.OnPickColorListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by houlik on 2018/5/10.
 */

public class AlertDialogCircleColor {

    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private String alertDialogTitle = "颜色选择";
    private String posTitle = "确定";
    private String negTitle = "取消";
    private View inflate;
    private ImageView picColor, showColor;
    private TextView textHex, textA, textR, textG, textB;
    private Bitmap bitmap;
    private OnAlertDialogCircleColorListener onAlertDialogCircleColorListener;
    private SeekBar alphaSB;
    private String hexRGB;
    private String hexAlpha;
    private List<Integer> listColor = new ArrayList<Integer>();
    private OnColorListListener onColorListListener;
    private Context context;
    private Class<?> cls;

    /**
     *
     * @param context 哪个Activity使用
     * @param themeResId
     * @param cls
     */
    public AlertDialogCircleColor(@NonNull final Context context, @StyleRes int themeResId, final Class<?> cls) {
        this.context = context;
        this.cls = cls;
        builder = new AlertDialog.Builder(context, themeResId);
        inflate = View.inflate(context, R.layout.alertdialog_pixel_activity_pickcolor, null);
        picColor = inflate.findViewById(R.id.alertdialog_activity_pickcolor_iv);
        picColor.setImageResource(R.drawable.color_picker_r);
        showColor = inflate.findViewById(R.id.alertdialog_activity_pickcolor_iv_show);
        textHex = inflate.findViewById(R.id.alertdialog_activity_pickcolor_tv_hex);
        textA = inflate.findViewById(R.id.alertdialog_activity_pickcolor_tv_a);
        textR = inflate.findViewById(R.id.alertdialog_activity_pickcolor_tv_r);
        textG = inflate.findViewById(R.id.alertdialog_activity_pickcolor_tv_g);
        textB = inflate.findViewById(R.id.alertdialog_activity_pickcolor_tv_b);
        alphaSB = inflate.findViewById(R.id.alertdialog_activity_pickcolor_sb_alpha);
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.color_picker_r);



        //通过颜色监听器得到颜色值赋值于所需变量
        ColorUtils.getInstance().setOnPickColorListener(new OnPickColorListener() {
            @Override
            public void getPickColorHexRGB(String hex) {
                textHex.setText("#" + hex);
                hexRGB = hex;

                System.out.println(hexRGB);
            }

            @Override
            public void getPickColorRGB(int r, int g, int b) {
                textR.setText(String.valueOf(r));
                textG.setText(String.valueOf(g));
                textB.setText(String.valueOf(b));
            }

            @Override
            public void getPickColorHexAlpha(String hex) {
                textHex.setText("#" + hex + hexRGB);
                hexAlpha = hex;
            }

            @Override
            public void getPickColorAlpha(int alpha) {
                textA.setText(String.valueOf(alpha));
            }
        });

        //颜色图取值监听器
        picColor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //设置seekBar最大值
                alphaSB.setProgress(255);
                //如果坐标Y大于0 Y小于位图高度
                if (event.getY() > 0 && event.getY() < bitmap.getHeight()) {
                    //如果坐标X大于0 X小于位图宽度
                    if (event.getX() > 0 && event.getX() < bitmap.getWidth()) {
                        //把坐标颜色值赋值与背景图
                        showColor.setBackgroundColor(Color.parseColor("#" + ColorUtils.getInstance().pickRGBColor(bitmap, event)));
                        //把坐标颜色值传给颜色监听器
                        onAlertDialogCircleColorListener.getPaintColor(Color.parseColor("#" + ColorUtils.getInstance().pickRGBColor(bitmap, event)));
                    }
                }
                return true;
            }
        });

        //alpha值 seekBar 监听器
        alphaSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //如果当前成员hexRGB16进制值不等于空
                if(hexRGB != null) {
                    //修改背景图颜色
                    showColor.setBackgroundColor(Color.parseColor("#" + (ColorUtils.getInstance().pickAlphaColor(progress) + hexRGB)));
                    //重新把坐标颜色值传给颜色监听器
                    onAlertDialogCircleColorListener.getPaintColor(Color.parseColor("#" + (ColorUtils.getInstance().pickAlphaColor(progress) + hexRGB)));
                    //刷新背景图
                    showColor.invalidate();
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        //初始化完毕后开始创建对话框显示
        builder.setView(inflate).setTitle(alertDialogTitle).setPositiveButton(posTitle, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //通知颜色监听器已经得到颜色值
                onAlertDialogCircleColorListener.isSelectColor(true);
                //如果alpha值不等于空
                if(hexAlpha != null && hexRGB != null) {
                    listColor.add(Color.parseColor("#" + hexAlpha + hexRGB));
                }else if(hexRGB != null){
                    listColor.add(Color.parseColor("#" + hexRGB));
                }
                if(listColor.size() > 10){
                    listColor.remove(0);
                }
                onColorListListener.getListColor(listColor);
            }
        }).setNegativeButton(negTitle, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //如果点击取消,颜色监听器将得到颜色值为0
                onAlertDialogCircleColorListener.getPaintColor(0);
            }
        }).setNeutralButton("查询", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intentCheck = new Intent(context.getApplicationContext(), cls);
                context.startActivity(intentCheck);
            }
        }).setCancelable(false).show();
    }

    //颜色监听器接口
    public interface OnAlertDialogCircleColorListener {
        void isSelectColor(boolean isSelect);
        void getPaintColor(int color);
    }

    public interface OnColorListListener{
        void getListColor(List list);
    }

    //初始化颜色监听器
    public void setOnAlertDialogCircleColorListener(OnAlertDialogCircleColorListener listener, OnColorListListener onColorListListener) {
        this.onAlertDialogCircleColorListener = listener;
        this.onColorListListener = onColorListListener;
    }



}
