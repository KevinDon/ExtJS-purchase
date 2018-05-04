package com.newaim.purchase.archives.flow.finance.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "na_sample_payment_detail")
public class SamplePaymentDetail implements Serializable{

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.newaim.core.jpa.IdGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name = "prefix", value = "SPD")})
    private String id;

    private String samplePaymentId;

    private String productId;
    private String sku;
    private Integer currency;
    private BigDecimal sampleFeeAud;//样品金额
    private BigDecimal sampleFeeRmb;
    private BigDecimal sampleFeeUsd;
    private BigDecimal rateAudToRmb;//汇率
    private BigDecimal rateAudToUsd;
    private Integer sampleFeeRefund;//样品费是否可退
    private Integer qty;//数量

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSamplePaymentId() {
        return samplePaymentId;
    }

    public void setSamplePaymentId(String samplePaymentId) {
        this.samplePaymentId = samplePaymentId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getCurrency() {
        return currency;
    }

    public void setCurrency(Integer currency) {
        this.currency = currency;
    }

    public BigDecimal getSampleFeeAud() {
        return sampleFeeAud;
    }

    public void setSampleFeeAud(BigDecimal sampleFeeAud) {
        this.sampleFeeAud = sampleFeeAud;
    }

    public BigDecimal getSampleFeeRmb() {
        return sampleFeeRmb;
    }

    public void setSampleFeeRmb(BigDecimal sampleFeeRmb) {
        this.sampleFeeRmb = sampleFeeRmb;
    }

    public BigDecimal getSampleFeeUsd() {
        return sampleFeeUsd;
    }

    public void setSampleFeeUsd(BigDecimal sampleFeeUsd) {
        this.sampleFeeUsd = sampleFeeUsd;
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

    public Integer getSampleFeeRefund() {
        return sampleFeeRefund;
    }

    public void setSampleFeeRefund(Integer sampleFeeRefund) {
        this.sampleFeeRefund = sampleFeeRefund;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }
}
