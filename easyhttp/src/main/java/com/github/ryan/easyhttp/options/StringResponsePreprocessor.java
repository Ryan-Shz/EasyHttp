package com.github.ryan.easyhttp.options;

import io.reactivex.functions.Function;
import retrofit2.Response;

/**
 * Response对象预处理器
 *
 * create by Ryan
 * 2018/6/11
 */
public class StringResponsePreprocessor implements Function<Response<String>, Response<String>> {

    private ResponsePreprocessor mInterceptor;

    public StringResponsePreprocessor(ResponsePreprocessor interceptor) {
        mInterceptor = interceptor;
    }

    @Override
    public Response<String> apply(Response<String> stringResponse) throws Exception {
        if (mInterceptor != null) {
            mInterceptor.process(stringResponse);
        }
        return stringResponse;
    }
}
