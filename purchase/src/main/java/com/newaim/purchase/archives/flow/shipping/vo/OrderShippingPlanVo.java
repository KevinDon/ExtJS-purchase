package com.newaim.purchase.archives.flow.shipping.vo;

import com.google.common.collect.Lists;
import com.newaim.purchase.config.json.JsonMoney;

import javax.persistence.Transient;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class OrderShippingPlanVo implements Serializable{

    private String id;
    @JsonMoney
    private BigDecimal totalContainerQty;

    /**
     * 服务商相关字段
     */
    private String serviceProviderId;
    private String serviceProviderCnName;
    private String serviceProviderEnName;
    private String serviceProviderQuotationId;

    private Date readyDate;//预计完货时间
    private Date etd;//预计发运时间
    private Date eta;
    private Date createdAt;//创建时间
    private Date updatedAt;//更新时间
    private Date startTime;//申请时间
    private Date endTime;

    private String creatorId;//申请人ID
    private String creatorCnName;
    private String creatorEnName;

    private String departmentId;
    private String departmentCnName;
    private String departmentEnName;

    private Integer status;
    private Integer flowStatus;//流程状态

    /**处理人ID*/
    private String handlerId;
    private String handlerCnName;
    private String handlerEnName;
    /**处理部门ID*/
    private String handlerDepartmentId;
    private String handlerDepartmentCnName;
    private String handlerDepartmentEnName;
    private Date handledAt;//處理時間

    /**确认人ID*/
    private String reviewerId;
    private String reviewerCnName;
    private String reviewerEnName;

    /**确认部门*/
    private String reviewerDepartmentId;
    private String reviewerDepartmentCnName;
    private String reviewerDepartmentEnName;
    private String orderNumbers;

    private List<OrderShippingPlanDetailVo> details = Lists.newArrayList();
    /**
     * 流程实例ID
     */
    private String processInstanceId;
    private String businessId;
    /**挂起状态*/
    private Integer hold;

    private Integer flagOrderShippingApplyStatus;
    private Date flagOrderShippingApplyTime;
    private String flagOrderShippingApplyId;
    private String flagFlowOrderShippingApplyId;
    private Integer flagCostStatus;
    private Date flagCostTime;
    private String flagCostId;

    /**清关标记*/
    private Integer flagCustomClearanceStatus;
    private Date flagCustomClearanceTime;
    private String flagCustomClearanceId;
    /**
     * 审核
     */
    @Transient
    private int approved;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getTotalContainerQty() {
        return totalContainerQty;
    }

    public void setTotalContainerQty(BigDecimal totalContainerQty) {
        this.totalContainerQty = totalContainerQty;
    }

    public String getServiceProviderId() {
        return serviceProviderId;
    }

    public void setServiceProviderId(String serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
    }

    public String getServiceProviderCnName() {
        return serviceProviderCnName;
    }

    public void setServiceProviderCnName(String serviceProviderCnName) {
        this.serviceProviderCnName = serviceProviderCnName;
    }

    public String getServiceProviderEnName() {
        return serviceProviderEnName;
    }

    public void setServiceProviderEnName(String serviceProviderEnName) {
        this.serviceProviderEnName = serviceProviderEnName;
    }

    public String getServiceProviderQuotationId() {
        return serviceProviderQuotationId;
    }

    public void setServiceProviderQuotationId(String serviceProviderQuotationId) {
        this.serviceProviderQuotationId = serviceProviderQuotationId;
    }

    public Integer getFlagCustomClearanceStatus() {
        return flagCustomClearanceStatus;
    }

    public void setFlagCustomClearanceStatus(Integer flagCustomClearanceStatus) {
        this.flagCustomClearanceStatus = flagCustomClearanceStatus;
    }

    public Date getFlagCustomClearanceTime() {
        return flagCustomClearanceTime;
    }

    public void setFlagCustomClearanceTime(Date flagCustomClearanceTime) {
        this.flagCustomClearanceTime = flagCustomClearanceTime;
    }

    public String getFlagCustomClearanceId() {
        return flagCustomClearanceId;
    }

    public void setFlagCustomClearanceId(String flagCustomClearanceId) {
        this.flagCustomClearanceId = flagCustomClearanceId;
    }

    public Date getReadyDate() {
        return readyDate;
    }

    public void setReadyDate(Date readyDate) {
        this.readyDate = readyDate;
    }

    public Date getEtd() {
        return etd;
    }

    public void setEtd(Date etd) {
        this.etd = etd;
    }

    public Date getEta() {
        return eta;
    }

    public void setEta(Date eta) {
        this.eta = eta;
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

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public Integer getFlowStatus() {
        return flowStatus;
    }

    public void setFlowStatus(Integer flowStatus) {
        this.flowStatus = flowStatus;
    }

    public String getHandlerId() {
        return handlerId;
    }

    public void setHandlerId(String handlerId) {
        this.handlerId = handlerId;
    }

    public String getHandlerCnName() {
        return handlerCnName;
    }

    public void setHandlerCnName(String handlerCnName) {
        this.handlerCnName = handlerCnName;
    }

    public String getHandlerEnName() {
        return handlerEnName;
    }

    public void setHandlerEnName(String handlerEnName) {
        this.handlerEnName = handlerEnName;
    }

    public String getHandlerDepartmentId() {
        return handlerDepartmentId;
    }

    public void setHandlerDepartmentId(String handlerDepartmentId) {
        this.handlerDepartmentId = handlerDepartmentId;
    }

    public String getHandlerDepartmentCnName() {
        return handlerDepartmentCnName;
    }

    public void setHandlerDepartmentCnName(String handlerDepartmentCnName) {
        this.handlerDepartmentCnName = handlerDepartmentCnName;
    }

    public String getHandlerDepartmentEnName() {
        return handlerDepartmentEnName;
    }

    public void setHandlerDepartmentEnName(String handlerDepartmentEnName) {
        this.handlerDepartmentEnName = handlerDepartmentEnName;
    }

    public Date getHandledAt() {
        return handledAt;
    }

    public void setHandledAt(Date handledAt) {
        this.handledAt = handledAt;
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

    public List<OrderShippingPlanDetailVo> getDetails() {
        return details;
    }

    public void setDetails(List<OrderShippingPlanDetailVo> details) {
        this.details = details;
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

    public Integer getHold() {
        return hold;
    }

    public void setHold(Integer hold) {
        this.hold = hold;
    }

    public Integer getFlagOrderShippingApplyStatus() {
        return flagOrderShippingApplyStatus;
    }

    public void setFlagOrderShippingApplyStatus(Integer flagOrderShippingApplyStatus) {
        this.flagOrderShippingApplyStatus = flagOrderShippingApplyStatus;
    }

    public Date getFlagOrderShippingApplyTime() {
        return flagOrderShippingApplyTime;
    }

    public void setFlagOrderShippingApplyTime(Date flagOrderShippingApplyTime) {
        this.flagOrderShippingApplyTime = flagOrderShippingApplyTime;
    }

    public String getFlagOrderShippingApplyId() {
        return flagOrderShippingApplyId;
    }

    public void setFlagOrderShippingApplyId(String flagOrderShippingApplyId) {
        this.flagOrderShippingApplyId = flagOrderShippingApplyId;
    }

    public String getFlagFlowOrderShippingApplyId() {
        return flagFlowOrderShippingApplyId;
    }

    public void setFlagFlowOrderShippingApplyId(String flagFlowOrderShippingApplyId) {
        this.flagFlowOrderShippingApplyId = flagFlowOrderShippingApplyId;
    }

    public Integer getFlagCostStatus() {
        return flagCostStatus;
    }

    public void setFlagCostStatus(Integer flagCostStatus) {
        this.flagCostStatus = flagCostStatus;
    }

    public Date getFlagCostTime() {
        return flagCostTime;
    }

    public void setFlagCostTime(Date flagCostTime) {
        this.flagCostTime = flagCostTime;
    }

    public String getFlagCostId() {
        return flagCostId;
    }

    public void setFlagCostId(String flagCostId) {
        this.flagCostId = flagCostId;
    }

    public String getOrderNumbers() {
        return orderNumbers;
    }

    public void setOrderNumbers(String orderNumbers) {
        this.orderNumbers = orderNumbers;
    }
}
