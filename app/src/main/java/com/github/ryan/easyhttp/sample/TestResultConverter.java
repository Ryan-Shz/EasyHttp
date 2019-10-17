package com.github.ryan.easyhttp.sample;

import com.github.ryan.easyhttp.converter.DefaultStringResultConverter;

/**
 * Created by Ryan
 * at 2019/9/25
 */
public class TestResultConverter<T> extends DefaultStringResultConverter<T> {

    TestResultConverter(Class<? extends T> targetClass) {
        super(targetClass);
    }

    @Override
    protected String preProcessResult(String original) {
        return super.preProcessResult(original);
    }
}
