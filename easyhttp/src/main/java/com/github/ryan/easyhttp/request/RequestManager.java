package com.github.ryan.easyhttp.request;

import com.github.ryan.easyhttp.EasyHttp;
import com.github.ryan.easyhttp.callback.BaseObserver;
import com.github.ryan.easyhttp.callback.HttpCallback;
import com.github.ryan.easyhttp.caller.DownloadServiceCaller;
import com.github.ryan.easyhttp.caller.GetRequestCaller;
import com.github.ryan.easyhttp.caller.PostRequestCaller;
import com.github.ryan.easyhttp.caller.ServiceCaller;
import com.github.ryan.easyhttp.chain.ChainsList;
import com.github.ryan.easyhttp.chain.DefaultChainsList;
import com.github.ryan.easyhttp.chain.download.DownloadChainsList;
import com.github.ryan.easyhttp.multiple.MergeSource;
import com.github.ryan.easyhttp.multiple.TripleSource;

import java.util.Arrays;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function3;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * create by Ryan
 * 2018/6/11
 */
@SuppressWarnings("unchecked")
public class RequestManager {

    private static final String METHOD_GET = "GET";
    private Retrofit mDefaultRetrofit;

    private RequestManager() {

    }

    private static class SingletonHolder {
        private static final RequestManager INSTANCE = new RequestManager();
    }

    public static RequestManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    <T> T execute(EasyHttp http) {
        ChainsList<T> chains = new DefaultChainsList<>(http);
        Observable curr = createObservable(http);
        return chains.start(curr);
    }

    private Observable createObservable(EasyHttp http) {
        Retrofit retrofit = getAvailableRetrofit(http);
        ServiceCaller caller;
        if (METHOD_GET.equalsIgnoreCase(http.getMethod())) {
            caller = new GetRequestCaller(retrofit);
        } else {
            caller = new PostRequestCaller(retrofit);
        }
        return flat(http, caller);
    }

    <T> T download(EasyHttp http) {
        Retrofit retrofit = getAvailableRetrofit(http);
        ServiceCaller caller = new DownloadServiceCaller(retrofit);
        Observable observable = caller.call(http);
        ChainsList<T> chains = new DownloadChainsList<>(http);
        return chains.start(observable);
    }

    // 将EasyHttp对象转换为RxJava的Observable对象
    // chains.wrap(origin)会使所有设置过的操作符生效
    public Observable wrapObservable(EasyHttp http) {
        ChainsList chains = new DefaultChainsList<>(http);
        Observable origin = createObservable(http);
        return chains.wrap(origin);
    }

    // 将2个请求合并
    // 1. 处理多个请求的回调线程逻辑时，当且仅当所有请求的回调都要求在主线程时，才会在主线程回调
    // 2. 任意一个请求的Tag都可以用来取消合并请求中的所有请求
    // 3. 任意一个请求的生命周期都可以取消合并请求中的所有请求
    <T, V> void mergeRequest(EasyHttp<T> t, EasyHttp<V> v, HttpCallback<MergeSource<T, V>> callback) {
        Observable.zip(wrapObservable(t), wrapObservable(v), (BiFunction<T, V, MergeSource<T, V>>) MergeSource::new)
                .observeOn(t.isCallbackOnMainThread() && v.isCallbackOnMainThread() ? AndroidSchedulers.mainThread() : Schedulers.io())
                .subscribe(new BaseObserver<>(callback, Arrays.asList(t.getTag(), v.getTag())));
    }

    // 将3个请求合并，和2个请求合并的逻辑是一样的
    <T, V, R> void mergeRequest(EasyHttp<T> t, EasyHttp<V> v, EasyHttp<R> r, HttpCallback<TripleSource<T, V, R>> callback) {
        Observable.zip(wrapObservable(t), wrapObservable(v), wrapObservable(r), (Function3<T, V, R, TripleSource<T, V, R>>) TripleSource::new)
                .observeOn(t.isCallbackOnMainThread() && v.isCallbackOnMainThread() && r.isCallbackOnMainThread() ? AndroidSchedulers.mainThread() : Schedulers.io())
                .subscribe(new BaseObserver<>(callback, Arrays.asList(t.getTag(), v.getTag(), r.getTag())));
    }

    private Observable flat(EasyHttp http, ServiceCaller caller) {
        int interval = http.getInterval();
        if (interval > 0) {
            return Observable.interval(0, interval, http.getIntervalTimeUnit()).flatMap(aLong -> caller.call(http));
        }
        return Observable.just(http).flatMap(caller::call);
    }

    // 获取可用的Retrofit对象
    private Retrofit getAvailableRetrofit(EasyHttp http) {
        Retrofit retrofit = http.getRetrofit();
        if (retrofit == null) {
            if (mDefaultRetrofit == null) {
                throw new IllegalStateException("please call EasyHttp.initialize(Retrofit retrofit) to specify a default retrofit obj.");
            }
            retrofit = mDefaultRetrofit;
        }
        return retrofit;
    }

    public void retrofit(Retrofit retrofit) {
        mDefaultRetrofit = retrofit;
    }
}
