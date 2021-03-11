package com.houlik.libhoulik.testing;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;

/**
 * Created by houlik on 2018/11/18.
 */

public class SensorBuilder {

    /**
     * sensorManager.getDefaultSensor(int)
     * 1. Sensor.TYPE_ACCELEROMETER 加速度传感器
     * 2. Sensor.TYPE_MAGNETIC_FIELD 磁场传感器
     * 3. Sensor.TYPE_GYROSCOPE 陀螺仪传感器
     * 4. Sensor.TYPE_LIGHT 光传感器
     * 5. Sensor.TYPE_TEMPERATURE 温度传感器
     * 6. Sensor.TYPE_PROXIMITY 接近传感器
     * 7. Sensor.TYPE_ORIENTATION 姿态传感器
     */


    private Context context;
    private SensorManager sensorManager;
    private int sensorType;
    private Sensor sensor;
    private SensorEventListener sensorEventListener;



    private SensorBuilder(Builder builder) {
        this.context = builder.context;
        this.sensorType = builder.sensorType;
        initSensorManager();
        sensorEventListener = builder.sensorEventListener;

        System.out.println("Name : " + sensor.getName());
        System.out.println("Vendor : " + sensor.getVendor());
        System.out.println("To String : " + sensor.toString());

        //19
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            System.out.println("FifoMaxEventCount : " + sensor.getFifoMaxEventCount());
            System.out.println("FifoReservedEventCount : " + sensor.getFifoReservedEventCount());
        }

        //20
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            System.out.println("String Type : " + sensor.getStringType());
        }

        //21
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            System.out.println("MaxDelay : " + sensor.getMaxDelay());
            System.out.println("ReportingMode : " + sensor.getReportingMode());
            System.out.println("isWakeUpSensor : " + sensor.isWakeUpSensor());
        }

        //24
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            System.out.println(sensor.getHighestDirectReportRateLevel());
            System.out.println("Id : " + sensor.getId());
            System.out.println("isAdditionalInfoSupported : " + sensor.isAdditionalInfoSupported());
            System.out.println("isDynamicSensor : " + sensor.isDynamicSensor());
        }
        System.out.println("MaximumRange : " + sensor.getMaximumRange());
        System.out.println("MinDelay : " + sensor.getMinDelay());
        System.out.println("Power : " + sensor.getPower());
        System.out.println("Type : " + sensor.getType());
        System.out.println("Version : " + sensor.getVersion());
    }

    private void initSensorManager() {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(sensorType);
    }

    /**
     * 在创建传感器Activity的onResume中注册此传感器
     *
     * 传感器采样频率
     * SENSOR_DELAY_FASTEST 最快频率
     * SENSOR_DELAY_UI 普通用户界面的频率
     * SENSOR_DELAY_GAME 游戏的频率
     * SENSOR_DELAY_NORMAL 默认频率 - 适合屏幕横竖状态的自动切换
     * @param sensorManagerSensorDelay
     */
    public void registerAtActivityOnResume(int sensorManagerSensorDelay){
        sensorManager.registerListener(sensorEventListener,sensor,sensorManagerSensorDelay);
    }

    public void unRegisterAtActivityOnPause(){
        sensorManager.unregisterListener(sensorEventListener);
    }

    public static class Builder{

        private Context context;
        private int sensorType;
        private SensorEventListener sensorEventListener;

        public Builder setContext(Context context){
            this.context = context;
            return this;
        }

        /**
         * 传感器类型
         * sensorManager.getDefaultSensor(int)
         * 1. Sensor.TYPE_ACCELEROMETER 加速度传感器 X|Y|Z
         * 2. Sensor.TYPE_MAGNETIC_FIELD 磁场传感器 X|Y|Z
         * 3. Sensor.TYPE_GYROSCOPE 陀螺仪传感器 X|Y|Z
         * 4. Sensor.TYPE_LIGHT 光传感器 只有一个值 - 光照强度
         * 5. Sensor.TYPE_TEMPERATURE 温度传感器 只有一个值 - 环境温度
         * 6. Sensor.TYPE_PROXIMITY 接近传感器 只有一个值 - 探测是否有物体离手机屏幕非常近 - 不同手机型号有差异
         * 7. Sensor.TYPE_ORIENTATION 姿态传感器 3个轴值需要由加速度传感器与磁场传感器的值6轴连算得出 (飞机)
         * 如 :
         * float[] R = new float[9];
         * vlAccelerometer = event.values;
         * SensorManager.getRotationMatrix(R, null, vlAccelerometer,vlmanager);
         * float[] Values = new float[3];
         * SensorManager.getOrientation(R, Values);
         * 之后可以通过 Values 数组获取姿态值
         *
         * @param sensorType
         */
        public Builder setSensorType(int sensorType){
            this.sensorType = sensorType;
            return this;
        }

        /**
         *
         * @param sensorEventListener
         * @return
         */
        public Builder setSensorEventListener(SensorEventListener sensorEventListener){
            this.sensorEventListener = sensorEventListener;
            return this;
        }

        /**
         * 最后一步
         * 通过当前process获取SensorBuilder对象在Activity的onResume中注册传感器
         * 以及
         * 在Activity的onPause中注销传感器避免不必要的耗费手机电量
         * @return
         */
        public SensorBuilder process(){
            return new SensorBuilder(this);
        }


    }





}

