package com.newaim.purchase.desktop.message.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="na_message_sku")
public class MessageSku implements Serializable{
	
	@Id
	@GeneratedValue(generator = "idGenerator")
	@GenericGenerator(name = "idGenerator", strategy = "com.newaim.core.jpa.IdGenerator",
			parameters = {@org.hibernate.annotations.Parameter(name = "prefix", value = "MSGS")})
	private String id;
	private String sku;
	private String title;
	private String content;

	private Integer status;
	private Integer read;
	private Date createdAt;
	private Date updatedAt;
	
	private String toUserId;
	private String toUserCnName;
	private String toUserEnName;
	private String toDepartmentId;
	private String toDepartmentCnName;
	private String toDepartmentEnName;
	
	private String creatorId;
	private String creatorCnName;
	private String creatorEnName;
	private String departmentId;
	private String departmentCnName;
	private String departmentEnName;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getRead() {
		return read;
	}
	public void setRead(Integer read) {
		this.read = read;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
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
	public String getToUserId() {
		return toUserId;
	}
	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}
	public String getToUserCnName() {
		return toUserCnName;
	}
	public void setToUserCnName(String toUserCnName) {
		this.toUserCnName = toUserCnName;
	}
	public String getToUserEnName() {
		return toUserEnName;
	}
	public void setToUserEnName(String toUserEnName) {
		this.toUserEnName = toUserEnName;
	}
	public String getToDepartmentId() {
		return toDepartmentId;
	}
	public void setToDepartmentId(String toDepartmentId) {
		this.toDepartmentId = toDepartmentId;
	}
	public String getToDepartmentCnName() {
		return toDepartmentCnName;
	}
	public void setToDepartmentCnName(String toDepartmentCnName) {
		this.toDepartmentCnName = toDepartmentCnName;
	}
	public String getToDepartmentEnName() {
		return toDepartmentEnName;
	}
	public void setToDepartmentEnName(String toDepartmentEnName) {
		this.toDepartmentEnName = toDepartmentEnName;
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
	
}
