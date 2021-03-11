package com.houlik.libhoulik.android.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;

/**
 * 分享 / 测试中
 * Created by Houlik on 2017-11-30.
 */

public class SharedUtils {

    private Context context;
    //纯文本
    private String TEXT_PLAIN = "text/plain";
    //HTML文档
    private String TEXT_HTML = "text/html";
    //XHTML文档
    private String APP_XHTML = "application/xhtml";
    //GIF 图像
    private String IMAGE_GIF = "image/gif";
    //JPEG图像 [PHP中为：image/pjpeg]
    private String IMAGE_JPEG = "image/jpeg";
    //PNG图像 [PHP中为：image/x-png]
    private String IMAGE_PNG = "image/png";
    //MPEG 动画
    private String VIDEO_MPEG = "video/mpeg";
    //任意的二进制数据
    private String APP_OCTET_STREAM = "application/octet-stream";
    //PDF 文档
    private String APP_PDF = "application/pdf";
    //Microsoft Word 文件
    private String APP_MSWORD = "application/msword";
    //RFC 822形式
    private String MESSAGE_RFC822 = "message/rfc822";
    //HTML邮件的HTML形式和纯文本形式，相同内容使用不同形式表示
    private String MULTIPART_ALTERNATIVE = "multipart/alternative";
    //使用HTTP的POST方法提交的表单
    private String APP_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
    //同上，但主要用于表单提交时伴随文件上传的场合
    private String MULTIPART_FORM_DATA = "multipart/form-data";


    public enum MIME_TYPE {TEXT_PLAIN,TEXT_HTML,APP_XHTML,IMAGE_GIF,IMAGE_JPEG,IMAGE_PNG,VIDEO_MPEG,APP_OCTET_STREAM,
        APP_PDF,APP_MSWORD,MESSAGE_RFC822,MULTIPART_ALTERNATIVE,APP_X_WWW_FORM_URLENCODED,MULTIPART_FORM_DATA}

    public SharedUtils(Context context){
        this.context = context;
    }

    //得到正确的Mime值
    private String getMimeType(MIME_TYPE type){
        switch (type){
            case TEXT_PLAIN:
                return TEXT_PLAIN;
            case TEXT_HTML:
                return TEXT_HTML;
            case APP_XHTML:
                return APP_XHTML;
            case IMAGE_GIF:
                return IMAGE_GIF;
            case IMAGE_JPEG:
                return IMAGE_JPEG;
            case IMAGE_PNG:
                return IMAGE_PNG;
            case VIDEO_MPEG:
                return VIDEO_MPEG;
            case APP_OCTET_STREAM:
                return APP_OCTET_STREAM;
            case APP_PDF:
                return APP_PDF;
            case APP_MSWORD:
                return APP_MSWORD;
            case MESSAGE_RFC822:
                return MESSAGE_RFC822;
            case MULTIPART_ALTERNATIVE:
                return MULTIPART_ALTERNATIVE;
            case APP_X_WWW_FORM_URLENCODED:
                return APP_X_WWW_FORM_URLENCODED;
            case MULTIPART_FORM_DATA:
                return MULTIPART_FORM_DATA;
        }
        return null;
    }

    /**
     * 得到图片的完整路径
     * @param resourceID
     * @return
     */
    private String getResourcesUri(int resourceID){
        Resources resource = context.getResources();
        String uriPath = ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                resource.getResourcePackageName(resourceID) + "/" +
                resource.getResourceTypeName(resourceID) + "/" +
                resource.getResourceEntryName(resourceID);
        return  uriPath;
    }


    /**
     * 仅图片全格式分享
     * @param context
     * @param filePath 图片路径 FileUtils.getInstance().getSDPath()+ 文件名称➕格式
     * @param shareTitle
     */
    public void shareImage2App(Context context, String filePath, String shareTitle){
        Intent shareIntent=new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, filePath);
        context.startActivity(Intent.createChooser(shareIntent,shareTitle));
    }


    /**
     * 还在测试中
     * 分享流的工具,分享文本不需要得到相关文件路径
     * @param resourceID
     * @param type
     * @param subject
     * @param content 内容
     * @param title
     */
    public void sharedApp(int resourceID, MIME_TYPE type, String subject, String content, String title) {
        if(resourceID == 0) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType(getMimeType(type));
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            shareIntent.putExtra(Intent.EXTRA_TEXT, content);
            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(Intent.createChooser(shareIntent, title));
        }else{
            String path = getResourcesUri(resourceID);
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType(getMimeType(type));
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(path));
            context.startActivity(Intent.createChooser(shareIntent, title));
        }
    }
}
