package com.github.ryan.easyhttp.options;

/**
 * create by Ryan
 * 2018/6/12 上午10:00
 */
public class RetryOptions {

    private int count;
    private int delay;

    private RetryOptions() {

    }

    public static RetryOptions from(int count, int delay) {
        RetryOptions options = new RetryOptions();
        options.count = count;
        options.delay = delay;
        return options;
    }

    public int getCount() {
        return count;
    }

    public int getDelay() {
        return delay;
    }
}
