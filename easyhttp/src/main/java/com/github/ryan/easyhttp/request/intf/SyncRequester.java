package com.github.ryan.easyhttp.request.intf;

/**
 * Created by Ryan
 * at 2019/9/21
 */
public interface SyncRequester<T> {

    T get();
    T post();
    T download();
}
