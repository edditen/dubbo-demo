package com.tenchael.dubbo.plugin.metrics;

import com.tenchael.dubbo.plugin.jmx.MBean;
import com.tenchael.dubbo.plugin.jmx.MBeanRegistry;
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

public class MetricsManager {
    private static final Logger LOG = LoggerFactory.getLogger(MetricsManager.class);
    private final Map<String, Counter> counters;
    private final Set<ObjectName> objectNames;
    private final ReentrantLock lock = new ReentrantLock();
    private final Executor executor = Executors.newSingleThreadExecutor();


    private final MBeanRegistry mBeanRegistry;

    public MetricsManager() {
        this.counters = new ConcurrentHashMap<>();
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

    private String metricsKey(String category, String name) {
        return new StringBuilder(name)
                .append("@")
                .append(category)
                .toString();
    }

    private void register(MBean mBean) {
        try {
            ObjectName oname = mBeanRegistry.register(Counter.DEFAULT_ONAME_BASE,
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
