package com.newaim.purchase.archives.product.vo;

import com.newaim.purchase.config.json.JsonMoney;

import java.io.Serializable;
import java.math.BigDecimal;

public class CostTariffVo implements Serializable{

    private String id;
    private String costId;
    private String hsCode;
    private String description;
    private Integer currency;
    @JsonMoney
    private BigDecimal rateAudToRmb;
    @JsonMoney
    private BigDecimal rateAudToUsd;
    private BigDecimal salesValueAud;
    private BigDecimal salesValueRmb;
    private BigDecimal salesValueUsd;
    private Integer salesQty;
    private BigDecimal dutyRate;
    private BigDecimal tariffAud;
    private BigDecimal tariffRmb;
    private BigDecimal tariffUsd;

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

    public String getHsCode() {
        return hsCode;
    }

    public void setHsCode(String hsCode) {
        this.hsCode = hsCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Integer getSalesQty() {
        return salesQty;
    }

    public void setSalesQty(Integer salesQty) {
        this.salesQty = salesQty;
    }

    public BigDecimal getDutyRate() {
        return dutyRate;
    }

    public void setDutyRate(BigDecimal dutyRate) {
        this.dutyRate = dutyRate;
    }

    public BigDecimal getTariffAud() {
        return tariffAud;
    }

    public void setTariffAud(BigDecimal tariffAud) {
        this.tariffAud = tariffAud;
    }

    public BigDecimal getTariffRmb() {
        return tariffRmb;
    }

    public void setTariffRmb(BigDecimal tariffRmb) {
        this.tariffRmb = tariffRmb;
    }

    public BigDecimal getTariffUsd() {
        return tariffUsd;
    }

    public void setTariffUsd(BigDecimal tariffUsd) {
        this.tariffUsd = tariffUsd;
    }
}
