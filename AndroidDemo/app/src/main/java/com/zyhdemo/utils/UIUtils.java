package com.zyhdemo.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by zyh on 2017/4/18.
 */

public class UIUtils {
    public static void showToastShort(Context mContext, String message) {
        if (message != null) {
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        }
    }

    public static void showToastLong(Context mContext, String message) {
        if (message != null) {
            Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
        }
    }
    /**
     * 显示dialog这块
     */
}
