package com.github.ryan.easyhttp.chain;

import com.github.ryan.easyhttp.EasyHttp;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Ryan
 * at 2019/9/20
 */
public class SubscribeOnChain extends BaseObservableChain {

    SubscribeOnChain(EasyHttp http) {
        super(http);
    }

    @Override
    public Observable process(Observable observable) {
        return getHttp().isSyncRequest() ? observable : observable.subscribeOn(Schedulers.io());
    }
}
