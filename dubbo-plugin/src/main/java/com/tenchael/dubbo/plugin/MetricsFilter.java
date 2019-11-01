package com.tenchael.dubbo.plugin;

import com.tenchael.dubbo.plugin.metrics.MetricsMXRecord;
import com.tenchael.dubbo.plugin.utils.NameUtils;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

@Activate(group = CommonConstants.PROVIDER, value = Constants.METRICS_KEY)
public class MetricsFilter implements Filter {

    private final MetricsMXRecord metricsRecord;

    private static final String REQUESTS_METRICS = "requests";
    private static final String COMPLEMENTS_METRICS = "complemented";
    private static final String FAILED_METRICS = "failed";

    public MetricsFilter() {
        this.metricsRecord = new MetricsMXRecord();
    }

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String methodName = buildMethodName(invoker, invocation);

        metricsRecord.incr(REQUESTS_METRICS, methodName);
        try {
            Result result = invoker.invoke(invocation);
            if (result == null || result.hasException()) {
                metricsRecord.incr(FAILED_METRICS, methodName);
            }
            return result;
        } catch (Exception e) {
            metricsRecord.incr(FAILED_METRICS, methodName);
            throw e;
        } finally {
            metricsRecord.incr(COMPLEMENTS_METRICS, methodName);
        }
    }


    private String buildMethodName(Invoker<?> invoker, Invocation invocation) {
        return NameUtils.fullMethodName(invoker, invocation);
    }
}
