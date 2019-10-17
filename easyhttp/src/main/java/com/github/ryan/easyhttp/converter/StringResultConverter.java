package com.github.ryan.easyhttp.converter;

import io.reactivex.functions.Function;
import retrofit2.Response;

/**
 * create by Ryan
 * 2018/6/11
 */
public class StringResultConverter<T> implements Function<Response<String>, T> {

    private ResultConverter<T, Response<String>> mConverter;

    public StringResultConverter(ResultConverter<T, Response<String>> converter) {
        mConverter = converter;
    }

    @Override
    public T apply(Response<String> httpResponseResponse) throws Exception {
        if (mConverter == null) {
            return null;
        }
        return mConverter.convert(httpResponseResponse);
    }
}
