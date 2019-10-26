package com.tenchael.dubbo.plugin;

import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

@Activate(group = CommonConstants.PROVIDER, value = "metrics")
public class MetricsFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        System.out.println("invoke method: " + invoker.getInterface());
        return invoker.invoke(invocation);
    }
}
