package com.zyhdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zyhdemo.imageloader.ImageLoad;
import com.zyhdemo.network.Http;
import com.zyhdemo.network.Https;
import com.zyhdemo.network.NetRequest;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class MainActivity extends AppCompatActivity {
    TextView tv_show_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_show_text= (TextView) findViewById(R.id.tv_show_text);
//        ImageLoad.disPlay("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2006772353,1636214348&fm=117&gp=0.jpg",iv_use,R.mipmap.ic_launcher);
    }

    public void getHtml(View view) {
        String myUrl = "http://www.geeboo.com/library/cGetMyLibBookAction.go?email=12345aaa6@qq.com&nowPage=1&password=2139013067J6HB44B6C9&versionNumber=312&terminalType=1&accountId=19261";
        Http.aSyncGet(this, null, myUrl, null, new NetRequest.NetRequestListener() {
            @Override
            public void onAccessComplete(boolean success, String object, VolleyError error, String flag) {
                Log.i("after",  "   volley  数据     "+  object);
                tv_show_text.setText(  ""+object );
            }
        });
    }
}