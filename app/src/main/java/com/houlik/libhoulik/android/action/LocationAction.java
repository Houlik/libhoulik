package com.houlik.libhoulik.android.action;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.houlik.libhoulik.android.util.HLLog;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author Houlik
 * @since 2023/8/17
 * @description 通过GPS或者Network定位
 *
 */
@RequiresApi(api = Build.VERSION_CODES.N)
public class LocationAction {

    private final String TAG = "LocationAction";
    private Context context;
    private Activity activity;
    private LocationManager locationManager = null;
    private GpsStatus.Listener gpsStatusListener = null;
    private LocationListener locationListener = null;
    private OnGpsResult onGpsResult = null;
    //多少秒更新 1000 = 1秒
    private long timeRepeat = 1000L;
    //距离多远更新 1 = 1米
    private int minDistance = 1;

    public LocationAction(Context context, OnGpsResult onGpsResult){
        this.context = context;
        this.activity = (Activity)context;
        this.timeRepeat = timeRepeat;
        this.minDistance = minDistance;
        this.onGpsResult = onGpsResult;
        locationListener();
        gpsStatusListener();
        initGps();
    }

    /**
     * 初始化GPS
     */
    public void initGps(){
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        // 判断GPS是否正常启动
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(context, "请开启GPS导航...", Toast.LENGTH_SHORT).show();
            // 返回开启GPS导航设置界面
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            activity.startActivityForResult(intent, 0);
            return;
        }

        // 为获取地理位置信息时设置查询条件
        String bestProvider = locationManager.getBestProvider(getCriteria(), true);
        // 获取位置信息
        // 如果不设置查询要求，getLastKnownLocation方法传人的参数为LocationManager.GPS_PROVIDER

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        //assert bestProvider != null; 断言bestProvider不是空, 如果是空就报Assertion异常
        Location location = locationManager.getLastKnownLocation(bestProvider);
        if(location != null) {
            getAddress(location);
        }

        // 监听状态
        locationManager.addGpsStatusListener(gpsStatusListener);

        // 绑定监听，有4个参数
        // 参数1，设备：有GPS_PROVIDER和NETWORK_PROVIDER两种
        // 参数2，位置信息更新周期，单位毫秒
        // 参数3，位置变化最小距离：当位置距离变化超过此值时，将更新位置信息
        // 参数4，监听
        // 备注：参数2和3，如果参数3不为0，则以参数3为准；参数3为0，则通过时间来定时更新；两者为0，则随时刷新

        // 1秒更新一次，或最小位移变化超过1米更新一次；
        // 注意：此处更新准确度非常低，推荐在service里面启动一个Thread，在run中sleep(10000);然后执行handler.sendMessage(),更新位置
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, timeRepeat, minDistance, locationListener);
    }

    private void gpsStatusListener(){
        gpsStatusListener = new GpsStatus.Listener() {
            @Override
            public void onGpsStatusChanged(int event) {
                switch (event) {
                    // 第一次定位
                    case GpsStatus.GPS_EVENT_FIRST_FIX:
                        HLLog.i(TAG, "GPS 第一次定位");
                        break;
                    // 卫星状态改变
                    case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                        HLLog.i(TAG, "GPS 卫星状态改变");
                        // 获取当前状态
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        GpsStatus gpsStatus = locationManager.getGpsStatus(null);
                        // 获取卫星颗数的默认最大值
                        int maxSatellites = gpsStatus.getMaxSatellites();
                        // 创建一个迭代器保存所有卫星
                        Iterator<GpsSatellite> iters = gpsStatus.getSatellites().iterator();
                        int count = 0;
                        while (iters.hasNext() && count <= maxSatellites) {
                            GpsSatellite s = iters.next();
                            count++;
                        }
                        System.out.println("GPS 搜索到："+count+"颗卫星");
                        break;
                    // 定位启动
                    case GpsStatus.GPS_EVENT_STARTED:
                        HLLog.i(TAG, "GPS 定位启动");
                        break;
                    // 定位结束
                    case GpsStatus.GPS_EVENT_STOPPED:
                        HLLog.i(TAG, "GPS 定位结束");
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private void locationListener(){
        locationListener = new LocationListener() {
            //位置信息变化时触发
            @Override
            public void onLocationChanged(Location location) {
                getAddress(location);
            }

            //GPS状态变化时触发
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                switch (status) {
                    // GPS状态为可见时
                    case LocationProvider.AVAILABLE:
                        HLLog.i(TAG, "GPS 当前GPS状态为可见状态");
                        break;
                    // GPS状态为服务区外时
                    case LocationProvider.OUT_OF_SERVICE:
                        HLLog.i(TAG, "GPS 当前GPS状态为服务区外状态");
                        break;
                    // GPS状态为暂停服务时
                    case LocationProvider.TEMPORARILY_UNAVAILABLE:
                        HLLog.i(TAG, "GPS 当前GPS状态为暂停服务状态");
                        break;
                    default:
                        break;
                }
            }

            //GPS开启时触发
            @Override
            public void onProviderEnabled(String provider) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                Location location = locationManager.getLastKnownLocation(provider);
                getAddress(location);
            }

            //GPS禁用时触发
            @Override
            public void onProviderDisabled(String provider) {
                getAddress(null);
            }
        };
    }

    /**
     *  @return 返回Criteria 查询条件
     */
    private Criteria getCriteria(){
        Criteria criteria= new Criteria();
        // 设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        // 设置是否要求速度
        criteria.setSpeedRequired( false);
        // 设置是否允许运营商收费
        criteria.setCostAllowed( false);
        // 设置是否需要方位信息
        criteria.setBearingRequired( false);
        // 设置是否需要海拔信息
        criteria.setAltitudeRequired( false);
        // 设置对电源的需求
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        return criteria;
    }

    /**
     * 实时获取GPS相关信息
     * @param location 获取到的Location
     */
    private void getAddress(Location location) {
        List<Address> result = null;
        try {
            if (location != null) {
                Geocoder gc = new Geocoder(context, Locale.getDefault());
                result = gc.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if(onGpsResult != null){
                    onGpsResult.getGpsResult(result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface OnGpsResult{
        void getGpsResult(List<Address> result);
    }

}
