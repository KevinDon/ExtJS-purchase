package com.newaim.purchase.admin.system.vo;

import java.io.Serializable;

public class PortalVo implements Serializable{

	private String id;
	private String creatorId;
	private String moduleKey;
	private Integer colNum;
	private Integer rowNum;
	private Integer moduleType;
	private Integer status;
	private Integer height;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public String getModuleKey() {
		return moduleKey;
	}

	public void setModuleKey(String moduleKey) {
		this.moduleKey = moduleKey;
	}

	public Integer getColNum() {
		return colNum;
	}

	public void setColNum(Integer colNum) {
		this.colNum = colNum;
	}

	public Integer getRowNum() {
		return rowNum;
	}

	public void setRowNum(Integer rowNum) {
		this.rowNum = rowNum;
	}

	public Integer getModuleType() {
		return moduleType;
	}

	public void setModuleType(Integer moduleType) {
		this.moduleType = moduleType;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }
}