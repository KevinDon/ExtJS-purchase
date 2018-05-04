package com.newaim.purchase.admin.account.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import net.sf.json.JSONArray;

public class ResourceVo implements Serializable{
	private String id;

	private String parentId;

	private String cnName;

	private String enName;

	private Integer leaf;

	private Integer level;

	private Integer type;

	private String url;

	private Integer status;

	private Integer sort;

	private String itemIcon;

	private Date createdAt;

	private String code;

	@JsonIgnore
	private String function;

	private List<List<String>> functions;
	
	private List<ResourceVo> children;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public String getItemIcon() {
		return itemIcon;
	}

	public void setItemIcon(String itemIcon) {
		this.itemIcon = itemIcon;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<ResourceVo> getChildren() {
		return children;
	}

	public void setChildren(List<ResourceVo> children) {
		this.children = children;
	}

	public String getFunction() {
		return function;
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	public void setFunction(String function) {
		if(StringUtils.isNotBlank(function)){
			JSONArray ja = JSONArray.fromObject(function);
			functions = JSONArray.toList(ja);
		}
		this.function = function;
	}
	
	public List<List<String>> getFunctions() {
		if(functions == null && StringUtils.isNotBlank(function)){
			JSONArray ja = JSONArray.fromObject(function);
			functions = JSONArray.toList(ja);
		}
		
		return functions;
	}

	public void setFunctions(List<List<String>> functions) {
		
		List<String> list2 = new ArrayList<String>();
        list2.add("");
        
		if(functions != null && functions.size()>0){
			for(List<String> ls: functions){
				ls.removeAll(list2);
			}
			JSONArray ja = JSONArray.fromObject(functions);
			function = ja.toString();
		}
		
		this.functions = functions;
	}

}
