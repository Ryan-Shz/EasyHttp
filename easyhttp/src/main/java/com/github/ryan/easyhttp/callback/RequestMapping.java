package com.github.ryan.easyhttp.callback;

import com.github.ryan.easyhttp.EasyHttp;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.disposables.Disposable;

/**
 * 映射已发送的请求
 * <p>
 * Created by Ryan
 * on 2020/7/23
 */
public class RequestMapping {

    private Map<Object, Disposable> mRequestMap = new ConcurrentHashMap<>();

    public static RequestMapping getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final RequestMapping INSTANCE = new RequestMapping();
    }

    void saveRequest(EasyHttp http, Disposable disposable) {
        if (http == null || http.getTag() == null) {
            return;
        }
        mRequestMap.put(http.getTag(), disposable);
    }

    void removeRequest(EasyHttp http) {
        if (http == null || http.getTag() == null) {
            return;
        }
        cancelByTag(http.getTag());
    }

    public void cancelByTag(Object tag) {
        Disposable disposable = mRequestMap.remove(tag);
        if (disposable != null) {
            disposable.dispose();
        }
    }

    public void cancelAll() {
        for (Object key : mRequestMap.keySet()) {
            cancelByTag(key);
        }
        mRequestMap.clear();
    }
}
