package com.tenchael.dubbo.plugin.jmx;

public interface CounterMXBean extends Metric {

    long getCount();

    void incr();

    void incr(long delta);

    void decr();

    void decr(long delta);
}
