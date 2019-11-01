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
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class MetricsMXRecord {
    private final Map<String, CountMetricsBean> metricsBeans;
    private final Set<ObjectName> objectNames;

    private static final Logger LOG = LoggerFactory.getLogger(MetricsMXRecord.class);

    private final ReentrantLock lock = new ReentrantLock();
    private final Executor executor = Executors.newSingleThreadExecutor();


    private final MBeanRegistry mBeanRegistry;

    public MetricsMXRecord() {
        this.metricsBeans = new ConcurrentHashMap<>();
        this.objectNames = new HashSet<>();
        this.mBeanRegistry = MBeanRegistry.getInstance();
        this.mBeanRegistry.setSwallowedExceptionListener(e -> LOG.warn(e.getMessage()));
    }

    public void incr(String metricsType, String name) {
        String key = metricsKey(metricsType, name);
        CountMetricsBean metricsBean = metricsBeans.get(key);
        if (metricsBean == null) {
            lock.lock();
            try {
                if (metricsBean == null) {
                    metricsBean = new CountMetricsBean(name);
                    metricsBeans.putIfAbsent(key, metricsBean);
                    asyncRegister(metricsType, metricsBean);
                }
            } finally {
                lock.unlock();
            }
        }
        metricsBean.incr();
    }

    private String metricsKey(String metricsType, String name) {
        return new StringBuilder(name)
                .append("@")
                .append(metricsType)
                .toString();
    }

    private void register(String metricsType, MetricsMXBean mxBean) {
        try {
            ObjectName oname = mBeanRegistry.register(CountMetricsBean.DEFAULT_ONAME_BASE,
                    metricsType, mxBean);
            this.objectNames.add(oname);
        } catch (Exception e) {
            //handle the exception, can not interrupt thread because exception
            LOG.error("register mxBean occurs error", e);
        }
    }

    private void asyncRegister(final String metricsType, final MetricsMXBean mxBean) {
        executor.execute(() -> {
            register(metricsType, mxBean);
        });
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
