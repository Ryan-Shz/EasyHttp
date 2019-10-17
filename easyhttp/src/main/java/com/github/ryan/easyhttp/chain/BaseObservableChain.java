package com.github.ryan.easyhttp.chain;

import com.github.ryan.easyhttp.EasyHttp;

/**
 * Created by Ryan
 * at 2019/9/24
 */
public abstract class BaseObservableChain implements ObservableChain {

    private EasyHttp mHttp;

    public BaseObservableChain(EasyHttp http){
        mHttp = http;
    }

    public EasyHttp getHttp() {
        return mHttp;
    }
}
