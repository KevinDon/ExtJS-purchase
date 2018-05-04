package com.newaim.purchase.admin.system.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.google.common.collect.Lists;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="na_dictionary_category")
public class DictionaryCategory implements Serializable{
	
	@Id
	@GeneratedValue(generator = "idGenerator")
	@GenericGenerator(name = "idGenerator", strategy = "com.newaim.core.jpa.IdGenerator",
			parameters = {@org.hibernate.annotations.Parameter(name = "prefix", value = "DDC")})
	private String id;
	
	private Integer status;
	
	private Integer sort;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "category_id")
	private List<DictionaryCategoryDesc> desc = Lists.newArrayList();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public List<DictionaryCategoryDesc> getDesc() {
		return desc;
	}

	public void setDesc(List<DictionaryCategoryDesc> desc) {
		this.desc = desc;
	}

}
