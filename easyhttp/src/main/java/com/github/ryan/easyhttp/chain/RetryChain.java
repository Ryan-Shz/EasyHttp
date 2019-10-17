package com.github.ryan.easyhttp.chain;

import com.github.ryan.easyhttp.EasyHttp;
import com.github.ryan.easyhttp.options.RetryOptions;
import com.github.ryan.easyhttp.options.RetryWhenExceptionFunction;

import io.reactivex.Observable;

/**
 * Created by Ryan
 * at 2019/9/20
 */
public class RetryChain extends BaseObservableChain {

    RetryChain(EasyHttp http) {
        super(http);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Observable process(Observable observable) {
        RetryOptions retryOptions = getHttp().getRetryOptions();
        if (retryOptions != null) {
            int count = retryOptions.getCount();
            int delay = retryOptions.getDelay();
            return observable.retryWhen(new RetryWhenExceptionFunction(count, delay));
        }
        return observable;
    }
}
