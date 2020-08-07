package com.github.ryan.easyhttp.caller;

import android.support.v4.util.ArrayMap;

import com.github.ryan.easyhttp.options.ParamsInterceptor;
import com.github.ryan.easyhttp.EasyHttp;

import java.util.List;
import java.util.Map;

import retrofit2.Retrofit;

/**
 * Created by Ryan
 * at 2019/9/20
 */
abstract class BaseServiceCaller implements ServiceCaller {

    Retrofit mRetrofit;

    BaseServiceCaller(Retrofit retrofit) {
        mRetrofit = retrofit;
    }

    // 处理头部
    @SuppressWarnings("unchecked")
    Map<String, Object> generateHeaderMap(EasyHttp http) {
        Map<String, Object> params = http.getInnerHeaderParams();
        List<ParamsInterceptor> interceptors = http.getHeaderParamsInterceptors();
        if (interceptors != null && !interceptors.isEmpty()) {
            int headerInterceptorParamsSize = http.getHeaderInterceptorsParamsSize();
            int headerInnerParamsSize = params != null ? params.size() : 0;
            if (headerInterceptorParamsSize >= 0) {
                Map<String, Object> temp = params;
                params = new ArrayMap<>(headerInterceptorParamsSize + headerInnerParamsSize);
                for (ParamsInterceptor interceptor : interceptors) {
                    interceptor.process(params);
                }
                if (headerInnerParamsSize > 0) {
                    params.putAll(temp);
                }
            }
        }
        return params == null ? new ArrayMap<>() : params;
    }

    @SuppressWarnings("unchecked")
    Map<String, Object> generateParams(EasyHttp http) {
        Map<String, Object> params = http.getInnerParams();
        List<ParamsInterceptor> interceptors = http.getParamsInterceptors();
        if (interceptors != null && !interceptors.isEmpty()) {
            int innerParamsSize = params != null ? params.size() : 0;
            int interceptorsSize = http.getInterceptorsParamsSize();
            if (interceptorsSize >= 0) {
                Map<String, Object> temp = params;
                params = new ArrayMap<>(interceptorsSize + innerParamsSize);
                // 先添加参数拦截器中的参数
                for (ParamsInterceptor interceptor : interceptors) {
                    interceptor.process(params);
                }
                if (innerParamsSize > 0) {
                    params.putAll(temp);
                }
            }
        }
        return params == null ? new ArrayMap<>() : params;
    }
}
