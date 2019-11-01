package com.tenchael.dubbo.plugin.metrics;

import com.tenchael.dubbo.plugin.jmx.MetricsMXBean;

import java.util.concurrent.atomic.AtomicLong;

public class CountMetricsBean implements MetricsMXBean {

    public static final String DEFAULT_ONAME_BASE =
            "com.tenchael.dubbo.plugin.metrics:type=CountMetricsBean,name=";

    public static final String DEFAULT_JMX_NAME_PREFIX = "metrics";

    private String name;

    private final AtomicLong count;

    public CountMetricsBean(String name) {
        this.name = name;
        this.count = new AtomicLong(0);
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

}
