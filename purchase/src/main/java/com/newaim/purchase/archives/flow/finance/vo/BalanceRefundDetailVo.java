package com.newaim.purchase.archives.flow.finance.vo;

import com.newaim.purchase.config.json.JsonMoney;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class BalanceRefundDetailVo implements Serializable{

    private String id;

    /**差额退款ID*/
    private String balanceRefundId;

    private String asnId;
    private String asnNumber;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBalanceRefundId() {
        return balanceRefundId;
    }

    public void setBalanceRefundId(String balanceRefundId) {
        this.balanceRefundId = balanceRefundId;
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
}
