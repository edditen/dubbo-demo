package com.tenchael.dubbo.provider.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.tenchael.dubbo.api.DemoService;
import com.tenchael.dubbo.provider.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;

@Service
public class DemoServiceImpl implements DemoService {

	@Autowired
	private UserRepository userDao;

	static Serializable cache;

	public String sayHello(String msg) {
		return "Hello " + msg;
	}

	@Override
	public	<T extends Serializable>  List<T> get() {
		System.out.println("------------------------------------------------");
		return null;
	}


	@Override
	public	<T extends Serializable>  T get2() {
		System.out.println("------------------------------------------------");
		return (T) cache;
	}

	@Override
	public <T extends Serializable> void set(T user) {
		System.out.println("===============================");
		cache = user;
		/*if(user instanceof User){
			userDao.set((User) user);
		}*/

	}
}