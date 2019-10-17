package com.github.ryan.easyhttp.options;

import io.reactivex.functions.Function;
import retrofit2.Response;
import retrofit2.http.Body;

/**
 * Response对象预处理器
 *
 * create by Ryan
 * 2018/6/11
 */
public class ResBodyResponsePreprocessor implements Function<Response<Body>, Response<Body>> {

    private ResponsePreprocessor mInterceptor;

    public ResBodyResponsePreprocessor(ResponsePreprocessor interceptor) {
        mInterceptor = interceptor;
    }

    @Override
    public Response<Body> apply(Response<Body> response) throws Exception {
        if (mInterceptor != null) {
            mInterceptor.process(response);
        }
        return response;
    }
}
