package com.houlik.libhoulik.android.service;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * 例子:
 * 在Service中
 * public class WeatherService extends Service {
 *
 *     private final String TAG = "WeatherService";
 *     private InnerBind innerBind = new InnerBind();
 *     private ServiceCallBack serviceCallBack;
 *
 *     @Nullable
 *     @Override
 *     public IBinder onBind(Intent intent) {
 *         return innerBind;
 *     }
 *
 *     通过内部类与activity通信
 *     public class InnerBind extends Binder {
 *
 *         private Context context;
 *         private List<Map> mapList = new ArrayList();
 *
 *         public InnerBind(){}
 *
 *         public void setContext(Context context){
 *             this.context = context;
 *         }
 *
 *         public WeatherService getService(){
 *             return WeatherService.this;
 *         }
 *     }
 *
 *     public void setCallBack(ServiceCallBack serviceCallBack){
 *         this.serviceCallBack = serviceCallBack;
 *     }
 *
 *     public interface ServiceCallBack{
 *         void getResult(List<Map> weatherResult);
 *     }
 *
 *     @Override
 *     public void onCreate() {
 *         System.out.println("创建");
 *     }
 *
 *     @Override
 *     public int onStartCommand(Intent intent, int flags, int startId) {
 *         while(isStart) {
 *
 *         }
 *         return super.onStartCommand(intent, flags, startId);
 *     }
 *
 *     @Override
 *     public void onDestroy() {
 *         super.onDestroy();
 *     }
 *
 * }
 *
 *
 * 在activity中获取调用
 * Intent intent = new Intent(Activity.this, Service.class);
 * bindService(intent, connection, Context.BIND_AUTO_CREATE);
 *
 * private ServiceConnection connection = new ServiceConnection() {
 *         @Override
 *         public void onServiceConnected(ComponentName name, IBinder service) {
 *             innerBind = (WeatherService.InnerBind) service;
 *             innerBind.setContext(MainFragmentActivity.this);
 *             innerBind.processWebView("");
 *
 *             WeatherService sv = innerBind.getService();
 *             sv.setCallBack(new WeatherService.ServiceCallBack() {
 *                 @Override
 *                 public void getResult(final List<Map> weatherResult) {
 *                     // do something
 *                 }
 *             });
 *         }
 *
 *         @Override
 *         public void onServiceDisconnected(ComponentName name) {
 *
 *         }
 *     };
 *
 * onDestroy中
 * //解绑
 * unbindService(connection);
 *
 * Service
 * 如果启动service无反应请检查 manifest 配置 以及 启动service配置是否正确
 * Created by Houlik on 19/1/2017.
 */

public class Service extends android.app.Service {

    private final String LOG_TAG = "SERVICE DEMO";

    //内部类继承 Binder 得到当前类的方法
    //用于和activity之间的数据传送
    public class LocalBinder extends Binder {
        //通过这里获取到 service 对象
        Service getService() {
            return Service.this;
        }

        void doMethod(){
            //do something
        }
    }

    private LocalBinder binder = new LocalBinder();

    public void getserviceMethod(){
        Log.d(LOG_TAG,"getServiceMethod()");
    }

    @Override
    public void onCreate() {
        //只有执行一次初始化
        super.onCreate();
        Log.d(LOG_TAG,"onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //多次执行
        Log.d(LOG_TAG,"onStartCommand()");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(LOG_TAG,"onDestroy()");
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        //与activity调用者解绑
        Log.d(LOG_TAG,"onUnbind()");
        return super.onUnbind(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        //与activity调用者绑定
        Log.d(LOG_TAG,"onBind()");
        //返回内部类对象,在connecting中可获取到当前服务的内部对象
        return binder;
    }

    /**
     * 重新绑定
     * @param intent
     */
    @Override
    public void onRebind(Intent intent) {
        Log.d(LOG_TAG,"onRebind()");
        super.onRebind(intent);
    }
}
