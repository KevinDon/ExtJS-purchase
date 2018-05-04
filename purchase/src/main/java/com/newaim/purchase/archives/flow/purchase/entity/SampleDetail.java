package com.newaim.purchase.archives.flow.purchase.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "na_sample_detail")
public class SampleDetail implements Serializable{

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.newaim.core.jpa.IdGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name = "prefix", value = "SAMD")})
    private String id;
    private String sampleId;
    private String productId;
    private String sku;
    private String sampleName;
    private Integer qty;
    private Integer currency;
    private BigDecimal sampleFeeAud;
    private BigDecimal sampleFeeRmb;
    private BigDecimal sampleFeeUsd;
    private BigDecimal rateAudToRmb;
    private BigDecimal rateAudToUsd;
    private Integer sampleFeeRefund;
    private String sampleReceiver;

    private Date sampleWarehouseDate;

    private Date sampleOutboundDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSampleId() {
        return sampleId;
    }

    public void setSampleId(String sampleId) {
        this.sampleId = sampleId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getSampleName() {
        return sampleName;
    }

    public void setSampleName(String sampleName) {
        this.sampleName = sampleName;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
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

    public String getSampleReceiver() {
        return sampleReceiver;
    }

    public void setSampleReceiver(String sampleReceiver) {
        this.sampleReceiver = sampleReceiver;
    }

    public Date getSampleWarehouseDate() {
        return sampleWarehouseDate;
    }

    public void setSampleWarehouseDate(Date sampleWarehouseDate) {
        this.sampleWarehouseDate = sampleWarehouseDate;
    }

    public Date getSampleOutboundDate() {
        return sampleOutboundDate;
    }

    public void setSampleOutboundDate(Date sampleOutboundDate) {
        this.sampleOutboundDate = sampleOutboundDate;
    }
}
