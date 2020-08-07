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
public class MapChain extends BaseObservableChain {

    MapChain(EasyHttp http) {
        super(http);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Observable process(Observable observable) {
        return observable.map(new StringResultConverter<>(getHttp().getResultConverter()));
    }
}
