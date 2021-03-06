package com.github.ryan.easyhttp;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.view.View;

import com.github.ryan.easyhttp.callback.RequestMapping;
import com.github.ryan.easyhttp.lifecycle.LifecycleRegistry;
import com.github.ryan.easyhttp.request.RealMergeRequester;
import com.github.ryan.easyhttp.request.RealTripleRequester;
import com.github.ryan.easyhttp.request.intf.MergeRequester;
import com.github.ryan.easyhttp.request.intf.TripleRequester;
import com.github.ryan.easyhttp.retrofit.InitSettings;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.github.ryan.easyhttp.callback.HttpCallback;
import com.github.ryan.easyhttp.converter.DefaultResBodyResultConverter;
import com.github.ryan.easyhttp.converter.DefaultStringResultConverter;
import com.github.ryan.easyhttp.converter.ResultConverter;
import com.github.ryan.easyhttp.callback.DownloadCallback;
import com.github.ryan.easyhttp.options.DownloadOptions;
import com.github.ryan.easyhttp.options.ParamsInterceptor;
import com.github.ryan.easyhttp.options.RequestBodyCreator;
import com.github.ryan.easyhttp.options.ResponsePreprocessor;
import com.github.ryan.easyhttp.options.RetryOptions;
import com.github.ryan.easyhttp.request.RealAsyncRequester;
import com.github.ryan.easyhttp.request.RealSyncRequester;
import com.github.ryan.easyhttp.request.RequestManager;
import com.github.ryan.easyhttp.request.intf.SyncRequester;
import com.github.ryan.easyhttp.retrofit.RetrofitFactory;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;

/**
 * create by Ryan
 * 2018/6/11
 */
public class EasyHttp<T> {

    private static final String METHOD_GET = "GET";
    private static final String METHOD_POST = "POST";
    private String mBaseUrl;
    private String mPath;
    private String mFullUrl;
    private String method;
    private Object mTag = this;
    private Map<String, Object> mInnerParams;
    private Map<String, Object> mInnerHeaderParams;
    private LifecycleProvider mLifeProvider;
    private LifecycleTransformer mLifeTransformer;
    private List<ParamsInterceptor> mParamsInterceptors;
    private List<ParamsInterceptor> mHeaderParamsInterceptors;
    private Retrofit mRetrofit;
    private RetryOptions mRetryOptions;
    private RequestBodyCreator mRequestBodyCreator;
    private ResultConverter mResultConverter;
    private ResponsePreprocessor mResponsePreprocessor;
    private boolean mSyncRequest = false;
    private int mDelay;
    private TimeUnit mDelayTimeUnit;
    private int mInterval;
    private TimeUnit mIntervalTimeUnit;
    private DownloadOptions mDownloadOptions;
    private HttpCallback<T> mHttpCallback;
    private boolean mCallbackOnMainThread = true;

    protected EasyHttp(Class<? extends T> target) {
        if (target == File.class || target == ResponseBody.class) {
            setResultConverter(new DefaultResBodyResultConverter<>(this, target));
        } else {
            setResultConverter(new DefaultStringResultConverter<>(target));
        }
    }

    public EasyHttp<T> setBaseUrl(String baseUrl) {
        this.mBaseUrl = baseUrl;
        return this;
    }

    public EasyHttp<T> setFullUrl(String url) {
        this.mFullUrl = url;
        return this;
    }

    public EasyHttp<T> setPath(String path) {
        this.mPath = path;
        return this;
    }

    public EasyHttp<T> with(Activity activity) {
        LifecycleRegistry.handle(activity, this);
        return this;
    }

    public EasyHttp<T> with(View view) {
        LifecycleRegistry.handle(view, this);
        return this;
    }

    public EasyHttp<T> with(Fragment fragment) {
        LifecycleRegistry.handle(fragment, this);
        return this;
    }

    public EasyHttp<T> with(Context context) {
        LifecycleRegistry.handle(context, this);
        return this;
    }

    public EasyHttp<T> setTag(Object tag) {
        this.mTag = tag;
        return this;
    }

    public EasyHttp<T> addParam(String key, Object value) {
        ensureInnerParamsNotNull();
        mInnerParams.put(key, value);
        return this;
    }

    public EasyHttp<T> setParams(Map<String, Object> params) {
        this.mInnerParams = params;
        return this;
    }

    public EasyHttp<T> addParams(Map<String, Object> params) {
        ensureInnerParamsNotNull();
        this.mInnerParams.putAll(params);
        return this;
    }

    public EasyHttp<T> addHeaderParam(String key, Object value) {
        ensureInnerHeaderParamsNotNull();
        mInnerHeaderParams.put(key, value);
        return this;
    }

    public EasyHttp<T> setHeaderParams(Map<String, Object> params) {
        this.mInnerHeaderParams = params;
        return this;
    }

    public static void initialize(InitSettings settings) {
        Retrofit retrofit = RetrofitFactory.create(settings);
        RequestManager.getInstance().retrofit(retrofit);
    }

    public EasyHttp<T> bindLifecycle(LifecycleProvider provider) {
        mLifeProvider = provider;
        return this;
    }

    public EasyHttp<T> bindLifecycleUtilEvent(LifecycleTransformer transformer) {
        mLifeTransformer = transformer;
        return this;
    }

    public EasyHttp<T> addParamsInterceptor(ParamsInterceptor interceptor) {
        addParameterInterceptor(interceptor);
        return this;
    }

    public EasyHttp<T> addHeaderParamsInterceptor(ParamsInterceptor interceptor) {
        addHeaderParameterInterceptor(interceptor);
        return this;
    }

    public EasyHttp<T> retrofit(Retrofit retrofit) {
        mRetrofit = retrofit;
        return this;
    }

    public EasyHttp<T> setRetryOptions(RetryOptions options) {
        mRetryOptions = options;
        return this;
    }

    public EasyHttp<T> setResultConverter(ResultConverter converter) {
        mResultConverter = converter;
        return this;
    }

    public EasyHttp<T> setRequestBodyCreator(RequestBodyCreator creator) {
        mRequestBodyCreator = creator;
        return this;
    }

    public EasyHttp<T> delay(int delay, TimeUnit unit) {
        mDelay = delay;
        mDelayTimeUnit = unit;
        return this;
    }

    public EasyHttp<T> interval(int interval, TimeUnit unit) {
        mInterval = interval;
        mIntervalTimeUnit = unit;
        return this;
    }

    public void post() {
        method(METHOD_POST);
        new RealAsyncRequester(this).post();
    }

    public void get() {
        method(METHOD_GET);
        new RealAsyncRequester(this).get();
    }

    private void ensureDownloadOptionsNotNull() {
        if (mDownloadOptions == null) {
            mDownloadOptions = new DownloadOptions();
        }
    }

    public EasyHttp<T> targetFile(File targetFile) {
        ensureDownloadOptionsNotNull();
        mDownloadOptions.setTargetFile(targetFile);
        return this;
    }

    public void download(DownloadCallback callback) {
        ensureDownloadOptionsNotNull();
        mDownloadOptions.setCallback(callback);
        new RealAsyncRequester(this).download();
    }

    public SyncRequester<T> sync() {
        mSyncRequest = true;
        return new RealSyncRequester<>(this);
    }

    public String getFullUrl() {
        if (mFullUrl != null) {
            return mFullUrl;
        }
        if (mBaseUrl != null) {
            return mBaseUrl + mPath;
        }
        return mPath;
    }

    public EasyHttp<T> setResponsePreprocessor(ResponsePreprocessor responsePreprocessor) {
        this.mResponsePreprocessor = responsePreprocessor;
        return this;
    }

    public LifecycleProvider getLifeProvider() {
        return mLifeProvider;
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }

    public RetryOptions getRetryOptions() {
        return mRetryOptions;
    }

    public RequestBodyCreator getRequestBodyCreator() {
        return mRequestBodyCreator;
    }

    public ResultConverter getResultConverter() {
        return mResultConverter;
    }

    public ResponsePreprocessor getResponsePreprocessor() {
        return mResponsePreprocessor;
    }

    public List<ParamsInterceptor> getParamsInterceptors() {
        return mParamsInterceptors;
    }

    public boolean isSyncRequest() {
        return mSyncRequest;
    }

    public int getDelay() {
        return mDelay;
    }

    private void ensureInnerParamsNotNull() {
        if (mInnerParams == null) {
            mInnerParams = new ArrayMap<>();
        }
    }

    private void ensureInnerHeaderParamsNotNull() {
        if (mInnerHeaderParams == null) {
            mInnerHeaderParams = new ArrayMap<>();
        }
    }

    private void ensureParametersNotNull() {
        if (mParamsInterceptors == null) {
            mParamsInterceptors = new LinkedList<>();
        }
    }

    private void ensureHeaderParametersNotNull() {
        if (mHeaderParamsInterceptors == null) {
            mHeaderParamsInterceptors = new LinkedList<>();
        }
    }

    private void addParameterInterceptor(ParamsInterceptor interceptor) {
        ensureParametersNotNull();
        if (!mParamsInterceptors.contains(interceptor)) {
            mParamsInterceptors.add(interceptor);
        }
    }

    private void addHeaderParameterInterceptor(ParamsInterceptor interceptor) {
        ensureHeaderParametersNotNull();
        if (!mHeaderParamsInterceptors.contains(interceptor)) {
            mHeaderParamsInterceptors.add(interceptor);
        }
    }

    public Map<String, Object> getInnerParams() {
        return mInnerParams;
    }

    public Map<String, Object> getInnerHeaderParams() {
        return mInnerHeaderParams;
    }

    public List<ParamsInterceptor> getHeaderParamsInterceptors() {
        return mHeaderParamsInterceptors;
    }

    public int getInterceptorsParamsSize() {
        if (mParamsInterceptors == null || mParamsInterceptors.isEmpty()) {
            return 0;
        }
        int size = 0;
        for (ParamsInterceptor interceptor : mParamsInterceptors) {
            size += interceptor.getParamsSize();
        }
        return size;
    }

    public int getHeaderInterceptorsParamsSize() {
        if (mHeaderParamsInterceptors == null || mHeaderParamsInterceptors.isEmpty()) {
            return 0;
        }
        int size = 0;
        for (ParamsInterceptor interceptor : mHeaderParamsInterceptors) {
            size += interceptor.getParamsSize();
        }
        return size;
    }

    public EasyHttp<T> method(String method) {
        this.method = method;
        return this;
    }

    public void execute() {
        switch (method) {
            case METHOD_GET:
                get();
                break;
            case METHOD_POST:
                post();
                break;
            default:
                break;
        }
    }

    public <V> MergeRequester<T, V> merge(EasyHttp<V> v) {
        return new RealMergeRequester<>(this, v);
    }

    public <V, W> TripleRequester<T, V, W> merge(EasyHttp<V> v, EasyHttp<W> r) {
        return new RealTripleRequester<>(this, v, r);
    }

    public DownloadOptions getDownloadOptions() {
        return mDownloadOptions;
    }

    public HttpCallback<T> getHttpCallback() {
        return mHttpCallback;
    }

    public LifecycleTransformer getLifeTransformer() {
        return mLifeTransformer;
    }

    public static void cancelByTag(Object tag) {
        RequestMapping.getInstance().cancelByTag(tag);
    }

    public static void cancelAll() {
        RequestMapping.getInstance().cancelAll();
    }

    public Object getTag() {
        return mTag;
    }

    public String getMethod() {
        return method;
    }

    public EasyHttp<T> callback(HttpCallback<T> callback) {
        mHttpCallback = callback;
        return this;
    }

    public int getInterval() {
        return mInterval;
    }

    public TimeUnit getIntervalTimeUnit() {
        return mIntervalTimeUnit;
    }

    public TimeUnit getDelayTimeUnit() {
        return mDelayTimeUnit;
    }

    public Observable toObservable() {
        return RequestManager.getInstance().wrapObservable(this);
    }

    public EasyHttp<T> setCallbackOnMainThread(boolean callbackOnMainThread) {
        this.mCallbackOnMainThread = callbackOnMainThread;
        return this;
    }

    public boolean isCallbackOnMainThread() {
        return mCallbackOnMainThread;
    }
}
