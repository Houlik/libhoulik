package com.houlik.libhoulik.android.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author : houlik
 * @since : 2020/12/30
 * email : houlik@126.com
 * 注释 : 压缩
 */
public class ZipUtils {

    private static ZipUtils newInstance = new ZipUtils();

    private ZipUtils(){}

    public static ZipUtils getInstance(){
        synchronized (ZipUtils.class){
            if(newInstance == null){
                return new ZipUtils();
            }
            return newInstance;
        }
    }

    /**
     * 压缩文件
     * @param path
     * @param zipPathName
     * @throws IOException
     */
    public void zipPath(String path, String zipPathName) throws IOException {
        ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(path));
        File file = new File(zipPathName);
        //如果是文件
        if(file.isFile()){
            ZipEntry zipEntry = new ZipEntry(zipPathName);
            FileInputStream fileInputStream = new FileInputStream(file);
            zipOutputStream.putNextEntry(zipEntry);
            int len;
            byte[] buffer = new byte[1024];
            while((len = fileInputStream.read(buffer)) != -1){
                zipOutputStream.write(buffer, 0, len);
            }
            zipOutputStream.closeEntry();
        }else{
            //如果是文件夹
            String[] fileList = file.list();
            //如果文件夹是空的
            if(fileList.length <= 0){
                ZipEntry zipEntry = new ZipEntry(zipPathName + File.separator);
                zipOutputStream.putNextEntry(zipEntry);
                zipOutputStream.closeEntry();
            }
            //如果文件夹里有文件
            for (int i = 0; i < fileList.length; i++) {
                zipPath(path, zipPathName);
            }
        }
    }
}
