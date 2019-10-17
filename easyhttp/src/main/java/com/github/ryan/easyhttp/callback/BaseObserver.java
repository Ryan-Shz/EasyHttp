package com.github.ryan.easyhttp.callback;

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

    public BaseObserver(IHttpCallback<T> callback) {
        mCallback = callback;
    }

    @Override
    public void onSubscribe(Disposable d) {
        if (mCallback != null) {
            mCallback.onStart();
        }
    }

    @Override
    public void onNext(T response) {
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
        if (mCallback != null) {
            mCallback.onFailure(e);
        }
    }

    @Override
    public void onComplete() {

    }
}
