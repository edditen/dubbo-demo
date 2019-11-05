package com.tenchael.dubbo.plugin.jmx;

import com.tenchael.dubbo.plugin.utils.SwallowedExceptionListener;

import javax.management.*;
import java.lang.management.ManagementFactory;

public class MBeanRegistry {

    private static final String DEFAULT_JMX_NAME_PREFIX = "metrics";

    private static volatile MBeanRegistry instance = new MBeanRegistry();

    private MBeanServer mBeanServer;

    private SwallowedExceptionListener swallowedExceptionListener;


    public MBeanRegistry() {
        try {
            mBeanServer = ManagementFactory.getPlatformMBeanServer();
        } catch (Error e) {
            mBeanServer = MBeanServerFactory.createMBeanServer();
        }
    }

    public static MBeanRegistry getInstance() {
        return instance;
    }

    public static void setInstance(MBeanRegistry instance) {
        MBeanRegistry.instance = instance;
    }

    public MBeanServer getPlatformMBeanServer() {
        return mBeanServer;
    }

    public ObjectName register(String nameBase, String namePrefix, MBean mBean) {
        ObjectName objectName = null;
        MBeanServer mbs = getPlatformMBeanServer();
        int i = 1;
        boolean registered = false;
        String base = nameBase;
        while (!registered) {
            try {
                ObjectName objName = new ObjectName(base + namePrefix + "_" + i);
                mbs.registerMBean(mBean, objName);
                objectName = objName;
                registered = true;
            } catch (MalformedObjectNameException e) {
                // Must be an invalid name. Use the defaults instead.
                swallowException(e);
                namePrefix = DEFAULT_JMX_NAME_PREFIX;
                base = nameBase;
            } catch (InstanceAlreadyExistsException e) {
                // Increment the index and try again
                swallowException(e);
                i++;
            } catch (MBeanRegistrationException e) {
                // Shouldn't happen. Skip registration if it does.
                swallowException(e);
                registered = true;
            } catch (NotCompliantMBeanException e) {
                // Shouldn't happen. Skip registration if it does.
                swallowException(e);
                registered = true;
            }
        }
        return objectName;
    }

    public void unregister(ObjectName oname) {
        if (oname != null) {
            try {
                getPlatformMBeanServer().unregisterMBean(oname);
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
