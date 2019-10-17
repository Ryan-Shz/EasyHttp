package com.github.ryan.easyhttp.chain;

import com.github.ryan.easyhttp.EasyHttp;
import com.github.ryan.easyhttp.options.ResponsePreprocessor;
import com.github.ryan.easyhttp.options.StringResponsePreprocessor;

import io.reactivex.Observable;

/**
 * Created by Ryan
 * at 2019/9/21
 */
public class StringResPreprocessorChain extends BaseObservableChain {

    StringResPreprocessorChain(EasyHttp http) {
        super(http);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Observable process(Observable observable) {
        ResponsePreprocessor interceptor = getHttp().getResponsePreprocessor();
        if (interceptor != null) {
            return observable.map(new StringResponsePreprocessor(interceptor));
        }
        return observable;
    }
}
