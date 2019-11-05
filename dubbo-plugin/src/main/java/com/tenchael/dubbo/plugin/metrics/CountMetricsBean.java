package com.tenchael.dubbo.plugin.metrics;

import com.tenchael.dubbo.plugin.jmx.MetricsMXBean;

import java.util.concurrent.atomic.AtomicLong;

public class CountMetricsBean implements MetricsMXBean {

    public static final String DEFAULT_ONAME_BASE =
            "com.tenchael.dubbo.plugin.metrics:type=CountMetricsBean,name=";

    public static final String DEFAULT_JMX_NAME_PREFIX = "metrics";

    private String category;
    private String name;

    private final AtomicLong count;

    public CountMetricsBean(String name) {
        this(null, name);
    }

    public CountMetricsBean(String category, String name) {
        this.category = category;
        this.name = name;
        this.count = new AtomicLong(0);
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
    public long getValue() {
        return count.get();
    }

    public void incr() {
        this.count.incrementAndGet();
    }

    public void incr(long delta) {
        this.count.addAndGet(delta);
    }

}
