package com.zyhdemo.common;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import java.io.File;

/**
 * Created by zyh on 2017/4/17.
 * 配置类
 */

public class CommonConfig {

    private static CommonConfig me;

    public static CommonConfig instance() {
        return me;
    }

    public CommonConfig() {
        me = this;
    }

    public CommonConfig(Context context) {
        me = this;
        init(context);

    }

    /*
         * 手机信息
         */
    public static String PHONE_MODEL = Build.MODEL; // 手机型号
    public static String PHONE_BRAND = Build.BRAND; // 手机厂商
    public static String PHONE_SN; // 手机唯一码
    public static String PHONE_ID;// 手机序列号
    public static String PHONE_RESOLUTION;// 屏幕分辨率
    public static float PHONE_DENSITY;
    public static String VERSION = "1.0"; // 接口版本
    public static String PHONE_SYSTEM;// 手机操作系统
    public static int SDK_INT = Build.VERSION.SDK_INT; // 系统版本号
    public static DisplayMetrics DM;
    private static String PATH = "";
    private static String CACHE = "";
    public static String FILE_ROOT = "";// 文件服务器地址
    //测试模式
    public static boolean isDebug=true;
    private void init(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
                && Environment.getExternalStorageDirectory().canWrite()) {
            // 有访问SDCARD
            File sdCardDir = Environment.getExternalStorageDirectory();
            PATH = sdCardDir.getPath() + "/ZYHTEST/";
        } else {
            PATH = "/ZYHTEST/";
        }
        CACHE = PATH + "/cache/";
        initPath();
//        loadPhoneInfo(context);
//        initSharedPreferences(context);
    }

    /**
     * 实例化路径
     */
    private static void initPath() {
        // 创建资源根路径
        File file = new File(PATH);
        if (!file.exists()) {
            file.mkdirs();
        }

        File cache = new File(CACHE);
        if (!cache.exists()) {
            cache.mkdirs();
        }
    }

    /**
     * 加载手机相关信息
     */
    private static void loadPhoneInfo(Context context) {
        getPhoneSN(context);
        DM = context.getResources().getDisplayMetrics();
        PHONE_DENSITY = DM.density;
        PHONE_RESOLUTION = DM.widthPixels + " " + DM.heightPixels;

    }

    /**
     * 获取PhoneSN
     *
     * @param
     * @return PhoneSN
     */
    public static String getPhoneSN(Context context) {
        if (null == PHONE_SN || "".equals(PHONE_SN)) {
            TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            PHONE_ID = manager.getDeviceId();// 手机序列号
            PHONE_SYSTEM = Settings.System.getString(context.getApplicationContext().getContentResolver(),
                    Settings.System.ANDROID_ID);// 系统序列号
            PHONE_SN = PHONE_ID + PHONE_SYSTEM;// 设备唯一码
        }
        return PHONE_SN;

    }

    public String getAppPath() {
        return PATH;
    }

    public String getAppCachePath() {
        return CACHE;
    }

}
