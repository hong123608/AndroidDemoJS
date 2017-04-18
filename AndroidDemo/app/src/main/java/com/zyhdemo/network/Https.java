package com.zyhdemo.network;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.Map;

/**
 * Created by zyh on 2017/4/17.
 */

public class Https {
    /**
     * get 异步请求
     *
     * @param url
     * @param params
     * @param callback
     */
    public static void aSyncGet(String url, Map<String, String> params, Callback callback) {
        //添加参数

        OkHttpUtils.get().url(url).params(params).build().execute(callback);

    }

    /**
     * get 异步请求
     *
     * @param url
     * @param callback
     */
    public static void aSyncGet(String url, Callback callback) {
        //添加参数
        OkHttpUtils.get().url(url).build().execute(callback);
    }

    /**
     * post 异步请求
     *
     * @param url
     * @param params
     * @param callback
     */
    public static void aSyncPost(String url, Map<String, String> params, Callback callback) {
        //添加参数

        OkHttpUtils
                .post()
                .url(url)
                .params(params)
                .build()
                .execute(callback);

    }

    /**
     * post 异步请求
     *
     * @param url
     * @param callback
     */
    public static void aSyncPost(String url, Callback callback) {
        //添加参数
        OkHttpUtils
                .post()
                .url(url)
                .build()
                .execute(callback);
    }

    /**
     * post  提交单张图片 异步请求
     *
     * @param url
     * @param callback
     */
//    public static void aSyncPost(String url, Map<String, String> params, Callback callback) {
//        OkHttpUtils.post()//
//                .addFile("mFile", "messenger_01.png", file)//
////                .addFile("mFile", "test1.txt", file2)//
//                .url(url)
//                .params(params)//
//                .build()//
//                .execute(callback);
//    }


}
