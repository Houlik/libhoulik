package com.houlik.libhoulik.android.util;

import android.graphics.Canvas;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * getMovementFlags 设定滑动方向
 * START  右向左 END左向右 LEFT  向左 RIGHT向右  UP向上
 * 如果某个值传0，表示不触发该操作，次数设置支持上下拖拽，支持向右滑动
 * makeMovementFlags(0, ItemTouchHelper.DOWN | ItemTouchHelper.UP);
 *
 * onMove 长按拖动
 *
 * onSwiped 滑动操作
 *
 * onChildDraw 滑动过程显示的绘制 - 实现背景及图片
 * dX, dY 当前滑动宽高
 * actionState == ItemTouchHelper.ACTION_STATE_SWIPE 滑动状态
 * 例子:
 * if(dY < 0){
 *                         //当前RecyclerView的Item
 *                         View itemView = viewHolder.itemView;
 *
 *                         //绘制外框
 *                         Paint paint = new Paint(Color.BLACK);
 *                         paint.setStyle(Paint.Style.STROKE);
 *                         c.drawRect(itemView.getLeft() + 5, itemView.getTop() + 10, itemView.getRight() - 5, itemView.getBottom() - 10, paint);
 *
 *                         //拉下保存系列操作
 *                         Drawable d = ContextCompat.getDrawable(activity, android.R.drawable.ic_menu_save);
 *                         //d.setBounds(left, top, right, bottom);
 *                         d.setBounds(itemView.getLeft() + 30, itemView.getTop() + 40, itemView.getRight() - 30, itemView.getBottom() - 40);
 *                         d.draw(c);
 *
 *                         //跟着滑动
 *                         Drawable drawable = ContextCompat.getDrawable(activity, android.R.drawable.ic_menu_save);
 *                         drawable.setBounds(itemView.getRight() + (int) dX + 20, itemView.getTop() + 40, (int) (itemView.getRight() + dX + 150), itemView.getBottom() - 40);
 *                         drawable.draw(c);
 *                     }
 *
 * 让控件可以滑动或拖动执行其它操作
 * @author houlik
 * @since 2020/09/25
 */
public class ItemTouchHelperUtils {

    private ItemTouchHelper.Callback callback;
    private OnITHListener onITHListener;

    public ItemTouchHelperUtils(OnITHListener onITHListener, RecyclerView recyclerView){
        this.onITHListener = onITHListener;
        this.callback = processITHCallback();
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private ItemTouchHelper.Callback processITHCallback(){
        callback = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                //callback.makeMovementFlags(dragFlags, swipeFlags)
                return onITHListener.setMovementFlags(callback, recyclerView, viewHolder);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return onITHListener.dragMoveAction(recyclerView, viewHolder, target);
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                onITHListener.swipedAction(viewHolder, direction);
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                onITHListener.drawAction(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {

                onITHListener.clearView(recyclerView, viewHolder);

                super.clearView(recyclerView, viewHolder);
            }
        };

        return callback;
    }

    public interface OnITHListener{

        int setMovementFlags(ItemTouchHelper.Callback callback, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder);

        boolean dragMoveAction(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target);

        void swipedAction(@NonNull RecyclerView.ViewHolder viewHolder, int direction);

        void drawAction(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive);

        void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder);
    }

}
