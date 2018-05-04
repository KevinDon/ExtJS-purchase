package com.newaim.purchase.flow.workflow.vo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

public class MyAgentSettingVo implements Serializable{
	
	private String id;
	
	private Date toTime;
	private Date fromTime;
	
	private String workflowId;
	private String businessId;
	private String flowCode;
	
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

	private Integer status;
	private Date createdAt;
	private Date updatedAt;
	private String creatorId;
	private String creatorCnName;
	private String creatorEnName;
	private String departmentId;
	private String departmentCnName;
	private String departmentEnName;
	
	private String cnName;
	private String enName;
	private String days;
	
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
	
	public String getDays() {
		return days;
	}
	public void setDays(String days) {
		this.days = days;
	}
	public String getFlowCode() {
		return flowCode;
	}
	public void setFlowCode(String flowCode) {
		this.flowCode = flowCode;
	}
	public Date getToTime() {
		return toTime;
	}
	public void setToTime(Date toTime) {
		this.toTime = toTime;
	}
	public Date getFromTime() {
		return fromTime;
	}
	public void setFromTime(Date fromTime) {
		this.fromTime = fromTime;
	}
	public String getWorkflowId() {
		return workflowId;
	}
	public void setWorkflowId(String workflowId) {
		this.workflowId = workflowId;
	}
	public String getBusinessId() {
		return businessId;
	}
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
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
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
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
}
