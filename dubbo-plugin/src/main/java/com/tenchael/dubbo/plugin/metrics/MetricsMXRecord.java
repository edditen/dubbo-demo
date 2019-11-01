package com.tenchael.dubbo.plugin.metrics;

import com.tenchael.dubbo.plugin.jmx.MBeanRegistry;
import com.tenchael.dubbo.plugin.jmx.MetricsMXBean;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;

import javax.management.ObjectName;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class MetricsMXRecord {

    private final Map<String, CountMetricsBean> metricsBeans;
    private final Set<ObjectName> objectNames;

    private static final Logger LOG = LoggerFactory.getLogger(MetricsMXRecord.class);

    private final Object lock = new Object();


    private final MBeanRegistry mBeanRegistry;

    public MetricsMXRecord() {
        this.metricsBeans = new ConcurrentHashMap<>();
        this.objectNames = new HashSet<>();
        this.mBeanRegistry = MBeanRegistry.getInstance();
        this.mBeanRegistry.setSwallowedExceptionListener(e -> LOG.warn(e.getMessage()));
    }

    public void incr(String metricsType, String name) {
        String metricsKey = new StringBuilder(name)
                .append("@")
                .append(metricsType)
                .toString();
        CountMetricsBean metricsBean = metricsBeans.get(metricsKey);
        if (metricsBean == null) {
            synchronized (lock) {
                if (metricsBean == null) {
                    metricsBean = new CountMetricsBean(name);
                    register(metricsType, metricsBean);
                    metricsBeans.putIfAbsent(metricsKey, metricsBean);
                }
            }

        }
        metricsBean.incr();
    }

    private void register(String metricsType, MetricsMXBean mxBean) {
        ObjectName oname = mBeanRegistry.register(CountMetricsBean.DEFAULT_ONAME_BASE,
                metricsType, mxBean);
        this.objectNames.add(oname);
    }

    private void unregister(ObjectName oname) {
        mBeanRegistry.unregister(oname);
    }

    public void close() {
        for (ObjectName oname : objectNames) {
            unregister(oname);
        }
    }


}
