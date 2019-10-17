package com.github.ryan.easyhttp.sample;

import android.app.Application;

import com.github.ryan.easyhttp.EasyHttp;
import com.github.ryan.easyhttp.retrofit.RetrofitBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by Ryan
 * on 2019/10/17
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        OkHttpClient mBaseOkHttpClient = new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();
        RetrofitBuilder builder = new RetrofitBuilder().client(mBaseOkHttpClient);
        EasyHttp.setRetrofitBuilder(builder);
    }
}
