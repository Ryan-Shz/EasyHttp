package com.github.ryan.easyhttp.chain.download;

import com.github.ryan.easyhttp.EasyHttp;
import com.github.ryan.easyhttp.callback.BaseObserver;
import com.github.ryan.easyhttp.callback.DownloadCallback;
import com.github.ryan.easyhttp.chain.BaseObservableChain;
import com.github.ryan.easyhttp.converter.ResBodyResultConverter;
import com.github.ryan.easyhttp.converter.ResultConverter;
import com.github.ryan.easyhttp.options.DownloadOptions;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Ryan
 * at 2019/9/21
 */
public class DownloadSubscribeChain extends BaseObservableChain {

    DownloadSubscribeChain(EasyHttp http) {
        super(http);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Observable process(Observable observable) {
        EasyHttp http = getHttp();
        boolean isSync = http.isSyncRequest();
        ResultConverter resultConverter = http.getResultConverter();
        Observable syncObservable = observable.map(new ResBodyResultConverter(resultConverter));
        if (isSync) {
            return syncObservable;
        }
        DownloadOptions options = http.getDownloadOptions();
        DownloadCallback callback = options.getCallback();
        syncObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver(callback));
        return null;
    }
}
