package com.github.ryan.easyhttp.chain;

import com.github.ryan.easyhttp.EasyHttp;
import com.github.ryan.easyhttp.callback.BaseObserver;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Ryan
 * at 2019/9/23
 */
public abstract class BaseChainsList<T> implements ChainsList<T> {

    private List<ObservableChain> mChainList;
    private EasyHttp mHttp;

    protected BaseChainsList(EasyHttp http) {
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
        Observable current = wrap(observable);
        if (mHttp.isSyncRequest()) {
            return (T) current.blockingFirst();
        }
        if (mHttp.isCallbackOnMainThread()) {
            current = current.observeOn(AndroidSchedulers.mainThread());
        }
        current.subscribe(new BaseObserver(getHttp()));
        return null;
    }

    @Override
    public Observable wrap(Observable observable) {
        Observable current = observable;
        for (ObservableChain chain : mChainList) {
            current = chain.process(current);
            if (current == null) {
                break;
            }
        }
        return current;
    }
}
