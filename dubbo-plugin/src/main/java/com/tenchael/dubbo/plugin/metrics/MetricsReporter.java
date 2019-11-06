package com.tenchael.dubbo.plugin.metrics;

import com.tenchael.dubbo.plugin.jmx.MBean;
import com.tenchael.dubbo.plugin.jmx.MBeanRegistry;
import com.tenchael.dubbo.plugin.utils.NameUtils;
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

public class MetricsReporter {
    private static final Logger LOG = LoggerFactory.getLogger(MetricsReporter.class);
    private final Map<String, Counter> counters;
    private final Map<String, Histogram> histograms;
    private final Set<ObjectName> objectNames;
    private final ReentrantLock lock = new ReentrantLock();
    private final Executor executor = Executors.newFixedThreadPool(20);


    private final MBeanRegistry mBeanRegistry;

    public MetricsReporter() {
        this.counters = new ConcurrentHashMap<>();
        this.histograms = new ConcurrentHashMap<>();
        this.objectNames = new HashSet<>();
        this.mBeanRegistry = MBeanRegistry.getInstance();
        this.mBeanRegistry.setSwallowedExceptionListener(e -> LOG.warn(e.getMessage()));
    }

    public void incr(String category, String name) {
        String key = metricsKey(category, name);
        Counter counter = counters.get(key);
        if (counter == null) {
            lock.lock();
            try {
                if (counter == null) {
                    counter = new Counter(category, name);
                    counters.putIfAbsent(key, counter);
                    asyncRegister(counter);
                }
            } finally {
                lock.unlock();
            }
        }
        counter.incr();
    }

    public void update(String category, String name, long value) {
        String key = metricsKey(category, name);
        Histogram histogram = histograms.get(key);
        if (histogram == null) {
            lock.lock();
            try {
                if (histogram == null) {
                    histogram = new Histogram(category, name);
                    histograms.putIfAbsent(key, histogram);
                    asyncRegister(histogram);
                }
            } finally {
                lock.unlock();
            }
        }
        histogram.update(value);
    }

    public void updateAtEnd(String category, String name, long begin) {
        update(category, name, System.nanoTime() - begin);
    }

    private String metricsKey(String category, String name) {
        return new StringBuilder(name)
                .append("@")
                .append(category)
                .toString();
    }

    private void register(MBean mBean) {
        try {
            ObjectName oname = mBeanRegistry.register(NameUtils.baseOName(mBean),
                    mBean.getCategory(), mBean);
            this.objectNames.add(oname);
        } catch (Exception e) {
            //handle the exception, can not interrupt thread because exception
            LOG.error("register mxBean occurs error", e);
        }
    }

    private void asyncRegister(final MBean mBean) {
        executor.execute(() -> {
            register(mBean);
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
