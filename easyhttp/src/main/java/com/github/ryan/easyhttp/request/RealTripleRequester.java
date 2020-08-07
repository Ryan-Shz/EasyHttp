package com.github.ryan.easyhttp.request;

import com.github.ryan.easyhttp.EasyHttp;
import com.github.ryan.easyhttp.callback.HttpCallback;
import com.github.ryan.easyhttp.multiple.TripleSource;
import com.github.ryan.easyhttp.request.intf.TripleRequester;

/**
 * Created by Ryan
 * on 2020/8/6
 */
public class RealTripleRequester<T, V, R> implements TripleRequester<T, V, R> {

    private EasyHttp<T> t;
    private EasyHttp<V> v;
    private EasyHttp<R> r;

    public RealTripleRequester(EasyHttp<T> t, EasyHttp<V> v, EasyHttp<R> r) {
        this.t = t;
        this.v = v;
        this.r = r;
    }

    @Override
    public void execute() {
        execute(null);
    }

    @Override
    public void execute(HttpCallback<TripleSource<T, V, R>> callback) {
        RequestManager.getInstance().mergeRequest(t, v, r, callback);
    }
}
