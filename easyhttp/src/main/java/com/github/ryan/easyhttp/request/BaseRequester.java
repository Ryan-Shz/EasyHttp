package com.github.ryan.easyhttp.request;

import com.github.ryan.easyhttp.EasyHttp;

/**
 * Created by Ryan
 * at 2019/9/24
 */
public abstract class BaseRequester {

    private EasyHttp mHttp;

    BaseRequester(EasyHttp http){
        mHttp = http;
    }

    public EasyHttp getHttp() {
        return mHttp;
    }
}
