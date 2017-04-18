package com.zyhdemo.utils;

import android.util.Log;

import com.zyhdemo.common.CommonConfig;


public class L {

    public static boolean FLAG = CommonConfig.isDebug;

    public static void d(String tag, String msg) {
        if (FLAG)
            Log.d(tag, msg);
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (FLAG)
            Log.d(tag, msg, tr);
    }

    public static void v(String tag, String msg) {
        if (FLAG)
            Log.v(tag, msg);
    }

    public static void v(String tag, String msg, Throwable tr) {
        if (FLAG)
            Log.v(tag, msg, tr);
    }

    public static void i(String tag, String msg) {
        if (FLAG)
            Log.i(tag, msg);
    }

    public static void i(String tag, String msg, Throwable tr) {
        if (FLAG)
            Log.i(tag, msg, tr);
    }

    public static void i(String msg) {
        if (FLAG)
            Log.i("L", msg);
    }
    public static void e(String tag, String msg) {
        if (FLAG)
            Log.e(tag, msg);
    }
}
