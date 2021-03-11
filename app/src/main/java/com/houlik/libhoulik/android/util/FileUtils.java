package com.houlik.libhoulik.android.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * 安卓文件类工具
 * 注意中英文字符和标点符号是不相容,稍微用错将出现难以查询何处出错
 * 如：中 = "／" 英 = "/"
 * 如：中 = ： 英 = : 等类似相差
 * Created by houlik on 2018/11/12.
 */

public class FileUtils {

    /**
     * Environment 常用方法：
     * 方法：getDataDirectory(),返回 FileBuilder ，获取 Android 数据目录。 /data
     * 方法：getDownloadCacheDirectory(),返回 FileBuilder ，获取 Android 下载/缓存内容目录。 /cache
     * 方法：getExternalStorageDirectory(),返回 FileBuilder ，获取外部存储目录即 SDCard => /storage/emulated/0
     *       其它=/data/data/com.android.providers.downloads/cache/...
     * 方法：getExternalStoragePublicDirectory(String type),返回 FileBuilder ，取一个高端的公用的外部存储器目录来摆放某些类型的文件
     * 方法：getExternalStorageState(),返回 FileBuilder ，获取外部存储设备的当前状态
     * 方法：getRootDirectory(),返回 FileBuilder ，获取 Android 的根目录 /system
     * context.getExternalFilesDir 这是当前应用程序的根目录
     */

    /**
     * Android 10 改变了文件的存储方式
     * 在Androidmainfest 里面的application添加 android:requestLegacyExternalStorage="true"
     * 使用原来的存储方式
     * Context.getExternalFilesDir 获取外建文件夹的根目录
     * /storage/emulated/0/Android/data/com.文件夹.文件/files 目录下可建
     */

    //外置根目录
    private String dirPath;

    //7.0使用
    //private String AbsoluteSDPath = Environment.getExternalStorageDirectory().getAbsolutePath().toString();

    public enum MIMETYPE {AUDIO, VIDEO, IMAGE, TEXT}

    private static FileUtils fileUtils = new FileUtils();

    private FileUtils(){}

    public static FileUtils getInstance(){
        if(fileUtils == null){
            fileUtils = new FileUtils();
        }
        return fileUtils;
    }

    /**
     * 获取自身软件的外置根路径
     * context.getExternalFilesDir("文件夹") 如果根路径下没有该文件夹,系统将自动创建一个该命名的新文件夹
     * @param context
     */
    public void setDirPath(Context context){
        this.dirPath = context.getExternalFilesDir(null).toString();
    }

    public String getDirPath() {
        return dirPath;
    }

    /**
     * 判断目录是否已经存在
     *
     * @param SDPath 完整文件夹路径
     * @return
     */
    public boolean isFolderExist(String SDPath) {
        File dir = new File(SDPath);
        if (SDPath != null) {
            if (dir.exists()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 如果目录不存在就创建新目录
     *
     * @param SDPath
     * @return
     */
    public File createSDDir(String SDPath) {
        File dir = new File(SDPath);
        if (SDPath != null) {
            //不存在创建
            if (!dir.exists()) {
                dir.mkdir();
            }
        }
        return dir;
    }

    /**
     * 判断文件是否存在
     *
     * @param SDPath   完整文件夹路径
     * @param fileName
     * @return
     */
    public boolean isFileExist(String SDPath, String fileName) {
        File file = new File(SDPath + "/" + fileName);
        return file.exists();
    }

    /**
     * 如果文件不存在就创建新文件，前提是目录保存是必须存在
     *
     * @param SDPath
     * @param fileName
     * @return
     */
    public File createSDFile(String SDPath, String fileName) {
        File file = new File(SDPath + "/" + fileName);
        if (SDPath != null && fileName != null) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * 如果SD卡无插入时返回true
     *
     * @return
     */
    public boolean isSDEmpty() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_REMOVED)) {
            return true;
        }
        return false;
    }

    /**
     * 创建多个文件夹,如果文件夹中还有文件夹中间必须使用 "/" 隔开
     * 例如: 文件夹/文件夹 无论是字符串还是数组中
     *
     * @param SDPath 文件夹路径
     * @param folder 文件夹名称，不包括路径
     */
    public void createMultiSDFolder(String SDPath, String[] folder) {
        for (int i = 0; i < folder.length; i++) {
            if (!isFolderExist(SDPath)) {
                createSDDir(SDPath);
            }
            if (isFolderExist(SDPath)) {
                if (!isFolderExist(SDPath + "/" + folder[i])) {
                    createSDDir(SDPath + "/" + folder[i]);
                }
            }
        }
    }

    /**
     * 创建多个文件
     *
     * @param SDPath
     * @param folder 文件夹名称 不包括路径
     * @param file
     */
    public void createMultiSDFile(String SDPath, String folder, String[] file) {
        for (int i = 0; i < file.length; i++) {
            if (isFolderExist(SDPath + folder)) {
                if (!isFileExist(SDPath + "/" + folder + "/", file[i])) {
                    createSDFile(SDPath + "/" + folder + "/", file[i]);
                }
            } else {
                Log.i("FileUtil ", "该目录不存在");
            }
        }
    }

    /**
     * 删除文件
     *
     * @param SDPath   路径
     * @param fileName 文件名
     */
    public void deleteFile(String SDPath, String fileName) {
        File file = new File(SDPath + "/" + fileName);
        file.delete();
    }

    /**
     * 删除文件
     *
     * @param filePath   文件路径
     */
    public void deleteFile(String filePath){
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     *
     * 第一种 例子
     * Intent intent = new Intent(Intent.ACTION_VIEW);
     * Uri uri = Uri.fromFile(file);
     * intent.addCategory(Intent.CATEGORY_DEFAULT);
     *
     * intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
     * intent.setDataAndType(uri, "text/plain"); 打开文本文件
     *
     * intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
     * intent.putExtra("oneshot", 0);
     * intent.putExtra("configchange", 0);
     * intent.setDataAndType(uri, "audio/*"); 音频
     *
     * intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
     * intent.putExtra("oneshot", 0);
     * intent.putExtra("configchange", 0);
     * intent.setDataAndType(uri, "video/*"); 视频
     *
     * intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
     * intent.setDataAndType(uri, "application/x-chm"); CHM文件
     *
     * intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
     * intent.setDataAndType(uri, "application/vnd.android.package-archive"); apk文件
     *
     * intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
     * intent.setDataAndType(uri, "application/vnd.ms-powerpoint"); ppt
     *
     * intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
     * intent.setDataAndType(uri, "application/vnd.ms-excel"); excel文件
     *
     * intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
     * intent.setDataAndType(uri, "application/msword"); word文件
     *
     * 第二种 例子
     * intent.setType(“image/*”);//选择图片
     * intent.setType(“audio/*”); //选择音频
     * intent.setType(“video/*”); //选择视频 （mp4 3gp 是android支持的视频格式）
     * intent.setType(“video/*;image/*”);//同时选择视频和图片
     *
     * MimeType 文件列表参考
     * Mozilla MDN Web Docs: https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Basics_of_HTTP/MIME_types/Common_types
     * 扩展名	文档类型	MIME 类型
     * .aac	    AAC audio	                                        audio/aac
     * .abw	    AbiWord document	                                application/x-abiword
     * .arc	    Archive document (multiple files embedded)	        application/x-freearc
     * .avi	    AVI: Audio Video Interleave	                        video/x-msvideo
     * .azw	    Amazon Kindle eBook format	                        application/vnd.amazon.ebook
     * .bin	    Any kind of binary data	                            application/octet-stream
     * .bmp	    Windows OS/2 Bitmap Graphics	                    image/bmp
     * .bz	    BZip archive	                                    application/x-bzip
     * .bz2	    BZip2 archive	                                    application/x-bzip2
     * .csh	    C-Shell script	                                    application/x-csh
     * .css	    Cascading Style Sheets (CSS)	                    text/css
     * .csv	    Comma-separated values (CSV)	                    text/csv
     * .doc	    Microsoft Word	                                    application/msword
     * .docx	Microsoft Word (OpenXML)	                        application/vnd.openxmlformats-officedocument.wordprocessingml.document
     * .eot	    MS Embedded OpenType fonts	                        application/vnd.ms-fontobject
     * .epub	Electronic publication (EPUB)	                    application/epub+zip
     * .gif	    Graphics Interchange Format (GIF)	                image/gif
     * .htm
     * .html	HyperText Markup Language (HTML)	                text/html
     * .ico	    Icon format	                                        image/vnd.microsoft.icon
     * .ics	    iCalendar format	                                text/calendar
     * .jar	    Java Archive (JAR)	                                application/java-archive
     * .jpeg
     * .jpg	    JPEG images	                                        image/jpeg
     * .js	    JavaScript	                                        text/javascript
     * .json	JSON format	                                        application/json
     * .jsonld	JSON-LD format	                                    application/ld+json
     * .mid
     * .midi	Musical Instrument Digital Interface (MIDI)	        audio/midi audio/x-midi
     * .mjs	    JavaScript module	                                text/javascript
     * .mp3	    MP3 audio	                                        audio/mpeg
     * .mpeg	MPEG Video	                                        video/mpeg
     * .mpkg	Apple Installer Package	                            application/vnd.apple.installer+xml
     * .odp	    OpenDocument presentation document	                application/vnd.oasis.opendocument.presentation
     * .ods	    OpenDocument spreadsheet document	                application/vnd.oasis.opendocument.spreadsheet
     * .odt	    OpenDocument text document	                        application/vnd.oasis.opendocument.text
     * .oga	    OGG audio	                                        audio/ogg
     * .ogv	    OGG video	                                        video/ogg
     * .ogx	    OGG	                                                application/ogg
     * .otf	    OpenType font	                                    font/otf
     * .png	    Portable Network Graphics	                        image/png
     * .pdf	    Adobe Portable Document Format (PDF)	            application/pdf
     * .ppt	    Microsoft PowerPoint	                            application/vnd.ms-powerpoint
     * .pptx	Microsoft PowerPoint (OpenXML)	                    application/vnd.openxmlformats-officedocument.presentationml.presentation
     * .rar	    RAR archive	                                        application/x-rar-compressed
     * .rtf	    Rich Text Format (RTF)	                            application/rtf
     * .sh	    Bourne shell script	                                application/x-sh
     * .svg	    Scalable Vector Graphics (SVG)	                    image/svg+xml
     * .swf	    Small web format (SWF) or Adobe Flash document	    application/x-shockwave-flash
     * .tar	    Tape Archive (TAR)	                                application/x-tar
     * .tif
     * .tiff	Tagged Image File Format (TIFF)	                    image/tiff
     * .ttf	    TrueType Font	                                    font/ttf
     * .txt	    Text, (generally ASCII or ISO 8859-n)	            text/plain
     * .vsd	    Microsoft Visio	                                    application/vnd.visio
     * .wav	    Waveform Audio Format	                            audio/wav
     * .weba	WEBM audio	                                        audio/webm
     * .webm	WEBM video	                                        video/webm
     * .webp	WEBP image	                                        image/webp
     * .woff	Web Open Font Format (WOFF)	                        font/woff
     * .woff2	Web Open Font Format (WOFF)	                        font/woff2
     * .xhtml	XHTML	                                            application/xhtml+xml
     * .xls	    Microsoft Excel	                                    application/vnd.ms-excel
     * .xlsx	Microsoft Excel (OpenXML)	                        application/vnd.openxmlformats-officedocument.spreadsheetml.sheet
     * .xml	    XML	                                                application/xml 代码对普通用户来说不可读 (RFC 3023, section 3)
     * text/xml 代码对普通用户来说可读 (RFC 3023, section 3)
     * .xul	    XUL	                                                application/vnd.mozilla.xul+xml
     * .zip	    ZIP archive	                                        application/zip
     * .3gp	    3GPP audio/video container	                        video/3gpp
     * audio/3gpp（若不含视频）
     * .3g2	    3GPP2 audio/video container	                        video/3gpp2
     * audio/3gpp2（若不含视频）
     * .7z	    7-zip archive	                                    application/x-7z-compressed
     *
     * @param activity
     * @param requestCode onActivityResult返回值
     * @param mimetype 设置类型，我这里是任意类型，任意后缀的可以这样写。
     */
    public void openFolder(Activity activity, int requestCode, MIMETYPE mimetype){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(getMimetype(mimetype));
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        activity.startActivityForResult(intent,requestCode);
    }

    /**
     * 选择文件夹
     * @param activity
     * @param requestCode
     */
    public void openFolder(Activity activity, int requestCode){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 只显示所指定类型的文件
     * @param mimetype
     * @return
     */
    private String getMimetype(MIMETYPE mimetype){
        String tmp;
        switch (mimetype){
            case AUDIO:
                tmp = "audio/*";
                break;
            case VIDEO:
                tmp = "video/*";
                break;
            case IMAGE:
                tmp = "image/*";
                break;
            case TEXT:
                tmp = "text/*";
                break;
            default:
                tmp = "*";
        }
        return tmp;
    }

    /**
     * 读取raw文件后执行最后一步while循环任务,在循环中创建临时变量和执行所需任务
     * @param context
     * @param rawRes raw 资源
     * @param charsetName 字体格式
     * @param onActionReadFile 需要执行的任务
     */
    public void readRawFile(Context context, int rawRes, String charsetName, OnActionReadFile onActionReadFile){
        InputStream inputStream = context.getResources().openRawResource(rawRes);
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream,charsetName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        onActionReadFile.action(bufferedReader);
    }

    /**
     * 读取asset文件后执行最后一步while循环任务,在循环中创建临时变量和执行所需任务
     * @param context
     * @param fileName asset资源文件全名称与格式
     * @param charsetName 字体格式
     * @param onActionReadFile 需要执行的任务
     */
    public void readAssetFile(Context context, String fileName, String charsetName, OnActionReadFile onActionReadFile){
        InputStream inputStream = null;
        try {
            inputStream = context.getResources().getAssets().open(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream,charsetName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        onActionReadFile.action(bufferedReader);
    }

    /**
     * 执行while循环任务的接口
     */
    public interface OnActionReadFile{
        //已经读取文件流,剩下执行while循环的任务
        void action(BufferedReader bufferedReaderReadLine);
    }
}