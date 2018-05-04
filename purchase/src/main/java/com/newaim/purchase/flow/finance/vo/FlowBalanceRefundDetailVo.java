package com.newaim.purchase.flow.finance.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.newaim.purchase.config.json.JsonMoney;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class FlowBalanceRefundDetailVo implements Serializable{

    @JsonIgnore
    private String id;

    /**业务流水号*/
    private String businessId;

    private String asnId;
    private String asnNumber;
    private String productId;
    private String sku;

    /**发运、实收、差异数量*/
    private Integer expectedQty;
    private Integer receivedQty;
    private Integer diffQty;

    /**结算币种*/
    private Integer currency;

    /**采购单价、汇率*/
    private BigDecimal priceAud;
    private BigDecimal priceRmb;
    private BigDecimal priceUsd;
    @JsonMoney
    private BigDecimal rateAudToRmb;
    @JsonMoney
    private BigDecimal rateAudToUsd;

    /**类型，供应商、服务商*/
    private Integer type;
    /**货代报价订单ID*/
    private String shippingOrderId;
    /**收费项目*/
    private String payProject;
    /**差额*/
    private BigDecimal diffAud;
    private BigDecimal diffRmb;
    private BigDecimal diffUsd;

    /**状态*/
    private Integer status;

    /**使用时间、备注*/
    private Date usedTime;
    private String remark;
    /**实收金额*/
    private BigDecimal receivedPriceAud;
    private BigDecimal receivedPriceRmb;
    private BigDecimal receivedPriceUsd;

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

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getAsnId() {
        return asnId;
    }

    public void setAsnId(String asnId) {
        this.asnId = asnId;
    }

    public String getAsnNumber() {
        return asnNumber;
    }

    public void setAsnNumber(String asnNumber) {
        this.asnNumber = asnNumber;
    }

    public Integer getExpectedQty() {
        return expectedQty;
    }

    public void setExpectedQty(Integer expectedQty) {
        this.expectedQty = expectedQty;
    }

    public Integer getReceivedQty() {
        return receivedQty;
    }

    public void setReceivedQty(Integer receivedQty) {
        this.receivedQty = receivedQty;
    }

    public Integer getDiffQty() {
        return diffQty;
    }

    public void setDiffQty(Integer diffQty) {
        this.diffQty = diffQty;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getShippingOrderId() {
        return shippingOrderId;
    }

    public void setShippingOrderId(String shippingOrderId) {
        this.shippingOrderId = shippingOrderId;
    }

    public String getPayProject() {
        return payProject;
    }

    public void setPayProject(String payProject) {
        this.payProject = payProject;
    }

    public BigDecimal getDiffAud() {
        return diffAud;
    }

    public void setDiffAud(BigDecimal diffAud) {
        this.diffAud = diffAud;
    }

    public BigDecimal getDiffRmb() {
        return diffRmb;
    }

    public void setDiffRmb(BigDecimal diffRmb) {
        this.diffRmb = diffRmb;
    }

    public BigDecimal getDiffUsd() {
        return diffUsd;
    }

    public void setDiffUsd(BigDecimal diffUsd) {
        this.diffUsd = diffUsd;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(Date usedTime) {
        this.usedTime = usedTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public BigDecimal getReceivedPriceAud() {
        return receivedPriceAud;
    }

    public void setReceivedPriceAud(BigDecimal receivedPriceAud) {
        this.receivedPriceAud = receivedPriceAud;
    }

    public BigDecimal getReceivedPriceRmb() {
        return receivedPriceRmb;
    }

    public void setReceivedPriceRmb(BigDecimal receivedPriceRmb) {
        this.receivedPriceRmb = receivedPriceRmb;
    }

    public BigDecimal getReceivedPriceUsd() {
        return receivedPriceUsd;
    }

    public void setReceivedPriceUsd(BigDecimal receivedPriceUsd) {
        this.receivedPriceUsd = receivedPriceUsd;
    }
}
