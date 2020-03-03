package com.github.ryan.easyhttp.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * create by Ryan
 * 2018/6/11
 */
public class RetrofitFactory {

    public static Retrofit create(RetrofitBuilder builder) {
        if(builder == null){
            throw new IllegalStateException("RetrofitBuilder is null!");
        }
        Converter.Factory factory = builder.getFactory();
        if (factory == null) {
            factory = GsonConverterFactory.create(createConverter());
        }
        OkHttpClient client = builder.getClient();
        return new Retrofit.Builder()
                .client(client)
                .baseUrl("https://github.com/Ryan-Shz/")
                .addConverterFactory(factory)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    private static Gson createConverter() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(String.class, new Json2StringDeserializer());
        return builder.create();
    }

}
