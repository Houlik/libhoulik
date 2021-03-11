package com.houlik.libhoulik.android.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.util.Log;


import java.util.List;

import androidx.core.app.ActivityCompat;

public class LocationUtils {

    private final String TAG = "LocationUtils";
    private LocationManager locationManager;

    private static LocationUtils locationUtils = new LocationUtils();

    private LocationUtils() {
    }

    public static LocationUtils getInstance() {
        if (locationUtils == null) {
            locationUtils = new LocationUtils();
        }
        return locationUtils;
    }

    /**
     * 获取Location
     * 这是第一步需要执行的
     * @param context
     * @param locationManagerProvider
     * @return
     */
    public Location getLocation(Context context, String locationManagerProvider){
        String strService = context.LOCATION_SERVICE;// 获取的是位置服务
        locationManager = (LocationManager) context.getSystemService(strService);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},0);
            Log.i(TAG, "没权限");
        }else{
            Log.i(TAG, "有权限");
        }
        return locationManager.getLastKnownLocation(locationManagerProvider);
    }

    /**
     * [Address[
     * addressLines=[0:"完整位置"],
     * feature=当前位置,
     * admin=省,
     * sub-admin=null,
     * locality=市,
     * thoroughfare=null,
     * postalCode=null,
     * countryCode=CN,
     * countryName=国,
     * hasLatitude=true,
     * latitude=纬度 | 范围,
     * hasLongitude=true,
     * longitude=经度,
     * phone=null,
     * url=null,
     * extras=Bundle[mParcelledData.dataSize=??]]]
     *
     * 获取 list.get(0).get???()
     * @param context
     * @param location
     * @return
     */
    public List<Address> geocoderLocation(Context context, Location location){
        List<Address> tmp = null;
        if(location != null){
            Geocoder geocoder = new Geocoder(context);
            double locLatitude = location.getAltitude();
            Log.i(TAG, " Altitude : " + locLatitude);
            double locLongitude = location.getLongitude();
            Log.i(TAG, " Longitude : " + locLongitude);
            try {
                tmp = geocoder.getFromLocation(locLatitude, locLongitude, 1);
                Log.i(TAG, tmp.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return tmp;
        }
        return null;
    }

    public List<Address> geocoderLocation(Context context, double locLatitude, double locLongitude){
        List<Address> tmp = null;
        Geocoder geocoder = new Geocoder(context);
        try {
            tmp = geocoder.getFromLocation(locLatitude, locLongitude, 3);
            Log.i(TAG, tmp.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmp;
    }

    public boolean checkIsHavingGeocoder(){
        return Geocoder.isPresent();
    }

    /**
     * 检测是否已经打开GPS, 否则直接跳到GPS设置界面
     * 如果已经打开将直接执行geocoderLocation方法返回Address集合
     * @param activity
     * @param location
     * @param requestCode
     * @return
     */
    public List<Address> checkGPS(Activity activity, Location location, int requestCode){
        LocationManager locationManager = (LocationManager) activity.getSystemService(activity.LOCATION_SERVICE);
        Log.i(TAG, "GPS 状态 : " + locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER));
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            return geocoderLocation(activity, location);
        }else{
            //跳转GPS设置界面
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            activity.startActivityForResult(intent, requestCode);
        }
        return null;
    }

    public LocationManager getLocationManager(){
        return locationManager;
    }
}
