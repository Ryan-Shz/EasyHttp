package com.github.ryan.easyhttp.callback;

import com.github.ryan.easyhttp.EasyHttp;

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
    private EasyHttp<T> mHttp;

    public BaseObserver(EasyHttp<T> http) {
        mCallback = http.getHttpCallback();
        mHttp = http;
    }


    public BaseObserver(EasyHttp<T> http, HttpCallback<T> callback) {
        mCallback = callback;
    }

    @Override
    public void onSubscribe(Disposable d) {
        RequestMapping.getInstance().saveRequest(mHttp, d);
        if (mCallback != null) {
            mCallback.onStart();
        }
    }

    @Override
    public void onNext(T response) {
        release();
        if (response == null) {
            return;
        }
        if (mCallback == null) {
            return;
        }
        mCallback.onSuccess(response);
    }

    @Override
    public void onError(Throwable e) {
        release();
        if (mCallback != null) {
            mCallback.onFailure(e);
        }
    }

    @Override
    public void onComplete() {

    }

    private void release() {
        RequestMapping.getInstance().removeRequest(mHttp);
    }
}
