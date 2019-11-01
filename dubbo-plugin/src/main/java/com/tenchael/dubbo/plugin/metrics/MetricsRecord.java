package com.tenchael.dubbo.plugin.metrics;

import com.tenchael.dubbo.plugin.jmx.MetricsMXBean;
import com.tenchael.dubbo.plugin.utils.SwallowedExceptionListener;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.util.concurrent.atomic.AtomicLong;

public class MetricsRecord implements MetricsMXBean {

    private static final String ONAME_BASE =
            "com.tenchael.dubbo.plugin.metrics:type=MetricsRecord,name=";

    private static final String DEFAULT_JMX_NAME_PREFIX = "metrics";

    public static final boolean JMX_ENABLE = true;

    private final ObjectName oname;

    private volatile SwallowedExceptionListener swallowedExceptionListener = null;

    private final AtomicLong total = new AtomicLong(0);

    public MetricsRecord() {
        if (JMX_ENABLE) {
            this.oname = jmxRegister(ONAME_BASE, DEFAULT_JMX_NAME_PREFIX);
        } else {
            this.oname = null;
        }


    }

    @Override
    public long getTotal() {
        return this.total.get();
    }

    public long incr() {
        return this.total.incrementAndGet();
    }

    private ObjectName jmxRegister(String jmxNameBase, String jmxNamePrefix) {
        ObjectName objectName = null;
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        int i = 1;
        boolean registered = false;
        String base = jmxNameBase;
        while (!registered) {
            try {
                ObjectName objName;
                // Skip the numeric suffix for the first pool in case there is
                // only one so the names are cleaner.
                if (i == 1) {
                    objName = new ObjectName(base + jmxNamePrefix);
                } else {
                    objName = new ObjectName(base + jmxNamePrefix + i);
                }
                mbs.registerMBean(this, objName);
                objectName = objName;
                registered = true;
            } catch (MalformedObjectNameException e) {
                if (DEFAULT_JMX_NAME_PREFIX.equals(
                        jmxNamePrefix) && jmxNameBase.equals(base)) {
                    // Shouldn't happen. Skip registration if it does.
                    registered = true;
                } else {
                    // Must be an invalid name. Use the defaults instead.
                    jmxNamePrefix = DEFAULT_JMX_NAME_PREFIX;
                    base = jmxNameBase;
                }
            } catch (InstanceAlreadyExistsException e) {
                // Increment the index and try again
                i++;
            } catch (MBeanRegistrationException e) {
                // Shouldn't happen. Skip registration if it does.
                registered = true;
            } catch (NotCompliantMBeanException e) {
                // Shouldn't happen. Skip registration if it does.
                registered = true;
            }
        }
        return objectName;
    }

    final void jmxUnregister() {
        if (oname != null) {
            try {
                ManagementFactory.getPlatformMBeanServer().unregisterMBean(
                        oname);
            } catch (MBeanRegistrationException e) {
                swallowException(e);
            } catch (InstanceNotFoundException e) {
                swallowException(e);
            }
        }
    }

    final void swallowException(Exception e) {
        SwallowedExceptionListener listener = getSwallowedExceptionListener();

        if (listener == null) {
            return;
        }

        try {
            listener.onSwallowException(e);
        } catch (OutOfMemoryError oome) {
            throw oome;
        } catch (VirtualMachineError vme) {
            throw vme;
        } catch (Throwable t) {
            // Ignore. Enjoy the irony.
        }
    }

    public final SwallowedExceptionListener getSwallowedExceptionListener() {
        return swallowedExceptionListener;
    }

    public final void setSwallowedExceptionListener(SwallowedExceptionListener swallowedExceptionListener) {
        this.swallowedExceptionListener = swallowedExceptionListener;
    }
}
