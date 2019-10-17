package com.github.ryan.easyhttp.converter;

/**
 * create by Ryan
 * 2018/6/12
 */
public interface ResultConverter<T, V> {

    T convert(V original) throws Exception;

}
