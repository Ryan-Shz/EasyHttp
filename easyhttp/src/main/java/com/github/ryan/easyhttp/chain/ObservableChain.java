package com.github.ryan.easyhttp.chain;

import io.reactivex.Observable;

/**
 * Created by Ryan
 * at 2019/9/20
 */
public interface ObservableChain {

    Observable process(Observable observable);

}
