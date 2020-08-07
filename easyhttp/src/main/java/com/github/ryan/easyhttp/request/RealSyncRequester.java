package com.github.ryan.easyhttp.request;

import com.github.ryan.easyhttp.EasyHttp;
import com.github.ryan.easyhttp.request.intf.SyncRequester;

/**
 * Created by Ryan
 * at 2019/9/21
 */
public class RealSyncRequester<T> extends BaseRequester implements SyncRequester<T> {

    public RealSyncRequester(EasyHttp http) {
        super(http);
    }

    @Override
    public T get() {
        return RequestManager.getInstance().execute(getHttp());
    }

    @Override
    public T post() {
        return RequestManager.getInstance().execute(getHttp());
    }

    @Override
    public T download() {
        EasyHttp http = getHttp();
        if (http == null || http.getDownloadOptions() == null) {
            throw new IllegalStateException();
        }
        return RequestManager.getInstance().download(getHttp());
    }
}
