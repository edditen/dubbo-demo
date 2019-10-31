# Dubbo-demo模版

---

采用Spring+Dubbbo+Zookeeper。

模块划分：

* demo-api
公用API；

* demo-provider
服务提供者，将服务注册到注册中心；

* demo-consumer
服务消费者，先从注册中心取得服务的主机地址，然后消费服务。




