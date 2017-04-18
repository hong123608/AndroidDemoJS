package com.zyhdemo.network;

import com.android.volley.toolbox.ImageLoader.ImageCache;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * 类名：BitmapLruCache.java<br>
 * 描述： 图片缓存类<br>
 * 创建者： jack<br>
 * 创建日期：2014-11-20<br>
 * 版本： <br>
 * 修改者： <br>
 * 修改日期：<br>
 */
public class BitmapLruCache extends LruCache<String, Bitmap> implements ImageCache {
    public BitmapLruCache(int maxSize) {
        super(maxSize);
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight();
    }

    @Override
    public Bitmap getBitmap(String url) {
        return get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        put(url, bitmap);
    }
}
