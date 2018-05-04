package com.newaim.purchase.archives.product.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "na_cost_charge_item")
public class CostChargeItem implements Serializable{

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.newaim.core.jpa.IdGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name = "prefix", value = "CTCI")})
    private String id;

    private String costId;
    private String itemId;
    private String itemCnName;
    private String itemEnName;
    private String unitId;
    private String unitCnName;
    private String unitEnName;
    private Integer currency;
    private BigDecimal rateAudToRmb;
    private BigDecimal rateAudToUsd;
    private BigDecimal priceAud;
    private BigDecimal priceRmb;
    private BigDecimal priceUsd;
    private BigDecimal receivedPriceAud;
    private BigDecimal receivedPriceUsd;
    private BigDecimal receivedPriceRmb;



    private Integer containerType;

    private Integer containerQty;


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
