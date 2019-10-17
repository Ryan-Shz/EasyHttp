package com.github.ryan.easyhttp.options;

import retrofit2.Response;

/**
 * create by Ryan
 * 2018/7/23
 */
public interface ResponsePreprocessor {

    void process(Response response);

}
