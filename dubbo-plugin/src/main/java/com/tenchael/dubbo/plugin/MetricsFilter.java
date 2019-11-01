package com.tenchael.dubbo.plugin;

import com.tenchael.dubbo.plugin.metrics.MetricsMXRecord;
import com.tenchael.dubbo.plugin.utils.NameUtils;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.rpc.*;

@Activate(group = CommonConstants.PROVIDER, value = Constants.METRICS_KEY)
public class MetricsFilter implements Filter {

    private final MetricsMXRecord metricsRecord;

    private static final String REQUESTS_METRICS = "requests";
    private static final String COMPLEMENTS_METRICS = "complemented";

    private static final Logger LOG = LoggerFactory.getLogger(MetricsMXRecord.class);


    public MetricsFilter() {
        LOG.debug("construct MetricsFilter: " + this);
        this.metricsRecord = new MetricsMXRecord();
    }

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String methodName = buildMethodName(invoker, invocation);
        if (LOG.isDebugEnabled()) {
            LOG.debug("invoke method: " + methodName);
        }
        metricsRecord.incr(REQUESTS_METRICS, methodName);
        try {
            return invoker.invoke(invocation);
        } finally {
            metricsRecord.incr(COMPLEMENTS_METRICS, methodName);
        }
    }


    private String buildMethodName(Invoker<?> invoker, Invocation invocation) {
        return NameUtils.fullMethodName(invoker, invocation);
    }
}
