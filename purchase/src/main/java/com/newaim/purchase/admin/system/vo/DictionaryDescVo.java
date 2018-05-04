package com.newaim.purchase.admin.system.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class DictionaryDescVo implements Serializable{
	@JsonIgnore
	private String id;
	
	private String dictId;
	
	private String name;
	
	private String context;
	
	private String lang;

	
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}
	

}
