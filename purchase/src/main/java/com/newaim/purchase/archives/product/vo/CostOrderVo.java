package com.newaim.purchase.archives.product.vo;

import com.newaim.purchase.config.json.JsonMoney;

import java.io.Serializable;
import java.math.BigDecimal;


public class CostOrderVo implements Serializable{

    private String id;
    private String costId;
    private String orderId;
    private String orderTitle;
    private String orderNumber;
    private Integer currency;
    @JsonMoney
    private BigDecimal rateAudToRmb;
    @JsonMoney
    private BigDecimal rateAudToUsd;

    private BigDecimal priceCostAud;
    private BigDecimal priceCostRmb;
    private BigDecimal priceCostUsd;
    private BigDecimal portFeeAud;
    private BigDecimal portFeeRmb;
    private BigDecimal portFeeUsd;
    private BigDecimal chargeItemFeeRmb;
    private BigDecimal chargeItemFeeUsd;
    private BigDecimal chargeItemFeeAud;
    private BigDecimal tariffAud;
    private BigDecimal tariffRmb;
    private BigDecimal tariffUsd;
    private BigDecimal customProcessingFeeAud;
    private BigDecimal customProcessingFeeRmb;
    private BigDecimal customProcessingFeeUsd;
    private BigDecimal otherFeeAud;
    private BigDecimal otherFeeRmb;
    private BigDecimal otherFeeUsd;
    private BigDecimal totalCostAud;
    private BigDecimal totalCostRmb;
    private BigDecimal totalCostUsd;
    //产品总的GST
    private BigDecimal gstAud;
    private BigDecimal gstRmb;
    private BigDecimal gstUsd;
    //总本地费GST
    private BigDecimal localGstAud;
    private BigDecimal localGstRmb;
    private BigDecimal localGstUsd;
    //总货值GST
    private BigDecimal valueGstAud;
    private BigDecimal valueGstRmb;
    private BigDecimal valueGstUsd;

    private BigDecimal totalCbm;
    private BigDecimal totalShippingCbm;
    private BigDecimal totalPackingCbm;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCostId() {
        return costId;
    }


    public BigDecimal getTotalCbm() {
        return totalCbm;
    }

    public void setTotalCbm(BigDecimal totalCbm) {
        this.totalCbm = totalCbm;
    }

    public BigDecimal getTotalShippingCbm() {
        return totalShippingCbm;
    }

    public void setTotalShippingCbm(BigDecimal totalShippingCbm) {
        this.totalShippingCbm = totalShippingCbm;
    }

    public BigDecimal getTotalPackingCbm() {
        return totalPackingCbm;
    }

    public void setTotalPackingCbm(BigDecimal totalPackingCbm) {
        this.totalPackingCbm = totalPackingCbm;
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

    public BigDecimal getPortFeeAud() {
        return portFeeAud;
    }

    public void setPortFeeAud(BigDecimal portFeeAud) {
        this.portFeeAud = portFeeAud;
    }

    public BigDecimal getPortFeeRmb() {
        return portFeeRmb;
    }

    public void setPortFeeRmb(BigDecimal portFeeRmb) {
        this.portFeeRmb = portFeeRmb;
    }

    public BigDecimal getPortFeeUsd() {
        return portFeeUsd;
    }

    public void setPortFeeUsd(BigDecimal portFeeUsd) {
        this.portFeeUsd = portFeeUsd;
    }

    public BigDecimal getChargeItemFeeRmb() {
        return chargeItemFeeRmb;
    }

    public void setChargeItemFeeRmb(BigDecimal chargeItemFeeRmb) {
        this.chargeItemFeeRmb = chargeItemFeeRmb;
    }

    public BigDecimal getChargeItemFeeUsd() {
        return chargeItemFeeUsd;
    }

    public void setChargeItemFeeUsd(BigDecimal chargeItemFeeUsd) {
        this.chargeItemFeeUsd = chargeItemFeeUsd;
    }

    public BigDecimal getChargeItemFeeAud() {
        return chargeItemFeeAud;
    }

    public void setChargeItemFeeAud(BigDecimal chargeItemFeeAud) {
        this.chargeItemFeeAud = chargeItemFeeAud;
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

    public BigDecimal getCustomProcessingFeeAud() {
        return customProcessingFeeAud;
    }

    public void setCustomProcessingFeeAud(BigDecimal customProcessingFeeAud) {
        this.customProcessingFeeAud = customProcessingFeeAud;
    }

    public BigDecimal getCustomProcessingFeeRmb() {
        return customProcessingFeeRmb;
    }

    public void setCustomProcessingFeeRmb(BigDecimal customProcessingFeeRmb) {
        this.customProcessingFeeRmb = customProcessingFeeRmb;
    }

    public BigDecimal getCustomProcessingFeeUsd() {
        return customProcessingFeeUsd;
    }

    public void setCustomProcessingFeeUsd(BigDecimal customProcessingFeeUsd) {
        this.customProcessingFeeUsd = customProcessingFeeUsd;
    }

    public BigDecimal getOtherFeeAud() {
        return otherFeeAud;
    }

    public void setOtherFeeAud(BigDecimal otherFeeAud) {
        this.otherFeeAud = otherFeeAud;
    }

    public BigDecimal getOtherFeeRmb() {
        return otherFeeRmb;
    }

    public void setOtherFeeRmb(BigDecimal otherFeeRmb) {
        this.otherFeeRmb = otherFeeRmb;
    }

    public BigDecimal getOtherFeeUsd() {
        return otherFeeUsd;
    }

    public void setOtherFeeUsd(BigDecimal otherFeeUsd) {
        this.otherFeeUsd = otherFeeUsd;
    }

    public BigDecimal getTotalCostAud() {
        return totalCostAud;
    }

    public void setTotalCostAud(BigDecimal totalCostAud) {
        this.totalCostAud = totalCostAud;
    }

    public BigDecimal getTotalCostRmb() {
        return totalCostRmb;
    }

    public void setTotalCostRmb(BigDecimal totalCostRmb) {
        this.totalCostRmb = totalCostRmb;
    }

    public BigDecimal getTotalCostUsd() {
        return totalCostUsd;
    }

    public void setTotalCostUsd(BigDecimal totalCostUsd) {
        this.totalCostUsd = totalCostUsd;
    }

    public BigDecimal getGstAud() {
        return gstAud;
    }

    public void setGstAud(BigDecimal gstAud) {
        this.gstAud = gstAud;
    }

    public BigDecimal getGstRmb() {
        return gstRmb;
    }

    public void setGstRmb(BigDecimal gstRmb) {
        this.gstRmb = gstRmb;
    }

    public BigDecimal getGstUsd() {
        return gstUsd;
    }

    public void setGstUsd(BigDecimal gstUsd) {
        this.gstUsd = gstUsd;
    }

    public BigDecimal getLocalGstAud() {
        return localGstAud;
    }

    public void setLocalGstAud(BigDecimal localGstAud) {
        this.localGstAud = localGstAud;
    }

    public BigDecimal getLocalGstRmb() {
        return localGstRmb;
    }

    public void setLocalGstRmb(BigDecimal localGstRmb) {
        this.localGstRmb = localGstRmb;
    }

    public BigDecimal getLocalGstUsd() {
        return localGstUsd;
    }

    public void setLocalGstUsd(BigDecimal localGstUsd) {
        this.localGstUsd = localGstUsd;
    }

    public BigDecimal getValueGstAud() {
        return valueGstAud;
    }

    public void setValueGstAud(BigDecimal valueGstAud) {
        this.valueGstAud = valueGstAud;
    }

    public BigDecimal getValueGstRmb() {
        return valueGstRmb;
    }

    public void setValueGstRmb(BigDecimal valueGstRmb) {
        this.valueGstRmb = valueGstRmb;
    }

    public BigDecimal getValueGstUsd() {
        return valueGstUsd;
    }

    public void setValueGstUsd(BigDecimal valueGstUsd) {
        this.valueGstUsd = valueGstUsd;
    }
}
