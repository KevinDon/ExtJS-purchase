package com.newaim.purchase.admin.account.vo;

import java.io.Serializable;

import com.newaim.core.utils.SessionUtils;


public class UserDepartmentVo implements Serializable{
	private String id;
	
	private String cnName;
	
	private String enName;
	
	private String title;
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCnName() {
		return cnName;
	}

	public void setCnName(String cnName) {
		this.cnName = cnName;
	}

	public String getEnName() {
		return enName;
	}

	public void setEnName(String enName) {
		this.enName = enName;
	}

	public String getTitle() {
		UserVo user = SessionUtils.currentUserVo();
		
		if(user.getLang().equals("zh_CN")){
			title = this.cnName;
		}else if(user.getLang().equals("en_AU")){
			title = this.enName;
		}
		
		return title;
	}
}
