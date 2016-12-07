package com.tenchael.dubbo.web.controller;

import java.io.Serializable;

/**
 * Created by tenchael on 2016/12/7.
 */
public class UserDto implements Serializable {
	private String name;

	public UserDto(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
