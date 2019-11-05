package com.tenchael.dubbo.plugin;

import com.tenchael.dubbo.plugin.metrics.MetricsManager;
import com.tenchael.dubbo.plugin.utils.NameUtils;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

import static org.apache.dubbo.common.constants.CommonConstants.CONSUMER;
import static org.apache.dubbo.common.constants.CommonConstants.PROVIDER;

@Activate(group = {CONSUMER, PROVIDER}, value = Constants.METRICS_KEY)
public class MetricsFilter implements Filter {

    private static final String REQUESTS_COUNTER_KEY = "requests";
    private static final String COMPLEMENTS_COUNTER_KEY = "complemented";
    private static final String FAILED_COUNTER_KEY = "failed";
    private static final String HISTOGRAM_KEY = "histogram";
    private final MetricsManager metricsManager;

    public MetricsFilter() {
        this.metricsManager = new MetricsManager();
    }

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String methodName = buildMethodName(invoker, invocation);
        long begin = System.nanoTime();
        metricsManager.incr(REQUESTS_COUNTER_KEY, methodName);
        try {
            Result result = invoker.invoke(invocation);
            if (result == null || result.hasException()) {
                metricsManager.incr(FAILED_COUNTER_KEY, methodName);
            }
            return result;
        } catch (Exception e) {
            metricsManager.incr(FAILED_COUNTER_KEY, methodName);
            throw e;
        } finally {
            metricsManager.incr(COMPLEMENTS_COUNTER_KEY, methodName);
            metricsManager.updateAtEnd(HISTOGRAM_KEY, methodName, begin);
        }
    }


    private String buildMethodName(Invoker<?> invoker, Invocation invocation) {
        return NameUtils.fullMethodName(invoker, invocation);
    }
}
