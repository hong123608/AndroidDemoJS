package com.zyhdemo.network;

import android.os.AsyncTask;

import com.zyhdemo.utils.L;
import com.zyhdemo.utils.Strings;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;

/**
 * 类名：FileDownload.java<br>
 * 描述： 文件下载工具类<br>
 * 创建者： jack<br>
 * 创建日期：2015-4-2<br>
 * 版本： <br>
 * 修改者： <br>
 * 修改日期：<br>
 */
public class FileDownload extends AsyncTask<Object, Object, Object> {
    private static String TAG = "FileDownload";
    private static final int BUFFER_SIZE = 1024 * 10;// 10k缓存
    private String mUrl;
    private File file;
    private int startPosition; // 断点续传：startpoint = (int) file.length();l
    private int curPosition;
    private int filesize = 0;

    private FileDownloadListener mListener;

    public static interface FileDownloadListener {
        public void onDownStart(String url);
        public void onDownProgress(String url, float progress);
        public void onDownEnd(String url);
        public void onDownError(String err);
    }

    public FileDownload(String url, File file, int startPosition, FileDownloadListener listener) {
        L.i(TAG, "url:" + url + " file;" + file.getAbsolutePath() + " startPosition;" + startPosition);
        this.mUrl = url;
        this.file = file;
        this.startPosition = startPosition;
        this.curPosition = startPosition;
        this.mListener = listener;
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
    public Object doInBackground(Object... params) {
        down();
        return null;
    }
    /**
     * 功能描述： 文件下载<br>
     * 创建者： jack<br>
     * 创建日期：2015-4-2<br>
     */
    private void down() {
        // 重置开始点
        startPosition = curPosition;

        BufferedInputStream bis = null;
        RandomAccessFile fos = null;
        byte[] buf = new byte[BUFFER_SIZE];
        URLConnection con = null;
        URLConnection testcon = null;
        URL url;
        try {
            url = new URL(mUrl);

            if (!file.exists()) {
                file.createNewFile();
            }
            con = url.openConnection();
            testcon = url.openConnection();
            filesize = testcon.getContentLength();
            L.i(TAG, "filesize=" + filesize);
            if (con.getReadTimeout() == 5 || filesize == -1) {
                // timeout
                publishProgress(1);
            }
            publishProgress(2);
            con.setAllowUserInteraction(true);
            // 设置当前线程下载的起点，终点
            con.setRequestProperty("Range", "bytes=" + startPosition + "-");

            if (startPosition >= filesize) {
                // 已经下载完成了
                publishProgress(4);
                return;
            }

            L.i(TAG, "con.getContentLength() =" + con.getContentLength() + " filesize;" + filesize + " startPosition;"
                    + startPosition);

            // 使用java中的RandomAccessFile 对文件进行随机读写操作
            fos = new RandomAccessFile(file, "rw");

            // 设置开始写文件的位置
            fos.seek(startPosition);
            bis = new BufferedInputStream(con.getInputStream());
            // 开始循环以流的形式读写文件
            while (curPosition < filesize) {

                int len = bis.read(buf, 0, BUFFER_SIZE);
                if (len == -1) {
                    break;
                }
                fos.write(buf, 0, len);
                curPosition += len;

                publishProgress(3, (float) curPosition / filesize);
            }
            bis.close();
            fos.close();
            publishProgress(4);
        } catch (IOException e) {
            e.printStackTrace();
            publishProgress(1, e.getMessage());
        }
    }
    @Override
    protected void onProgressUpdate(Object... values) {
        super.onProgressUpdate(values);
        if (mListener != null) {
            int state = Strings.getInt(values[0].toString());
            switch (state) {
                case 1 :
                    // error
                    mListener.onDownError(values[1].toString());
                    break;
                case 2 :
                    // 下载开始
                    mListener.onDownStart(mUrl);
                    break;
                case 3 :
                    mListener.onDownProgress(mUrl, Strings.getFloat(values[1].toString()));
                    break;
                case 4 :
                    mListener.onDownEnd(mUrl);
                    break;
            }
        }
    }

}
