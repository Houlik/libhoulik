package com.houlik.libhoulik.houlik.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * 通过KeyBuilder创建密钥以及存取密钥文件操作
 * 通过DataBuilder加密数据后保存以及读取解密后的数据
 * Created by houlik on 2018/12/1.
 */

public class EncryptionDecryption {
    private int[] key;
    private Random random;
    private String[] keyStr;
    private final int keySize = 128;
    private File keyFile;

    /**
     * 创建密码，如果key参数不为空则从外部文件获取密码记录
     * @param builder
     */
    private EncryptionDecryption(KeyBuilder builder){
        this.keyFile = builder.file;
    }

    /**
     * 制造随机数
     */
    private void buildRandomKey(){
        this.key = new int[keySize];
        this.random = new Random();
        this.keyStr = new String[keySize];
        for (int i = 0; i < keySize; i++) {
            key[i] = random.nextInt(keySize);
            keyStr[i] = String.valueOf(key[i]);
        }
    }

    /**
     * 保存密码至SD卡文件
     * @param file
     */
    private void saveKey2File(File file){
        DataOutputStream dataOutputStream = null;
        try {
            dataOutputStream = new DataOutputStream(new FileOutputStream(file));
            for (int i = 0; i < key.length; i++) {
                dataOutputStream.writeUTF(keyStr[i]);
                dataOutputStream.write("\n".getBytes());
            }
            dataOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 从SD卡文件读取密码,必须用此方法解密读取密码,对应保存密码的方法
     * @param file
     * @return
     */
    private String[] readKeyFromFile(File file){
        DataInputStream dataInputStream = null;
        String[] tmpStr;
        String[] newStr = new String[keySize];
        try {
            dataInputStream = new DataInputStream(new FileInputStream(file));
            int len = dataInputStream.available();
            byte[] bytes = new byte[len];
            dataInputStream.read(bytes);
            String str = new String(bytes,"UTF-8");
            tmpStr = str.split("\n");
            for (int i = 0; i < tmpStr.length; i++) {
                newStr[i] = tmpStr[i].trim();
            }
            dataInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newStr;
    }

    /**
     * 按照余数摘选密钥,用于加密时使用
     * @param index
     * @return
     */
    private static int encryptionDecryption(int index){
        return index % 128;
    }

    /**
     * 存储密钥
     */
    public void createKey(){
        buildRandomKey();
        saveKey2File(this.keyFile);
    }

    /**
     * 从SD卡文件读取密码
     * @return
     */
    public String[] readKey(){
        return readKeyFromFile(this.keyFile);
    }

    /**
     * 存取加密密钥
     */
    public static class KeyBuilder{
        private File file;

        /**
         * 密钥存取文件路径
         * @param file
         * @return
         */
        public KeyBuilder filePath(File file){
            this.file = file;
            return this;
        }

        /**
         * 开始执行
         * @return
         */
        public EncryptionDecryption process(){
            return new EncryptionDecryption(this);
        }
    }

    /**
     * 存取加解密数据
     */
    public static class DataBuilder{

        /**
         * 数据加密后保存到文件中
         * @param data 加密数据
         * @param files 文件保存路径
         * @param keyStr 加密密钥
         */
        public void encryption(String data, File files, String[] keyStr){
            //把字符串数据转字节
            byte[] bytes = data.getBytes();
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(files);
                for (int i = 0; i < bytes.length; i++){
                    fileOutputStream.write(bytes[i] + Integer.parseInt(keyStr[encryptionDecryption(i)]));
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * 先从文件中获取解密的钥匙,再解被加密的文件数据
         * @param files 数据文件路径
         * @param keyStr 传入获取到的字符串数组密码
         * @return
         */
        public char[] decryption(File files, String[] keyStr) {
            FileInputStream fileInputStream = null;
            int len = 0;
            char[] ret = null;
            try {
                fileInputStream = new FileInputStream(files);
                len = fileInputStream.available();
                ret = new char[len];
                for (int i = 0; i < len; i++) {
                    ret[i] = (char)(fileInputStream.read() - Integer.parseInt(keyStr[i]));
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return ret;
        }
    }
}
