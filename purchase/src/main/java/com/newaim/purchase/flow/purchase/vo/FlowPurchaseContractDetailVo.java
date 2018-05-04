package com.newaim.purchase.flow.purchase.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.newaim.purchase.archives.product.vo.ProductVo;
import com.newaim.purchase.config.json.JsonMoney;

import java.io.Serializable;
import java.math.BigDecimal;

public class FlowPurchaseContractDetailVo implements Serializable{

    @JsonIgnore
    private String id;

    private String businessId;
    private String productId;
    private ProductVo product;
    private String factoryCode;
    private Integer currency;
    private BigDecimal priceAud;
    private BigDecimal priceRmb;
    private BigDecimal priceUsd;
    @JsonMoney
    private BigDecimal rateAudToRmb;
    @JsonMoney
    private BigDecimal rateAudToUsd;
    private Integer orderQty;
    private BigDecimal orderValueAud;
    private BigDecimal orderValueRmb;
    private BigDecimal orderValueUsd;
    private Integer cartons;//箱数
    private String purchasePlanDetailId;
    private String purchasePlanId;
    private String purchasePlanBusinessId;
    private Integer availableQty;
    private Integer planAvailableQty;

    private Integer srcOrderQty;
    private Integer hold;
    private Integer isNeedDeposit;


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

    public ProductVo getProduct() {
        return product;
    }

    public void setProduct(ProductVo product) {
        this.product = product;
    }

    public String getFactoryCode() {
        return factoryCode;
    }

    public void setFactoryCode(String factoryCode) {
        this.factoryCode = factoryCode;
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

    public Integer getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(Integer orderQty) {
        this.orderQty = orderQty;
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

    public Integer getCartons() {
        return cartons;
    }

    public void setCartons(Integer cartons) {
        this.cartons = cartons;
    }

    public String getPurchasePlanDetailId() {
        return purchasePlanDetailId;
    }

    public void setPurchasePlanDetailId(String purchasePlanDetailId) {
        this.purchasePlanDetailId = purchasePlanDetailId;
    }

    public String getPurchasePlanId() {
        return purchasePlanId;
    }

    public void setPurchasePlanId(String purchasePlanId) {
        this.purchasePlanId = purchasePlanId;
    }

    public String getPurchasePlanBusinessId() {
        return purchasePlanBusinessId;
    }

    public void setPurchasePlanBusinessId(String purchasePlanBusinessId) {
        this.purchasePlanBusinessId = purchasePlanBusinessId;
    }

    public Integer getAvailableQty() {
        return availableQty;
    }

    public void setAvailableQty(Integer availableQty) {
        this.availableQty = availableQty;
    }

    public Integer getSrcOrderQty() {
        return srcOrderQty;
    }

    public void setSrcOrderQty(Integer srcOrderQty) {
        this.srcOrderQty = srcOrderQty;
    }

    public Integer getHold() {
        return hold;
    }

    public void setHold(Integer hold) {
        this.hold = hold;
    }

    public Integer getIsNeedDeposit() {
        return isNeedDeposit;
    }

    public void setIsNeedDeposit(Integer isNeedDeposit) {
        this.isNeedDeposit = isNeedDeposit;
    }

    public Integer getPlanAvailableQty() {
        return planAvailableQty;
    }

    public void setPlanAvailableQty(Integer planAvailableQty) {
        this.planAvailableQty = planAvailableQty;
    }
}
