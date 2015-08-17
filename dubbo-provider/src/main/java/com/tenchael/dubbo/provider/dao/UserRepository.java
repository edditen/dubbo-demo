package com.tenchael.dubbo.provider.dao;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.stereotype.Component;

import com.tenchael.dubbo.bean.User;

@Component
public class UserRepository {

	@Autowired
	private RedisTemplate<String, User> template;

	private ValueOperations<String, User> operations;

	@PostConstruct
	public void init() {
		// 这里设置value的序列化方式为JacksonJsonRedisSerializer
		template.setValueSerializer(new JacksonJsonRedisSerializer<>(User.class));
		operations = template.opsForValue();
	}

	public void set(User user) {
		String key = String.format("user:%s", user.getName());
		operations.set(key, user);
	}

	public User get(String userName) {
		String key = String.format("user:%s", userName);
		return operations.get(key);
	}

}
