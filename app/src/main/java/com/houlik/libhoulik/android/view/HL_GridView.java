package com.houlik.libhoulik.android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 设置 gridview 在 scrollview 里显示全部内容
 * Created by Houlik on 16/05/2017.
 */

public class HL_GridView extends GridView{

    public HL_GridView(Context context) {
        super(context);
    }

    public HL_GridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HL_GridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * UNSPECIFIED 这说明 parent 没有对 child 强加任何限制，child 可以是它想要的任何尺寸
     * EXACTLY Parent 为 child 决定了一个绝对尺寸，child 将会被赋予这些边界限制，不管 child 自己想要多大
     * AT_MOST Child 可以是自己任意的大小，但是有个绝对尺寸的上限
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
