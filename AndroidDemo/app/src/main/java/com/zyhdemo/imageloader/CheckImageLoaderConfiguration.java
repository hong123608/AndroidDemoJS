package com.zyhdemo.imageloader;

import java.io.File;

import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.zyhdemo.common.CommonConfig;

/**
 * 类名：CheckImageLoaderConfiguration.java<br>
 * 描述： imageLoader默认设置<br>
 * 创建者： jack<br>
 * 创建日期：2014-12-29<br>
 * 版本： <br>
 * 修改者： <br>
 * 修改日期：<br>
 */
public class CheckImageLoaderConfiguration {

    private static final long discCacheLimitTime = 3600 * 24 * 15L;

    public static void checkImageLoaderConfiguration(Context context) {
        if (!ImageLoad.checkImageLoader()) {
            // This configuration tuning is custom. You can tune every option,
            // you may tune some of them,
            // or you can create default configuration by
            // ImageLoaderConfiguration.createDefault(this);
            // method.
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                    .memoryCacheExtraOptions(220, 308).threadPriority(Thread.NORM_PRIORITY)
                    .denyCacheImageMultipleSizesInMemory().discCacheFileNameGenerator(new Md5FileNameGenerator())
                    .discCache(new UnlimitedDiscCache(new File(CommonConfig.instance().getAppCachePath())))
                    .tasksProcessingOrder(QueueProcessingType.LIFO).build();
            // .discCache(new
            // LimitedAgeDiscCache(StorageUtils.getCacheDirectory(context),new
            // Md5FileNameGenerator(), discCacheLimitTime))
            // Initialize ImageLoader with configuration.
            ImageLoad.getImageLoader().init(config);
        }
    }
}
