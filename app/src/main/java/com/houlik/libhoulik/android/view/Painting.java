package com.houlik.libhoulik.android.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by houlik on 2018/5/11.
 */

public class Painting {

    private Canvas canvas;
    private Paint paint;
    private Bitmap bitmap;
    private Context context;
    private int oldEventX;
    private int oldEventY;
    private int newEventX;
    private int newEventY;

    public Painting(Context context){
        this.context = context;
    }

    public Painting(Canvas canvas, Paint paint, Bitmap bitmap){
        this.canvas = canvas;
        this.paint = paint;
        this.bitmap = bitmap;
    }

    public Painting(View view, MotionEvent event){
        bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        draw(view, event);
    }

    public void draw(View view, MotionEvent event){
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                oldEventX = (int) view.getX();
                oldEventY = (int) view.getY();

                break;
            case MotionEvent.ACTION_MOVE:
                newEventX = (int) view.getX();
                newEventY = (int) view.getY();

                canvas.drawLine(oldEventX, oldEventY, newEventX, newEventY, paint);

                oldEventX = newEventX;
                oldEventY = newEventY;


                break;

            case MotionEvent.ACTION_UP:


                break;
        }
    }

}
