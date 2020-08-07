package com.github.ryan.easyhttp.caller;

import com.github.ryan.easyhttp.EasyHttp;
import com.google.gson.Gson;
import com.github.ryan.easyhttp.service.InnerService;
import com.github.ryan.easyhttp.options.RequestBodyCreator;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;

/**
 * Created by Ryan
 * at 2019/9/20
 */
public class PostRequestCaller extends BaseServiceCaller {

    private Gson mConverter;

    public PostRequestCaller(Retrofit retrofit) {
        super(retrofit);
        mConverter = new Gson();
    }

    @Override
    public Observable call(EasyHttp http) {
        Map<String, Object> header = generateHeaderMap(http);
        RequestBody requestBody = processBody(http);
        final InnerService service = mRetrofit.create(InnerService.class);
        return service.post(http.getFullUrl(), header, requestBody);
    }

    // 处理请求体
    private <T> RequestBody processBody(EasyHttp http) {
        RequestBodyCreator creator = http.getRequestBodyCreator();
        RequestBody requestBody;
        Map<String, Object> params = generateParams(http);
        if (creator != null) {
            requestBody = creator.createRequestBody(params);
        } else {
            String content = null;
            if (params != null && !params.isEmpty()) {
                content = mConverter.toJson(params);
                params.clear();
            }
            if (content == null) {
                content = "{}";
            }
            requestBody = RequestBody.create(content, MediaType.parse("application/json;charset=utf-8"));
        }
        return requestBody;
    }

}
