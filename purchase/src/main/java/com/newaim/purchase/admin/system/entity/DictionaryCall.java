package com.newaim.purchase.admin.system.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.google.common.collect.Lists;

@Entity
@Table(name="na_dictionary")
public class DictionaryCall implements Serializable{
	
	@Id
	private String id;
	
	private String codeMain;
	
	private String codeSub;
	
	@Column(name = "category_id")
	private String categoryId;
	
	private Integer sort;

	private Integer status;

	private Integer type;
	
	@OneToMany(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "dict_id")
	private List<DictionaryDesc> desc = Lists.newArrayList();
	
	@OneToMany(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "dict_id")
	@OrderBy("dict_id, sort")
	private List<DictionaryValue> options = Lists.newArrayList();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCodeMain() {
		return codeMain;
	}

	public void setCodeMain(String codeMain) {
		this.codeMain = codeMain;
	}

	public String getCodeSub() {
		return codeSub;
	}

	public void setCodeSub(String codeSub) {
		this.codeSub = codeSub;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
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

	public List<DictionaryDesc> getDesc() {
		return desc;
	}

	public void setDesc(List<DictionaryDesc> desc) {
		this.desc = desc;
	}

	public List<DictionaryValue> getOptions() {
		return options;
	}

	public void setOptions(List<DictionaryValue> options) {
		this.options = options;
	}

    public Integer getStatus() { return status; }

    public void setStatus(Integer status) { this.status = status; }

}
