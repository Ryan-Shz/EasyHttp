package com.github.ryan.easyhttp.options;

import okhttp3.RequestBody;

/**
 * create by Ryan
 * 2018/6/12
 */
public interface RequestBodyCreator {

    RequestBody createRequestBody();

}
