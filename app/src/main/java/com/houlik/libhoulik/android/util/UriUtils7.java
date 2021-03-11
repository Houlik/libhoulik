package com.houlik.libhoulik.android.util;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;

import java.io.File;

/**
 * Created by Houlik on 2018-01-10.
 */

public class UriUtils7 {

    private static UriUtils7 uriUtils7 = new UriUtils7();

    private UriUtils7(){}

    public static UriUtils7 getInstance(){
        if(uriUtils7 == null){
            new UriUtils7();
        }
        return uriUtils7;
    }

    /**
     * 注意：我们使用 getUriForFile(Context, String, File)
     * 来返回 content:// URI。对于以 Android 7.0（API 级别 24）及更高版本为目标平台的最新应用，
     * 跨越软件包边界传递 file:// URI 会导致出现 FileUriExposedException。
     * 因此，我们现在介绍一种更通用的方法来使用 FileProvider 存储图片。
     *
     *
     * Steps to replace file:// URI with content:// URI:
     *
     * 新建类继承FileProvider
     * Add a class extending FileProvider
     * public class GenericFileProvider extends FileProvider {     }
     *
     *
     * 在Manifest中注册
     * Add a FileProvider tag in AndroidManifest.xml under tag.
     * Specify an unique authority for the android:authorities attribute to avoid conflicts,
     * imported dependencies might specify ${applicationId}.provider and other commonly used authorities.
     *
     * 输入以下内容
     * <?xml version="1.0" encoding="utf-8"?>
     * <application>
     *        ...
     *        <provider
     *             android:name="android.support.v4.content.FileProvider"
     *             android:authorities="com.example.android.fileprovider" | "com.?.自定义的FileProvider类"
     *             android:exported="false"
     *             android:grantUriPermissions="true">
     *             <meta-data
     *                 android:name="android.support.FILE_PROVIDER_PATHS"
     *                 android:resource="@xml/provider_paths"></meta-data>
     *         </provider>
     *         ...
     * </application>
     *
     * 确保授权字符串与 getUriForFile(Context, String, File) 的第二个参数匹配。
     * 在提供程序定义的元数据部分中，您可以看到提供程序需要在专用资源文件 res/xml/file_paths.xml 中配置符合条件的路径。
     * 下面显示了此特定示例所需的内容：
     *
     * 自定义设置访问路径
     * <?xml version="1.0" encoding="utf-8"?>
     *     <paths xmlns:android="http://schemas.android.com/apk/res/android">
     *         <external-path name="my_images" path="Android/data/com.example.package.name/files/Pictures" />
     *     </paths>
     *
     *
     * 访问根目录路径
     * 新建XML文件
     * Then create a provider_paths.xml file in xml folder under res folder.
     * Folder may be needed to create if it doesn't exist. The content of the file is shown below.
     * It describes that we would like to share access to the External Storage at root folder (path=".") with the name external_files.
     *输入以下内容
     * <?xml version="1.0" encoding="utf-8"?>
     <paths xmlns:android="http://schemas.android.com/apk/res/android">
     <root-path name="Houlik" path="."/>
     </paths>
     *
     * path：需要临时授权访问的路径（.代表所有路径）
     name：就是你给这个访问路径起个名字

     <root-path/> 代表设备的根目录new FileBuilder("/");
     <files-path/> 代表context.getFilesDir()
     <cache-path/> 代表context.getCacheDir()
     <external-path/> 代表Environment.getExternalStorageDirectory()
     <external-files-path>代表context.getExternalFilesDirs()
     <external-cache-path>代表getExternalCacheDirs()

     <root-path name="root" path="" />
     <files-path name="files" path="" />
     <cache-path name="cache" path="" />
     <external-path name="external" path="" />
     <external-files-path name="name" path="path" />
     <external-cache-path name="name" path="path" />
     *
     *
     *
     *
     * 使用Intent调用必须调用以下内容
     * intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
     *
     * 适用 7.0 以上版本
     * @param context
     * @param authority "com.主名称.文件夹.provider文件名" ==>> com.houlik.folder.FileProvider
     * @param file
     * @return
     */
    public Uri getUri_7(Context context, String authority, File file){
        Uri uri = FileProvider.getUriForFile(context,authority,file);
        return uri;
    }

    public String getUriPath(String filename){
        Uri uri = Uri.parse(FileUtils.getInstance().getDirPath() + "/" + filename);
        String tmp_uriPath = uri.getPath();
        return tmp_uriPath;
    }

    /**
     * 依据版本得到图库图片的绝对路径
     * @param context
     * @param uri
     * @return
     */
    public String getUriRealPath(Context context, Uri uri){
        String filePath = null;
        if (Build.VERSION.SDK_INT >= 19) {
            if(DocumentsContract.isDocumentUri(context, uri)){
                String documentID = DocumentsContract.getDocumentId(uri);
                if(isMediaDocument(uri)){
                    String id = documentID.split(":")[1];
                    String selection = MediaStore.Images.Media._ID + "=?";
                    String[] selectionArgs = {id};
                    filePath = getDataColumn(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection, selectionArgs);
                }else if(isDownloadsDocument(uri)){
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(documentID));
                    filePath = getDataColumn(context, contentUri, null, null);
                }
            }else if("content".equalsIgnoreCase(uri.getScheme())){
                filePath = getDataColumn(context, uri, null, null);
            }else if("file".equals(uri.getScheme())){
                filePath = uri.getPath();
            }
            return filePath;

        } else {
            getDataColumn(context, uri, null, null);
        }
        return filePath;
    }

    /**
     * 是否为多媒体的文件
     * @param uri
     * @return
     */
    private boolean isMediaDocument(Uri uri){
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * 获取数据库表中的 _data 列, 即返回Uri对应的文件路径
     * @param context
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return
     */
    private String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs){
        String path = null;
        String[] projection = new String[]{MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri,projection, selection, selectionArgs, null);
        if(cursor != null && cursor.moveToFirst()){
            int columnIndex = cursor.getColumnIndexOrThrow(projection[0]);
            path = cursor.getString(columnIndex);
        }
        if(cursor != null) {
            cursor.close();
        }
        return path;
    }

    /**
     * 是否为下载的文件
     * @param uri
     * @return
     */
    private boolean isDownloadsDocument(Uri uri){
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }
}
