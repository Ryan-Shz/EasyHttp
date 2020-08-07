package com.github.ryan.easyhttp.callback;

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

    void beginRequest(Object tag, Disposable disposable) {
        if (tag == null) {
            return;
        }
        mRequestMap.put(tag, disposable);
    }

    void finishRequest(Object tag) {
        if (tag == null) {
            return;
        }
        // just remove
        mRequestMap.remove(tag);
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