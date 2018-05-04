package com.newaim.purchase.admin.account.vo;

import java.io.Serializable;
import java.util.List;

public class RoleResourceVo implements Serializable{

    private String id;

    private String roleId;
    
    private String resourceId;
    
    private String model;
    
    private String action;
    
    private String data;
    
    private List<String> functions;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}	

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public List<String> getFunctions() {
		return functions;
	}

	public void setFunctions(List<String> functions) {
		this.functions = functions;
	}

}
