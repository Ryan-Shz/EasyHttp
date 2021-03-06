package com.github.ryan.easyhttp.chain;

import com.github.ryan.easyhttp.EasyHttp;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

/**
 * Created by Ryan
 * at 2019/9/20
 */
public class DelayChain extends BaseObservableChain {

    DelayChain(EasyHttp http) {
        super(http);
    }

    @Override
    public Observable process(Observable observable) {
        EasyHttp http = getHttp();
        int delay = http.getDelay();
        return delay > 0 ? observable.delay(delay, http.getDelayTimeUnit()) : observable;
    }
}
