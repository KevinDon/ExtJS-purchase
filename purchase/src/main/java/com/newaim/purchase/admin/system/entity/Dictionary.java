package com.newaim.purchase.admin.system.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.google.common.collect.Lists;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="na_dictionary")
public class Dictionary implements Serializable{
	
	@Id
	@GeneratedValue(generator = "idGenerator")
	@GenericGenerator(name = "idGenerator", strategy = "com.newaim.core.jpa.IdGenerator",
		parameters = {@org.hibernate.annotations.Parameter(name = "prefix", value = "DD")})
	private String id;
	
	private String codeMain;
	
	private String codeSub;

	@Column(name = "category_id")
	private String categoryId;
	
	private Integer sort;
	
	private Integer status;
	
	private Integer custom;
	
	private Integer type;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "dict_id")
	private List<DictionaryDesc> desc = Lists.newArrayList();
	
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getCustom() {
		return custom;
	}

	public void setCustom(Integer custom) {
		this.custom = custom;
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

}
