package com.houlik.libhoulik.android.util;

//import org.apache.http.util.EncodingUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 普通文件存储,文件管理工具
 * Created by Houlik on 24/11/2016.
 */
public class IOUtils {


    private static IOUtils IOUtils = new IOUtils();

    private IOUtils(){}

    //单一模式
    public static IOUtils getInstance(){
        if(IOUtils == null){
            new IOUtils();
        }
        return IOUtils;
    }

    /**
     * 写入字符数据到文件
     * @param SDPath 路径
     * @param fileName 文件名
     * @param data 内容
     * @param isAppend 是为连接，否为覆盖
     */
    public void writeString2File(String SDPath, String fileName, String data,boolean isAppend){
        BufferedWriter  bufferedWriter = null;
        try{
            if(isAppend) {
                bufferedWriter = new BufferedWriter(new FileWriter(SDPath + "/" + fileName, isAppend));
                bufferedWriter.write(data);
                bufferedWriter.close();
            }else {
                bufferedWriter = new BufferedWriter(new FileWriter(SDPath+ "/" +fileName));
                bufferedWriter.write(data);
                bufferedWriter.newLine();
                bufferedWriter.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 读取文件返回字节
     * @param SDPath
     * @param fileName
     * @return
     */
    public byte[] readFile(String SDPath, String fileName){
        File file = new File(SDPath + "/" + fileName);
        FileInputStream fileInputStream = null;
        BufferedInputStream bufferedInputStream = null;
        byte[] b = null;
        try {
            fileInputStream = new FileInputStream(file);
            bufferedInputStream = new BufferedInputStream(fileInputStream);
            b = new byte[bufferedInputStream.available()];
            int len;
            while ((len = bufferedInputStream.read(b))!= -1){
                bufferedInputStream.read(b, 0, len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return b;
    }

    /**
     * 这是查询指定的文件内容
     * @param SDPath
     * @param fileName
     * @param numbToCheck
     * @return 把答案保存到集合中返回
     */
    public ArrayList checkFileContext(String SDPath, String fileName, int...numbToCheck) {
        BufferedReader reader = null;
        String fileContext;
        StringBuffer stringBuffer = new StringBuffer();
        ArrayList list = new ArrayList();
        try {
            reader = new BufferedReader(new FileReader(SDPath + "/" + fileName));
            while ((fileContext = reader.readLine()) != null) {
                //使用StringBuffer把得到的每一段fileContext内容拼接后保存到StringBuffer中
                stringBuffer.append(fileContext);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //查询指定的内容
        for (int i = 0; i < numbToCheck.length; i++) {
            list.add(stringBuffer.charAt(numbToCheck[i]-1));
        }
        return list;
    }

    /**
     * 这是把文本中每一段的内容保存到集合中
     * @param SDPath
     * @param fileName
     * @return 把内容保存到集合中返回
     */
    public  ArrayList eachLineSet2List(final String SDPath, final String fileName) {
        BufferedReader reader = null;
        String fileContext;
        ArrayList list = new ArrayList();
        try {
            reader = new BufferedReader(new FileReader(SDPath + "/" + fileName));
            while ((fileContext = reader.readLine()) != null) {
                //临时变量
                String[] temp = fileContext.split(",");
                for (int i = 0; i < temp.length; i++) {
                    //保存到集合中
                    list.add(temp[i]);
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    //将一个InputStream里面的数据写入到SD卡中
    public File write2SDFromInput(String fileName,String SDPath,InputStream input){
        File file=null;
        OutputStream output=null;
        try {
            //创建目录
            FileUtils.getInstance().createSDDir(SDPath);
            //创建文件
            file= FileUtils.getInstance().createSDFile(SDPath, fileName);
            //写数据流
            output=new FileOutputStream(file);
            byte buffer[]=new byte[4*1024];//每次存4K
            int temp;
            //写入数据
            while((temp=input.read(buffer))!=-1){
                output.write(buffer,0,temp);
            }
            output.flush();
        } catch (Exception e) {
            System.out.println("写数据异常："+e);
        }
        finally{
            try {
                output.close();
            } catch (Exception e2) {
                System.out.println(e2);
            }
        }
        return file;
    }


    /**
     * 保存字节到SD卡
     * @param file
     * @param bytes
     */
    public void setByte2SD(File file, byte[] bytes){
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断文件类型
     * @param file
     * @return
     */
    public String getMIMEType(File file) {
        String type = "";
        String fName = file.getName();
      /* 取得扩展名 */
        String end = fName.substring(fName.lastIndexOf("."), fName.length()).toLowerCase();

      /* 依扩展名的类型决定MimeType */
        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") || end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            type = "audio";
        } else if (end.equals("3gp") || end.equals("mp4")) {
            type = "video";
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg") || end.equals("bmp")) {
            type = "image";
        } else if (end.equals("apk")) {
        /* android.permission.INSTALL_PACKAGES */
            type = "application/vnd.android.package-archive";
        } else if (end.equals("txt") || end.equals("java")) {
        /* android.permission.INSTALL_PACKAGES */
            type = "text";
        } else {
            type = "*";
        }
      /*如果无法直接打开，就跳出软件列表给用户选择 */
        if (end.equals("apk")) {
        } else {
            type = type + "/*";
        }
        return type;
    }

    /**
     * 得到当前url路径的所有文件保存到集合中
     * @param url
     * @return
     */
    public List<?> listFile(String url){
        List<Object> list = new ArrayList();
        File file = new File(url);
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            list.add(files[i]);
        }
        return list;
    }
}
