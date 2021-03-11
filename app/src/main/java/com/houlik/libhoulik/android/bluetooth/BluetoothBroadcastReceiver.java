package com.houlik.libhoulik.android.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;

/**
 * 蓝牙广播
 * Created by houlik on 2018/10/3.
 */

public class BluetoothBroadcastReceiver extends BroadcastReceiver {

    private SearchCallBack searchCallBack;

    public BluetoothBroadcastReceiver(SearchCallBack searchCallBack){
        this.searchCallBack = searchCallBack;
    }

    /**
     * 例子:
     * switch (intent.getAction()){
     *             case BluetoothDevice.ACTION_FOUND:
     *                 //得到搜索到的蓝牙设备
     *                 bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
     *                 //通过BluetoothDevice执行需要的操作
     *                 searchCallBack.searchAction(bluetoothDevice);
     *                 break;
     *         }
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        searchCallBack.intentAction(context, intent);
    }

    /**
     * 蓝牙搜索回调
     */
    public interface SearchCallBack{
        IntentFilter addAction2IntentFilter();
        void intentAction(Context context, Intent intent);

    }
}
