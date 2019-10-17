package com.github.ryan.easyhttp.chain.download;

import com.github.ryan.easyhttp.EasyHttp;
import com.github.ryan.easyhttp.chain.BaseChainsList;

/**
 * Created by Ryan
 * at 2019/9/20
 */
public class DownloadChainsList<T> extends BaseChainsList<T> {

    public DownloadChainsList(EasyHttp http) {
        super(http);
    }

    @Override
    protected void initExtendChains() {
        addChain(new ResBodyPreprocessorChain(getHttp()));
        addChain(new DownloadSubscribeChain(getHttp()));
    }
}
