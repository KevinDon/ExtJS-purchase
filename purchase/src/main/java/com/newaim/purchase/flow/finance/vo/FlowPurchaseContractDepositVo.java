package com.newaim.purchase.flow.finance.vo;

import com.google.common.collect.Lists;
import com.newaim.purchase.admin.account.vo.UserVo;
import com.newaim.purchase.admin.system.vo.AttachmentVo;
import com.newaim.purchase.archives.flow.purchase.vo.PurchaseContractDetailVo;
import com.newaim.purchase.archives.flow.purchase.vo.PurchaseContractOtherDetailVo;
import com.newaim.purchase.archives.vendor.vo.VendorVo;
import com.newaim.purchase.config.json.JsonMoney;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class FlowPurchaseContractDepositVo implements Serializable{

    private String id;

    /**订单信息*/
    private String orderId;
    private String orderNumber;
    private String orderTitle;


    /**供应商信息*/
    private String vendorId;
    private VendorVo vendor;
    private String vendorName;
    private String vendorCnName;
    private String vendorEnName;

    /**结算币种、总定金、汇率*/
    private Integer currency;
    private BigDecimal depositRate;
    private BigDecimal totalValueDepositAud;
    private BigDecimal totalValueDepositRmb;
    private BigDecimal totalValueDepositUsd;
    private BigDecimal receivedTotalDepositAud;
    private BigDecimal receivedTotalDepositRmb;
    private BigDecimal receivedTotalDepositUsd;
    @JsonMoney
    private BigDecimal rateAudToRmb;
    @JsonMoney
    private BigDecimal rateAudToUsd;
    @JsonMoney
    private BigDecimal paymentRateAudToRmb;
    @JsonMoney
    private BigDecimal paymentRateAudToUsd;
    private BigDecimal totalOtherAud;
    private BigDecimal totalOtherRmb;
    private BigDecimal totalOtherUsd;

    private BigDecimal payableAud;
    private BigDecimal payableRmb;
    private BigDecimal payableUsd;

    private BigDecimal paymentAud;
    private BigDecimal paymentRmb;
    private BigDecimal paymentUsd;

    /**最迟支付时间*/
    private Date latestPaymentTime;
    private String remark;

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

    /**实例流程ID*/
    private String processInstanceId;

    /**确认人、确认人部门信息*/
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

    private int approved;

    //附件
    private List<AttachmentVo> attachments = Lists.newArrayList();

    List<PurchaseContractDetailVo> purchaseContractDetail = Lists.newArrayList();

    List<PurchaseContractOtherDetailVo> purchaseContractOtherDetails = Lists.newArrayList();

    private List<UserVo> flowNextHandler = Lists.newArrayList();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<PurchaseContractDetailVo> getPurchaseContractDetail() {
        return purchaseContractDetail;
    }

    public void setPurchaseContractDetail(List<PurchaseContractDetailVo> purchaseContractDetail) {
        this.purchaseContractDetail = purchaseContractDetail;
    }

    public String getOrderTitle() {
        return orderTitle;
    }

    public void setOrderTitle(String orderTitle) {
        this.orderTitle = orderTitle;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
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

    public Integer getCurrency() {
        return currency;
    }

    public void setCurrency(Integer currency) {
        this.currency = currency;
    }

    public BigDecimal getDepositRate() {
        return depositRate;
    }

    public void setDepositRate(BigDecimal depositRate) {
        this.depositRate = depositRate;
    }

    public BigDecimal getTotalValueDepositAud() {
        return totalValueDepositAud;
    }

    public void setTotalValueDepositAud(BigDecimal totalValueDepositAud) {
        this.totalValueDepositAud = totalValueDepositAud;
    }

    public BigDecimal getTotalValueDepositRmb() {
        return totalValueDepositRmb;
    }

    public void setTotalValueDepositRmb(BigDecimal totalValueDepositRmb) {
        this.totalValueDepositRmb = totalValueDepositRmb;
    }

    public BigDecimal getTotalValueDepositUsd() {
        return totalValueDepositUsd;
    }

    public void setTotalValueDepositUsd(BigDecimal totalValueDepositUsd) {
        this.totalValueDepositUsd = totalValueDepositUsd;
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

    public BigDecimal getReceivedTotalDepositAud() {
        return receivedTotalDepositAud;
    }

    public void setReceivedTotalDepositAud(BigDecimal receivedTotalDepositAud) {
        this.receivedTotalDepositAud = receivedTotalDepositAud;
    }

    public BigDecimal getReceivedTotalDepositRmb() {
        return receivedTotalDepositRmb;
    }

    public void setReceivedTotalDepositRmb(BigDecimal receivedTotalDepositRmb) {
        this.receivedTotalDepositRmb = receivedTotalDepositRmb;
    }

    public BigDecimal getReceivedTotalDepositUsd() {
        return receivedTotalDepositUsd;
    }

    public void setReceivedTotalDepositUsd(BigDecimal receivedTotalDepositUsd) {
        this.receivedTotalDepositUsd = receivedTotalDepositUsd;
    }

    public Date getLatestPaymentTime() {
        return latestPaymentTime;
    }

    public void setLatestPaymentTime(Date latestPaymentTime) {
        this.latestPaymentTime = latestPaymentTime;
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

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
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

    public VendorVo getVendor() {
        return vendor;
    }

    public void setVendor(VendorVo vendor) {
        this.vendor = vendor;
    }

    public int getApproved() {
        return approved;
    }

    public void setApproved(int approved) {
        this.approved = approved;
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

    public Integer getHold() {
        return hold;
    }

    public void setHold(Integer hold) {
        this.hold = hold;
    }

    public BigDecimal getPaymentRateAudToRmb() {
        return paymentRateAudToRmb;
    }

    public void setPaymentRateAudToRmb(BigDecimal paymentRateAudToRmb) {
        this.paymentRateAudToRmb = paymentRateAudToRmb;
    }

    public BigDecimal getPaymentRateAudToUsd() {
        return paymentRateAudToUsd;
    }

    public void setPaymentRateAudToUsd(BigDecimal paymentRateAudToUsd) {
        this.paymentRateAudToUsd = paymentRateAudToUsd;
    }

    public BigDecimal getTotalOtherAud() {
        return totalOtherAud;
    }

    public void setTotalOtherAud(BigDecimal totalOtherAud) {
        this.totalOtherAud = totalOtherAud;
    }

    public BigDecimal getTotalOtherRmb() {
        return totalOtherRmb;
    }

    public void setTotalOtherRmb(BigDecimal totalOtherRmb) {
        this.totalOtherRmb = totalOtherRmb;
    }

    public BigDecimal getTotalOtherUsd() {
        return totalOtherUsd;
    }

    public void setTotalOtherUsd(BigDecimal totalOtherUsd) {
        this.totalOtherUsd = totalOtherUsd;
    }

    public BigDecimal getPayableAud() {
        return payableAud;
    }

    public void setPayableAud(BigDecimal payableAud) {
        this.payableAud = payableAud;
    }

    public BigDecimal getPayableRmb() {
        return payableRmb;
    }

    public void setPayableRmb(BigDecimal payableRmb) {
        this.payableRmb = payableRmb;
    }

    public BigDecimal getPayableUsd() {
        return payableUsd;
    }

    public void setPayableUsd(BigDecimal payableUsd) {
        this.payableUsd = payableUsd;
    }

    public BigDecimal getPaymentAud() {
        return paymentAud;
    }

    public void setPaymentAud(BigDecimal paymentAud) {
        this.paymentAud = paymentAud;
    }

    public BigDecimal getPaymentRmb() {
        return paymentRmb;
    }

    public void setPaymentRmb(BigDecimal paymentRmb) {
        this.paymentRmb = paymentRmb;
    }

    public BigDecimal getPaymentUsd() {
        return paymentUsd;
    }

    public void setPaymentUsd(BigDecimal paymentUsd) {
        this.paymentUsd = paymentUsd;
    }

    public List<PurchaseContractOtherDetailVo> getPurchaseContractOtherDetails() {
        return purchaseContractOtherDetails;
    }

    public void setPurchaseContractOtherDetails(List<PurchaseContractOtherDetailVo> purchaseContractOtherDetails) {
        this.purchaseContractOtherDetails = purchaseContractOtherDetails;
    }
}
