package com.github.ryan.easyhttp.multiple;

/**
 * Created by Ryan
 * on 2020/8/6
 */
public class MergeSource<T, V> {

    private T t;
    private V v;

    public MergeSource(T t, V v) {
        this.t = t;
        this.v = v;
    }

    public void setT(T t) {
        this.t = t;
    }

    public void setV(V v) {
        this.v = v;
    }

    public T getT() {
        return t;
    }

    public V getV() {
        return v;
    }
}
