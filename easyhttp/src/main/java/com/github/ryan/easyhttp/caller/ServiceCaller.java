package com.github.ryan.easyhttp.caller;

import com.github.ryan.easyhttp.EasyHttp;

import io.reactivex.Observable;

/**
 * Created by Ryan
 * at 2019/9/20
 */
public interface ServiceCaller {

    Observable call(EasyHttp http);

}
