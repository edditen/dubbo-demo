package com.tenchael.dubbo.plugin.jmx;

public interface MetricsMXBean {

    String getCategory();

    String getName();

    long getValue();

}
