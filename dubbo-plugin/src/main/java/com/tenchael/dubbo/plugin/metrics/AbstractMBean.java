package com.tenchael.dubbo.plugin.metrics;

import com.tenchael.dubbo.plugin.jmx.MBean;

public abstract class AbstractMBean implements MBean {

    private String category;
    private String name;

    public AbstractMBean(String category, String name) {
        this.category = category;
        this.name = name;
    }


    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public String getName() {
        return name;
    }
}
