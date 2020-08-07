package com.github.ryan.easyhttp.chain;

import io.reactivex.Observable;

/**
 * Created by Ryan
 * at 2019/9/23
 */
public interface ChainsList<T> {

    T start(Observable observable);

    Observable wrap(Observable observable);
}
