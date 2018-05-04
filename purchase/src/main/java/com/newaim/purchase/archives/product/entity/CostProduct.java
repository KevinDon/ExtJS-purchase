package com.newaim.purchase.archives.product.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "na_cost_product")
public class CostProduct implements Serializable{

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.newaim.core.jpa.IdGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name = "prefix", value = "CTPD")})
    private String id;
    private String costId;
    private String orderId;
    private String orderNumber;
    private String orderTitle;
    private String productId;
    private String sku;
    private String hsCode;
    private String vendorId;
    private String vendorCnName;
    private String vendorEnName;
    private BigDecimal masterCartonCbm;
    private BigDecimal unitCbm;
    private Integer pcsPerCarton;
    private Integer currency;
    private BigDecimal rateAudToRmb;
    private BigDecimal rateAudToUsd;


    private BigDecimal priceAud;
    private BigDecimal priceRmb;
    private BigDecimal priceUsd;
    private Integer orderQty;
    private BigDecimal packingQty;
    private BigDecimal packingCartons;
    private BigDecimal orderValueAud;
    private BigDecimal orderValueRmb;
    private BigDecimal orderValueUsd;
    private BigDecimal salesPriceRmb;
    private BigDecimal salesPriceUsd;
    private BigDecimal salesPriceAud;
    private BigDecimal salesValueAud;
    private BigDecimal salesValueRmb;
    private BigDecimal salesValueUsd;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCostId() {
        return costId;
    }

    public void setCostId(String costId) {
        this.costId = costId;
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

    public String getOrderTitle() {
        return orderTitle;
    }

    public void setOrderTitle(String orderTitle) {
        this.orderTitle = orderTitle;
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

    public String getHsCode() {
        return hsCode;
    }

    public void setHsCode(String hsCode) {
        this.hsCode = hsCode;
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

    public BigDecimal getMasterCartonCbm() {
        return masterCartonCbm;
    }

    public void setMasterCartonCbm(BigDecimal masterCartonCbm) {
        this.masterCartonCbm = masterCartonCbm;
    }

    public BigDecimal getUnitCbm() {
        return unitCbm;
    }

    public void setUnitCbm(BigDecimal unitCbm) {
        this.unitCbm = unitCbm;
    }

    public Integer getPcsPerCarton() {
        return pcsPerCarton;
    }

    public void setPcsPerCarton(Integer pcsPerCarton) {
        this.pcsPerCarton = pcsPerCarton;
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

    public Integer getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(Integer orderQty) {
        this.orderQty = orderQty;
    }

    public BigDecimal getPackingQty() {
        return packingQty;
    }

    public void setPackingQty(BigDecimal packingQty) {
        this.packingQty = packingQty;
    }

    public BigDecimal getPackingCartons() {
        return packingCartons;
    }

    public void setPackingCartons(BigDecimal packingCartons) {
        this.packingCartons = packingCartons;
    }

    public BigDecimal getOrderValueAud() {
        return orderValueAud;
    }

    public void setOrderValueAud(BigDecimal orderValueAud) {
        this.orderValueAud = orderValueAud;
    }

    public BigDecimal getOrderValueRmb() {
        return orderValueRmb;
    }

    public void setOrderValueRmb(BigDecimal orderValueRmb) {
        this.orderValueRmb = orderValueRmb;
    }

    public BigDecimal getOrderValueUsd() {
        return orderValueUsd;
    }

    public void setOrderValueUsd(BigDecimal orderValueUsd) {
        this.orderValueUsd = orderValueUsd;
    }

    public BigDecimal getSalesPriceRmb() {
        return salesPriceRmb;
    }

    public void setSalesPriceRmb(BigDecimal salesPriceRmb) {
        this.salesPriceRmb = salesPriceRmb;
    }

    public BigDecimal getSalesPriceUsd() {
        return salesPriceUsd;
    }

    public void setSalesPriceUsd(BigDecimal salesPriceUsd) {
        this.salesPriceUsd = salesPriceUsd;
    }

    public BigDecimal getSalesPriceAud() {
        return salesPriceAud;
    }

    public void setSalesPriceAud(BigDecimal salesPriceAud) {
        this.salesPriceAud = salesPriceAud;
    }

    public BigDecimal getSalesValueAud() {
        return salesValueAud;
    }

    public void setSalesValueAud(BigDecimal salesValueAud) {
        this.salesValueAud = salesValueAud;
    }

    public BigDecimal getSalesValueRmb() {
        return salesValueRmb;
    }

    public void setSalesValueRmb(BigDecimal salesValueRmb) {
        this.salesValueRmb = salesValueRmb;
    }

    public BigDecimal getSalesValueUsd() {
        return salesValueUsd;
    }

    public void setSalesValueUsd(BigDecimal salesValueUsd) {
        this.salesValueUsd = salesValueUsd;
    }
}
