package com.newaim.purchase.archives.flow.shipping.entity;

import com.newaim.purchase.flow.workflow.entity.BusinessObject;
import com.newaim.purchase.flow.workflow.entity.FlowObject;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "na_order_shipping_plan")
public class OrderShippingPlan implements BusinessObject, Serializable {

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.newaim.core.jpa.IdGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name = "prefix", value = "OSP")})
    private String id;

    /**总柜量*/
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

    /**创建人信息*/
    private String creatorId;
    private String creatorCnName;
    private String creatorEnName;
    /**创建部门信息*/
    private String departmentId;
    private String departmentCnName;
    private String departmentEnName;

    /**状态、流程状态*/
    private Integer status;
    private Integer flowStatus;

    /**处理人ID*/
    private String handlerId;
    private String handlerCnName;
    private String handlerEnName;
    /**处理部门ID*/
    private String handlerDepartmentId;
    private String handlerDepartmentCnName;
    private String handlerDepartmentEnName;
    private Date handledAt;
    /**流程实例ID*/
    private String processInstanceId;
    private String businessId;

    /**确认人ID*/
    private String reviewerId;
    private String reviewerCnName;
    private String reviewerEnName;

    /**确认部门*/
    private String reviewerDepartmentId;
    private String reviewerDepartmentCnName;
    private String reviewerDepartmentEnName;
    private Integer flagOrderShippingApplyStatus;
    private Date flagOrderShippingApplyTime;
    private String flagOrderShippingApplyId;
    private String flagFlowOrderShippingApplyId;

    private Integer flagCostStatus;
    private Date flagCostTime;
    private String flagCostId;

    /**挂起状态*/
    private Integer hold;

    private String orderNumbers;
    /**清关标记*/
    private Integer flagCustomClearanceStatus;
    private Date flagCustomClearanceTime;
    private String flagCustomClearanceId;


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

    @Override
    public String getId() {
        return id;
    }

    @Override
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

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public Date getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public Date getStartTime() {
        return startTime;
    }

    @Override
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @Override
    public Date getEndTime() {
        return endTime;
    }

    @Override
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    public String getCreatorId() {
        return creatorId;
    }

    @Override
    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    @Override
    public String getCreatorCnName() {
        return creatorCnName;
    }

    @Override
    public void setCreatorCnName(String creatorCnName) {
        this.creatorCnName = creatorCnName;
    }

    @Override
    public String getCreatorEnName() {
        return creatorEnName;
    }

    @Override
    public void setCreatorEnName(String creatorEnName) {
        this.creatorEnName = creatorEnName;
    }

    @Override
    public String getDepartmentId() {
        return departmentId;
    }

    @Override
    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public String getDepartmentCnName() {
        return departmentCnName;
    }

    @Override
    public void setDepartmentCnName(String departmentCnName) {
        this.departmentCnName = departmentCnName;
    }

    @Override
    public String getDepartmentEnName() {
        return departmentEnName;
    }

    @Override
    public void setDepartmentEnName(String departmentEnName) {
        this.departmentEnName = departmentEnName;
    }

    @Override
    public Integer getStatus() {
        return status;
    }

    @Override
    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public Integer getFlowStatus() {
        return flowStatus;
    }

    @Override
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

    @Override
    public String getProcessInstanceId() {
        return processInstanceId;
    }

    @Override
    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    @Override
    public String getBusinessId() {
        return businessId;
    }

    @Override
    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    @Override
    public String getReviewerId() {
        return reviewerId;
    }

    @Override
    public void setReviewerId(String reviewerId) {
        this.reviewerId = reviewerId;
    }

    @Override
    public String getReviewerCnName() {
        return reviewerCnName;
    }

    @Override
    public void setReviewerCnName(String reviewerCnName) {
        this.reviewerCnName = reviewerCnName;
    }

    @Override
    public String getReviewerEnName() {
        return reviewerEnName;
    }

    @Override
    public void setReviewerEnName(String reviewerEnName) {
        this.reviewerEnName = reviewerEnName;
    }

    @Override
    public String getReviewerDepartmentId() {
        return reviewerDepartmentId;
    }

    @Override
    public void setReviewerDepartmentId(String reviewerDepartmentId) {
        this.reviewerDepartmentId = reviewerDepartmentId;
    }

    @Override
    public String getReviewerDepartmentCnName() {
        return reviewerDepartmentCnName;
    }

    @Override
    public void setReviewerDepartmentCnName(String reviewerDepartmentCnName) {
        this.reviewerDepartmentCnName = reviewerDepartmentCnName;
    }

    @Override
    public String getReviewerDepartmentEnName() {
        return reviewerDepartmentEnName;
    }

    @Override
    public void setReviewerDepartmentEnName(String reviewerDepartmentEnName) {
        this.reviewerDepartmentEnName = reviewerDepartmentEnName;
    }

    @Override
    public Integer getHold() {
        return hold;
    }

    @Override
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
