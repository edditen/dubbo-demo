package com.tenchael.dubbo.plugin;

import com.tenchael.dubbo.plugin.metrics.MetricsReporter;
import com.tenchael.dubbo.plugin.utils.NameUtils;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

import static com.tenchael.dubbo.plugin.Constants.Category.*;
import static org.apache.dubbo.common.constants.CommonConstants.CONSUMER;
import static org.apache.dubbo.common.constants.CommonConstants.PROVIDER;

@Activate(group = {CONSUMER, PROVIDER}, value = Constants.METRICS_KEY)
public class MetricsFilter implements Filter {
    private final MetricsReporter metricsReporter;

    public MetricsFilter() {
        this.metricsReporter = new MetricsReporter();
    }

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String methodName = buildMethodName(invoker, invocation);
        long begin = System.nanoTime();
        metricsReporter.incr(REQUESTS_COUNTER_KEY, methodName);
        try {
            Result result = invoker.invoke(invocation);
            if (result == null || result.hasException()) {
                metricsReporter.incr(FAILED_COUNTER_KEY, methodName);
            }
            return result;
        } catch (Exception e) {
            metricsReporter.incr(FAILED_COUNTER_KEY, methodName);
            throw e;
        } finally {
            metricsReporter.incr(COMPLEMENTS_COUNTER_KEY, methodName);
            metricsReporter.updateAtEnd(HISTOGRAM_KEY, methodName, begin);
        }
    }


    private String buildMethodName(Invoker<?> invoker, Invocation invocation) {
        return NameUtils.fullMethodName(invoker, invocation);
    }
}
