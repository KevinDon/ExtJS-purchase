package com.newaim.purchase.admin.system.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class DictionaryValueDescVo implements Serializable{

	@JsonIgnore
	private String id;
	
	@JsonIgnore
	private String dictValueId;
	
	private String text;
	
	private String lang;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDictValueId() {
		return dictValueId;
	}

	public void setDictValueId(String dictValueId) {
		this.dictValueId = dictValueId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	
}
