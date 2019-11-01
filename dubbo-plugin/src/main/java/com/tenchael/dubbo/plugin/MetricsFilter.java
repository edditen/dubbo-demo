package com.tenchael.dubbo.plugin;

import com.tenchael.dubbo.plugin.metrics.MetricsRecord;
import com.tenchael.dubbo.plugin.utils.NameUtils;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

@Activate(group = CommonConstants.PROVIDER, value = Constants.METRICS_KEY)
public class MetricsFilter implements Filter {

    private final MetricsRecord metricsRecord;


    public MetricsFilter() {
        System.out.println("construct MetricsFilter...");
        this.metricsRecord = new MetricsRecord();
    }

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String methodName = buildMethodName(invoker, invocation);
        System.out.println("invoke method: " + methodName);
        metricsRecord.incr();
        return invoker.invoke(invocation);
    }


    private String buildMethodName(Invoker<?> invoker, Invocation invocation) {
        return NameUtils.fullMethodName(invoker, invocation);
    }
}
