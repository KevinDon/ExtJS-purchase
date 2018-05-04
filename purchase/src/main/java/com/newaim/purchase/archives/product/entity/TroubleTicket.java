package com.newaim.purchase.archives.product.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "na_trouble_ticket")
public class TroubleTicket implements Serializable{

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.newaim.core.jpa.IdGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name = "prefix", value = "TBTK")})
    private String id;

    //订单信息
    private String omsOrderId;
    private String sellOrderId;
    private String orderId;
    private String orderNumber;
    private String vendorId;
    private String vendorCnName;
    private String vendorEnName;
    private String sellChannel;
    private String memberNickname;
    private String transactionNumber;
    private Integer qty;
    private String email;
    private Date orderAt;

    private Integer handleMode;
    //退款信息
    private Date refundAt;
    private Integer currency;
    private BigDecimal rateAudToRmb;
    private BigDecimal rateAudToUsd;
    private BigDecimal refundAmountAud;
    private BigDecimal refundAmountRmb;
    private BigDecimal refundAmountUsd;
    private String accountName;
    private String refundPaymentMode;
    private Integer isShipped;

    //重发信息
    private String newAddress;
    private BigDecimal reDeliveryValueAud;
    private BigDecimal reDeliveryValueRmb;
    private BigDecimal reDeliveryValueUsd;
    private String reDeliveryOrderId;
    private BigDecimal reDeliveryWeight;
    private Date reDeliveryAt;
    private Integer priority;

    //处理信息
    private String handleStatus;
    private String handlerId;
    private String handlerCnName;
    private String handlerEnName;
    private String handlerDepartmentId;
    private String handlerDepartmentCnName;
    private String handlerDepartmentEnName;
    private Date handledAt;

    //审批信息
    private String approverId;
    private String approverCnName;
    private String approverEnName;
    private String approverDepartmentId;
    private String approverDepartmentCnName;
    private String approverDepartmentEnName;
    private Date approvedAt;

    //创建信息
    private Integer status;
    private String creatorId;
    private String creatorCnName;
    private String creatorEnName;
    private Date createdAt;
    private Date updatedAt;
    private String departmentId;
    private String departmentCnName;
    private String departmentEnName;

    //退货信息
    private Integer returnMethod;
    private String returnTrackingNumber;
    private BigDecimal returnCostAud;
    private BigDecimal returnCostRmb;
    private BigDecimal returnCostUsd;
    private Date returnInitiated;
    private Date returnReceived;
    private Integer inspectionOutcome;

    //客户备注
    private String remark;

    @Transient
    private String comment;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOmsOrderId() {
        return omsOrderId;
    }

    public void setOmsOrderId(String omsOrderId) {
        this.omsOrderId = omsOrderId;
    }

    public String getSellOrderId() {
        return sellOrderId;
    }

    public void setSellOrderId(String sellOrderId) {
        this.sellOrderId = sellOrderId;
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

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
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

    public String getSellChannel() {
        return sellChannel;
    }

    public void setSellChannel(String sellChannel) {
        this.sellChannel = sellChannel;
    }

    public String getMemberNickname() {
        return memberNickname;
    }

    public void setMemberNickname(String memberNickname) {
        this.memberNickname = memberNickname;
    }

    public String getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(String transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getOrderAt() {
        return orderAt;
    }

    public void setOrderAt(Date orderAt) {
        this.orderAt = orderAt;
    }

    public Date getRefundAt() {
        return refundAt;
    }

    public void setRefundAt(Date refundAt) {
        this.refundAt = refundAt;
    }

    public Integer getCurrency() {
        return currency;
    }

    public void setCurrency(Integer currency) {
        this.currency = currency;
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

    public BigDecimal getRefundAmountAud() {
        return refundAmountAud;
    }

    public void setRefundAmountAud(BigDecimal refundAmountAud) {
        this.refundAmountAud = refundAmountAud;
    }

    public BigDecimal getRefundAmountRmb() {
        return refundAmountRmb;
    }

    public void setRefundAmountRmb(BigDecimal refundAmountRmb) {
        this.refundAmountRmb = refundAmountRmb;
    }

    public BigDecimal getRefundAmountUsd() {
        return refundAmountUsd;
    }

    public void setRefundAmountUsd(BigDecimal refundAmountUsd) {
        this.refundAmountUsd = refundAmountUsd;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getRefundPaymentMode() {
        return refundPaymentMode;
    }

    public void setRefundPaymentMode(String refundPaymentMode) {
        this.refundPaymentMode = refundPaymentMode;
    }

    public Integer getIsShipped() {
        return isShipped;
    }

    public void setIsShipped(Integer isShipped) {
        this.isShipped = isShipped;
    }

    public String getNewAddress() {
        return newAddress;
    }

    public void setNewAddress(String newAddress) {
        this.newAddress = newAddress;
    }

    public BigDecimal getReDeliveryValueAud() {
        return reDeliveryValueAud;
    }

    public void setReDeliveryValueAud(BigDecimal reDeliveryValueAud) {
        this.reDeliveryValueAud = reDeliveryValueAud;
    }

    public BigDecimal getReDeliveryValueRmb() {
        return reDeliveryValueRmb;
    }

    public void setReDeliveryValueRmb(BigDecimal reDeliveryValueRmb) {
        this.reDeliveryValueRmb = reDeliveryValueRmb;
    }

    public BigDecimal getReDeliveryValueUsd() {
        return reDeliveryValueUsd;
    }

    public void setReDeliveryValueUsd(BigDecimal reDeliveryValueUsd) {
        this.reDeliveryValueUsd = reDeliveryValueUsd;
    }

    public String getReDeliveryOrderId() {
        return reDeliveryOrderId;
    }

    public void setReDeliveryOrderId(String reDeliveryOrderId) {
        this.reDeliveryOrderId = reDeliveryOrderId;
    }

    public BigDecimal getReDeliveryWeight() {
        return reDeliveryWeight;
    }

    public void setReDeliveryWeight(BigDecimal reDeliveryWeight) {
        this.reDeliveryWeight = reDeliveryWeight;
    }

    public Date getReDeliveryAt() {
        return reDeliveryAt;
    }

    public void setReDeliveryAt(Date reDeliveryAt) {
        this.reDeliveryAt = reDeliveryAt;
    }

    public String getHandleStatus() {
        return handleStatus;
    }

    public void setHandleStatus(String handleStatus) {
        this.handleStatus = handleStatus;
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

    public String getApproverId() {
        return approverId;
    }

    public void setApproverId(String approverId) {
        this.approverId = approverId;
    }

    public String getApproverCnName() {
        return approverCnName;
    }

    public void setApproverCnName(String approverCnName) {
        this.approverCnName = approverCnName;
    }

    public String getApproverEnName() {
        return approverEnName;
    }

    public void setApproverEnName(String approverEnName) {
        this.approverEnName = approverEnName;
    }

    public String getApproverDepartmentId() {
        return approverDepartmentId;
    }

    public void setApproverDepartmentId(String approverDepartmentId) {
        this.approverDepartmentId = approverDepartmentId;
    }

    public String getApproverDepartmentCnName() {
        return approverDepartmentCnName;
    }

    public void setApproverDepartmentCnName(String approverDepartmentCnName) {
        this.approverDepartmentCnName = approverDepartmentCnName;
    }

    public String getApproverDepartmentEnName() {
        return approverDepartmentEnName;
    }

    public void setApproverDepartmentEnName(String approverDepartmentEnName) {
        this.approverDepartmentEnName = approverDepartmentEnName;
    }

    public Date getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(Date approvedAt) {
        this.approvedAt = approvedAt;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public Integer getReturnMethod() {
        return returnMethod;
    }

    public void setReturnMethod(Integer returnMethod) {
        this.returnMethod = returnMethod;
    }

    public String getReturnTrackingNumber() {
        return returnTrackingNumber;
    }

    public void setReturnTrackingNumber(String returnTrackingNumber) {
        this.returnTrackingNumber = returnTrackingNumber;
    }

    public BigDecimal getReturnCostAud() {
        return returnCostAud;
    }

    public void setReturnCostAud(BigDecimal returnCostAud) {
        this.returnCostAud = returnCostAud;
    }

    public BigDecimal getReturnCostRmb() {
        return returnCostRmb;
    }

    public void setReturnCostRmb(BigDecimal returnCostRmb) {
        this.returnCostRmb = returnCostRmb;
    }

    public BigDecimal getReturnCostUsd() {
        return returnCostUsd;
    }

    public void setReturnCostUsd(BigDecimal returnCostUsd) {
        this.returnCostUsd = returnCostUsd;
    }

    public Date getReturnInitiated() {
        return returnInitiated;
    }

    public void setReturnInitiated(Date returnInitiated) {
        this.returnInitiated = returnInitiated;
    }

    public Date getReturnReceived() {
        return returnReceived;
    }

    public void setReturnReceived(Date returnReceived) {
        this.returnReceived = returnReceived;
    }

    public Integer getInspectionOutcome() {
        return inspectionOutcome;
    }

    public void setInspectionOutcome(Integer inspectionOutcome) {
        this.inspectionOutcome = inspectionOutcome;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getHandleMode() {
        return handleMode;
    }

    public void setHandleMode(Integer handleMode) {
        this.handleMode = handleMode;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }
}
