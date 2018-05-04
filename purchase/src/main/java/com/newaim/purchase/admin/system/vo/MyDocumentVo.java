package com.newaim.purchase.admin.system.vo;

import java.io.Serializable;
import java.util.Date;

public class MyDocumentVo implements Serializable {
	
	private String id;
	private String name;
	private String path;
	private String extension;
	private String note;
	private String categoryId;
	private String categoryCnName;
	private String categoryEnName;
	private Long bytes;
    private String emailCid;  //仅邮件中使用的媒体ID
	private Integer status;
	private Integer shared;
	private Integer type;
	private Date createdAt;
	private Date updatedAt;
	private String creatorId;
	private String creatorCnName;
	private String creatorEnName;
	private String departmentId;
	private String departmentCnName;
	private String departmentEnName;
	
	private MyDocumentCategoryVo category;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryCnName() {
		return categoryCnName;
	}
	public void setCategoryCnName(String categoryCnName) {
		this.categoryCnName = categoryCnName;
	}
	public String getCategoryEnName() {
		return categoryEnName;
	}
	public void setCategoryEnName(String categoryEnName) {
		this.categoryEnName = categoryEnName;
	}
	public Long getBytes() {
		return bytes;
	}
	public void setBytes(Long bytes) {
		this.bytes = bytes;
	}
    public String getEmailCid() {
        return emailCid;
    }
    public void setEmailCid(String emailCid) {
        this.emailCid = emailCid;
    }
    public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getShared() {
		return shared;
	}
	public void setShared(Integer shared) {
		this.shared = shared;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	public String getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}
	public String getCreatorCnName() {
		return creatorCnName;
	}
	public void setCreatorCnName(String creatorCnName) {
		this.creatorCnName = creatorCnName;
	}
	public String getCreatorEnName() {
		return creatorEnName;
	}
	public void setCreatorEnName(String creatorEnName) {
		this.creatorEnName = creatorEnName;
	}
	public String getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}
	public String getDepartmentCnName() {
		return departmentCnName;
	}
	public void setDepartmentCnName(String departmentCnName) {
		this.departmentCnName = departmentCnName;
	}
	public String getDepartmentEnName() {
		return departmentEnName;
	}
	public void setDepartmentEnName(String departmentEnName) {
		this.departmentEnName = departmentEnName;
	}
	public MyDocumentCategoryVo getCategory() {
		return category;
	}
	public void setCategory(MyDocumentCategoryVo category) {
		this.category = category;
	}
}
