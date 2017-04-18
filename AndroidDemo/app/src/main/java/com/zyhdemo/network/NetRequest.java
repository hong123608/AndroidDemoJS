package com.zyhdemo.network;

import java.io.File;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.text.TextUtils;
import android.widget.ImageView;

import com.android.volley.Cache.Entry;
import com.android.volley.Network;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.zyhdemo.R;
import com.zyhdemo.common.CommonConfig;
import com.zyhdemo.utils.L;

/**
 * 类名：NetRequest.java<br>
 * 描述：Volley网络请求类 <br>
 * 创建者： jack<br>
 * 创建日期：2014-11-20<br>
 * 版本： <br>
 * 修改者： <br>
 * 修改日期：<br>
 */
public class NetRequest {
    private static final String TAG = "GEEBOO";

    private static final int LOADINGIMAGE = R.mipmap.ic_launcher;//loading图片

    private static final int ERRORIMAGE = R.mipmap.ic_launcher;

    private static final int IMAGEMAXMEASURE = 750;// 图片最大尺寸,0则不限制

    private static String CachePath;// volley缓存目录

    private static RequestQueue mRequestQueue;// 主要的请求类

    private static ImageLoader mImageLoader;// 图片加载器

    private static BitmapLruCache mLruCache;// 图片缓存器

    private static ImageRequest mImageRequest;// 图片请求(一直修改)

    private Context mcontext;

    private Dialog mDialog;

    private Map<String, String> heads;// 提交头

    private Map<String, String> params;// 提交的参数

    private String mReuestTag;// 请求标示

    private String cookies;// 提交的cookies

    private int timeout = 20000;// volley超时时间默认25秒

    private NetRequestListener mListener;// 回调方法

    private JsonRequest request;// vollery自定义请求类

    public NetRequest(Context context) {
        mcontext = context;
        CachePath = CommonConfig.instance().getAppCachePath() + "/velloy/";
    }

    /**
     * 获取vollery自定义请求的类(可以获取请求各种信息)
     *
     * @param
     * @create 2014-4-22 下午5:18:43
     */
    public JsonRequest getRequest() {
        return request;
    }

    /**
     * 获取返回的数据的请求头 NetAccess.getReHeaders();
     *
     * @return
     * @create 2014-4-22 下午5:20:51
     */
    public Map<String, String> getReHeaders() {
        Map<String, String> data = null;
        if (request != null) {
            data = request.getRe_header();
        }
        return data;
    }

    /**
     * 获取服务器给客户端的cookie
     *
     * @return
     * @create 2014-10-14 下午3:31:30
     */
    public String getRecookies() {
        if (request != null) {
            return request.getRe_cookies();
        }
        return null;
    }

    /**
     * 类名：NetAccess.java<br>
     * 描述： 回调接口<br>
     * 创建者： jack<br>
     * 创建日期：2014-11-20<br>
     * 版本： <br>
     * 修改者： <br>
     * 修改日期：<br>
     */

    public interface NetRequestListener {
        /**
         * 功能描述： <br>
         * 创建者： jack<br>
         * 创建日期：2014-11-20<br>
         *
         * @param success 是否成功
         * @param object  返回数据
         * @param error   错误码
         */
        public void onAccessComplete(boolean success, String object, VolleyError error, String flag);
    }

    /**
     * 功能描述： 根据地址删除缓存<br>
     * 创建者： jack<br>
     * 创建日期：2014-11-20<br>
     *
     * @param
     */
    public static void clearCache(String url) {
        try {
            if (url == null) {
                Set<String> mset = mLruCache.snapshot().keySet();
                String[] keys = mset.toArray(new String[mset.size()]);
                for (String key : keys) {
                    mLruCache.remove(key);
                }

                mRequestQueue.getCache().clear();
            } else {
                mLruCache.remove(getCache(url));
                mRequestQueue.getCache().remove(url);
            }
        } catch (Exception e) {
            // 可能出现
            // mLruCache、mRequestQueue空指针
        }
    }

    /**
     * 功能描述：获取请求对象<br>
     * 创建者： jack<br>
     * 创建日期：2014-11-20<br>
     *
     * @param context 上下文对象
     */
    public static NetRequest request(Context context) {
        checkVar(context);
        return new NetRequest(context);
    }

    /**
     * 功能描述：请求初始化<br>
     * 创建者： jack<br>
     * 创建日期：2014-11-20<br>
     *
     * @param
     */
    private static void checkVar(Context context) {
        // 网络请求类初始化
        if (mRequestQueue == null) {
            if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
                File file = new File(CachePath);
                DiskBasedCache cache = new DiskBasedCache(file, 20 * 1024 * 1024);
                HttpStack stack = null;
                if (android.os.Build.VERSION.SDK_INT >= 9) {
                    stack = new HurlStack();
                } else {
                    stack = new HttpClientStack(AndroidHttpClient.newInstance("volley/0"));
                }
                Network network = new BasicNetwork(stack);
                mRequestQueue = new RequestQueue(cache, network);
                mRequestQueue.start();
            } else {
                // 无sd卡 默认5Mb：data/data/package/cache/volley
                if (context != null) {
                    SSLContext con;
                    try {
                        con = SSLContext.getInstance("TLS");
                        con.init(null, new TrustManager[]{new FakeX509TrustManager()}, new SecureRandom());
                        mRequestQueue = Volley.newRequestQueue(context, new HurlStack(null, con.getSocketFactory()));
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (KeyManagementException e) {
                        e.printStackTrace();
                    }
                    if (mRequestQueue == null)
                        mRequestQueue = Volley.newRequestQueue(context);
                }
            }
        }

        // 图片缓存类初始化
        if (mLruCache == null) {
            int cacheSize = 0;
            if (context == null) {
                // LruCache通过构造函数传入缓存值，以KB为单位。
                int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
                // 使用最大可用内存值的1/8作为缓存的大小。
                cacheSize = maxMemory / 6;
            } else {
                // Use 1/8th of the available memory for this memory cache.
                int memClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
                cacheSize = 1024 * 1024 * memClass / 6;// 0.5>>50331648
            }
            mLruCache = new BitmapLruCache(cacheSize);
        }
        // 图片(缓存)请求类初始化
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(mRequestQueue, mLruCache);
        }
    }

    /**
     * 功能描述： 根据请求地址获取缓存<br>
     * 创建者： jack<br>
     * 创建日期：2014-11-20<br>
     *
     * @param
     */
    public static String getCache(String url) {
        String result = null;
        try {
            // 获取缓存
            Entry cachedata = mRequestQueue.getCache().get(url);
            if (cachedata != null) {
                if (cachedata.data != null) {
                    result = JsonRequest.decode(cachedata.data);
                }
            }
        } catch (Exception e) {
            // 可能出现
            // mRequestQueue空指针
        }
        return result;
    }

    /**
     * 功能描述： 缓存管理类<br>
     * 创建者： jack<br>
     * 创建日期：2014-11-20<br>
     *
     * @param
     */
    public static <T> T getCache(String url, Class<T> cls) {
        T result = null;
        try {
            // 获取缓存
            Entry cachedata = mRequestQueue.getCache().get(url);
            if (cachedata != null) {
                if (cachedata.data != null) {
                    Object obj = null;
                    if (cls.equals(Bitmap.class)) {
                        if (IMAGEMAXMEASURE != 0) {
                            BitmapFactory.Options opts = new BitmapFactory.Options();
                            opts.inJustDecodeBounds = true;
                            BitmapFactory.decodeByteArray(cachedata.data, 0, cachedata.data.length, opts);
                            opts.inSampleSize = calculateSampleSize(opts, IMAGEMAXMEASURE, IMAGEMAXMEASURE);
                            opts.inJustDecodeBounds = false;
                            obj = BitmapFactory.decodeByteArray(cachedata.data, 0, cachedata.data.length, opts);
                        } else {
                            obj = BitmapFactory.decodeByteArray(cachedata.data, 0, cachedata.data.length);
                        }
                    } else {
                        obj = JsonRequest.decode(cachedata.data);
                    }

                    result = (T) obj;
                }
            }
        } catch (Exception e) {
            // 可能出现 空指针、 (T) obj;强制失败
        }
        return result;
    }

    /**
     * 设置请求头
     *
     * @param heads
     * @return
     */
    public NetRequest setHeaders(Map<String, String> heads) {
        this.heads = heads;
        return this;
    }

    /**
     * 设置请求内容
     *
     * @param params
     * @return
     */
    public NetRequest setParams(Map<String, String> params) {
        this.params = params;
        return this;
    }

    /**
     * 设置请求内容
     *
     * @param params
     * @return
     */
    public NetRequest setParams2(Map<String, Object> params) {
        Map<String, String> param2 = new HashMap<String, String>();
        if (params != null) {
            for (String key : params.keySet()) {
                param2.put(key, params.get(key) + "");
            }
        }
        this.params = param2;
        return this;
    }

    public NetRequest setmDialog(Dialog mDialog) {
        this.mDialog = mDialog;
        return this;
    }

    /**
     * 设置请求标示
     *
     * @param requestTag flag
     * @return
     */
    public NetRequest setReuestTag(String requestTag) {
        this.mReuestTag = requestTag;
        return this;
    }

    /**
     * 设置请求cookies
     *
     * @param cookies
     * @return
     */
    public NetRequest setCookies(String cookies) {
        this.cookies = cookies;
        return this;
    }

    /**
     * 设置超时时间，默认20秒
     *
     * @param timeout
     * @return
     */
    public NetRequest setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    /**
     * 功能描述：开始get请求 <br>
     * 创建者： jack<br>
     * 创建日期：2014-11-20<br>
     *
     * @param url
     * @param listener
     */
    synchronized public void byGet(String url, NetRequestListener listener) {
        url += getParamStr(params);
        L.i(TAG, "gurl-->" + url);
        startrequest(url, Method.GET, false, listener);
    }

    /**
     * 功能描述：获取缓存后，在进行get请求 <br>
     * 创建者： jack<br>
     * 创建日期：2014-11-20<br>
     *
     * @param url
     * @param listener
     */
    synchronized public void byCacheGet(String url, NetRequestListener listener) {
        url += getParamStr(params);
        String cache = getCache(url);
        if (cache != null) {
            if (listener != null) {
                listener.onAccessComplete(true, cache, null, mReuestTag);
            }
        }
        L.i(TAG, "cgurl-->" + url);
        startrequest(url, Method.GET, true, listener);
    }

    /**
     * 功能描述： post请求<br>
     * 创建者： jack<br>
     * 创建日期：2014-11-20<br>
     *
     * @param
     */
    synchronized public void byPost(String url, NetRequestListener listener) {
        L.i(TAG, "purl-->" + url + getParamStr(params));
        startrequest(url, Method.POST, false, listener);
    }

    /**
     * 功能描述：获取缓存后，在进行post请求 <br>
     * 创建者： jack<br>
     * 创建日期：2014-11-20<br>
     *
     * @param
     */
    synchronized public void byCachePost(String url, NetRequestListener listener) {
        String cache = getCache(url);
        if (cache != null) {
            L.i(TAG, "pcache-->" + cache);
            if (listener != null) {
                listener.onAccessComplete(true, cache, null, mReuestTag);
            }
        }
        L.i(TAG, "cpurl-->" + url + getParamStr(params));
        startrequest(url, Method.POST, true, listener);
    }

    /**
     * 功能描述： 开始请求<br>
     * 创建者： jack<br>
     * 创建日期：2014-11-20<br>
     *
     * @param url       请求链接(post不包含参数、get包含参数)
     * @param savecache 是否缓存
     * @param listener  请求监听
     */
    private void startrequest(String url, int method, boolean savecache, NetRequestListener listener) {
        this.mListener = listener;

        if (mDialog != null && !mDialog.isShowing()) {
            mDialog.show();
        }

        ResponseListener relistener = new ResponseListener(this);
        request = new JsonRequest(method, url, relistener, relistener);
        request.setHeaders(heads);
        if (method != Method.GET) {
            request.setParams(params);
        }
        if (cookies == null) {
            cookies = CookieUtil.getCookie(mcontext, GetDomainName(url));
        }
        request.setCookies(cookies);
        request.setTimeout(timeout);
        request.setShouldCache(savecache);
        request.setTag(TextUtils.isEmpty(mReuestTag) ? TAG : mReuestTag);
        mRequestQueue.add(request);
//        mRequestQueue.start();  //解决出现的io读取异常
        // request.getReHeaders()
    }

    /**
     * 类名：NetAccess.java<br>
     * 描述： 请求回调<br>
     * 创建者： jack<br>
     * 创建日期：2014-11-20<br>
     * 版本： <br>
     * 修改者： <br>
     * 修改日期：<br>
     */
    private static class ResponseListener implements Response.Listener<String>, Response.ErrorListener {
        private NetRequest net;

        public ResponseListener(NetRequest netaccess) {
            this.net = netaccess;
        }

        @Override
        public void onResponse(String response) {
            L.i(TAG, "callback-->" + response);

            if (!(net.mDialog == null || !net.mDialog.isShowing())) {
                net.mDialog.dismiss();
            }

            if (net.mListener != null) {
                net.mListener.onAccessComplete(true, response, null, net.mReuestTag);
            }

            // 数据库返回的参数
            String recookie = net.request.getRe_cookies();
            if (recookie != null) {
                CookieUtil.setcookie(net.mcontext, GetDomainName(net.request.getUrl()), net.request.getRe_cookies());
            }

        }

        @Override
        public void onErrorResponse(VolleyError error) {
            L.i(TAG, "callback-->error:" + error.getMessage());

            if (!(net.mDialog == null || !net.mDialog.isShowing())) {
                net.mDialog.dismiss();
            }

            if (net.mListener != null) {
                net.mListener.onAccessComplete(false, null, error, net.mReuestTag);
            }

        }
    }

    /**
     * 功能描述： 图片异步加载<br>
     * 创建者： jack<br>
     * 创建日期：2014-11-20<br>
     *
     * @param
     */
    public static void image(ImageView imageview, String url) {
        image(imageview, url, LOADINGIMAGE, ERRORIMAGE, IMAGEMAXMEASURE);
    }

    /**
     * 功能描述： 图片异步加载<br>
     * 创建者： jack<br>
     * 创建日期：2014-11-20<br>
     *
     * @param imageview  显示控件
     * @param url        请求地址
     * @param loadingimg 加载中图片
     * @param errorimg   加载失败图片
     */
    public static void image(final ImageView imageview, String url, int loadingimg, int errorimg) {
        image(imageview, url, loadingimg, errorimg, IMAGEMAXMEASURE);
    }

    /**
     * 功能描述： 图片异步加载<br>
     * 创建者： jack<br>
     * 创建日期：2014-11-20<br>
     *
     * @param imageview  显示控件
     * @param url        图片地址
     * @param loadingimg 加载中显示的图片
     * @param errorimg   加载错误时显示的图片
     */
    public static void image(final ImageView imageview, final String url, final int loadingimg, final int errorimg,
                             final int maxmeasure) {
        if (imageview == null) {
            return;
        }

        checkVar(null);

        imageview.setTag(url);

        Bitmap bm = mLruCache.getBitmap(getCacheKey(url, maxmeasure, maxmeasure));
        if (bm == null) {
            bm = getCache(url, Bitmap.class);
            if (bm != null) {
                mLruCache.putBitmap(getCacheKey(url, maxmeasure, maxmeasure), bm);
            }
        }

        if (bm == null) {
            mImageLoader
                    .get(url, ImageLoader.getImageListener(imageview, loadingimg, errorimg), maxmeasure, maxmeasure);
        } else {
            final int bmsize = bm.getRowBytes() * bm.getHeight();
            imageview.setImageBitmap(bm);
            mImageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap bitmap) {
                    if (imageview.getTag().toString().equals(url)) {
                        imageview.setImageBitmap(bitmap);
                        if (bmsize != bitmap.getRowBytes() * bitmap.getHeight()) {
                            // 图片改变了，更新图片缓存
                            mLruCache.putBitmap(getCacheKey(url, maxmeasure, maxmeasure), bitmap);
                        }
                    } else if (!bitmap.isRecycled()) {
                        bitmap.recycle();
                    }
                }
            }, maxmeasure, maxmeasure, Config.ARGB_8888, new ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError arg0) {
                    // imageview.setImageResource(errorimg);
                }
            });
            if (mRequestQueue == null) {
                mRequestQueue = Volley.newRequestQueue(imageview.getContext());
            }
            mRequestQueue.add(mImageRequest);
        }
    }

    /**
     * 功能描述： 获取参数(得到 ?a=12&b=123)<br>
     * 创建者： jack<br>
     * 创建日期：2014-11-20<br>
     *
     * @param
     */
    public static String getParamStr(Map<String, String> params) {
        StringBuffer bf = new StringBuffer("?");
        if (params != null) {
            Set<String> mset = params.keySet();
            String[] keys = mset.toArray(new String[mset.size()]);

            for (String key : keys) {
                String value = params.get(key);
                if (value == null) {
                    params.remove(key);
                } else {
                    bf.append(key + "=" + params.get(key) + "&");
                }
            }
        }
        String str = bf.toString();
        return str.substring(0, str.length() - 1);
    }

    public static String getCacheKey(String url, int maxWidth, int maxHeight) {
        return (new StringBuilder(url.length() + 12)).append("#W").append(maxWidth).append("#H").append(maxHeight)
                .append(url).toString();
    }

    public static int calculateSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    /**
     * 功能描述：取得url域名<br>
     * 创建者： jack<br>
     * 创建日期：2014-11-20<br>
     *
     * @param
     */
    public static String GetDomainName(String url) {
        Pattern p = Pattern.compile("^http://[^/]+");
        Matcher matcher = p.matcher(url);
        if (matcher.find()) {
            return matcher.group();
        }
        return "";
    }

}
