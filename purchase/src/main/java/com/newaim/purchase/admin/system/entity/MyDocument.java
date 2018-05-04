package com.newaim.purchase.admin.system.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="na_files")
public class MyDocument implements Serializable {
	
	@Id
	@GeneratedValue(generator = "idGenerator")
	@GenericGenerator(name = "idGenerator", strategy = "com.newaim.core.jpa.IdGenerator",
			parameters = {@org.hibernate.annotations.Parameter(name = "prefix", value = "MD")})
	private String id;
	private String name;
	private String path;
	private String extension;
	private String note;
	private String categoryId;
	private Long bytes;
    private String emailCid;   //仅邮件中使用媒体ID
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
	
	@ManyToOne
    @JoinColumn(name = "categoryId", insertable=false, updatable=false)
	private MyDocumentCategory category;
	
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
	public MyDocumentCategory getCategory() {
		return category;
	}
	public void setCategory(MyDocumentCategory category) {
		this.category = category;
	}
	
}
