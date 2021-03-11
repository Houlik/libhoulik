package com.houlik.libhoulik.android.util;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * 传感器
 * Created by houlik on 2018/6/2.
 */

public class SensorHelper implements SensorEventListener {

    //传感器服务
    private SensorManager oSensorManager;
    //具体的传感器
    private Sensor oSensor;
    // 速度阈值，当摇晃速度达到这值后产生作用
    private final int MAX_SPEEDY = 2500;
    // 两次检测的时间间隔
    private final int UPTATE_INTERVAL_TIME = 100;
    // 手机上一个位置时重力感应坐标
    private float lastX;
    private float lastY;
    private float lastZ;
    // 上次检测时间
    private long lastUpdateTime;
    //摇晃后执行的操作接口
    private OnSensorActionListener mSensorActionListener;

    /**
     * 初始化传感器注册
     * @param context
     * @param onSensorActionListener 摇晃后执行的操作
     */
    public SensorHelper(Context context, OnSensorActionListener onSensorActionListener) {
        oSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        oSensor = oSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        registerSensor();
        this.mSensorActionListener = onSensorActionListener;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        int sensorType = event.sensor.getType();

        if (sensorType == Sensor.TYPE_ACCELEROMETER) {
            final int ACCELEROMETER = 18;
            if ((Math.abs(x) > ACCELEROMETER || Math.abs(y) > ACCELEROMETER || Math.abs(z) > ACCELEROMETER)) {
                mSensorActionListener.sensorAction();
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    //停止传感器
    public void stopSensor() {
        oSensorManager.unregisterListener(this);
    }

    //启动传感器
    public void registerSensor() {
        oSensorManager.registerListener(this, oSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    //摇摆后执行的操作接口
    public interface OnSensorActionListener{
        void sensorAction();
    }

    //######################################################################
    //使用时间加速度判断=不容易掌握
    private void timeAction(SensorEvent event) {
        // 现在检测时间
        long currentUpdateTime = System.currentTimeMillis();
        // 两次检测的时间间隔
        long timeInterval = currentUpdateTime - lastUpdateTime;
        // 判断是否达到了检测时间间隔
        if (timeInterval < UPTATE_INTERVAL_TIME) return;
        // 现在的时间变成last时间
        lastUpdateTime = currentUpdateTime;

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        // 获得x,y,z的变化值
        float deltaX = x - lastX;
        float deltaY = y - lastY;
        float deltaZ = z - lastZ;

        // 将现在的坐标变成last坐标
        lastX = x;
        lastY = y;
        lastZ = z;
        double speed = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) / timeInterval * 10000;

        //速度超出最高值，执行下一步
        if (speed >= MAX_SPEEDY) {
            mSensorActionListener.sensorAction();
        }
    }
}
