package com.zyhdemo.network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import android.os.AsyncTask;
import com.zyhdemo.utils.L;
import com.zyhdemo.utils.Strings;

/**
 * 类名：FileUpload.java<br>
 * 描述：文件上传工具类<br>
 * HashMap<String, String> params = new HashMap<String, String>();
 * params.put("page", "1"); params.put("name", "2");
 * 
 * HashMap<String, File> files = new HashMap<String, File>();
 * files.put("1.apk","/mnt/sdcard/1.apk")); files.put("name", new
 * File(Environment.getExternalStorageDirectory().getAbsolutePath(),"6.jpg"));
 * 
 * new fileupload("http://192.168.191.1:8081/uploadtest/servlet/upload2",params,
 * files,MainActivity.this).execute("");<br>
 * 创建者： jack<br>
 * 创建日期：2015-4-2<br>
 * 版本： <br>
 * 修改者： <br>
 * 修改日期：<br>
 */

public class FileUpload extends AsyncTask<Object, Object, Object> {
    public static final String TAG = "FileUpload";
    String mUrl;
    Map<String, Object> params;
    Map<String, File> files;
    UploadListener listener;

    public interface UploadListener {
        public void onUpFileStart(String filepath);
        public void onUpFileProgress(String filepath, float progress);
        public void onUpFileEnd(String filepath);
        public void onUploadEnd(boolean success, String object);
        public void onUploadError(String error);
    }

    public FileUpload(String url, Map<String, Object> params, Map<String, File> files, UploadListener listener) {
        this.mUrl = url;
        this.params = params;
        this.files = files;
        this.listener = listener;
    }

    @Override
    protected String doInBackground(Object... arg0) {
        String result = null;

        HttpURLConnection conn = null;
        DataOutputStream outStream = null;
        InputStream is = null;

        try {
            String BOUNDARY = java.util.UUID.randomUUID().toString();
            String PREFIX = "--", LINEND = "\r\n";
            String MULTIPART_FROM_DATA = "multipart/form-data";
            String CHARSET = "UTF-8";
            URL uri = new URL(mUrl);
            conn = (HttpURLConnection) uri.openConnection();
            conn.setReadTimeout(25 * 1000);
            conn.setDoInput(true);// 允许输入
            conn.setDoOutput(true);// 允许输出
            conn.setUseCaches(false);
            conn.setRequestMethod("POST"); // Post方式
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Charsert", "UTF-8");
            conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);
            // 首先组拼文本类型的参数
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINEND);
                sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINEND);
                sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
                sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
                sb.append(LINEND);
                sb.append(entry.getValue());
                sb.append(LINEND);
            }
            outStream = new DataOutputStream(conn.getOutputStream());
            outStream.write(sb.toString().getBytes());

            // 发送文件数据
            if (files != null)
                // for (Map.Entry<String, File> file : files.entrySet()) {
                for (String key : files.keySet()) {
                    File valuefile = files.get(key);

                    StringBuilder sb1 = new StringBuilder();
                    sb1.append(PREFIX);
                    sb1.append(BOUNDARY);
                    sb1.append(LINEND);
                    sb1.append("Content-Disposition: form-data; name=\"" + key + "\"; filename=\""
                            + valuefile.getName() + "\"" + LINEND);
                    sb1.append("Content-Type: multipart/form-data; charset=" + CHARSET + LINEND);
                    sb1.append(LINEND);

                    outStream.write(sb1.toString().getBytes());

                    publishProgress(1, valuefile.getAbsolutePath());

                    long fileLen = valuefile.length();
                    long step = 0;
                    is = new FileInputStream(valuefile);
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while ((len = is.read(buffer)) != -1) {
                        outStream.write(buffer, 0, len);
                        step += len;
                        publishProgress(2, valuefile.getAbsolutePath(), (float) step / fileLen);
                    }
                    is.close();
                    is = null;
                    outStream.write(LINEND.getBytes());
                    publishProgress(3, valuefile.getAbsolutePath());
                }
            // 请求结束标志
            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
            outStream.write(end_data);
            outStream.flush();
            // 得到响应码
            // success = conn.getResponseCode()==200;

            InputStream in = conn.getInputStream();
            InputStreamReader isReader = new InputStreamReader(in);
            BufferedReader bufReader = new BufferedReader(isReader);
            String line = null;
            result = "";
            while ((line = bufReader.readLine()) != null)
                result += line;
            outStream.close();
            outStream = null;
        } catch (Exception e) {
            e.printStackTrace();
            publishProgress(4, e.getMessage() + "");
        } finally {
            try {
                if (is != null)
                    is.close();
                if (outStream != null)
                    outStream.close();
                if (conn != null)
                    conn.disconnect();
            } catch (IOException ex) {
            }
        }
        return result;
    }
    @Override
    protected void onPreExecute() {
        // onPreExecute方法用于在执行后台任务前做一些UI操作
        L.i(TAG, "onPreExecute() called");
    }

    @Override
    protected void onCancelled() {
        // 取消操作
        L.i(TAG, "onCancelled() called");
        if (listener != null) {
            listener.onUploadEnd(false, null);
        }
    }

    @Override
    protected void onPostExecute(Object result) {
        // onPostExecute方法用于在执行完后台任务后更新UI,显示结果
        L.i(TAG, "onPostExecute(Result result) called");
        if (listener != null) {
            if (result == null) {
                listener.onUploadEnd(false, null);
            } else {
                listener.onUploadEnd(true, result.toString());
            }
        }
    }
    /**
     * 功能描述： 开始执行<br>
     * 创建者： jack<br>
     * 创建日期：2015-4-2<br>
     */
    public void doIt() {
        execute();
    }

    @Override
    protected void onProgressUpdate(Object... values) {
        // onProgressUpdate方法用于更新进度信息
        L.i(TAG, "onProgressUpdate() called");
        if (listener != null) {
            int state = Strings.getInt(values[0].toString());
            switch (state) {
                case 1 :
                    // 上传文件开始
                    listener.onUpFileStart(values[1].toString());
                    break;
                case 2 :
                    // 上传进度
                    listener.onUpFileProgress(values[1].toString(), Strings.getFloat(values[2].toString()));
                    break;
                case 3 :
                    listener.onUpFileEnd(values[1].toString());
                    break;
                case 4 :
                    listener.onUploadError(values[1].toString());
            }
        }
    }
}
