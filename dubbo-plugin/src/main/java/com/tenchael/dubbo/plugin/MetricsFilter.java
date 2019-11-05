package com.tenchael.dubbo.plugin;

import com.tenchael.dubbo.plugin.metrics.CounterRecord;
import com.tenchael.dubbo.plugin.utils.NameUtils;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

import static org.apache.dubbo.common.constants.CommonConstants.CONSUMER;
import static org.apache.dubbo.common.constants.CommonConstants.PROVIDER;

@Activate(group = {CONSUMER, PROVIDER}, value = Constants.METRICS_KEY)
public class MetricsFilter implements Filter {

    private static final String REQUESTS_METRICS = "requests";
    private static final String COMPLEMENTS_METRICS = "complemented";
    private static final String FAILED_METRICS = "failed";
    private final CounterRecord counterRecord;

    public MetricsFilter() {
        this.counterRecord = new CounterRecord();
    }

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String methodName = buildMethodName(invoker, invocation);

        counterRecord.incr(REQUESTS_METRICS, methodName);
        try {
            Result result = invoker.invoke(invocation);
            if (result == null || result.hasException()) {
                counterRecord.incr(FAILED_METRICS, methodName);
            }
            return result;
        } catch (Exception e) {
            counterRecord.incr(FAILED_METRICS, methodName);
            throw e;
        } finally {
            counterRecord.incr(COMPLEMENTS_METRICS, methodName);
        }
    }


    private String buildMethodName(Invoker<?> invoker, Invocation invocation) {
        return NameUtils.fullMethodName(invoker, invocation);
    }
}
