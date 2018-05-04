package com.newaim.purchase.archives.flow.finance.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.newaim.purchase.config.json.JsonMoney;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class FeeRegisterDetailVo implements Serializable{

    @JsonIgnore
    private String id;

    private String feeRegisterId;

    /**项目名称*/
    private String itemId;
    private String itemCnName;
    private String itemEnName;
    private Integer currency;
    private BigDecimal priceAud;
    private BigDecimal priceRmb;
    private BigDecimal priceUsd;
    private String orderNumber;

    private Integer qty;

    /**汇率*/
    @JsonMoney
    private BigDecimal rateAudToRmb;
    @JsonMoney
    private BigDecimal rateAudToUsd;

    /**实付汇率**/
    @JsonMoney
    private BigDecimal paymentRateAudToRmb;
    @JsonMoney
    private BigDecimal paymentRateAudToUsd;

    private String remark;
    /**创建时间*/
    private Date createdAt;

    private Integer type;

    private Integer settlementType;

    /**
     * 是否用于成本计算，1是 2否
     */
    private Integer applyCost;

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFeeRegisterId() {
        return feeRegisterId;
    }

    public void setFeeRegisterId(String feeRegisterId) {
        this.feeRegisterId = feeRegisterId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemCnName() {
        return itemCnName;
    }

    public void setItemCnName(String itemCnName) {
        this.itemCnName = itemCnName;
    }

    public String getItemEnName() {
        return itemEnName;
    }

    public void setItemEnName(String itemEnName) {
        this.itemEnName = itemEnName;
    }

    public Integer getCurrency() {
        return currency;
    }

    public void setCurrency(Integer currency) {
        this.currency = currency;
    }

    public BigDecimal getPriceAud() {
        return priceAud;
    }

    public void setPriceAud(BigDecimal priceAud) {
        this.priceAud = priceAud;
    }

    public BigDecimal getPriceRmb() {
        return priceRmb;
    }

    public void setPriceRmb(BigDecimal priceRmb) {
        this.priceRmb = priceRmb;
    }

    public BigDecimal getPriceUsd() {
        return priceUsd;
    }

    public void setPriceUsd(BigDecimal priceUsd) {
        this.priceUsd = priceUsd;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getSettlementType() {
        return settlementType;
    }

    public void setSettlementType(Integer settlementType) {
        this.settlementType = settlementType;
    }

    public Integer getApplyCost() {
        return applyCost;
    }

    public void setApplyCost(Integer applyCost) {
        this.applyCost = applyCost;
    }
}
