package com.github.ryan.easyhttp.multiple;

/**
 * Created by Ryan
 * on 2020/8/6
 */
public class TripleSource<T, V, R> {

    private T t;
    private V v;
    private R r;

    public TripleSource(T t, V v, R r) {
        this.t = t;
        this.v = v;
        this.r = r;
    }

    public T getT() {
        return t;
    }

    public V getV() {
        return v;
    }

    public R getR() {
        return r;
    }
}
