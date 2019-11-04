package com.tenchael.demo.consumer;

import com.tenchael.demo.api.DemoService;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;

import java.io.IOException;

public class Consumer {

    public static void main(String[] args) throws IOException {
        ReferenceConfig<DemoService> reference = new ReferenceConfig<>();
        reference.setApplication(new ApplicationConfig("demo-consumer"));
        reference.setRegistry(new RegistryConfig("nacos://127.0.0.1:8848"));
        reference.setInterface(DemoService.class);
        reference.setVersion("1.0.0");
        reference.setFilter("jmxMetrics");
        DemoService service = reference.get();
        String message = service.sayHello("dubbo");
        System.out.println(message);
        System.in.read();
    }

}
