package com.github.ryan.easyhttp.callback;

import com.github.ryan.easyhttp.EasyHttp;

import java.util.Collections;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 请求回调管理，不处理服务器异常，由IHttpCallback的子类扩展实现
 * <p>
 * create by Ryan
 * 2018/6/11
 */
public class BaseObserver<T> implements Observer<T> {

    private IHttpCallback<T> mCallback;
    private List<Object> mTagList;

    public BaseObserver(EasyHttp<T> http) {
        if (http != null) {
            mTagList = Collections.singletonList(http.getTag());
            mCallback = http.getHttpCallback();
        }
    }

    public BaseObserver(IHttpCallback<T> callback, Object tag) {
        mCallback = callback;
        if (tag != null) {
            mTagList = Collections.singletonList(tag);
        }
    }

    public BaseObserver(IHttpCallback<T> callback, List<Object> tags) {
        mCallback = callback;
        mTagList = tags;
    }

    @Override
    public void onSubscribe(Disposable d) {
        startRequest(d);
        if (mCallback != null) {
            mCallback.onStart();
        }
    }

    @Override
    public void onNext(T response) {
        if (mCallback == null) {
            return;
        }
        if (response == null) {
            mCallback.onFailure(new IllegalArgumentException("response is null!"));
            return;
        }
        mCallback.onSuccess(response);
    }

    @Override
    public void onError(Throwable e) {
        try {
            if (mCallback != null) {
                mCallback.onFailure(e);
            }
        } finally {
            finishRequest();
        }
    }

    @Override
    public void onComplete() {
        finishRequest();
    }

    private void startRequest(Disposable d) {
        if (mTagList == null) {
            return;
        }
        for (Object tag : mTagList) {
            RequestMapping.getInstance().beginRequest(tag, d);
        }
    }

    private void finishRequest() {
        if (mTagList == null) {
            return;
        }
        for (Object tag : mTagList) {
            RequestMapping.getInstance().finishRequest(tag);
        }
    }
}
