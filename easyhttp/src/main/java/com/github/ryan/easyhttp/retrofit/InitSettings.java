package com.github.ryan.easyhttp.retrofit;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Converter;

/**
 * Created by Ryan
 * at 2019/9/21
 */
public class InitSettings {

    private OkHttpClient mClient;
    private List<Converter.Factory> mFactories;

    public static class Builder {
        private OkHttpClient client;
        private List<Converter.Factory> factories = new ArrayList<>();

        public Builder client(OkHttpClient client) {
            this.client = client;
            return this;
        }

        public Builder addFactory(Converter.Factory factory) {
            if (!factories.contains(factory)) {
                factories.add(factory);
            }
            return this;
        }

        public InitSettings build() {
            InitSettings settings = new InitSettings();
            settings.mClient = client;
            settings.mFactories = factories;
            return settings;
        }
    }

    public OkHttpClient getClient() {
        return mClient;
    }

    public List<Converter.Factory> getFactories() {
        return mFactories;
    }
}
