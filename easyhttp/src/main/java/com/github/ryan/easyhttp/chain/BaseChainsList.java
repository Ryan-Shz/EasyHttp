package com.github.ryan.easyhttp.chain;

import android.support.annotation.NonNull;

import com.github.ryan.easyhttp.EasyHttp;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Ryan
 * at 2019/9/23
 */
public abstract class BaseChainsList<T> implements ChainsList<T> {

    private List<ObservableChain> mChainList;
    private EasyHttp mHttp;

    protected BaseChainsList(@NonNull EasyHttp http) {
        mHttp = http;
        mChainList = new ArrayList<>();
        initBaseChains();
        initExtendChains();
    }

    protected void addChain(ObservableChain chain) {
        if (!mChainList.contains(chain)) {
            mChainList.add(chain);
        }
    }

    protected EasyHttp getHttp() {
        return mHttp;
    }

    private void initBaseChains() {
        EasyHttp http = getHttp();
        addChain(new SubscribeOnChain(http));
        addChain(new RetryChain(http));
        addChain(new LifeCycleChain(http));
        addChain(new DelayChain(http));
    }

    protected abstract void initExtendChains();

    @SuppressWarnings("unchecked")
    @Override
    public T start(Observable observable) {
        Observable current = observable;
        for (ObservableChain chain : mChainList) {
            current = chain.process(current);
            if (current == null) {
                break;
            }
        }
        if (current != null) {
            return (T) current.blockingFirst();
        }
        return null;
    }

}
