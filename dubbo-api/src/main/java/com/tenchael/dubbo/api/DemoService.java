package com.tenchael.dubbo.api;

import java.io.Serializable;
import java.util.List;

import com.tenchael.dubbo.bean.User;

public interface DemoService {
	String sayHello(String msg);
	
	<T extends Serializable> void set(T user);

	<T extends Serializable> List<T> get();

}
