package com.newaim.purchase.flow.inspection.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
@Entity
@Table(name = "na_flow_order_qc_detail")
public class FlowOrderQcDetail implements Serializable{

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.newaim.core.jpa.IdGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name = "prefix", value = "FOQD")})
    private String id;

    @Column(name = "business_id")
    private String businessId;

    private String orderId;
    private String orderTitle;
    private String orderNumber;

    private Integer currency;
    @JsonIgnore
    private BigDecimal totalPriceAud;//总价格
    @JsonIgnore
    private BigDecimal totalPriceRmb;
    @JsonIgnore
    private BigDecimal totalPriceUsd;
    private Integer totalOrderQty;//总数量
    @JsonIgnore
    private BigDecimal rateAudToRmb;//汇率
    @JsonIgnore
    private BigDecimal rateAudToUsd;
    private BigDecimal depositAud;//定金
    private BigDecimal depositRmb;
    private BigDecimal depositUsd;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderTitle() {
        return orderTitle;
    }

    public void setOrderTitle(String orderTitle) {
        this.orderTitle = orderTitle;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Integer getCurrency() {
        return currency;
    }

    public void setCurrency(Integer currency) {
        this.currency = currency;
    }

    public BigDecimal getTotalPriceAud() {
        return totalPriceAud;
    }

    public void setTotalPriceAud(BigDecimal totalPriceAud) {
        this.totalPriceAud = totalPriceAud;
    }

    public BigDecimal getTotalPriceRmb() {
        return totalPriceRmb;
    }

    public void setTotalPriceRmb(BigDecimal totalPriceRmb) {
        this.totalPriceRmb = totalPriceRmb;
    }

    public BigDecimal getTotalPriceUsd() {
        return totalPriceUsd;
    }

    public void setTotalPriceUsd(BigDecimal totalPriceUsd) {
        this.totalPriceUsd = totalPriceUsd;
    }

    public Integer getTotalOrderQty() {
        return totalOrderQty;
    }

    public void setTotalOrderQty(Integer totalOrderQty) {
        this.totalOrderQty = totalOrderQty;
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

    public BigDecimal getDepositAud() {
        return depositAud;
    }

    public void setDepositAud(BigDecimal depositAud) {
        this.depositAud = depositAud;
    }

    public BigDecimal getDepositRmb() {
        return depositRmb;
    }

    public void setDepositRmb(BigDecimal depositRmb) {
        this.depositRmb = depositRmb;
    }

    public BigDecimal getDepositUsd() {
        return depositUsd;
    }

    public void setDepositUsd(BigDecimal depositUsd) {
        this.depositUsd = depositUsd;
    }
}
