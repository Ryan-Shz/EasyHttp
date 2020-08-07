package com.github.ryan.easyhttp.request.intf;


import com.github.ryan.easyhttp.callback.HttpCallback;
import com.github.ryan.easyhttp.multiple.TripleSource;

/**
 * Created by Ryan
 * on 2020/8/6
 */
public interface TripleRequester<T, V, R> {

    void execute();

    void execute(HttpCallback<TripleSource<T, V, R>> callback);

}
