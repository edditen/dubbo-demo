# dubbo-fiter-plugin

## 解决什问题？

## JMX, how to?

通过 `8866` 端口暴露 JMX 服务，增加 `JVM` 启动参数：

```bash
-Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=8866 -Dcom.sun.management.jmxremote.local.only=false -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false 
```
