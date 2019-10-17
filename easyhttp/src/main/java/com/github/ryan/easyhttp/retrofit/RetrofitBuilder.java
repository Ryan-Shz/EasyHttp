package com.github.ryan.easyhttp.retrofit;

import okhttp3.OkHttpClient;

/**
 * Created by Ryan
 * at 2019/9/21
 */
public class RetrofitBuilder {

    private OkHttpClient client;

    public RetrofitBuilder client(OkHttpClient client) {
        this.client = client;
        return this;
    }

    public OkHttpClient getClient() {
        return client;
    }
}
