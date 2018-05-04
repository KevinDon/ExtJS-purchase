package com.newaim.purchase.archives.product.vo;

import java.io.Serializable;

public class ProductCategorySimpleVo implements Serializable{
	private String id;
	
	private String cnName;
	
	private String enName;

	private String name;

	private String omsId;

	private String omsCode;

	private String omsName;
	
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOmsId() {
		return omsId;
	}

	public void setOmsId(String omsId) {
		this.omsId = omsId;
	}

	public String getOmsCode() {
		return omsCode;
	}

	public void setOmsCode(String omsCode) {
		this.omsCode = omsCode;
	}

	public String getOmsName() {
		return omsName;
	}

	public void setOmsName(String omsName) {
		this.omsName = omsName;
	}
}
