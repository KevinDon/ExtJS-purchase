package com.newaim.purchase.archives.product.vo;

import com.newaim.purchase.config.json.JsonMoney;

import java.io.Serializable;
import java.math.BigDecimal;


public class CostChargeItemVo implements Serializable{

    private String id;

    private String costId;
    private String itemId;
    private String itemCnName;
    private String itemEnName;
    private String unitId;
    private String unitCnName;
    private String unitEnName;
    private Integer currency;
    @JsonMoney
    private BigDecimal rateAudToRmb;
    @JsonMoney
    private BigDecimal rateAudToUsd;
    private BigDecimal priceAud;
    private BigDecimal priceRmb;
    private BigDecimal priceUsd;
    private Integer containerType;

    private Integer containerQty;

    private BigDecimal receivedPriceAud;
    private BigDecimal receivedPriceUsd;
    private BigDecimal receivedPriceRmb;


    public BigDecimal getReceivedPriceAud() {
        return receivedPriceAud;
    }

    public void setReceivedPriceAud(BigDecimal receivedPriceAud) {
        this.receivedPriceAud = receivedPriceAud;
    }

    public BigDecimal getReceivedPriceUsd() {
        return receivedPriceUsd;
    }

    public void setReceivedPriceUsd(BigDecimal receivedPriceUsd) {
        this.receivedPriceUsd = receivedPriceUsd;
    }

    public BigDecimal getReceivedPriceRmb() {
        return receivedPriceRmb;
    }

    public void setReceivedPriceRmb(BigDecimal receivedPriceRmb) {
        this.receivedPriceRmb = receivedPriceRmb;
    }

    public Integer getContainerType() {
        return containerType;
    }

    public void setContainerType(Integer containerType) {
        this.containerType = containerType;
    }

    public Integer getContainerQty() {
        return containerQty;
    }

    public void setContainerQty(Integer containerQty) {
        this.containerQty = containerQty;
    }

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

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getUnitCnName() {
        return unitCnName;
    }

    public void setUnitCnName(String unitCnName) {
        this.unitCnName = unitCnName;
    }

    public String getUnitEnName() {
        return unitEnName;
    }

    public void setUnitEnName(String unitEnName) {
        this.unitEnName = unitEnName;
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
}
