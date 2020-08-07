package com.github.ryan.easyhttp.request.intf;

import com.github.ryan.easyhttp.callback.HttpCallback;
import com.github.ryan.easyhttp.multiple.MergeSource;

/**
 * Created by Ryan
 * on 2020/8/6
 */
public interface MergeRequester<T, V> {

    void execute();

    void execute(HttpCallback<MergeSource<T, V>> callback);

}
