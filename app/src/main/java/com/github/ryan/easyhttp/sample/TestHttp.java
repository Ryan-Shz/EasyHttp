package com.github.ryan.easyhttp.sample;

import com.github.ryan.easyhttp.EasyHttp;

/**
 * Created by Ryan
 * at 2019/9/25
 */
public class TestHttp<T> extends EasyHttp<T> {

    public static <T> TestHttp<T> from(Class<? extends T> targetClass) {
        return new TestHttp<>(targetClass);
    }

    private TestHttp(Class<? extends T> target) {
        super(target);
    }

}
