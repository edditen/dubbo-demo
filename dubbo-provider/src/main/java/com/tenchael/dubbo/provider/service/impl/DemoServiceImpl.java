package com.tenchael.dubbo.provider.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.tenchael.dubbo.api.DemoService;
import com.tenchael.dubbo.bean.User;
import com.tenchael.dubbo.provider.dao.UserRepository;

@Service
public class DemoServiceImpl implements DemoService {

	@Autowired
	private UserRepository userDao;

	public String sayHello(String msg) {
		return "Hello " + msg;
	}

	@Override
	public User getUserByName(String name) {
		return userDao.get(name);
	}

	@Override
	public List<User> getUsers() {
		// TODO
		return null;
	}

	@Override
	public void setUser(User user) {
		userDao.set(user);
	}
}