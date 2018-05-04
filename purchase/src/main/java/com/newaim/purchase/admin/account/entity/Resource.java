package com.newaim.purchase.admin.account.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;

import net.sf.json.JSONArray;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="na_account_resource")
public class Resource implements Serializable{
	
	@Id
	@GeneratedValue(generator = "idGenerator")
	@GenericGenerator(name = "idGenerator", strategy = "com.newaim.core.jpa.IdGenerator",
			parameters = {@org.hibernate.annotations.Parameter(name = "prefix", value = "MOD")})
	private String id;
		
	private String cnName;
	
	private String enName;
	
	@Column(name = "parent_id")
	private String parentId;
	
	private Integer leaf;
	
	private Integer level;
	
	private Integer type;
	
	private String url;
	
	private Integer status;
	
	private Integer sort;
	
	private String itemIcon;
	
	private Date createdAt;
	
	private String code;
	
	private String function;
	
	@Transient
	private List<List<String>> functions;
	
	@OneToMany(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "parent_id")
	@Fetch(FetchMode.SUBSELECT)
	@OrderBy("sort ASC")
	private List<Resource> children = Lists.newArrayList();

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

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public List<Resource> getChildren() {
		return children;
	}

	public void setChildren(List<Resource> children) {
		this.children = children;
	}

	public List<List<String>> getFunctions() {
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
