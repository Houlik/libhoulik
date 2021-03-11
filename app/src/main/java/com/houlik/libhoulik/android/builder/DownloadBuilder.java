package com.houlik.libhoulik.android.builder;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import androidx.annotation.NonNull;
import android.widget.Toast;



/**
 * 用于下载大数据文件
 * 通过建造者模式设置,最后调用process来执行
 * 如果希望通过广播执行一些事务可以在setRequestIsBroadcastReceiver里OnBroadcastreceiver来实现
 * 可以通过Builder类remove按照id来取消下载中的继续
 * Created by houlik on 2018/11/4.
 */

public class DownloadBuilder{

    //获取程序下载对象
    private DownloadManager dm;
    //下载前设置
    private DownloadManager.Request request;
    private long downloadID;

    private DownloadBuilder(Builder builder) {
        dm = (DownloadManager) builder.context.getSystemService(Context.DOWNLOAD_SERVICE);
        builder.dm = dm;
        try {
            request = new DownloadManager.Request(builder.http);
            setRequestSetting(request, builder);
            setRequestBroadcastReceiver(builder);
            downloadID = dm.enqueue(request);
            builder.downloadID = downloadID;
        }catch (Exception e){
            Toast.makeText(builder.context,"请检查网址是否正确",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    /**
     * 设置request设定
     *
     * @param request
     * @param builder
     */
    private void setRequestSetting(DownloadManager.Request request, Builder builder) {
        request.setVisibleInDownloadsUi(builder.isVisible);
        request.setNotificationVisibility(builder.visibility);
        request.setTitle(builder.title);
        request.setDescription(builder.description);
        request.setAllowedNetworkTypes(builder.flags);
        //如果自定义保存位置
        if (builder.isCustom) {
            request.setDestinationUri(builder.des);
        } else {
            if (builder.isPublic) {
                //如果公开就保存在SDCard外
                request.setDestinationInExternalPublicDir(builder.environment, builder.fileName);
            } else {
                //如果不公开就保存在软件内部里
                request.setDestinationInExternalFilesDir(builder.context, builder.environment, builder.fileName);
            }
        }
    }

    /**
     * 如果需要广播就启动
     */
    private void setRequestBroadcastReceiver(Builder builder) {
        if (builder.isBroadcastReceiver) {
            builder.onBroadcastReceiver.setBroadcastReceiver(builder);
        }
    }

    public static class Builder {
        //上下文
        private Context context;
        //网址 http/https://......
        private Uri http;
        //保存位置-完整
        private Uri des;
        //是否可视化
        private boolean isVisible;
        //通知
        private int visibility;
        //网络下载类型
        private int flags;
        //保存位置 - 如: 下载，音乐，电影，文件，等文件夹中 | 或者是自定义的文件夹中
        private String environment;
        //文件名
        private String fileName;
        //通知栏主题
        private String title;
        //通知栏内容
        private String description;
        //是否保存为公开
        private boolean isPublic;
        //是否自定义保存
        private boolean isCustom;
        //是否广播
        private boolean isBroadcastReceiver;
        //广播设置接口
        private OnBroadcastReceiver onBroadcastReceiver;
        //下载执行操作的ID
        private long downloadID;
        //下载器对象
        private DownloadManager dm;

        /**
         * 上下文
         *
         * @param context
         * @return
         */
        public Builder setContext(@NonNull Context context) {
            this.context = context;
            return this;
        }

        /**
         * 下载的网址
         * @param uri
         * @return
         */
        public Builder setRequestHttpAddress(@NonNull Uri uri) {
            this.http = uri;
            return this;
        }

        /**
         * 是否保存在自定义的位置上
         * 如果是必须与setRequestDestinationUri(Uri)方法同时使用
         * @param isCustom
         * @return
         */
        public Builder setRequestIsSaveAsCustom(@NonNull boolean isCustom) {
            this.isCustom = isCustom;
            return this;
        }

        /**
         * 是否保存在公开位置上
         * 这是与setRequestDestinationInExternalPublicDir_FileDir(String, String )方法同时使用
         * @param isPublic
         * @return
         */
        public Builder setRequestIsSaveAsPublic(@NonNull boolean isPublic) {
            this.isPublic = isPublic;
            return this;
        }

        /**
         * 指定保存的位置 - SD卡
         * 如果是 DESC_CUSTOM
         * Uri.fromFile(new FileBuilder(FileUtils.getInstance().getSDPath()+folder[0]+"/FileName"))
         *
         * @param uri
         * @return
         */
        public Builder setRequestDestinationUri(@NonNull Uri uri) {
            this.des = uri;
            return this;
        }

        /**
         * 默认保存的位置Environment 或者 SD卡下
         * Environment - SD卡/Android/data/customPackage/files/environment参考如下/customFile
         * DIRECTORY_MUSIC|DIRECTORY_PICTURES|DIRECTORY_MOVIES|DIRECTORY_DOWNLOADS|DIRECTORY_DCIM|DIRECTORY_DOCUMENTS| 这是最常保存文件所使用的文件夹
         * DIRECTORY_RINGTONES|DIRECTORY_ALARMS|DIRECTORY_NOTIFICATIONS|DIRECTORY_PODCASTS| 这是不常保存文件所使用的文件夹
         * SD卡下 - SD卡/Android/data/customPackage/files/customFolder/customFile
         *
         * 不公开 mnt/sdcard/Android/data/com.???.???
         * 公开 SD卡/storage/####-####/文件夹/文件名
         *
         * @param environment
         * @param fileName
         * @return
         */
        public Builder setRequestDestinationInExternalPublicDir_FileDir(@NonNull String environment,@NonNull  String fileName) {
            this.environment = environment;
            this.fileName = fileName;

            return this;
        }

        /**
         * 是否设置下载过程为可视化
         *
         * @param isVisible
         * @return
         */
        public Builder setRequestVisibleInDownloadsUi(@NonNull boolean isVisible) {
            this.isVisible = isVisible;
            return this;
        }

        /**
         * 设置哪一种类型的通知
         * DownloadManager.Request.VISIBILITY_VISIBLE = 0
         * DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED = 1
         * DownloadManager.Request.VISIBILITY_HIDDEN = 2
         * 不想在通知栏显示必须在manifest里添加如下权限
         * <uses-permission android:name="android.permission.DOWNLOAD_WITHOUT_NOTIFICATION" />
         *
         * DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION = 3
         *
         * @param requestVisibility
         * @return
         */
        public Builder setRequestNotificationVisibility(@NonNull int requestVisibility) {
            this.visibility = requestVisibility;
            return this;
        }

        /**
         * 选择下载网络类型
         * DownloadManager.Request.NETWORK_WIFI
         * DownloadManager.Request.NETWORK_MOBILE
         *
         * @param requestFlags
         * @return
         */
        public Builder setRequestAllowedNetWorkTypes(@NonNull int requestFlags) {
            this.flags = requestFlags;
            return this;
        }

        /**
         * 通知栏主题
         *
         * @param title
         * @return
         */
        public Builder setRequestTitle(@NonNull String title) {
            this.title = title;
            return this;
        }

        /**
         * 通知栏内容
         *
         * @param description
         * @return
         */
        public Builder setRequestDescription(@NonNull String description) {
            this.description = description;
            return this;
        }

        /**
         * 设置是否需要广播监听
         * DownloadManager.ACTION_DOWNLOAD_COMPLETE
         * DownloadManager.ACTION_NOTIFICATION_CLICKED
         * DownloadManager.ACTION_VIEW_DOWNLOADS
         * @param isBroadcastReceiver
         * @param onBroadcastReceiver
         * @return
         */
        public Builder setRequestIsBroadcastReceiver(@NonNull boolean isBroadcastReceiver, @NonNull OnBroadcastReceiver onBroadcastReceiver) {
            this.isBroadcastReceiver = isBroadcastReceiver;
            if (isBroadcastReceiver) {
                this.onBroadcastReceiver = onBroadcastReceiver;
            }
            return this;
        }

        /**
         * 通过广播intent 获取 intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1); 这是下载完毕得到的ID
         * 当前方法是直接得到启动时得到的下载ID
         * @return
         */
        public long getDownloadID(){
            return downloadID;
        }

        /**
         * 取消正在下载的数据
         */
        public void removeDownload(@NonNull long downloadID){
            dm.remove(downloadID);
        }

        public void getDownloadProgress(){
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL);
            Cursor cursor = dm.query(query);
            cursor.moveToFirst();

            System.out.println(cursor.getPosition());
        }

        /**
         * 开始执行
         * @return
         */
        public DownloadBuilder process() {
            return new DownloadBuilder(this);
        }
    }

    /**
     * 执行广播任务
     */
    public interface OnBroadcastReceiver{
        void setBroadcastReceiver(Builder builder);
    }

}
