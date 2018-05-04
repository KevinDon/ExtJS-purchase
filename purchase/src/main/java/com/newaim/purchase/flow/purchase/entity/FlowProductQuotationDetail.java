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

@Entity
@Table(name = "na_flow_product_quotation_detail")
public class FlowProductQuotationDetail implements Serializable{

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.newaim.core.jpa.IdGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name = "prefix", value = "FPQD")})
    private String id;

    @Column(name = "business_id")
    private String businessId;

    @Column(name = "product_id")
    private String productId;

    private String sku;
    
    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "product_id", insertable=false, updatable=false)
    private Product product;

    private Integer currency;

    /**报价*/
    private BigDecimal priceAud;
    private BigDecimal priceRmb;
    private BigDecimal priceUsd;

    /**汇率*/
    private BigDecimal rateAudToRmb;
    private BigDecimal rateAudToUsd;

    /**起订量*/
    private Integer moq;

    /**历史报价*/
    private BigDecimal prevPriceAud;
    private BigDecimal prevPriceRmb;
    private BigDecimal prevPriceUsd;

    /**历史汇率*/
    private BigDecimal prevRateAudToRmb;
    private BigDecimal prevRateAudToUsd;

    /**上次起订量*/
    private Integer prevMoq;
    private String originPortId;
    private String originPortCnName;
    private String originPortEnName;
    private String destinationPortId;
    private String destinationPortCnName;
    private String destinationPortEnName;

    private Integer hold;

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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
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

    public Integer getMoq() {
        return moq;
    }

    public void setMoq(Integer moq) {
        this.moq = moq;
    }

    public BigDecimal getPrevPriceAud() {
        return prevPriceAud;
    }

    public void setPrevPriceAud(BigDecimal prevPriceAud) {
        this.prevPriceAud = prevPriceAud;
    }

    public BigDecimal getPrevPriceRmb() {
        return prevPriceRmb;
    }

    public void setPrevPriceRmb(BigDecimal prevPriceRmb) {
        this.prevPriceRmb = prevPriceRmb;
    }

    public BigDecimal getPrevPriceUsd() {
        return prevPriceUsd;
    }

    public void setPrevPriceUsd(BigDecimal prevPriceUsd) {
        this.prevPriceUsd = prevPriceUsd;
    }

    public BigDecimal getPrevRateAudToRmb() {
        return prevRateAudToRmb;
    }

    public void setPrevRateAudToRmb(BigDecimal prevRateAudToRmb) {
        this.prevRateAudToRmb = prevRateAudToRmb;
    }

    public BigDecimal getPrevRateAudToUsd() {
        return prevRateAudToUsd;
    }

    public void setPrevRateAudToUsd(BigDecimal prevRateAudToUsd) {
        this.prevRateAudToUsd = prevRateAudToUsd;
    }

    public Integer getPrevMoq() {
        return prevMoq;
    }

    public void setPrevMoq(Integer prevMoq) {
        this.prevMoq = prevMoq;
    }

    public String getOriginPortId() {
        return originPortId;
    }

    public void setOriginPortId(String originPortId) {
        this.originPortId = originPortId;
    }

    public String getOriginPortCnName() {
        return originPortCnName;
    }

    public void setOriginPortCnName(String originPortCnName) {
        this.originPortCnName = originPortCnName;
    }

    public String getOriginPortEnName() {
        return originPortEnName;
    }

    public void setOriginPortEnName(String originPortEnName) {
        this.originPortEnName = originPortEnName;
    }

    public String getDestinationPortId() {
        return destinationPortId;
    }

    public void setDestinationPortId(String destinationPortId) {
        this.destinationPortId = destinationPortId;
    }

    public String getDestinationPortCnName() {
        return destinationPortCnName;
    }

    public void setDestinationPortCnName(String destinationPortCnName) {
        this.destinationPortCnName = destinationPortCnName;
    }

    public String getDestinationPortEnName() {
        return destinationPortEnName;
    }

    public void setDestinationPortEnName(String destinationPortEnName) {
        this.destinationPortEnName = destinationPortEnName;
    }

    public Integer getHold() {
        return hold;
    }

    public void setHold(Integer hold) {
        this.hold = hold;
    }
}
