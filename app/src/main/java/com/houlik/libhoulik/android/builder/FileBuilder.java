package com.houlik.libhoulik.android.builder;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/**
 * 这是一对主文件夹 对一文件夹 对多文件
 * 通过Builder简单设置自动创建主文件夹，文件夹，文件
 * Created by houlik on 2018/11/12.
 */

public class FileBuilder {

    //根目录
    private String dirPath;

    //7.0使用
    //private String AbsoluteSDPath = Environment.getExternalStorageDirectory().getAbsolutePath();

    private Context context;
    private String[] sdFolder = null;
    private String dirFolder = null;
    private String[] file = null;
    private boolean atSDCard = false;

    private FileBuilder(Builder builder) {
        dirPath = builder.context.getExternalFilesDir(null).toString();
        this.context = builder.context;
        this.sdFolder = builder.sdFolder;
        this.dirFolder = builder.dirFolder;
        this.file = builder.file;
        this.atSDCard = builder.atSDCard;

        create();
    }

    /**
     * 如果目录不存在就创建新目录
     *
     * @param folder 文件夹
     * @return
     */
    private void createSDSingleFolder(String folder) {
        File dir = new File(folder);
        if (folder != null) {
            //如果目录不存在就创建
            if (!dir.exists()) {
                dir.mkdir();
            }
        }
    }

    /**
     * 判断目录是否已经存在
     *
     * @param folder 完整文件夹路径
     * @return
     */
    private boolean isFolderExist(String folder) {
        File dir = new File(folder);
        if (folder != null) {
            //如果文件夹已经存在
            if (dir.exists()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 如果文件不存在就创建新文件，前提是目录保存是必须存在
     *
     * @param folder   完整文件夹路径
     * @param fileName
     * @return
     */
    private void createSDSingleFile(String folder, String fileName) {
        java.io.File file = new java.io.File(folder + "/" + fileName);
        if (folder != null && fileName != null) {
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 判断文件是否存在
     *
     * @param folder   完整文件夹路径
     * @param fileName
     * @return
     */
    private boolean isFileExist(String folder, String fileName) {
        java.io.File file = new java.io.File(folder + "/" + fileName);
        return file.exists();
    }

    /**
     * 如果SD卡无插入时返回true
     *
     * @return
     */
    private boolean isSDEmpty() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_REMOVED)) {
            return true;
        }
        return false;
    }

    private void create() {
        //如果是保存在SD卡
        if (atSDCard) {
            //如果SD卡不存在
            if (isSDEmpty()) {
                Toast.makeText(context, "请插入SD卡", Toast.LENGTH_LONG).show();
            } else {
                //如果主文件夹不存在
                if (!isFolderExist(dirPath + "/" + dirFolder)) {
                    //创建主文件夹
                    createSDSingleFolder(dirPath + "/" + dirFolder);
                    for (int i = 0; i < sdFolder.length; i++) {
                        //如果根目录下主文件夹下文件夹不存在
                        if (!isFolderExist(dirPath + "/" + dirFolder + "/" + sdFolder[i])) {
                            //创建
                            createSDSingleFolder(dirPath + "/" + dirFolder + "/" + sdFolder[i]);

                            for (int j = 0; j < file.length; j++) {
                                //如果根目录下主文件夹下文件夹下文件不存在
                                if (!isFileExist(dirPath + "/" + dirFolder + "/" + sdFolder[i], file[j])) {
                                    createSDSingleFile(dirPath + "/" + dirFolder + "/" + sdFolder[i] , file[j]);
                                }
                            }
                            //如果根目录下主文件夹下文件夹存在
                        } else {
                            for (int j = 0; j < file.length; j++) {
                                //如果根目录下主文件夹下文件夹下文件不存在
                                if (!isFileExist(dirPath + "/" + dirFolder + "/" + sdFolder[i], file[j])) {
                                    createSDSingleFile(dirPath + "/" + dirFolder + "/" + sdFolder[i] , file[j]);
                                } else {
                                    Toast.makeText(context, "文件已经存在", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }
                    //如果主文件夹存在
                }else{
                    for (int i = 0; i < sdFolder.length; i++) {
                        //如果根目录下主文件夹下文件夹不存在
                        if (!isFolderExist(dirPath + "/" + dirFolder + "/" + sdFolder[i])) {
                            //创建
                            createSDSingleFolder(dirPath + "/" + dirFolder + "/" + sdFolder[i]);
                            for (int j = 0; j < file.length; j++) {
                                //如果根目录下主文件夹下文件夹下文件不存在
                                if (!isFileExist(dirPath + "/" + dirFolder + "/" + sdFolder[i], file[j])) {
                                    createSDSingleFile(dirPath + "/" + dirFolder + "/" + sdFolder[i], file[j]);
                                }
                            }
                            //如果根目录下主文件夹下文件夹存在
                        } else {
                            for (int j = 0; j < file.length; j++) {
                                //如果根目录下主文件夹下文件夹下文件不存在
                                if (!isFileExist(dirPath + "/" + dirFolder + "/" + sdFolder[i], file[j])) {
                                    createSDSingleFile(dirPath + "/" + dirFolder + "/" + sdFolder[i], file[j]);
                                } else {
                                    Toast.makeText(context, "文件已经存在", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }
                }
            }
        } else {

        }
    }

    public static class Builder {
        private Context context;
        private String[] sdFolder;
        private String dirFolder;
        private String[] file;
        private boolean atSDCard;

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        /**
         * 目录主文件夹
         *
         * @param dirFolder
         * @return
         */
        public Builder setDirFolder(String dirFolder) {
            this.dirFolder = dirFolder;
            return this;
        }

        /**
         * 目录主文件夹下的文件夹
         *
         * @param sdFolder
         * @return
         */
        public Builder setFolder(String[] sdFolder) {
            this.sdFolder = sdFolder;
            return this;
        }

        /**
         * 目录文件夹下的文件夹文件
         *
         * @param file
         * @return
         */
        public Builder setFile(String[] file) {
            this.file = file;
            return this;
        }

        /**
         * 是否保存在SD卡
         *
         * @param atSDCard
         * @return
         */
        public Builder setAtSDCard(boolean atSDCard) {
            this.atSDCard = atSDCard;
            return this;
        }

        /**
         * 开始执行创建
         *
         * @return
         */
        public FileBuilder process() {
            return new FileBuilder(this);
        }


    }
}
