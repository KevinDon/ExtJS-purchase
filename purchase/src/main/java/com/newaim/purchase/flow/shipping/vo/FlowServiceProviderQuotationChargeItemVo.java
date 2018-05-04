package com.newaim.purchase.flow.shipping.vo;

import com.newaim.purchase.config.json.JsonMoney;

import java.io.Serializable;
import java.math.BigDecimal;

public class FlowServiceProviderQuotationChargeItemVo implements Serializable{

    private String id;

    private String businessId;
    private String flowOrderShippingPlanId;

    /**
     * 收费项目相关字段
     */
    private String feeType;
    private String itemId;
    private String itemCnName;
    private String itemEnName;
    private String unitId;
    private String unitCnName;
    private String unitEnName;

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
    private BigDecimal priceOtherAud;
    private BigDecimal priceOtherRmb;
    private BigDecimal priceOtherUsd;

    /**柜量*/
    private BigDecimal gp20Qty;
    private BigDecimal gp40Qty;
    private BigDecimal hq40Qty;
    private BigDecimal lclCbm;

    /**
     * 上一次报价
     */
    @JsonMoney
    private BigDecimal prevRateAudToRmb;
    @JsonMoney
    private BigDecimal prevRateAudToUsd;

    private BigDecimal prevPriceGp20Aud;
    private BigDecimal prevPriceGp20Rmb;
    private BigDecimal prevPriceGp20Usd;
    private BigDecimal prevPriceGp40Aud;
    private BigDecimal prevPriceGp40Rmb;
    private BigDecimal prevPriceGp40Usd;
    private BigDecimal prevPriceHq40Aud;
    private BigDecimal prevPriceHq40Rmb;
    private BigDecimal prevPriceHq40Usd;
    private BigDecimal prevPriceLclAud;
    private BigDecimal prevPriceLclRmb;
    private BigDecimal prevPriceLclUsd;
    private BigDecimal prevPriceOtherAud;
    private BigDecimal prevPriceOtherRmb;
    private BigDecimal prevPriceOtherUsd;

    /**上次柜量*/
    private BigDecimal prevGp20Qty;
    private BigDecimal prevGp40Qty;
    private BigDecimal prevHq40Qty;
    private BigDecimal prevLclCbm;

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

    public String getFlowOrderShippingPlanId() {
        return flowOrderShippingPlanId;
    }

    public void setFlowOrderShippingPlanId(String flowOrderShippingPlanId) {
        this.flowOrderShippingPlanId = flowOrderShippingPlanId;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
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

    public BigDecimal getPriceOtherAud() {
        return priceOtherAud;
    }

    public void setPriceOtherAud(BigDecimal priceOtherAud) {
        this.priceOtherAud = priceOtherAud;
    }

    public BigDecimal getPriceOtherRmb() {
        return priceOtherRmb;
    }

    public void setPriceOtherRmb(BigDecimal priceOtherRmb) {
        this.priceOtherRmb = priceOtherRmb;
    }

    public BigDecimal getPriceOtherUsd() {
        return priceOtherUsd;
    }

    public void setPriceOtherUsd(BigDecimal priceOtherUsd) {
        this.priceOtherUsd = priceOtherUsd;
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

    public BigDecimal getPrevPriceGp20Aud() {
        return prevPriceGp20Aud;
    }

    public void setPrevPriceGp20Aud(BigDecimal prevPriceGp20Aud) {
        this.prevPriceGp20Aud = prevPriceGp20Aud;
    }

    public BigDecimal getPrevPriceGp20Rmb() {
        return prevPriceGp20Rmb;
    }

    public void setPrevPriceGp20Rmb(BigDecimal prevPriceGp20Rmb) {
        this.prevPriceGp20Rmb = prevPriceGp20Rmb;
    }

    public BigDecimal getPrevPriceGp20Usd() {
        return prevPriceGp20Usd;
    }

    public void setPrevPriceGp20Usd(BigDecimal prevPriceGp20Usd) {
        this.prevPriceGp20Usd = prevPriceGp20Usd;
    }

    public BigDecimal getPrevPriceGp40Aud() {
        return prevPriceGp40Aud;
    }

    public void setPrevPriceGp40Aud(BigDecimal prevPriceGp40Aud) {
        this.prevPriceGp40Aud = prevPriceGp40Aud;
    }

    public BigDecimal getPrevPriceGp40Rmb() {
        return prevPriceGp40Rmb;
    }

    public void setPrevPriceGp40Rmb(BigDecimal prevPriceGp40Rmb) {
        this.prevPriceGp40Rmb = prevPriceGp40Rmb;
    }

    public BigDecimal getPrevPriceGp40Usd() {
        return prevPriceGp40Usd;
    }

    public void setPrevPriceGp40Usd(BigDecimal prevPriceGp40Usd) {
        this.prevPriceGp40Usd = prevPriceGp40Usd;
    }

    public BigDecimal getPrevPriceHq40Aud() {
        return prevPriceHq40Aud;
    }

    public void setPrevPriceHq40Aud(BigDecimal prevPriceHq40Aud) {
        this.prevPriceHq40Aud = prevPriceHq40Aud;
    }

    public BigDecimal getPrevPriceHq40Rmb() {
        return prevPriceHq40Rmb;
    }

    public void setPrevPriceHq40Rmb(BigDecimal prevPriceHq40Rmb) {
        this.prevPriceHq40Rmb = prevPriceHq40Rmb;
    }

    public BigDecimal getPrevPriceHq40Usd() {
        return prevPriceHq40Usd;
    }

    public void setPrevPriceHq40Usd(BigDecimal prevPriceHq40Usd) {
        this.prevPriceHq40Usd = prevPriceHq40Usd;
    }

    public BigDecimal getPrevPriceLclAud() {
        return prevPriceLclAud;
    }

    public void setPrevPriceLclAud(BigDecimal prevPriceLclAud) {
        this.prevPriceLclAud = prevPriceLclAud;
    }

    public BigDecimal getPrevPriceLclRmb() {
        return prevPriceLclRmb;
    }

    public void setPrevPriceLclRmb(BigDecimal prevPriceLclRmb) {
        this.prevPriceLclRmb = prevPriceLclRmb;
    }

    public BigDecimal getPrevPriceLclUsd() {
        return prevPriceLclUsd;
    }

    public void setPrevPriceLclUsd(BigDecimal prevPriceLclUsd) {
        this.prevPriceLclUsd = prevPriceLclUsd;
    }

    public BigDecimal getPrevPriceOtherAud() {
        return prevPriceOtherAud;
    }

    public void setPrevPriceOtherAud(BigDecimal prevPriceOtherAud) {
        this.prevPriceOtherAud = prevPriceOtherAud;
    }

    public BigDecimal getPrevPriceOtherRmb() {
        return prevPriceOtherRmb;
    }

    public void setPrevPriceOtherRmb(BigDecimal prevPriceOtherRmb) {
        this.prevPriceOtherRmb = prevPriceOtherRmb;
    }

    public BigDecimal getPrevPriceOtherUsd() {
        return prevPriceOtherUsd;
    }

    public void setPrevPriceOtherUsd(BigDecimal prevPriceOtherUsd) {
        this.prevPriceOtherUsd = prevPriceOtherUsd;
    }

    public BigDecimal getPrevGp20Qty() {
        return prevGp20Qty;
    }

    public void setPrevGp20Qty(BigDecimal prevGp20Qty) {
        this.prevGp20Qty = prevGp20Qty;
    }

    public BigDecimal getPrevGp40Qty() {
        return prevGp40Qty;
    }

    public void setPrevGp40Qty(BigDecimal prevGp40Qty) {
        this.prevGp40Qty = prevGp40Qty;
    }

    public BigDecimal getPrevHq40Qty() {
        return prevHq40Qty;
    }

    public void setPrevHq40Qty(BigDecimal prevHq40Qty) {
        this.prevHq40Qty = prevHq40Qty;
    }

    public BigDecimal getPrevLclCbm() {
        return prevLclCbm;
    }

    public void setPrevLclCbm(BigDecimal prevLclCbm) {
        this.prevLclCbm = prevLclCbm;
    }
}
