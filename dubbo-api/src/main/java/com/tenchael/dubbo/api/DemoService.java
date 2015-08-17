package com.tenchael.dubbo.api;

import java.util.List;

import com.tenchael.dubbo.bean.User;

public interface DemoService {
	String sayHello(String msg);
	
	void setUser(User user);

	User getUserByName(String name);

	List<User> getUsers();
}
