package com.newaim.purchase.desktop.sta.vo;

import com.newaim.purchase.config.json.JsonMoney;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


public class StaCostVo implements Serializable{

    private String id;

    private String orderId;
    private String productId;
    private String sku;
    private Integer orderQty;
    private Integer currency;//结算货币
    /**汇率*/
    @JsonMoney
    private BigDecimal rateAudToRmb;
    @JsonMoney
    private BigDecimal rateAudToUsd;
    /**采购成本费*/
    private BigDecimal priceCostAud;
    private BigDecimal priceCostRmb;
    private BigDecimal priceCostUsd;
    /**海运成本费*/
    private BigDecimal portFeeAud;
    private BigDecimal portFeeRmb;
    private BigDecimal portFeeUsd;
    /**本地费*/
    private BigDecimal chargeItemFeeAud;
    private BigDecimal chargeItemFeeRmb;
    private BigDecimal chargeItemFeeUsd;
    /**关税*/
    private BigDecimal tariffAud;
    private BigDecimal tariffRmb;
    private BigDecimal tariffUsd;
    private BigDecimal customProcessingFeeAud;
    private BigDecimal customProcessingFeeRmb;
    private BigDecimal customProcessingFeeUsd;
    private BigDecimal otherFeeAud;
    private BigDecimal otherFeeRmb;
    private BigDecimal otherFeeUsd;
    private BigDecimal gstAud;
    private BigDecimal gstRmb;
    private BigDecimal gstUsd;
    private BigDecimal totalCostAud;
    private BigDecimal totalCostRmb;
    private BigDecimal totalCostUsd;
    private BigDecimal totalCostTaxAud;
    private BigDecimal totalCostTaxRmb;
    private BigDecimal totalCostTaxUsd;
    private BigDecimal totalOrderCostAud;
    private BigDecimal totalOrderCostRmb;
    private BigDecimal totalOrderCostUsd;
    private BigDecimal addedCostAud;
    private BigDecimal addedCostRmb;
    private BigDecimal addedCostUsd;
    private BigDecimal totalAddedCostAud;
    private BigDecimal totalAddedCostRmb;
    private BigDecimal totalAddedCostUsd;
    private BigDecimal electronicProcessingFeeAud;
    private BigDecimal electronicProcessingFeeRmb;
    private BigDecimal electronicProcessingFeeUsd;
    private BigDecimal cbm;
    private BigDecimal totalCbm;
    private BigDecimal totalItemCbm;

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

    public BigDecimal getTotalCbm() {
        return totalCbm;
    }

    public void setTotalCbm(BigDecimal totalCbm) {
        this.totalCbm = totalCbm;
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

    public Integer getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(Integer orderQty) {
        this.orderQty = orderQty;
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

    public BigDecimal getChargeItemFeeAud() {
        return chargeItemFeeAud;
    }

    public void setChargeItemFeeAud(BigDecimal chargeItemFeeAud) {
        this.chargeItemFeeAud = chargeItemFeeAud;
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

    public BigDecimal getTotalCostTaxAud() {
        return totalCostTaxAud;
    }

    public void setTotalCostTaxAud(BigDecimal totalCostTaxAud) {
        this.totalCostTaxAud = totalCostTaxAud;
    }

    public BigDecimal getTotalCostTaxRmb() {
        return totalCostTaxRmb;
    }

    public void setTotalCostTaxRmb(BigDecimal totalCostTaxRmb) {
        this.totalCostTaxRmb = totalCostTaxRmb;
    }

    public BigDecimal getTotalCostTaxUsd() {
        return totalCostTaxUsd;
    }

    public void setTotalCostTaxUsd(BigDecimal totalCostTaxUsd) {
        this.totalCostTaxUsd = totalCostTaxUsd;
    }

    public BigDecimal getTotalOrderCostAud() {
        return totalOrderCostAud;
    }

    public void setTotalOrderCostAud(BigDecimal totalOrderCostAud) {
        this.totalOrderCostAud = totalOrderCostAud;
    }

    public BigDecimal getTotalOrderCostRmb() {
        return totalOrderCostRmb;
    }

    public void setTotalOrderCostRmb(BigDecimal totalOrderCostRmb) {
        this.totalOrderCostRmb = totalOrderCostRmb;
    }

    public BigDecimal getTotalOrderCostUsd() {
        return totalOrderCostUsd;
    }

    public void setTotalOrderCostUsd(BigDecimal totalOrderCostUsd) {
        this.totalOrderCostUsd = totalOrderCostUsd;
    }

    public BigDecimal getAddedCostAud() {
        return addedCostAud;
    }

    public void setAddedCostAud(BigDecimal addedCostAud) {
        this.addedCostAud = addedCostAud;
    }

    public BigDecimal getAddedCostRmb() {
        return addedCostRmb;
    }

    public void setAddedCostRmb(BigDecimal addedCostRmb) {
        this.addedCostRmb = addedCostRmb;
    }

    public BigDecimal getAddedCostUsd() {
        return addedCostUsd;
    }

    public void setAddedCostUsd(BigDecimal addedCostUsd) {
        this.addedCostUsd = addedCostUsd;
    }

    public BigDecimal getTotalAddedCostAud() {
        return totalAddedCostAud;
    }

    public void setTotalAddedCostAud(BigDecimal totalAddedCostAud) {
        this.totalAddedCostAud = totalAddedCostAud;
    }

    public BigDecimal getTotalAddedCostRmb() {
        return totalAddedCostRmb;
    }

    public void setTotalAddedCostRmb(BigDecimal totalAddedCostRmb) {
        this.totalAddedCostRmb = totalAddedCostRmb;
    }

    public BigDecimal getTotalAddedCostUsd() {
        return totalAddedCostUsd;
    }

    public void setTotalAddedCostUsd(BigDecimal totalAddedCostUsd) {
        this.totalAddedCostUsd = totalAddedCostUsd;
    }

    public BigDecimal getElectronicProcessingFeeAud() {
        return electronicProcessingFeeAud;
    }

    public void setElectronicProcessingFeeAud(BigDecimal electronicProcessingFeeAud) {
        this.electronicProcessingFeeAud = electronicProcessingFeeAud;
    }

    public BigDecimal getElectronicProcessingFeeRmb() {
        return electronicProcessingFeeRmb;
    }

    public void setElectronicProcessingFeeRmb(BigDecimal electronicProcessingFeeRmb) {
        this.electronicProcessingFeeRmb = electronicProcessingFeeRmb;
    }

    public BigDecimal getElectronicProcessingFeeUsd() {
        return electronicProcessingFeeUsd;
    }

    public void setElectronicProcessingFeeUsd(BigDecimal electronicProcessingFeeUsd) {
        this.electronicProcessingFeeUsd = electronicProcessingFeeUsd;
    }

    public BigDecimal getCbm() {
        return cbm;
    }

    public void setCbm(BigDecimal cbm) {
        this.cbm = cbm;
    }

    public BigDecimal getTotalItemCbm() {
        return totalItemCbm;
    }

    public void setTotalItemCbm(BigDecimal totalItemCbm) {
        this.totalItemCbm = totalItemCbm;
    }
}
