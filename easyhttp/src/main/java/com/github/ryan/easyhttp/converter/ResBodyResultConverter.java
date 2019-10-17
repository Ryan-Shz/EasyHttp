package com.github.ryan.easyhttp.converter;

import io.reactivex.functions.Function;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * create by Ryan
 * 2018/6/11
 */
public class ResBodyResultConverter<T> implements Function<Response<ResponseBody>, T> {

    private ResultConverter<T, Response<ResponseBody>> mConverter;

    public ResBodyResultConverter(ResultConverter<T, Response<ResponseBody>> converter) {
        mConverter = converter;
    }

    @Override
    public T apply(Response<ResponseBody> httpResponseResponse) throws Exception {
        if (mConverter == null) {
            return null;
        }
        return mConverter.convert(httpResponseResponse);
    }

}
