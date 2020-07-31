package com.github.ryan.easyhttp.chain;

import com.github.ryan.easyhttp.EasyHttp;
import com.github.ryan.easyhttp.callback.BaseObserver;
import com.github.ryan.easyhttp.converter.StringResultConverter;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Ryan
 * at 2019/9/21
 */
public class SubscribeChain extends BaseObservableChain {

    SubscribeChain(EasyHttp http) {
        super(http);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Observable process(Observable observable) {
        boolean isSync = getHttp().isSyncRequest();
        Observable syncObservable = observable.map(new StringResultConverter<>(getHttp().getResultConverter()));
        if (isSync) {
            return syncObservable;
        }
        syncObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver(getHttp()));
        return null;
    }
}
