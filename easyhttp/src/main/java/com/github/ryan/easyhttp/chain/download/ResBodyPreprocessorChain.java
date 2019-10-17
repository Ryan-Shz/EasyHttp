package com.github.ryan.easyhttp.chain.download;

import com.github.ryan.easyhttp.EasyHttp;
import com.github.ryan.easyhttp.chain.BaseObservableChain;
import com.github.ryan.easyhttp.options.ResBodyResponsePreprocessor;
import com.github.ryan.easyhttp.options.ResponsePreprocessor;

import io.reactivex.Observable;

/**
 * Created by Ryan
 * at 2019/9/21
 */
public class ResBodyPreprocessorChain extends BaseObservableChain {

    ResBodyPreprocessorChain(EasyHttp http) {
        super(http);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Observable process(Observable observable) {
        ResponsePreprocessor interceptor = getHttp().getResponsePreprocessor();
        if (interceptor != null) {
            return observable.map(new ResBodyResponsePreprocessor(interceptor));
        }
        return observable;
    }
}
