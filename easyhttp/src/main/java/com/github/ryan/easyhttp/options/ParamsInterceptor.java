package com.github.ryan.easyhttp.options;

import java.util.Map;


/**
 * 参数拦截器，自定义添加参数过程
 *
 * create by Ryan
 * 2018/6/11
 */
public interface ParamsInterceptor {

    /**
     * 将请求所需参数添加至params中
     *
     * @param params 参数集合
     */
    void process(Map<String, Object> params);

    /**
     * 返回此参数拦截器即将添加的参数数量
     * 准确返回参数数量可以优化请求的参数构建
     *
     * @return 即将添加的参数数量
     */
    int getParamsSize();

}
