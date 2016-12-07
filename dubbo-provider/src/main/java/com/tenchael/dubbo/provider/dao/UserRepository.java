package com.tenchael.dubbo.provider.dao;

import com.tenchael.dubbo.bean.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserRepository {

	private Map<String, User> template = new HashMap<>();

	public void set(User user) {
		String key = String.format("user:%s", user.getName());
		template.put(key, user);
	}

	public List<User> get() {
		return new ArrayList<User>(template.values());
	}

}
