package com.newaim.purchase.flow.purchase.entity;

import com.newaim.purchase.archives.product.entity.Product;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "na_flow_sample_detail")
public class FlowSampleDetail implements Serializable{

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.newaim.core.jpa.IdGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name = "prefix", value = "FSAMD")})
    private String id;
    @Column(name = "business_id")
    private String businessId;

    @Column(name = "product_id")
    private String productId;

    private String sku;

    private String sampleName;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "product_id", insertable=false, updatable=false)
    private Product product;

    private Integer qty;

    private Integer currency;
    private BigDecimal sampleFeeAud;//样品金额
    private BigDecimal sampleFeeRmb;
    private BigDecimal sampleFeeUsd;
    private BigDecimal rateAudToRmb;//汇率
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
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
