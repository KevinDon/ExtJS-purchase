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

import org.hibernate.annotations.GenericGenerator;

import com.google.common.collect.Lists;

@Entity
@Table(name="na_dictionary_value")
public class DictionaryValue implements Serializable{
	
	@Id
	@GeneratedValue(generator = "idGenerator")
	@GenericGenerator(name = "idGenerator", strategy = "com.newaim.core.jpa.IdGenerator",
			parameters = {@org.hibernate.annotations.Parameter(name = "prefix", value = "DDV")})
	private String id;
	
	@Column(name = "dict_id")
	private String dictId;
	
	private String value;

	private Integer sort;
	
	private Integer status;
	
	private Integer isDefault;
	
	private Integer custom;

	private String param1;

	private String param2;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "dict_value_id")
	private List<DictionaryValueDesc> desc = Lists.newArrayList();


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getDictId() {
		return dictId;
	}


	public void setDictId(String dictId) {
		this.dictId = dictId;
	}


	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
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


	public Integer getIsDefault() {
		return isDefault;
	}


	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}


	public Integer getCustom() {
		return custom;
	}


	public void setCustom(Integer custom) {
		this.custom = custom;
	}


	public List<DictionaryValueDesc> getDesc() {
		return desc;
	}


	public void setDesc(List<DictionaryValueDesc> desc) {
		this.desc = desc;
	}

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public String getParam2() {
        return param2;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }
}
