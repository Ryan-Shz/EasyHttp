package com.github.ryan.easyhttp.request;

import com.github.ryan.easyhttp.EasyHttp;
import com.github.ryan.easyhttp.callback.HttpCallback;
import com.github.ryan.easyhttp.multiple.MergeSource;
import com.github.ryan.easyhttp.request.intf.MergeRequester;

/**
 * Created by Ryan
 * on 2020/8/6
 */
public class RealMergeRequester<T, V> implements MergeRequester<T, V> {

    private EasyHttp<T> mMerge1;
    private EasyHttp<V> mMerge2;

    public RealMergeRequester(EasyHttp<T> merge1, EasyHttp<V> merge2) {
        mMerge1 = merge1;
        mMerge2 = merge2;
    }

    @Override
    public void execute() {
        execute(null);
    }

    @Override
    public void execute(HttpCallback<MergeSource<T, V>> callback) {
        RequestManager.getInstance().mergeRequest(mMerge1, mMerge2, callback);
    }
}
