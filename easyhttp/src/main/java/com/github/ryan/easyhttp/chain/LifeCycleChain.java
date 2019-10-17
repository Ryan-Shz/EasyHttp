package com.github.ryan.easyhttp.chain;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.github.ryan.easyhttp.EasyHttp;

import io.reactivex.Observable;

/**
 * Created by Ryan
 * at 2019/9/20
 */

public class LifeCycleChain extends BaseObservableChain {

    LifeCycleChain(EasyHttp http) {
        super(http);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Observable process(Observable observable) {
        EasyHttp http = getHttp();
        LifecycleTransformer transformer = http.getLifeTransformer();
        if (transformer != null) {
            return observable.compose(transformer);
        }
        LifecycleProvider provider = http.getLifeProvider();
        if (provider != null) {
            return observable.compose(provider.bindToLifecycle());
        }
        return observable;
    }
}
