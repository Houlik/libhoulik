package com.houlik.libhoulik.android.util;

import java.io.File;

/**
 * Created by Houlik on 2017-11-13.
 */

public class PathUtils {

    private static PathUtils pathUtils = new PathUtils();

    private PathUtils(){}

    public static PathUtils getInstance(){
        if(pathUtils == null){
            new PathUtils();
        }
        return pathUtils;
    }

    //根路径 /storage
    private static final String ENV_ANDROID_STORAGE = "ANDROID_STORAGE";
    private static final File DIR_ANDROID_STORAGE = getDirectory(ENV_ANDROID_STORAGE, "/storage");
    private static File getDirectory(String variableName, String defaultPath) {
        String path = System.getenv(variableName);
        return path == null ? new File(defaultPath) : new File(path);
    }

    /**
     * 得到根路径
     * @return
     */
    public File getStorageDirectory() {
        return DIR_ANDROID_STORAGE;
    }
}
