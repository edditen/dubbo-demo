package com.tenchael.dubbo.plugin.metrics;

import com.tenchael.dubbo.plugin.jmx.CounterMXBean;

import java.util.concurrent.atomic.AtomicLong;

public class Counter implements CounterMXBean {

    public static final String DEFAULT_ONAME_BASE =
            "com.tenchael.dubbo.plugin.metrics:type=Counter,name=";

    private final AtomicLong count;

    private String category;
    private String name;

    public Counter(String name) {
        this(null, name);
    }

    public Counter(String category, String name) {
        this(category, name, 0);
    }

    public Counter(String category, String name, long initValue) {
        this.category = category;
        this.name = name;
        this.count = new AtomicLong(initValue);
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public long getCount() {
        return this.count.get();
    }

    @Override
    public void incr() {
        this.count.incrementAndGet();
    }

    @Override
    public void incr(long delta) {
        this.count.addAndGet(delta);
    }

    @Override
    public void decr() {
        this.count.decrementAndGet();
    }

    @Override
    public void decr(long delta) {
        this.count.addAndGet(-delta);
    }
}
