package com.houlik.libhoulik.hl3api;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.nostra13.universalimageloader.cache.memory.MemoryCache;

import java.util.Collection;

/**
 * 利用第三方框架缓存图片 - 未测试
 * @author houlik
 * @since 2020/11/20
 */
public class BaseMemoryCache implements MemoryCache {

    private LruCache<String, Bitmap> mMemeryCache;
    private final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    private final int cacheSize = maxMemory / 4;
    private static BaseMemoryCache baseMemoryCache = new BaseMemoryCache();

    private BaseMemoryCache(){
        //图片的内存缓存,key为图片的uri,值为图片本身
        mMemeryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
            }
        };
    }

    public static BaseMemoryCache getInstance(){
        if(baseMemoryCache == null){
            return new BaseMemoryCache();
        }
        return baseMemoryCache;
    }

    @Override
    public boolean put(String s, Bitmap bitmap) {
        mMemeryCache.put(s, bitmap);
        return false;
    }

    @Override
    public Bitmap get(String s) {
        mMemeryCache.get(s);
        return null;
    }

    @Override
    public Bitmap remove(String s) {
        mMemeryCache.remove(s);
        return null;
    }

    @Override
    public Collection<String> keys() {
        return null;
    }

    @Override
    public void clear() {}
}
