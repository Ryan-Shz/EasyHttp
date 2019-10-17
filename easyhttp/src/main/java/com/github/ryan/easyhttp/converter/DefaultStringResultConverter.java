package com.github.ryan.easyhttp.converter;

import com.google.gson.Gson;

import retrofit2.Response;

/**
 * create by Ryan
 * 2018/6/21
 */
public class DefaultStringResultConverter<T> implements ResultConverter<T, Response<String>> {

    private Class<? extends T> mTargetClass;
    private Gson mDecoder;

    public DefaultStringResultConverter(Class<? extends T> targetClass) {
        mTargetClass = targetClass;
        mDecoder = new Gson();
    }

    @SuppressWarnings("unchecked")
    @Override
    public T convert(Response<String> original) {
        if (mTargetClass == Response.class) {
            return (T) original;
        }
        String data = preProcessResult(original.body());
        if (data == null) {
            return null;
        }
        if (mTargetClass == String.class) {
            return (T) data;
        }
        return mDecoder.fromJson(data, mTargetClass);
    }

    protected String preProcessResult(String original) {
        return original;
    }
}
