package com.newaim.purchase.admin.account.vo;

import com.google.common.collect.Lists;
import com.newaim.core.utils.SessionUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


public class DepartmentVo implements Serializable{
	private String id;
	
	private String cnName;
	
	private String enName;
	
	private String title;
	
	private String parentId;
	
	private Integer leaf;
	
	private Integer level;
	
	private Integer status;
	
	private Integer sort;
	
	private Date createdAt;
	
	private List<DepartmentVo> children = Lists.newArrayList();

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

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Integer getLeaf() {
		return leaf;
	}

	public void setLeaf(Integer leaf) {
		this.leaf = leaf;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public List<DepartmentVo> getChildren() {
		return children;
	}

	public void setChildren(List<DepartmentVo> children) {
		this.children = children;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	
}
