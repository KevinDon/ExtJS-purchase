package com.newaim.purchase.flow.shipping.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "na_flow_asn_packing_detail")
public class FlowAsnPackingDetail implements Serializable{

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.newaim.core.jpa.IdGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name = "prefix", value = "FASNPD")})
    private String id;
    @Column(name = "business_id")
    private String businessId;
    private String asnPackingId;
    private String ccPackingDetailId;

    private String orderId;
    private String orderNumber;
    private String productId;
    private String sku;
    private String ean;
    /**采购数量、应收箱数、实收数量、实收箱数*/
    private Integer expectedQty;
    private Integer expectedCartons;
    private Integer receivedQty;
    private Integer receivedCartons;

    private Integer currency;
    /**采购成本、汇率*/
    private BigDecimal priceCostAud;
    private BigDecimal priceCostRmb;
    private BigDecimal priceCostUsd;
    private BigDecimal rateAudToRmb;
    private BigDecimal rateAudToUsd;

    private BigDecimal priceAud;
    private BigDecimal priceRmb;
    private BigDecimal priceUsd;
    private Integer chargebackStatus;

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

	public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getAsnPackingId() {
        return asnPackingId;
    }

    public void setAsnPackingId(String asnPackingId) {
        this.asnPackingId = asnPackingId;
    }

    public String getCcPackingDetailId() {
        return ccPackingDetailId;
    }

    public void setCcPackingDetailId(String ccPackingDetailId) {
        this.ccPackingDetailId = ccPackingDetailId;
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

    public String getEan() {
        return ean;
    }

    public void setEan(String ean) {
        this.ean = ean;
    }

    public Integer getExpectedQty() {
        return expectedQty;
    }

    public void setExpectedQty(Integer expectedQty) {
        this.expectedQty = expectedQty;
    }

    public Integer getExpectedCartons() {
        return expectedCartons;
    }

    public void setExpectedCartons(Integer expectedCartons) {
        this.expectedCartons = expectedCartons;
    }

    public Integer getReceivedQty() {
        return receivedQty;
    }

    public void setReceivedQty(Integer receivedQty) {
        this.receivedQty = receivedQty;
    }

    public Integer getReceivedCartons() {
        return receivedCartons;
    }

    public void setReceivedCartons(Integer receivedCartons) {
        this.receivedCartons = receivedCartons;
    }

    public Integer getCurrency() {
        return currency;
    }

    public void setCurrency(Integer currency) {
        this.currency = currency;
    }

    public BigDecimal getPriceCostAud() {
        return priceCostAud;
    }

    public void setPriceCostAud(BigDecimal priceCostAud) {
        this.priceCostAud = priceCostAud;
    }

    public BigDecimal getPriceCostRmb() {
        return priceCostRmb;
    }

    public void setPriceCostRmb(BigDecimal priceCostRmb) {
        this.priceCostRmb = priceCostRmb;
    }

    public BigDecimal getPriceCostUsd() {
        return priceCostUsd;
    }

    public void setPriceCostUsd(BigDecimal priceCostUsd) {
        this.priceCostUsd = priceCostUsd;
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

    public Integer getChargebackStatus() {
        return chargebackStatus;
    }

    public void setChargebackStatus(Integer chargebackStatus) {
        this.chargebackStatus = chargebackStatus;
    }
}
