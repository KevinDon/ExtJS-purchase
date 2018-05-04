package com.newaim.purchase.desktop.message.vo;

import java.io.Serializable;
import java.util.Date;

public class MessageVo implements Serializable{
	
	private String id;
	private String title;
	private String content;

	private Integer status;
	private Integer read;
	private Integer type;
	private Integer target;
	private Date createdAt;
	private Date updatedAt;
	
	private String toUserId;
	private String toUserCnName;
	private String toUserEnName;
	private String toDepartmentId;
	private String toDepartmentCnName;
	private String toDepartmentEnName;
	
	private String fromUserId;
	private String fromUserCnName;
	private String fromUserEnName;
	private String fromDepartmentId;
	private String fromDepartmentCnName;
	private String fromDepartmentEnName;
	
	private String moduleName;
	private String businessId;
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
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getTarget() {
		return target;
	}
	public void setTarget(Integer target) {
		this.target = target;
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
	public String getFromUserId() {
		return fromUserId;
	}
	public void setFromUserId(String fromUserId) {
		this.fromUserId = fromUserId;
	}
	public String getFromUserCnName() {
		return fromUserCnName;
	}
	public void setFromUserCnName(String fromUserCnName) {
		this.fromUserCnName = fromUserCnName;
	}
	public String getFromUserEnName() {
		return fromUserEnName;
	}
	public void setFromUserEnName(String fromUserEnName) {
		this.fromUserEnName = fromUserEnName;
	}
	public String getFromDepartmentId() {
		return fromDepartmentId;
	}
	public void setFromDepartmentId(String fromDepartmentId) {
		this.fromDepartmentId = fromDepartmentId;
	}
	public String getFromDepartmentCnName() {
		return fromDepartmentCnName;
	}
	public void setFromDepartmentCnName(String fromDepartmentCnName) {
		this.fromDepartmentCnName = fromDepartmentCnName;
	}
	public String getFromDepartmentEnName() {
		return fromDepartmentEnName;
	}
	public void setFromDepartmentEnName(String fromDepartmentEnName) {
		this.fromDepartmentEnName = fromDepartmentEnName;
	}
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public String getBusinessId() {
		return businessId;
	}
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	
}
