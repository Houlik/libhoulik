package com.houlik.libhoulik.android.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.util.Log;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import androidx.annotation.RequiresApi;

/**
 * 网络
 * 必须注册权限 android.permission.INTERNET
 * Created by Houlik on 2017-11-30.
 */

public class NetworkUtils {

    /**
     * 10.0.2.1 网关地址
     * 10.0.2.2 原本操作系统 127.0.0.1 的别名地址
     * 10.0.2.3 第一优先 DNS
     * 10.0.2.4 - 6 第二，第三，第四 顺位 DNS
     * 10.0.2.15 AVD本机网络地址
     * 127.0.0.1 AVD本机回路地址
     */

    private final String TAG = "NetworkUtils";
    private ConnectivityManager connectivityManager;

    public NetworkUtils(Context context){
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    /**
     * 取得本地ip地址
     * @return
     */
    public String getLocalIPAddress(){
        try {
            for(Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();){
                NetworkInterface networkInterface = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = networkInterface.getInetAddresses(); enumIpAddr.hasMoreElements();){
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if(!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()){
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 用于判断当前 NetworkCapabilities 是什么类型网络连接
     * TRANSPORT_CELLULAR 移动
     * TRANSPORT_BLUETOOTH 蓝牙
     * TRANSPORT_WIFI
     * TRANSPORT_ETHERNET 有线
     * TRANSPORT_VPN
     * NET_CAPABILITY_INTERNET 网络是否连接
     * @param networkCapabilitiesType 查询连接类型
     * @return
     */
    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean stateNetworkConnecting(int networkCapabilitiesType){
        Network network = connectivityManager.getActiveNetwork();
        if(network == null){
            return false;
        }
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
        if(networkCapabilities == null){
            return false;
        }
        return networkCapabilities.hasTransport(networkCapabilitiesType);
    }

    /**
     * 检测是否已连接上任意的网络
     * @param networkCapabilities
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean checkIsConnect2NetworkCapabilities(int[] networkCapabilities){
        boolean[] isNC = new boolean[networkCapabilities.length];
        for (int i = 0; i < networkCapabilities.length; i++) {
            isNC[i] = stateNetworkConnecting(networkCapabilities[i]);
        }
        for (int i = 0; i < isNC.length; i++) {
            if(isNC[i] == true){
                return true;
            }
        }
        return false;
    }

    /**
     * 检测是否已连接网络
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean checkIsConnect2NetworkCapabilities(){
        return checkIsConnect2NetworkCapabilities(new int[]{NetworkCapabilities.TRANSPORT_CELLULAR, NetworkCapabilities.TRANSPORT_WIFI});
    }

    /**
     * 检测是否有网络连接
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean checkNetWorkIsConnect(){
         if(connectivityManager.isDefaultNetworkActive()) {
             Network network = connectivityManager.getActiveNetwork();
             NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
             System.out.println("Network Capabilities : " + networkCapabilities);
             if(networkCapabilities != null){
                 return true;
             }
         }
         return false;
    }
}
