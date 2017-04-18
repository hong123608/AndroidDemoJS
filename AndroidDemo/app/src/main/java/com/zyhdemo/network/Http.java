package com.zyhdemo.network;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;

import com.zyhdemo.common.CommonConfig;
import com.zyhdemo.exception.BaseException;
import com.zyhdemo.exception.HttpDataErrorException;
import com.zyhdemo.exception.HttpException;
import com.zyhdemo.exception.UnLoginException;
import com.zyhdemo.utils.StringUtils;
//import com.cliff.kernel.exception.BaseException;
//import com.cliff.kernel.exception.HttpDataErrorException;
//import com.cliff.kernel.exception.HttpException;
//import com.cliff.kernel.exception.UnLoginException;
//
//import com.cliff.kernel.network.FileUpload;
//import com.cliff.kernel.network.FileUpload.UploadListener;
//import com.cliff.kernel.network.NetRequest;
//import com.cliff.kernel.network.NetRequest.NetRequestListener;
//import com.cliff.kernel.network.StreamFileUpload;
//import com.cliff.kernel.util.StringUtils;
//import com.cliff.xgll.account.model.Account;
//import com.cliff.xgll.common.CommonConfig;
//import com.cliff.xgll.common.emoji.EmojiParser;

/**
 * 类名：Http.java<br>
 * 描述： 请求服务器端数据工具类<br>
 * 创建者： jack<br>
 * 创建日期：2015-4-16<br>
 * 版本： <br>
 * 修改者： <br>
 * 修改日期：<br>
 */
public class Http {
    public static final String TAG = Http.class.getSimpleName();

    /**
     * 编码
     */
    public static String encode(String str) {
        if (str == null)
            return "";
        try {
//            return URLEncoder.encode(EmojiParser.getInstance(null).parseEmoji(str), "UTF-8");
            return URLEncoder.encode(str, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String decode(String str) {
        if (str == null)
            return "";
        try {
            return URLDecoder.decode(str, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 将返回的数据流转换成字符串
     *
     * @param in 返回的数据流
     * @return 获得的字符串
     */
    public static String convertString(InputStream in, int len) {
        String responseBody = "";
        StringBuffer sb = new StringBuffer();
        try {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(in, HTTP.UTF_8), 1024);
                try {
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            responseBody = sb.toString();
        } catch (OutOfMemoryError e) {
            sb = new StringBuffer("");
        }

        return responseBody;
    }

    /**
     * 功能描述： 返回数据分析<br>
     * 创建者： jack<br>
     * 创建日期：2014-4-8<br>
     *
     * @param data
     * @return
     * @throws
     */
    public static String _analysData(String data) throws BaseException {
        try {
            if (StringUtils.isEmpty(data)) {
                throw new HttpDataErrorException();
            }
            JSONObject jsonObject = new JSONObject(data);
            int flag = jsonObject.getInt("flag");
            if (flag == 1) {//返回数据  成功与否判断

                if (CommonConfig.FILE_ROOT.equals("")) {//文件服务器地址
                    if (!jsonObject.isNull("path")) {
                        CommonConfig.FILE_ROOT = jsonObject.optString("path");
                    }
                }

                if (!jsonObject.isNull("data")) {
                    return jsonObject.getString("data");
                } else {
                    return null;
                }

            } else {
                if (!jsonObject.isNull("message")) {
                    throw new HttpException(true, jsonObject.getString("message"));
                } else {
                    throw new HttpDataErrorException();
                }
            }
        } catch (JSONException e) {
            throw new HttpDataErrorException();
        } catch (OutOfMemoryError ex) {
            throw new HttpDataErrorException();
        }
    }

    /**
     * 功能描述： 创建请求地址<br>
     * 创建者： jack<br>
     * 创建日期：2014-4-10<br>
     *
     * @param url   服务器地址
     * @param key   参数key
     * @param value 参数值
     * @return
     */
    public static String buildURL(String url, String[] key, String[] value) {
        StringBuffer sb = new StringBuffer(url);
        if (key != null && key.length > 0) {
            sb.append("?");
            for (int i = 0; i < key.length; i++) {
                sb.append(key[i]).append("=").append(value[i]);
                if (i < key.length - 1)
                    sb.append("&");
            }
        }
        return sb.toString();
    }

    // ****************************volley http********************************//

    /**
     * 功能描述：组织无用户信息的请求参数 <br>
     * 创建者： jack<br>
     * 创建日期：2015-4-16<br>
     *
     * @param keys
     * @param values
     * @return
     */
    public static Map<String, Object> buidParams(String[] keys, Object[] values) {
        Map<String, Object> params = new HashMap<String, Object>();
        if (keys != null && values != null) {
            for (int i = 0; i < keys.length; i++) {
                params.put(keys[i], values[i]);
            }
        }
        return addDefaultParams(params);
    }

    private static Map<String, Object> addDefaultParams(Map<String, Object> params) {
        if (params == null) {
            params = new HashMap<String, Object>();
        }
        //配置参数根据需要在添加
////        params.put("optTerminal", CommonConfig.TERMINAL_TYPE);  //终端类型,区别  安卓.苹果,浏览器..
//        params.put("terminalDevice", CommonConfig.PHONE_MODEL);
//        params.put("terminalFactory", CommonConfig.PHONE_BRAND);
//        params.put("terminalSn", CommonConfig.PHONE_SN);
////        params.put("browser", CommonConfig.BROWSER);
//        params.put("resolution", CommonConfig.PHONE_RESOLUTION);
//        params.put("version", CommonConfig.VERSION);
        return params;
    }

    /**
     * 功能描述： 组织带用户信息的请求参数<br>
     * 创建者： jack<br>
     * 创建日期：2015-4-16<br>
     *
     * @param keys
     * @param values
     * @return
     */
    public static Map<String, Object> buidParamsWithAccount(Context mContext, String[] keys, Object[] values) {
        Map<String, Object> params = buidParams(keys, values);
//        params.put("phone", Account.Instance(mContext).getmUserBean().getPhone());
//        params.put("password", Account.Instance(mContext).getmUserBean().getPass());
//        params.put("userName", encode(Account.Instance(mContext).getmUserBean().getUserName()));
        return params;
    }

    /**
     * 功能描述：异步post请求<br>
     * 创建者： jack<br>
     * 创建日期：2014-11-20<br>
     *
     * @param
     */
    public static void aSyncPost(Context context, Dialog dialog, String url, Map<String, Object> params,
                                 NetRequest.NetRequestListener listener) {
        NetRequest.request(context).setmDialog(dialog).setParams2(addDefaultParams(params)).byPost(url, listener);
    }

    /**
     * 功能描述：异步post请求 <br>
     * 创建者： jack<br>
     * 创建日期：2014-11-20<br>
     *
     * @param
     */
    public static void aSyncPost(Context context, Dialog dialog, String url, String[] keys, Object[] values,
                                 NetRequest.NetRequestListener listener) {
        aSyncPost(context, dialog, url, buidParams(keys, values), listener);
    }

    /**
     * 功能描述：需要用户post请求<br>
     * 创建者： jack<br>
     * 创建日期：2014-11-20<br>
     *
     * @param
     */
//    public static void aSyncPostWithAccount(Context context, Dialog dialog, String url, String[] keys, Object[] values,
//                                            NetRequest.NetRequestListener listener) throws BaseException {
//        if (!Account.Instance(context).isLogin()) {
//            throw new UnLoginException();
//        }
//        aSyncPost(context, dialog, url, buidParamsWithAccount(context, keys, values), listener);
//    }

    /**
     * 功能描述： 需要用户的文件流上传post请求<br>
     * 创建者： huangfei<br>
     * 创建日期：2015-5-14<br>
     *
     * @param
     */
//    public static void aSyncStreamPostWithAccount(Context context, String url, Map<String, Object> params,
//                                                  Map<String, InputStream> is, StreamFileUpload.UploadListener listener) throws BaseException {
//
//        String[] keys = null;
//        Object[] values = null;
//        if (params != null) {
//            keys = new String[params.size()];
//            values = new Object[params.size()];
//            int i = 0;
//            for (Map.Entry<String, Object> entry : params.entrySet()) {
//
//                keys[i] = entry.getKey();
//                values[i] = entry.getValue();
//                i++;
//            }
//        }
//
//        if (!Account.Instance(context).isLogin()) {
//            throw new UnLoginException();
//        }
//
//        new StreamFileUpload(url, buidParamsWithAccount(context, keys, values), is, listener).doIt();
//    }

    /**
     * 功能描述： 需要用户的文件上传post请求<br>
     * 创建者： huangfei<br>
     * 创建日期：2015-5-14<br>
     *
     * @param
     */
//    public static void aSyncFilePostWithAccount(Context context, String url, Map<String, Object> params,
//                                                Map<String, File> files, FileUpload.UploadListener listener) throws BaseException {
//
//        String[] keys = null;
//        Object[] values = null;
//        if (params != null) {
//            keys = new String[params.size()];
//            values = new Object[params.size()];
//            int i = 0;
//            for (Map.Entry<String, Object> entry : params.entrySet()) {
//
//                keys[i] = entry.getKey();
//                values[i] = entry.getValue();
//                i++;
//            }
//        }
//
//        if (!Account.Instance(context).isLogin()) {
//            throw new UnLoginException();
//        }
//
//        new FileUpload(url, buidParamsWithAccount(context, keys, values), files, listener).doIt();
//    }


    /**
     * 功能描述：需要用户post请求<br>
     * 创建者： lidongdong<br>
     * 创建日期：2014-11-20<br>
     *
     * @param
     */
//    public static void aSyncPostWithAccount(Context context, Dialog dialog, String url, Map<String, Object> params,
//                                            NetRequest.NetRequestListener listener) throws BaseException {
//        String[] keys = null;
//        Object[] values = null;
//        if (params != null) {
//            keys = new String[params.size()];
//            values = new Object[params.size()];
//            int i = 0;
//            for (Map.Entry<String, Object> entry : params.entrySet()) {
//
//                keys[i] = entry.getKey();
//                values[i] = entry.getValue();
//                i++;
//            }
//        }
//
//        if (!Account.Instance(context).isLogin()) {
//            throw new UnLoginException();
//        }
//        //L.i("DD",TAG+"====发送消息得到postjson==="+buidParamsWithAccount(context, keys, values).toString());
//        aSyncPost(context, dialog, url, buidParamsWithAccount(context, keys, values), listener);
//    }

    /**
     * 功能描述：异步get请求<br>
     * 创建者： jack<br>
     * 创建日期：2014-11-20<br>
     *
     * @param
     */
    public static void aSyncGet(Context context, Dialog dialog, String url, Map<String, Object> params,
                                NetRequest.NetRequestListener listener) {
        NetRequest.request(context).setmDialog(dialog).setParams2(addDefaultParams(params)).byGet(url, listener);
    }

    /**
     * 功能描述：异步get请求 <br>
     * 创建者： jack<br>
     * 创建日期：2014-11-20<br>
     *
     * @param
     */
    public static void aSyncGet(Context context, Dialog dialog, String url, String[] keys, Object[] values,
                                NetRequest.NetRequestListener listener) {
        aSyncGet(context, dialog, url, buidParams(keys, values), listener);
    }

    /**
     * 功能描述：需要用户get请求<br>
     * 创建者： jack<br>
     * 创建日期：2014-11-20<br>
     *
     * @param
     */
//    public static void aSyncGetWithAccount(Context context, Dialog dialog, String url, String[] keys, Object[] values,
//                                           NetRequest.NetRequestListener listener) throws BaseException {
//        if (!Account.Instance(context).isLogin()) {
//            throw new UnLoginException();
//        }
//        aSyncGet(context, dialog, url, buidParamsWithAccount(context, keys, values), listener);
//    }

}
