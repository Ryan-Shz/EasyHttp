package com.github.ryan.easyhttp.options;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;

/**
 * create by Ryan
 * 2018/6/12
 */
public class RetryWhenExceptionFunction implements Function<Observable<? extends Throwable>, Observable<?>> {

    // 重试次数
    private int mRetryCount;
    // 延迟3s重试
    private long mRetryDelay;

    public RetryWhenExceptionFunction(int count, long delay) {
        this.mRetryCount = count;
        this.mRetryDelay = delay;
    }

    @Override
    public Observable<?> apply(Observable<? extends Throwable> observable) throws Exception {
        return observable
                .zipWith(Observable.range(1, mRetryCount + 1), new BiFunction<Throwable, Integer, Wrapper>() {
                    @Override
                    public Wrapper apply(Throwable throwable, Integer integer) throws Exception {
                        return new Wrapper(throwable, integer);
                    }
                }).flatMap(new Function<Wrapper, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Wrapper wrapper) throws Exception {
                        // 网络访问异常都是IOException或其子类
                        if (wrapper.throwable instanceof IOException && wrapper.index < mRetryCount + 1) {
                            return Observable.timer(mRetryDelay + (wrapper.index - 1) * mRetryDelay, TimeUnit.MILLISECONDS);
                        }
                        return Observable.error(wrapper.throwable);
                    }
                });
    }

    private class Wrapper {
        private int index;
        private Throwable throwable;

        Wrapper(Throwable throwable, int index) {
            this.index = index;
            this.throwable = throwable;
        }
    }

}

