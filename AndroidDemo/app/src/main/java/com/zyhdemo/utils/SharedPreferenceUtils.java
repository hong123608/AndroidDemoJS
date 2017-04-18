package com.zyhdemo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by zyh on 2017/4/17.
 */

public class SharedPreferenceUtils {
    private static SharedPreferenceUtils SharedPreferenceUtilsInstace;

    private SharedPreferenceUtils() {
    }

    public static SharedPreferenceUtils getInstance() {
        if (SharedPreferenceUtilsInstace == null) {
            synchronized (SharedPreferenceUtils.class) {
                if (SharedPreferenceUtilsInstace == null) {
                    SharedPreferenceUtilsInstace = new SharedPreferenceUtils();
                }
            }
        }
        return SharedPreferenceUtilsInstace;
    }

    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;

    private void initSharedPreferences(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences("zyhtest", Activity.MODE_PRIVATE);
        }
        if (editor == null) {
            editor = sharedPreferences.edit();
        }
    }
}