package com.github.ryan.easyhttp.converter;

import com.github.ryan.easyhttp.EasyHttp;
import com.github.ryan.easyhttp.options.DownloadOptions;
import com.github.ryan.easyhttp.utils.DownloadUtils;

import java.io.File;
import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * create by Ryan
 * 2018/6/21
 */
public class DefaultResBodyResultConverter<T> implements ResultConverter<T, Response<ResponseBody>> {

    private EasyHttp mHttp;
    private Class<? extends T> mTargetClass;

    public DefaultResBodyResultConverter(EasyHttp http, Class<? extends T> targetClass) {
        mTargetClass = targetClass;
        mHttp = http;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T convert(Response<ResponseBody> original) throws Exception {
        if (mTargetClass == Response.class) {
            return (T) original;
        }
        if (mTargetClass == okhttp3.Response.class) {
            return (T) original.raw();
        }
        ResponseBody data = original.body();
        if (data == null) {
            return null;
        }
        writeToTargetFile(data);
        if (mTargetClass == ResponseBody.class) {
            return (T) original;
        }
        if (mTargetClass == File.class) {
            DownloadOptions options = mHttp.getDownloadOptions();
            return (T) options.getTargetFile();
        }
        return null;
    }

    private void writeToTargetFile(ResponseBody responseBody) throws IOException {
        DownloadOptions options = mHttp.getDownloadOptions();
        if (options != null && options.getTargetFile() != null) {
            DownloadUtils.writeToFile(responseBody, options.getTargetFile(), options.getCallback());
        }
    }
}
