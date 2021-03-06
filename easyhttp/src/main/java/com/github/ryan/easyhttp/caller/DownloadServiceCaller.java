package com.github.ryan.easyhttp.caller;

import com.github.ryan.easyhttp.EasyHttp;
import com.github.ryan.easyhttp.service.InnerService;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Retrofit;

/**
 * Created by Ryan
 * at 2019/9/23
 */
public class DownloadServiceCaller extends BaseServiceCaller {

    public DownloadServiceCaller(Retrofit retrofit) {
        super(retrofit);
    }

    @Override
    public Observable call(EasyHttp http) {
        final InnerService service = mRetrofit.create(InnerService.class);
        Map<String, Object> header = generateHeaderMap(http);
        Map<String, Object> queryMap = generateParams(http);
        return service.download(http.getFullUrl(), header, queryMap);
    }
}