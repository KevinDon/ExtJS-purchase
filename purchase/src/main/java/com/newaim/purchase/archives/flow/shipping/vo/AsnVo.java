package com.newaim.purchase.archives.flow.shipping.vo;

import com.google.common.collect.Lists;
import com.newaim.purchase.archives.flow.shipping.entity.AsnPackingDetail;


import javax.persistence.Transient;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class AsnVo implements Serializable{

    private String id;

    /**送仓计划ID*/
    private String warehousePlanId;
    /**有配件*/
    private Integer accessories;
    /**asn编号*/
    private String asnNumber;
    private String receiveLocation;
    /**收货日期*/
    private Date receiveDate;
    /**仓库ID*/
    private String warehouseId;
    private Date receiveStartDate;
    private Date receiveEndDate;

    /**
     * 订单相关字段
     */
    private String orderId;
    private String orderNumber;
    private String orderTitle;

    /**创建、更新、申请、完成时间*/
    private Date createdAt;
    private Date updatedAt;
    private Date startTime;
    private Date endTime;

    /**创建人信息*/
    private String creatorId;
    private String creatorCnName;
    private String creatorEnName;

    /**创建部门信息*/
    private String departmentId;
    private String departmentCnName;
    private String departmentEnName;

    /**状态*/
    private Integer status;
    private Integer flowStatus;

    /**流程实例ID*/
    private String processInstanceId;
    private String businessId;
    private String remark;
    /**审核*/
    @Transient
    private int approved;

    /**确认人信息*/
    private String reviewerId;
    private String reviewerCnName;
    private String reviewerEnName;

    /**确认部门*/
    private String reviewerDepartmentId;
    private String reviewerDepartmentCnName;
    private String reviewerDepartmentEnName;
    /**挂起状态*/
    private Integer hold;
    /**同步标记*/
    private Integer flagSyncStatus;
    private String flagSyncId;
    private Date flagSyncDate;

    private  Integer flagCompleteStatus;//订单完成状态
    private String  flagCompleteId;//订单完成id
    private Date flagCompleteTime;//订单完成时间
    private List<AsnPackingDetailVo> details = Lists.newArrayList();
    
    /**集装箱编号*/
    private String containerNumber;
    /**封印编号*/
    private String sealsNumber;
    /**柜型*/
    private String containerType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContainerNumber() {
		return containerNumber;
	}

	public void setContainerNumber(String containerNumber) {
		this.containerNumber = containerNumber;
	}

	public String getSealsNumber() {
		return sealsNumber;
	}

	public void setSealsNumber(String sealsNumber) {
		this.sealsNumber = sealsNumber;
	}

	public String getContainerType() {
		return containerType;
	}

	public void setContainerType(String containerType) {
		this.containerType = containerType;
	}

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getWarehousePlanId() {
        return warehousePlanId;
    }

    public void setWarehousePlanId(String warehousePlanId) {
        this.warehousePlanId = warehousePlanId;
    }

    public Integer getAccessories() {
        return accessories;
    }

    public void setAccessories(Integer accessories) {
        this.accessories = accessories;
    }

    public Date getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Date receiveDate) {
        this.receiveDate = receiveDate;
    }

    public String getAsnNumber() {
        return asnNumber;
    }

    public void setAsnNumber(String asnNumber) {
        this.asnNumber = asnNumber;
    }

    public String getReceiveLocation() {
        return receiveLocation;
    }

    public void setReceiveLocation(String receiveLocation) {
        this.receiveLocation = receiveLocation;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Date getReceiveStartDate() {
        return receiveStartDate;
    }

    public void setReceiveStartDate(Date receiveStartDate) {
        this.receiveStartDate = receiveStartDate;
    }

    public Date getReceiveEndDate() {
        return receiveEndDate;
    }

    public void setReceiveEndDate(Date receiveEndDate) {
        this.receiveEndDate = receiveEndDate;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderTitle() {
        return orderTitle;
    }

    public void setOrderTitle(String orderTitle) {
        this.orderTitle = orderTitle;
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

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getFlowStatus() {
        return flowStatus;
    }

    public void setFlowStatus(Integer flowStatus) {
        this.flowStatus = flowStatus;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public int getApproved() {
        return approved;
    }

    public void setApproved(int approved) {
        this.approved = approved;
    }

    public String getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(String reviewerId) {
        this.reviewerId = reviewerId;
    }

    public String getReviewerCnName() {
        return reviewerCnName;
    }

    public void setReviewerCnName(String reviewerCnName) {
        this.reviewerCnName = reviewerCnName;
    }

    public String getReviewerEnName() {
        return reviewerEnName;
    }

    public void setReviewerEnName(String reviewerEnName) {
        this.reviewerEnName = reviewerEnName;
    }

    public String getReviewerDepartmentId() {
        return reviewerDepartmentId;
    }

    public void setReviewerDepartmentId(String reviewerDepartmentId) {
        this.reviewerDepartmentId = reviewerDepartmentId;
    }

    public String getReviewerDepartmentCnName() {
        return reviewerDepartmentCnName;
    }

    public void setReviewerDepartmentCnName(String reviewerDepartmentCnName) {
        this.reviewerDepartmentCnName = reviewerDepartmentCnName;
    }

    public String getReviewerDepartmentEnName() {
        return reviewerDepartmentEnName;
    }

    public void setReviewerDepartmentEnName(String reviewerDepartmentEnName) {
        this.reviewerDepartmentEnName = reviewerDepartmentEnName;
    }

    public List<AsnPackingDetailVo> getDetails() {
        return details;
    }

    public void setDetails(List<AsnPackingDetailVo> details) {
        this.details = details;
    }

    public Integer getHold() {
        return hold;
    }

    public void setHold(Integer hold) {
        this.hold = hold;
    }

    public Integer getFlagSyncStatus() {
        return flagSyncStatus;
    }

    public void setFlagSyncStatus(Integer flagSyncStatus) {
        this.flagSyncStatus = flagSyncStatus;
    }

    public String getFlagSyncId() {
        return flagSyncId;
    }

    public void setFlagSyncId(String flagSyncId) {
        this.flagSyncId = flagSyncId;
    }

    public Date getFlagSyncDate() {
        return flagSyncDate;
    }

    public void setFlagSyncDate(Date flagSyncDate) {
        this.flagSyncDate = flagSyncDate;
    }

    public Integer getFlagCompleteStatus() {
        return flagCompleteStatus;
    }

    public void setFlagCompleteStatus(Integer flagCompleteStatus) {
        this.flagCompleteStatus = flagCompleteStatus;
    }

    public String getFlagCompleteId() {
        return flagCompleteId;
    }

    public void setFlagCompleteId(String flagCompleteId) {
        this.flagCompleteId = flagCompleteId;
    }

    public Date getFlagCompleteTime() {
        return flagCompleteTime;
    }

    public void setFlagCompleteTime(Date flagCompleteTime) {
        this.flagCompleteTime = flagCompleteTime;
    }
}
