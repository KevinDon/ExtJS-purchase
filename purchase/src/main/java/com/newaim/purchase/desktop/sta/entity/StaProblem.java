package com.newaim.purchase.desktop.sta.entity;

import org.hibernate.annotations.GenericGenerator;
import sun.security.krb5.internal.Ticket;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name="na_sta_problem")
public class StaProblem implements Serializable{

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.newaim.core.jpa.IdGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name = "prefix", value = "SP")})
    private String id;
    private String ticketNo;
    private String categoryId;
    private String categoryCnName;
    private String categoryEnName;
    private String departmentId;
    private String departmentCnName;
    private String departmentEnName;
    private String sku;

    private String orderId;
    private String orderNumber;
    private Date orderAt;
    private Integer orderSent;

    private String sellChannel;
    private BigDecimal refundAmountAud;
    private BigDecimal refundAmountRmb;
    private BigDecimal refundAmountUsd;
    private Date refundAt;

    private String approverId;
    private String approverCnName;
    private String approverEnName;

    private String codeMain;
    private String codeSub;
    private String title;
    private String description;

    /**供应商信息*/
    private String vendorId;
    private String vendorCnName;
    private String vendorEnName;

    private Date createdAt;
    private Integer currency;//结算货币
    /**汇率*/
    private BigDecimal rateAudToRmb;
    private BigDecimal rateAudToUsd;


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

    public String getTicketNo() {
        return ticketNo;
    }

    public void setTicketNo(String ticketNo) {
        this.ticketNo = ticketNo;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryCnName() {
        return categoryCnName;
    }

    public void setCategoryCnName(String categoryCnName) {
        this.categoryCnName = categoryCnName;
    }

    public String getCategoryEnName() {
        return categoryEnName;
    }

    public void setCategoryEnName(String categoryEnName) {
        this.categoryEnName = categoryEnName;
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

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Date getOrderAt() {
        return orderAt;
    }

    public void setOrderAt(Date orderAt) {
        this.orderAt = orderAt;
    }

    public Integer getOrderSent() {
        return orderSent;
    }

    public void setOrderSent(Integer orderSent) {
        this.orderSent = orderSent;
    }

    public String getSellChannel() {
        return sellChannel;
    }

    public void setSellChannel(String sellChannel) {
        this.sellChannel = sellChannel;
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

    public Date getRefundAt() {
        return refundAt;
    }

    public void setRefundAt(Date refundAt) {
        this.refundAt = refundAt;
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

    public String getCodeMain() {
        return codeMain;
    }

    public void setCodeMain(String codeMain) {
        this.codeMain = codeMain;
    }

    public String getCodeSub() {
        return codeSub;
    }

    public void setCodeSub(String codeSub) {
        this.codeSub = codeSub;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
