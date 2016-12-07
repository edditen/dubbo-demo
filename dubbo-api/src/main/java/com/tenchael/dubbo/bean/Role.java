package com.tenchael.dubbo.bean;

import java.io.Serializable;

/**
 * Created by tenchael on 2016/12/7.
 */
public class Role implements Serializable {
	private String roleId;

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
}
