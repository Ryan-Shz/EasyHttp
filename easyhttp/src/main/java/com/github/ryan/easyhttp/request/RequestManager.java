package com.github.ryan.easyhttp.request;

import com.github.ryan.easyhttp.EasyHttp;
import com.github.ryan.easyhttp.caller.DownloadServiceCaller;
import com.github.ryan.easyhttp.caller.GetRequestCaller;
import com.github.ryan.easyhttp.caller.PostRequestCaller;
import com.github.ryan.easyhttp.caller.ServiceCaller;
import com.github.ryan.easyhttp.chain.ChainsList;
import com.github.ryan.easyhttp.chain.DefaultChainsList;
import com.github.ryan.easyhttp.chain.download.DownloadChainsList;

import io.reactivex.Observable;
import retrofit2.Retrofit;

/**
 * create by Ryan
 * 2018/6/11
 */
public class RequestManager {

    static final String METHOD_GET = "GET";
    static final String METHOD_POST = "POST";
    private Retrofit mDefaultRetrofit;

    private RequestManager() {

    }

    private static class SingletonHolder {
        private static final RequestManager INSTANCE = new RequestManager();
    }

    public static RequestManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    <T> T execute(EasyHttp http, String method) {
        Retrofit retrofit = getUsableRetrofit(http);
        ServiceCaller caller;
        if (METHOD_GET.equals(method)) {
            caller = new GetRequestCaller(retrofit);
        } else {
            caller = new PostRequestCaller(retrofit);
        }
        Observable observable = caller.call(http);
        ChainsList<T> chains = new DefaultChainsList<>(http);
        return chains.start(observable);
    }

    <T> T download(EasyHttp http){
        Retrofit retrofit = getUsableRetrofit(http);
        ServiceCaller caller = new DownloadServiceCaller(retrofit);
        Observable observable = caller.call(http);
        ChainsList<T> chains = new DownloadChainsList<>(http);
        return chains.start(observable);
    }

    // 获取可用的Retrofit对象
    private Retrofit getUsableRetrofit(EasyHttp http) {
        Retrofit retrofit = http.getRetrofit();
        if (retrofit == null) {
            if (mDefaultRetrofit == null) {
                throw new RuntimeException("please call EasyHttp.setRetrofitBuilder(Retrofit retrofit) to specify a default retrofit obj.");
            }
            retrofit = mDefaultRetrofit;
        }
        return retrofit;
    }

    public void retrofit(Retrofit retrofit) {
        mDefaultRetrofit = retrofit;
    }

}
