package com.newaim.purchase.flow.finance.entity;

import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.flow.workflow.entity.FlowVendorWithCurrencyObject;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "na_flow_purchase_contract_deposit")
public class FlowPurchaseContractDeposit implements FlowVendorWithCurrencyObject, Serializable {

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.newaim.core.jpa.IdGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name = "prefix", value = "FPCD")})
    private String id;

    /**订单信息*/
    private String orderId;
    private String orderNumber;
    private String orderTitle;

    /**供应商信息*/
    private String vendorId;
    private String vendorCnName;
    private String vendorEnName;

    /**结算币种、总定金、汇率*/
    private Integer currency;
    private BigDecimal depositRate;
    private BigDecimal totalValueDepositAud;
    private BigDecimal totalValueDepositRmb;
    private BigDecimal totalValueDepositUsd;
    /**实收总定金*/
    private BigDecimal receivedTotalDepositAud;
    private BigDecimal receivedTotalDepositRmb;
    private BigDecimal receivedTotalDepositUsd;

    private BigDecimal paymentRateAudToRmb;
    private BigDecimal paymentRateAudToUsd;
    private BigDecimal rateAudToRmb;
    private BigDecimal rateAudToUsd;

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

    @Transient
    private int approved;

    @Override
    public String getId() {
        return id;
    }

    @Override
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

    public String getOrderTitle() {
        return orderTitle;
    }

    public void setOrderTitle(String orderTitle) {
        this.orderTitle = orderTitle;
    }

    @Override
    public String getVendorId() {
        return vendorId;
    }

    @Override
    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    @Override
    public String getVendorCnName() {
        return vendorCnName;
    }

    @Override
    public void setVendorCnName(String vendorCnName) {
        this.vendorCnName = vendorCnName;
    }

    @Override
    public String getVendorEnName() {
        return vendorEnName;
    }

    @Override
    public void setVendorEnName(String vendorEnName) {
        this.vendorEnName = vendorEnName;
    }

    @Override
    public Integer getCurrency() {
        return currency;
    }

    @Override
    public void setCurrency(Integer currency) {
        this.currency = currency;
    }

    public BigDecimal getDepositRate() {
        return depositRate;
    }

    public void setDepositRate(BigDecimal depositRate) {
        this.depositRate = depositRate;
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

    @Override
    public String getProcessInstanceId() {
        return processInstanceId;
    }

    @Override
    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
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
    public String getAssigneeId() {
        return assigneeId;
    }

    @Override
    public void setAssigneeId(String assigneeId) {
        this.assigneeId = assigneeId;
    }

    @Override
    public String getAssigneeCnName() {
        return assigneeCnName;
    }

    @Override
    public void setAssigneeCnName(String assigneeCnName) {
        this.assigneeCnName = assigneeCnName;
    }

    @Override
    public String getAssigneeEnName() {
        return assigneeEnName;
    }

    @Override
    public void setAssigneeEnName(String assigneeEnName) {
        this.assigneeEnName = assigneeEnName;
    }

    public int getApproved() {
        return approved;
    }

    public void setApproved(int approved) {
        this.approved = approved;
    }

    @Override
    public Integer getHold() {
        return hold;
    }

    @Override
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

    @Transient
    public String getVendorName() {
        if(SessionUtils.isCnLang()){
            return  this.vendorCnName;
        }else if(SessionUtils.isEnLang()){
            return this.vendorEnName;
        }
        return null;
    }

    @Transient
    public String getCreatorName() {
        if(SessionUtils.isCnLang()){
            return  this.getCreatorCnName();
        }else if(SessionUtils.isEnLang()){
            return this.getCreatorEnName();
        }
        return null;
    }

    @Transient
    public String getDepartmentName() {
        if(SessionUtils.isCnLang()){
            return  this.getDepartmentCnName();
        }else if(SessionUtils.isEnLang()){
            return this.getDepartmentEnName();
        }
        return null;
    }



}
