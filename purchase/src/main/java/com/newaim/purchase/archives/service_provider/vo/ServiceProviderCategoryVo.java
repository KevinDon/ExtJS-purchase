package com.newaim.purchase.archives.service_provider.vo;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;


public class ServiceProviderCategoryVo implements Serializable{
	private String id;
	
	private String cnName;
	
	private String enName;
	
	private String name;
	
	private String parentId;
	
	private Integer leaf;
	
	private Integer level;
	
	private Integer status;
	
	private Integer sort;

	private Integer type;

	private String buyerId;
	private String buyerCnName;
	private String buyerEnName;
	
	private List<ServiceProviderCategoryVo> children = Lists.newArrayList();

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}

	public String getBuyerCnName() {
		return buyerCnName;
	}

	public void setBuyerCnName(String buyerCnName) {
		this.buyerCnName = buyerCnName;
	}

	public String getBuyerEnName() {
		return buyerEnName;
	}

	public void setBuyerEnName(String buyerEnName) {
		this.buyerEnName = buyerEnName;
	}

	public List<ServiceProviderCategoryVo> getChildren() {
		return children;
	}

	public void setChildren(List<ServiceProviderCategoryVo> children) {
		this.children = children;
	}
	
}
