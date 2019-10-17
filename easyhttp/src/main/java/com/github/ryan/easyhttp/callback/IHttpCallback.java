package com.github.ryan.easyhttp.callback;

/**
 * create by Ryan
 * 2018/6/11
 */
public interface IHttpCallback<T> {

    /**
     * 开始请求
     */
    void onStart();

    /**
     * 请求成功，返回数据
     *
     * @param t 数据
     */
    void onSuccess(T t);

    /**
     * 网络异常
     *
     * @param e 错误异常
     */
    void onFailure(Throwable e);

}
