package com.newaim.purchase.flow.purchase.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.newaim.purchase.archives.product.vo.ProductVo;
import com.newaim.purchase.config.json.JsonMoney;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class FlowSampleDetailVo implements Serializable{

    @JsonIgnore
    private String id;
    private String businessId;
    private String productId;
    private String sku;
    private String sampleName;
    private ProductVo product;
    private Integer qty;
    private Integer currency;

    private BigDecimal sampleFeeAud;//样品金额
    private BigDecimal sampleFeeRmb;
    private BigDecimal sampleFeeUsd;
    @JsonMoney
    private BigDecimal rateAudToRmb;//汇率
    @JsonMoney
    private BigDecimal rateAudToUsd;
    private Integer sampleFeeRefund;//样品费是否可退
    private String sampleReceiver;//样品去向
    private Date sampleWarehouseDate;

    private Date sampleOutboundDate;

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

    public ProductVo getProduct() {
        return product;
    }

    public void setProduct(ProductVo product) {
        this.product = product;
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
