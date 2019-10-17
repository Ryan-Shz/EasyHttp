package com.github.ryan.easyhttp.caller;

import com.github.ryan.easyhttp.EasyHttp;
import com.github.ryan.easyhttp.service.InnerService;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Retrofit;

/**
 * Created by Ryan
 * at 2019/9/20
 */
public class GetRequestCaller extends BaseServiceCaller {

    public GetRequestCaller(Retrofit retrofit) {
        super(retrofit);
    }

    @Override
    public Observable call(EasyHttp http) {
        final InnerService service = mRetrofit.create(InnerService.class);
        Map<String, String> header = generateHeaderMap(http);
        Map<String, String> queryMap = generateParams(http);
        return service.get(http.getFullUrl(), header, queryMap);
    }


}
