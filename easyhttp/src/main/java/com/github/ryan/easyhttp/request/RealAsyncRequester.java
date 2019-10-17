package com.github.ryan.easyhttp.request;

import com.github.ryan.easyhttp.EasyHttp;
import com.github.ryan.easyhttp.request.intf.AsyncRequester;

/**
 * Created by Ryan
 * at 2019/9/21
 */
public class RealAsyncRequester extends BaseRequester implements AsyncRequester {

    public RealAsyncRequester(EasyHttp http) {
        super(http);
    }

    @Override
    public void get() {
        RequestManager.getInstance().execute(getHttp(), RequestManager.METHOD_GET);
    }

    @Override
    public void post() {
        RequestManager.getInstance().execute(getHttp(), RequestManager.METHOD_POST);
    }

    @Override
    public void download() {
        if (getHttp().getDownloadOptions() == null) {
            throw new IllegalStateException();
        }
        RequestManager.getInstance().download(getHttp());
    }
}
