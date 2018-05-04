package com.newaim.purchase.admin.account.vo;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;
import com.newaim.purchase.admin.account.entity.Role;
import com.newaim.purchase.admin.account.entity.User;

public class UserRoleUnionVo implements Serializable{
	private String id;

	private String userId;
	
	private String roleId;
	
//	private List<Role> roles = Lists.newArrayList();  
//	
//	private List<User> users = Lists.newArrayList();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

//	public List<Role> getRoles() {
//		return roles;
//	}
//
//	public void setRoles(List<Role> roles) {
//		this.roles = roles;
//	}
//
//	public List<User> getUsers() {
//		return users;
//	}
//
//	public void setUsers(List<User> users) {
//		this.users = users;
//	}
	
}
