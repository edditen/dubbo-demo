package com.tenchael.dubbo.consumer.action;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tenchael.dubbo.api.DemoService;
import com.tenchael.dubbo.bean.User;

@Component
public class DemoAction {
	@Reference(version = "1.0.0")
	private DemoService demoService;

	public String echo(String msg) {
		return demoService.sayHello(msg);
	}

	public User getUser(String name) {
		return demoService.getUserByName(name);
	}

}
