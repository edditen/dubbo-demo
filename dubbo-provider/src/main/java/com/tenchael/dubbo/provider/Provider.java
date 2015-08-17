package com.tenchael.dubbo.provider;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Provider {
	// in local project
	public static final String contextLocation = "provider-context.xml";

	// load context file from network
	// public static final String contextLocation =
	// "http://192.168.1.60/cfg-files/provider-context.xml";
	public static void main(String[] args) throws Exception {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { contextLocation });
		context.start();
		System.in.read(); // 按任意键退出
	}
}
