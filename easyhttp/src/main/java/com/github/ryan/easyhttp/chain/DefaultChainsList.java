package com.github.ryan.easyhttp.chain;

import com.github.ryan.easyhttp.EasyHttp;

/**
 * Created by Ryan
 * at 2019/9/20
 */
public class DefaultChainsList<T> extends BaseChainsList<T> {

    public DefaultChainsList(EasyHttp http) {
        super(http);
    }

    @Override
    protected void initExtendChains() {
        addChain(new StringResPreprocessorChain(getHttp()));
        addChain(new MapChain(getHttp()));
    }
}
