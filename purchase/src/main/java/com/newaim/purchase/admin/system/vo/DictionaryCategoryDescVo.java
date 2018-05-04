package com.newaim.purchase.admin.system.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class DictionaryCategoryDescVo implements Serializable{

	@JsonIgnore
	private String id;
	
	@JsonIgnore
	private String categoryId;
	
	private String title;
	
	private String lang;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	
}
