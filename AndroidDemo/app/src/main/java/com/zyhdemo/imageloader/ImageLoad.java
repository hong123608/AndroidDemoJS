package com.zyhdemo.imageloader;

import java.io.File;
import java.net.URI;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;

/**
 * 类名：ImageLoad.java<br>
 * 描述： ImageLoad图片加载类<br>
 * 创建者： jack<br>
 * 创建日期：2014-12-30<br>
 * 版本： <br>
 * 修改者： <br>
 * 修改日期：<br>
 */
public class ImageLoad {

    private static ImageLoader imageLoader = ImageLoader.getInstance();

    public static ImageLoader getImageLoader() {
        return imageLoader;
    }

    public static boolean checkImageLoader() {
        return imageLoader.isInited();
    }

    /**
     * 功能描述： 控件图片地址绑定加载显示<br>
     * 创建者： jack<br>
     * 创建日期：2015-3-31<br>
     *
     * @param uri                  图片地址
     * @param rotateImageViewAware 显示控件
     * @param default_pic          默认显示图片
     */
    public static void disPlay(String uri, ImageAware rotateImageViewAware, int default_pic) {
        DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(default_pic)
                .showImageForEmptyUri(default_pic).showImageOnFail(default_pic).cacheInMemory(true).cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565).displayer(new SimpleBitmapDisplayer()).build();

        imageLoader.displayImage(uri, rotateImageViewAware, options);
    }

    /**
     * 功能描述： 控件图片地址绑定加载显示<br>
     * 创建者： jack<br>
     * 创建日期：2015-3-31<br>
     *
     * @param uri        资源地址
     * @param imageView  显示控件
     * @param defaultImg 默认图片
     */
    public static void disPlay(String uri, ImageView imageView, int defaultImg) {
        disPlay(uri, imageView, defaultImg, defaultImg);
    }

    /**
     * 功能描述： 控件图片地址绑定加载显示<br>
     * 创建者： jack<br>
     * 创建日期：2015-3-31<br>
     *
     * @param uri        资源地址
     * @param imageView  显示控件
     * @param loadingImg 加载中图片
     * @param defaultImg 默认图片
     */
    public static void disPlay(String uri, ImageView imageView, int loadingImg, int defaultImg) {
        DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(loadingImg)
                .showImageForEmptyUri(defaultImg).showImageOnFail(defaultImg).cacheInMemory(true).cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565).displayer(new SimpleBitmapDisplayer()).build();
        imageLoader.displayImage(uri, imageView, options);
    }

    /**
     * 功能描述： <br>
     * 创建者： jack<br>
     * 创建日期：2015-4-2<br>
     *
     * @param uri       图片地址
     * @param imageview 图片显示对象
     * @param listener  加载监听
     */
    public static void disPaly(String uri, ImageView imageview, ImageLoadingListener listener) {
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565).displayer(new SimpleBitmapDisplayer()).build();
        imageLoader.displayImage(uri, imageview, options, listener);
    }

    /**
     * 功能描述： <br>
     * 创建者： jack<br>
     * 创建日期：2015-4-2<br>
     *
     * @param uri              图片地址
     * @param imageview        图片显示对象
     * @param listener         加载监听
     * @param progressListener 加载进度监听
     */
    public static void disPaly(String uri, ImageView imageview, ImageLoadingListener listener,
                               ImageLoadingProgressListener progressListener) {
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565).displayer(new SimpleBitmapDisplayer()).build();
        imageLoader.displayImage(uri, imageview, options, listener, progressListener);
    }

    /*
     * 清除内存和磁盘上的缓存
     */
    public static void clear() {
        imageLoader.clearMemoryCache();
        imageLoader.clearDiscCache();
    }

    public static void resume() {
        imageLoader.resume();
    }

    /**
     * 暂停加载
     */
    public static void pause() {
        imageLoader.pause();
    }

    /**
     * 停止加载
     */
    public static void stop() {
        imageLoader.stop();
    }

    /**
     * 销毁加载
     */
    public static void destroy() {
        imageLoader.destroy();
    }

    /**
     * 功能描述： 根据资源路径加载缓存图片<br>
     * 创建者： jack<br>
     * 创建日期：2015-3-31<br>
     *
     * @param
     */
    public static Bitmap getBitmapByPath(String path) {

        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        return imageLoader.loadImageSync(path, options);
        // Bitmap bt = null;
        // if (path.startsWith("http:")) {
        // File f = imageLoader.getDiscCache().get(path);
        // if (f.exists()) {
        // try {
        // bt = BitmapFactory.decodeFile(f.getAbsolutePath());
        // } catch (OutOfMemoryError e) {
        //
        // }
        // } else {
        // bt = imageLoader.loadImageSync(path);
        // }
        // } else if (path.startsWith("file:")) {
        // File f = new File(URI.create(path));
        // if (f.exists()) {
        // try {
        // bt = BitmapFactory.decodeFile(f.getAbsolutePath());
        // } catch (OutOfMemoryError e) {
        // }
        // }
        // }
        // return bt;
    }
}
