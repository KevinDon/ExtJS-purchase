package com.newaim.purchase.admin.system.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

public class MyDocumentCategoryDescVo implements Serializable {

	@JsonIgnore
	private String id;
	
	@JsonIgnore
	private String categoryId;
	
	private String title;
	
	private String context;
	
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

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}
	
}
