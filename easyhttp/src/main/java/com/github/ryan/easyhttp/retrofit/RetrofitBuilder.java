package com.github.ryan.easyhttp.retrofit;

import okhttp3.OkHttpClient;
import retrofit2.Converter;

/**
 * Created by Ryan
 * at 2019/9/21
 */
public class RetrofitBuilder {

    private OkHttpClient client;
    private Converter.Factory factory;

    public RetrofitBuilder client(OkHttpClient client) {
        this.client = client;
        return this;
    }

    public OkHttpClient getClient() {
        return client;
    }

    public void setFactory(Converter.Factory factory) {
        this.factory = factory;
    }

    public Converter.Factory getFactory() {
        return factory;
    }
}
