package com.tenchael.demo.provider;

import com.tenchael.demo.api.DemoService;

public class DemoServiceImpl implements DemoService {
    @Override
    public String sayHello(String msg) {
        return "hello " + msg;
    }
}
