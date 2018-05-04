package com.newaim.purchase.archives.product.entity;

import com.newaim.core.utils.SessionUtils;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "na_cost")
public class Cost implements Serializable{

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.newaim.core.jpa.IdGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name = "prefix", value = "COST")})
    private String id;

    private String orderShippingPlanId;
    private String orderShippingPlanBusinessId;
    /**海运时长*/
    private Integer sailingDays;
    private Integer currency;
    private BigDecimal rateAudToRmb;
    private BigDecimal rateAudToUsd;
    private BigDecimal customProcessingFeeAud;
    private BigDecimal customProcessingFeeRmb;
    private BigDecimal customProcessingFeeUsd;
    private Integer status;
    private String creatorId;
    private String creatorCnName;
    private String creatorEnName;
    private Date createdAt;
    private Date updatedAt;
    private String departmentId;
    private String departmentCnName;
    private String departmentEnName;
    private Date retd;
    private Date reta;

    /**总发运体积**/
    private BigDecimal totalShippingCbm;
    /**费用支付合计*/
    private BigDecimal paymentSumAud;
    private BigDecimal paymentSumUsd;
    private BigDecimal paymentSumRmb;
    /**海运费用合计*/
    private BigDecimal freightSumAud;
    private BigDecimal freightSumUsd;
    private BigDecimal freightSumRmb;
    /**货柜数*/
    private Integer freightContainerQty;
    /**本地费用合计*/
    private BigDecimal chargeSumAud;
    private BigDecimal chargeSumRmb;
    private BigDecimal chargeSumUsd;
    /**关税合计*/
    private BigDecimal tariffSumAud;
    private BigDecimal tariffSumRmb;
    private BigDecimal tariffSumUsd;
    /**合同金额合计*/
    private BigDecimal valueSumAud;
    private BigDecimal valueSumRmb;
    private BigDecimal valueSumUsd;
    /**其他费用合计*/
    private BigDecimal otherSumAud;
    private BigDecimal otherSumRmb;
    private BigDecimal otherSumUsd;
    /**冲销费用合计*/
    private BigDecimal writeOffSumAud;
    private BigDecimal writeOffSumRmb;
    private BigDecimal writeOffSumUsd;

    //总本地费GST
    private BigDecimal localGstAud;
    private BigDecimal localGstRmb;
    private BigDecimal localGstUsd;
    //总货值GST
    private BigDecimal valueGstAud;
    private BigDecimal valueGstRmb;
    private BigDecimal valueGstUsd;

    //相关订单
    private String orderNumbers;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderShippingPlanId() {
        return orderShippingPlanId;
    }

    public void setOrderShippingPlanId(String orderShippingPlanId) {
        this.orderShippingPlanId = orderShippingPlanId;
    }

    public String getOrderShippingPlanBusinessId() {
        return orderShippingPlanBusinessId;
    }

    public void setOrderShippingPlanBusinessId(String orderShippingPlanBusinessId) {
        this.orderShippingPlanBusinessId = orderShippingPlanBusinessId;
    }

    public Integer getSailingDays() {
        return sailingDays;
    }

    public void setSailingDays(Integer sailingDays) {
        this.sailingDays = sailingDays;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorCnName() {
        return creatorCnName;
    }

    public void setCreatorCnName(String creatorCnName) {
        this.creatorCnName = creatorCnName;
    }

    public String getCreatorEnName() {
        return creatorEnName;
    }

    public void setCreatorEnName(String creatorEnName) {
        this.creatorEnName = creatorEnName;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentCnName() {
        return departmentCnName;
    }

    public void setDepartmentCnName(String departmentCnName) {
        this.departmentCnName = departmentCnName;
    }

    public String getDepartmentEnName() {
        return departmentEnName;
    }

    public void setDepartmentEnName(String departmentEnName) {
        this.departmentEnName = departmentEnName;
    }

    public Date getRetd() {
        return retd;
    }

    public void setRetd(Date retd) {
        this.retd = retd;
    }

    public Date getReta() {
        return reta;
    }

    public void setReta(Date reta) {
        this.reta = reta;
    }


    public BigDecimal getTotalShippingCbm() {
        return totalShippingCbm;
    }

    public void setTotalShippingCbm(BigDecimal totalShippingCbm) {
        this.totalShippingCbm = totalShippingCbm;
    }

    public BigDecimal getPaymentSumAud() {
        return paymentSumAud;
    }

    public void setPaymentSumAud(BigDecimal paymentSumAud) {
        this.paymentSumAud = paymentSumAud;
    }

    public BigDecimal getPaymentSumUsd() {
        return paymentSumUsd;
    }

    public void setPaymentSumUsd(BigDecimal paymentSumUsd) {
        this.paymentSumUsd = paymentSumUsd;
    }

    public BigDecimal getPaymentSumRmb() {
        return paymentSumRmb;
    }

    public void setPaymentSumRmb(BigDecimal paymentSumRmb) {
        this.paymentSumRmb = paymentSumRmb;
    }

    public BigDecimal getFreightSumAud() {
        return freightSumAud;
    }

    public void setFreightSumAud(BigDecimal freightSumAud) {
        this.freightSumAud = freightSumAud;
    }

    public BigDecimal getFreightSumUsd() {
        return freightSumUsd;
    }

    public void setFreightSumUsd(BigDecimal freightSumUsd) {
        this.freightSumUsd = freightSumUsd;
    }

    public BigDecimal getFreightSumRmb() {
        return freightSumRmb;
    }

    public void setFreightSumRmb(BigDecimal freightSumRmb) {
        this.freightSumRmb = freightSumRmb;
    }

    public Integer getFreightContainerQty() {
        return freightContainerQty;
    }

    public void setFreightContainerQty(Integer freightContainerQty) {
        this.freightContainerQty = freightContainerQty;
    }

    public BigDecimal getChargeSumAud() {
        return chargeSumAud;
    }

    public void setChargeSumAud(BigDecimal chargeSumAud) {
        this.chargeSumAud = chargeSumAud;
    }

    public BigDecimal getChargeSumRmb() {
        return chargeSumRmb;
    }

    public void setChargeSumRmb(BigDecimal chargeSumRmb) {
        this.chargeSumRmb = chargeSumRmb;
    }

    public BigDecimal getChargeSumUsd() {
        return chargeSumUsd;
    }

    public void setChargeSumUsd(BigDecimal chargeSumUsd) {
        this.chargeSumUsd = chargeSumUsd;
    }

    public BigDecimal getTariffSumAud() {
        return tariffSumAud;
    }

    public void setTariffSumAud(BigDecimal tariffSumAud) {
        this.tariffSumAud = tariffSumAud;
    }

    public BigDecimal getTariffSumRmb() {
        return tariffSumRmb;
    }

    public void setTariffSumRmb(BigDecimal tariffSumRmb) {
        this.tariffSumRmb = tariffSumRmb;
    }

    public BigDecimal getTariffSumUsd() {
        return tariffSumUsd;
    }

    public void setTariffSumUsd(BigDecimal tariffSumUsd) {
        this.tariffSumUsd = tariffSumUsd;
    }

    public BigDecimal getValueSumAud() {
        return valueSumAud;
    }

    public void setValueSumAud(BigDecimal valueSumAud) {
        this.valueSumAud = valueSumAud;
    }

    public BigDecimal getValueSumRmb() {
        return valueSumRmb;
    }

    public void setValueSumRmb(BigDecimal valueSumRmb) {
        this.valueSumRmb = valueSumRmb;
    }

    public BigDecimal getValueSumUsd() {
        return valueSumUsd;
    }

    public void setValueSumUsd(BigDecimal valueSumUsd) {
        this.valueSumUsd = valueSumUsd;
    }

    public BigDecimal getOtherSumAud() {
        return otherSumAud;
    }

    public void setOtherSumAud(BigDecimal otherSumAud) {
        this.otherSumAud = otherSumAud;
    }

    public BigDecimal getOtherSumRmb() {
        return otherSumRmb;
    }

    public void setOtherSumRmb(BigDecimal otherSumRmb) {
        this.otherSumRmb = otherSumRmb;
    }

    public BigDecimal getOtherSumUsd() {
        return otherSumUsd;
    }

    public void setOtherSumUsd(BigDecimal otherSumUsd) {
        this.otherSumUsd = otherSumUsd;
    }

    public BigDecimal getWriteOffSumAud() {
        return writeOffSumAud;
    }

    public void setWriteOffSumAud(BigDecimal writeOffSumAud) {
        this.writeOffSumAud = writeOffSumAud;
    }

    public BigDecimal getWriteOffSumRmb() {
        return writeOffSumRmb;
    }

    public void setWriteOffSumRmb(BigDecimal writeOffSumRmb) {
        this.writeOffSumRmb = writeOffSumRmb;
    }

    public BigDecimal getWriteOffSumUsd() {
        return writeOffSumUsd;
    }

    public void setWriteOffSumUsd(BigDecimal writeOffSumUsd) {
        this.writeOffSumUsd = writeOffSumUsd;
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

    @Transient
    public String getCreatorName() {
        if(SessionUtils.isCnLang()){
            return  this.creatorCnName;
        }else if(SessionUtils.isEnLang()){
            return this.creatorEnName;
        }
        return null;
    }

    @Transient
    public String getDepartmentName() {
        if(SessionUtils.isCnLang()){
            return  this.departmentCnName;
        }else if(SessionUtils.isEnLang()){
            return this.departmentEnName;
        }
        return null;
    }

    public String getOrderNumbers() {
        return orderNumbers;
    }

    public void setOrderNumbers(String orderNumbers) {
        this.orderNumbers = orderNumbers;
    }
}
