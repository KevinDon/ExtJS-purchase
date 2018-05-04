package com.newaim.purchase.flow.finance.vo;

import com.google.common.collect.Lists;
import com.newaim.purchase.admin.account.vo.UserVo;
import com.newaim.purchase.admin.system.vo.AttachmentVo;
import com.newaim.purchase.archives.vendor.vo.VendorVo;
import com.newaim.purchase.config.json.JsonMoney;

import javax.persistence.Transient;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class FlowBalanceRefundVo implements Serializable{

    private String id;

    /**供应商信息*/
    private String vendorId;
    private VendorVo vendor;
    private String vendorName;
    private String vendorCnName;
    private String vendorEnName;

    /**订单信息*/
    private String orderId;
    private String orderBusinessId;
    private String orderNumber;
    private String orderTitle;
    /**样品信息*/
    private String samplePaymentId;
    private String samplePaymentBusinessId;

    /**扣款原因*/
    private String chargebackReason;
    /**结算货币*/
    private Integer currency;
    /**总金额、汇率*/
    private BigDecimal totalFeeAud;
    private BigDecimal totalFeeRmb;
    private BigDecimal totalFeeUsd;
    @JsonMoney
    private BigDecimal rateAudToRmb;
    @JsonMoney
    private BigDecimal rateAudToUsd;

    /**类型、备注*/
    private Integer type;
    private String remark;

    /**创建、更新、申请、完成时间*/
    private Date createdAt;
    private Date updatedAt;
    private Date startTime;
    private Date endTime;

    /**创建人、部门信息*/
    private String creatorId;
    private String creatorCnName;
    private String creatorEnName;
    private String departmentId;
    private String departmentCnName;
    private String departmentEnName;

    /**状态*/
    private Integer status;
    private Integer flowStatus;
    private Integer chargebackStatus;
    /**流程实例ID*/
    private String processInstanceId;
    private int approved;

    /**确认人、确认部门信息*/
    private String reviewerId;
    private String reviewerCnName;
    private String reviewerEnName;
    private String reviewerDepartmentId;
    private String reviewerDepartmentCnName;
    private String reviewerDepartmentEnName;

    /**当前处理人*/
    private String assigneeId;
    private String assigneeCnName;
    private String assigneeEnName;
    /**挂起状态*/
    private Integer hold;

    private List<FlowBalanceRefundDetailVo> details = Lists.newArrayList();
    private List<AttachmentVo> attachments = Lists.newArrayList();

    private List<UserVo> flowNextHandler = Lists.newArrayList();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public VendorVo getVendor() {
        return vendor;
    }

    public void setVendor(VendorVo vendor) {
        this.vendor = vendor;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVendorCnName() {
        return vendorCnName;
    }

    public void setVendorCnName(String vendorCnName) {
        this.vendorCnName = vendorCnName;
    }

    public String getVendorEnName() {
        return vendorEnName;
    }

    public void setVendorEnName(String vendorEnName) {
        this.vendorEnName = vendorEnName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderBusinessId() {
        return orderBusinessId;
    }

    public void setOrderBusinessId(String orderBusinessId) {
        this.orderBusinessId = orderBusinessId;
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

    public String getSamplePaymentId() {
        return samplePaymentId;
    }

    public void setSamplePaymentId(String samplePaymentId) {
        this.samplePaymentId = samplePaymentId;
    }

    public String getSamplePaymentBusinessId() {
        return samplePaymentBusinessId;
    }

    public void setSamplePaymentBusinessId(String samplePaymentBusinessId) {
        this.samplePaymentBusinessId = samplePaymentBusinessId;
    }

    public String getChargebackReason() {
        return chargebackReason;
    }

    public void setChargebackReason(String chargebackReason) {
        this.chargebackReason = chargebackReason;
    }

    public Integer getCurrency() {
        return currency;
    }

    public void setCurrency(Integer currency) {
        this.currency = currency;
    }

    public BigDecimal getTotalFeeAud() {
        return totalFeeAud;
    }

    public void setTotalFeeAud(BigDecimal totalFeeAud) {
        this.totalFeeAud = totalFeeAud;
    }

    public BigDecimal getTotalFeeRmb() {
        return totalFeeRmb;
    }

    public void setTotalFeeRmb(BigDecimal totalFeeRmb) {
        this.totalFeeRmb = totalFeeRmb;
    }

    public BigDecimal getTotalFeeUsd() {
        return totalFeeUsd;
    }

    public void setTotalFeeUsd(BigDecimal totalFeeUsd) {
        this.totalFeeUsd = totalFeeUsd;
    }

    public BigDecimal getRateAudToRmb() {
        return rateAudToRmb;
    }

    public void setRateAudToRmb(BigDecimal rateAudToRmb) {
        this.rateAudToRmb = rateAudToRmb;
    }

    public BigDecimal getRateAudToUsd() {
        return rateAudToUsd;
    }

    public void setRateAudToUsd(BigDecimal rateAudToUsd) {
        this.rateAudToUsd = rateAudToUsd;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public Integer getChargebackStatus() {
        return chargebackStatus;
    }

    public void setChargebackStatus(Integer chargebackStatus) {
        this.chargebackStatus = chargebackStatus;
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

    public String getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(String assigneeId) {
        this.assigneeId = assigneeId;
    }

    public String getAssigneeCnName() {
        return assigneeCnName;
    }

    public void setAssigneeCnName(String assigneeCnName) {
        this.assigneeCnName = assigneeCnName;
    }

    public String getAssigneeEnName() {
        return assigneeEnName;
    }

    public void setAssigneeEnName(String assigneeEnName) {
        this.assigneeEnName = assigneeEnName;
    }

    public Integer getHold() {
        return hold;
    }

    public void setHold(Integer hold) {
        this.hold = hold;
    }

    public List<FlowBalanceRefundDetailVo> getDetails() {
        return details;
    }

    public void setDetails(List<FlowBalanceRefundDetailVo> details) {
        this.details = details;
    }

    public List<AttachmentVo> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentVo> attachments) {
        this.attachments = attachments;
    }

    public List<UserVo> getFlowNextHandler() {
        return flowNextHandler;
    }

    public void setFlowNextHandler(List<UserVo> flowNextHandler) {
        this.flowNextHandler = flowNextHandler;
    }
}
