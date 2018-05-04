package com.newaim.purchase.archives.product.vo;

import com.newaim.purchase.config.json.JsonMoney;

import java.io.Serializable;
import java.math.BigDecimal;

public class CostPortVo implements Serializable{

    private String id;
    private String costId;
    private String originPortId;
    private String originPortCnName;
    private String originPortEnName;
    private String destinationPortId;
    private String destinationPortCnName;
    private String destinationPortEnName;

    /**结算货币、汇率*/
    private Integer currency;
    @JsonMoney
    private BigDecimal rateAudToRmb;
    @JsonMoney
    private BigDecimal rateAudToUsd;

    /**
     * 价格相关
     */
    private BigDecimal priceGp20Aud;
    private BigDecimal priceGp20Rmb;
    private BigDecimal priceGp20Usd;
    private BigDecimal priceGp40Aud;
    private BigDecimal priceGp40Rmb;
    private BigDecimal priceGp40Usd;
    private BigDecimal priceHq40Aud;
    private BigDecimal priceHq40Rmb;
    private BigDecimal priceHq40Usd;
    private BigDecimal priceLclAud;
    private BigDecimal priceLclRmb;
    private BigDecimal priceLclUsd;
    private BigDecimal priceGp20InsuranceAud;
    private BigDecimal priceGp20InsuranceRmb;
    private BigDecimal priceGp20InsuranceUsd;
    private BigDecimal priceGp40InsuranceAud;
    private BigDecimal priceGp40InsuranceRmb;
    private BigDecimal priceGp40InsuranceUsd;
    private BigDecimal priceHq40InsuranceAud;
    private BigDecimal priceHq40InsuranceRmb;
    private BigDecimal priceHq40InsuranceUsd;
    private BigDecimal priceLclInsuranceAud;
    private BigDecimal priceLclInsuranceRmb;
    private BigDecimal priceLclInsuranceUsd;

    /**柜量*/
    private BigDecimal gp20Qty;
    private BigDecimal gp40Qty;
    private BigDecimal hq40Qty;
    private BigDecimal lclCbm;

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

    public BigDecimal getPriceGp20Aud() {
        return priceGp20Aud;
    }

    public void setPriceGp20Aud(BigDecimal priceGp20Aud) {
        this.priceGp20Aud = priceGp20Aud;
    }

    public BigDecimal getPriceGp20Rmb() {
        return priceGp20Rmb;
    }

    public void setPriceGp20Rmb(BigDecimal priceGp20Rmb) {
        this.priceGp20Rmb = priceGp20Rmb;
    }

    public BigDecimal getPriceGp20Usd() {
        return priceGp20Usd;
    }

    public void setPriceGp20Usd(BigDecimal priceGp20Usd) {
        this.priceGp20Usd = priceGp20Usd;
    }

    public BigDecimal getPriceGp40Aud() {
        return priceGp40Aud;
    }

    public void setPriceGp40Aud(BigDecimal priceGp40Aud) {
        this.priceGp40Aud = priceGp40Aud;
    }

    public BigDecimal getPriceGp40Rmb() {
        return priceGp40Rmb;
    }

    public void setPriceGp40Rmb(BigDecimal priceGp40Rmb) {
        this.priceGp40Rmb = priceGp40Rmb;
    }

    public BigDecimal getPriceGp40Usd() {
        return priceGp40Usd;
    }

    public void setPriceGp40Usd(BigDecimal priceGp40Usd) {
        this.priceGp40Usd = priceGp40Usd;
    }

    public BigDecimal getPriceHq40Aud() {
        return priceHq40Aud;
    }

    public void setPriceHq40Aud(BigDecimal priceHq40Aud) {
        this.priceHq40Aud = priceHq40Aud;
    }

    public BigDecimal getPriceHq40Rmb() {
        return priceHq40Rmb;
    }

    public void setPriceHq40Rmb(BigDecimal priceHq40Rmb) {
        this.priceHq40Rmb = priceHq40Rmb;
    }

    public BigDecimal getPriceHq40Usd() {
        return priceHq40Usd;
    }

    public void setPriceHq40Usd(BigDecimal priceHq40Usd) {
        this.priceHq40Usd = priceHq40Usd;
    }

    public BigDecimal getPriceLclAud() {
        return priceLclAud;
    }

    public void setPriceLclAud(BigDecimal priceLclAud) {
        this.priceLclAud = priceLclAud;
    }

    public BigDecimal getPriceLclRmb() {
        return priceLclRmb;
    }

    public void setPriceLclRmb(BigDecimal priceLclRmb) {
        this.priceLclRmb = priceLclRmb;
    }

    public BigDecimal getPriceLclUsd() {
        return priceLclUsd;
    }

    public void setPriceLclUsd(BigDecimal priceLclUsd) {
        this.priceLclUsd = priceLclUsd;
    }

    public BigDecimal getPriceGp20InsuranceAud() {
        return priceGp20InsuranceAud;
    }

    public void setPriceGp20InsuranceAud(BigDecimal priceGp20InsuranceAud) {
        this.priceGp20InsuranceAud = priceGp20InsuranceAud;
    }

    public BigDecimal getPriceGp20InsuranceRmb() {
        return priceGp20InsuranceRmb;
    }

    public void setPriceGp20InsuranceRmb(BigDecimal priceGp20InsuranceRmb) {
        this.priceGp20InsuranceRmb = priceGp20InsuranceRmb;
    }

    public BigDecimal getPriceGp20InsuranceUsd() {
        return priceGp20InsuranceUsd;
    }

    public void setPriceGp20InsuranceUsd(BigDecimal priceGp20InsuranceUsd) {
        this.priceGp20InsuranceUsd = priceGp20InsuranceUsd;
    }

    public BigDecimal getPriceGp40InsuranceAud() {
        return priceGp40InsuranceAud;
    }

    public void setPriceGp40InsuranceAud(BigDecimal priceGp40InsuranceAud) {
        this.priceGp40InsuranceAud = priceGp40InsuranceAud;
    }

    public BigDecimal getPriceGp40InsuranceRmb() {
        return priceGp40InsuranceRmb;
    }

    public void setPriceGp40InsuranceRmb(BigDecimal priceGp40InsuranceRmb) {
        this.priceGp40InsuranceRmb = priceGp40InsuranceRmb;
    }

    public BigDecimal getPriceGp40InsuranceUsd() {
        return priceGp40InsuranceUsd;
    }

    public void setPriceGp40InsuranceUsd(BigDecimal priceGp40InsuranceUsd) {
        this.priceGp40InsuranceUsd = priceGp40InsuranceUsd;
    }

    public BigDecimal getPriceHq40InsuranceAud() {
        return priceHq40InsuranceAud;
    }

    public void setPriceHq40InsuranceAud(BigDecimal priceHq40InsuranceAud) {
        this.priceHq40InsuranceAud = priceHq40InsuranceAud;
    }

    public BigDecimal getPriceHq40InsuranceRmb() {
        return priceHq40InsuranceRmb;
    }

    public void setPriceHq40InsuranceRmb(BigDecimal priceHq40InsuranceRmb) {
        this.priceHq40InsuranceRmb = priceHq40InsuranceRmb;
    }

    public BigDecimal getPriceHq40InsuranceUsd() {
        return priceHq40InsuranceUsd;
    }

    public void setPriceHq40InsuranceUsd(BigDecimal priceHq40InsuranceUsd) {
        this.priceHq40InsuranceUsd = priceHq40InsuranceUsd;
    }

    public BigDecimal getPriceLclInsuranceAud() {
        return priceLclInsuranceAud;
    }

    public void setPriceLclInsuranceAud(BigDecimal priceLclInsuranceAud) {
        this.priceLclInsuranceAud = priceLclInsuranceAud;
    }

    public BigDecimal getPriceLclInsuranceRmb() {
        return priceLclInsuranceRmb;
    }

    public void setPriceLclInsuranceRmb(BigDecimal priceLclInsuranceRmb) {
        this.priceLclInsuranceRmb = priceLclInsuranceRmb;
    }

    public BigDecimal getPriceLclInsuranceUsd() {
        return priceLclInsuranceUsd;
    }

    public void setPriceLclInsuranceUsd(BigDecimal priceLclInsuranceUsd) {
        this.priceLclInsuranceUsd = priceLclInsuranceUsd;
    }

    public BigDecimal getGp20Qty() {
        return gp20Qty;
    }

    public void setGp20Qty(BigDecimal gp20Qty) {
        this.gp20Qty = gp20Qty;
    }

    public BigDecimal getGp40Qty() {
        return gp40Qty;
    }

    public void setGp40Qty(BigDecimal gp40Qty) {
        this.gp40Qty = gp40Qty;
    }

    public BigDecimal getHq40Qty() {
        return hq40Qty;
    }

    public void setHq40Qty(BigDecimal hq40Qty) {
        this.hq40Qty = hq40Qty;
    }

    public BigDecimal getLclCbm() {
        return lclCbm;
    }

    public void setLclCbm(BigDecimal lclCbm) {
        this.lclCbm = lclCbm;
    }
}
