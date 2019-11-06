package com.tenchael.dubbo.plugin.jmx;

public interface GaugeMXBean<T> extends MBean {

    T getValue();
}
