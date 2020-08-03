package com.github.ryan.easyhttp.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

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

    public static Retrofit create(InitSettings builder) {
        if (builder == null) {
            throw new IllegalStateException("InitSettings is null!");
        }
        OkHttpClient client = builder.getClient();
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .client(client)
                .baseUrl("https://github.com/Ryan-Shz/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        List<Converter.Factory> factories = builder.getFactories();
        if (factories == null) {
            factories = new ArrayList<>();
            factories.add(GsonConverterFactory.create(createConverter()));
        } else {
            for (Converter.Factory factory : factories) {
                retrofitBuilder.addConverterFactory(factory);
            }
        }
        return retrofitBuilder.build();
    }

    private static Gson createConverter() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(String.class, new Json2StringDeserializer());
        return builder.create();
    }
}
