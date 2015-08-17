# Dubbo-demo模版

---

采用Spring+Dubbbo+Zookeeper。

模块划分：

* dubbo-api
公用API；
* dubbo-provider
服务提供者，将服务注册到Zookeeper供使用；
* dubbo-consumer
服务消费者，先从Zookeeper取得服务的主机地址，然后消费服务。




