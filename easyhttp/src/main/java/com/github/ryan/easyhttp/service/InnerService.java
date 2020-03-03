package com.github.ryan.easyhttp.service;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * create by Ryan
 * 2018/6/11
 */
public interface InnerService {

    @GET
    Observable<Response<String>> get(@Url String url, @HeaderMap Map<String, Object> headers, @QueryMap Map<String, Object> fields);

    @POST
    Observable<Response<String>> post(@Url String url, @HeaderMap Map<String, Object> headers, @Body RequestBody requestBody);

    @Streaming
    @GET
    Observable<Response<ResponseBody>> download(@Url String url, @HeaderMap Map<String, Object> headers, @QueryMap Map<String, Object> fields);

}
