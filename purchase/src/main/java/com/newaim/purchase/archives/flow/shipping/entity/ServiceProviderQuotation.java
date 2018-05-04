package com.newaim.purchase.archives.flow.shipping.entity;

import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.flow.workflow.entity.BusinessObject;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "na_service_provider_quotation")
public class ServiceProviderQuotation implements BusinessObject, Serializable {

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.newaim.core.jpa.IdGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name = "prefix", value = "SPQ")})
    private String id;

    private String name;

    /**
     * 服务商相关字段
     */
    private String serviceProviderId;
    private String serviceProviderCnName;
    private String serviceProviderEnName;

    /**
     * 起始港、目的港
     */
    private String originPortId;
    private String originPortCnName;
    private String originPortEnName;
    private String destinationPortId;
    private String destinationPortCnName;
    private String destinationPortEnName;

    private BigDecimal currencyAdjustment;
    private String contractId;
    private String type;
    private String flowOrderShippingPlanId;


    /**结算货币、汇率*/
    private Integer currency;
    private BigDecimal rateAudToRmb;
    private BigDecimal rateAudToUsd;

    /**生效、失效日期*/
    private Date effectiveDate;
    private Date validUntil;
    /**
     * 价格相关
     */
    /**
     * 价格相关
     */
    private BigDecimal totalPriceAud;
    private BigDecimal totalPriceRmb;
    private BigDecimal totalPriceUsd;
    @Column(name = "total_price_gp20_aud")
    private BigDecimal totalPriceGp20Aud;
    @Column(name = "total_price_gp20_rmb")
    private BigDecimal totalPriceGp20Rmb;
    @Column(name = "total_price_gp20_usd")
    private BigDecimal totalPriceGp20Usd;
    @Column(name = "total_price_gp40_aud")
    private BigDecimal totalPriceGp40Aud;
    @Column(name = "total_price_gp40_rmb")
    private BigDecimal totalPriceGp40Rmb;
    @Column(name = "total_price_gp40_usd")
    private BigDecimal totalPriceGp40Usd;
    @Column(name = "total_price_hq40_aud")
    private BigDecimal totalPriceHq40Aud;
    @Column(name = "total_price_hq40_rmb")
    private BigDecimal totalPriceHq40Rmb;
    @Column(name = "total_price_hq40_usd")
    private BigDecimal totalPriceHq40Usd;
    private BigDecimal totalPriceLclAud;
    private BigDecimal totalPriceLclRmb;
    private BigDecimal totalPriceLclUsd;
    private BigDecimal totalPriceOtherAud;
    private BigDecimal totalPriceOtherRmb;
    private BigDecimal totalPriceOtherUsd;

    /**柜量*/
    @Column(name = "total_gp20_qty")
    private BigDecimal totalGp20Qty;
    @Column(name = "total_gp40_qty")
    private BigDecimal totalGp40Qty;
    @Column(name = "total_hq40_qty")
    private BigDecimal totalHq40Qty;
    private BigDecimal totalLclCbm;

    /**创建、更新、申请、完成时间*/
    private Date createdAt;
    private Date updatedAt;
    private Date startTime;
    private Date endTime;

    /**申请人ID*/
    private String creatorId;
    private String creatorCnName;
    private String creatorEnName;
    /**部门ID*/
    private String departmentId;
    private String departmentCnName;
    private String departmentEnName;

    /**状态*/
    private Integer status;
    private Integer flowStatus;
    /**流程实例ID*/
    private String processInstanceId;
    private String businessId;

    /**确认人、确认部门信息*/
    private String reviewerId;
    private String reviewerCnName;
    private String reviewerEnName;
    private String reviewerDepartmentId;
    private String reviewerDepartmentCnName;
    private String reviewerDepartmentEnName;
    /**挂起状态*/
    private Integer hold;
    /**审核*/
    @Transient
    private int approved;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServiceProviderId() {
        return serviceProviderId;
    }

    public void setServiceProviderId(String serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
    }

    public String getServiceProviderCnName() {
        return serviceProviderCnName;
    }

    public void setServiceProviderCnName(String serviceProviderCnName) {
        this.serviceProviderCnName = serviceProviderCnName;
    }

    public String getServiceProviderEnName() {
        return serviceProviderEnName;
    }

    public void setServiceProviderEnName(String serviceProviderEnName) {
        this.serviceProviderEnName = serviceProviderEnName;
    }

    public BigDecimal getTotalGp20Qty() {
        return totalGp20Qty;
    }

    public void setTotalGp20Qty(BigDecimal totalGp20Qty) {
        this.totalGp20Qty = totalGp20Qty;
    }

    public BigDecimal getTotalGp40Qty() {
        return totalGp40Qty;
    }

    public void setTotalGp40Qty(BigDecimal totalGp40Qty) {
        this.totalGp40Qty = totalGp40Qty;
    }

    public BigDecimal getTotalHq40Qty() {
        return totalHq40Qty;
    }

    public void setTotalHq40Qty(BigDecimal totalHq40Qty) {
        this.totalHq40Qty = totalHq40Qty;
    }

    public BigDecimal getTotalLclCbm() {
        return totalLclCbm;
    }

    public void setTotalLclCbm(BigDecimal totalLclCbm) {
        this.totalLclCbm = totalLclCbm;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFlowOrderShippingPlanId() {
        return flowOrderShippingPlanId;
    }

    public void setFlowOrderShippingPlanId(String flowOrderShippingPlanId) {
        this.flowOrderShippingPlanId = flowOrderShippingPlanId;
    }

    public BigDecimal getCurrencyAdjustment() {
        return currencyAdjustment;
    }

    public void setCurrencyAdjustment(BigDecimal currencyAdjustment) {
        this.currencyAdjustment = currencyAdjustment;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
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

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Date getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(Date validUntil) {
        this.validUntil = validUntil;
    }

    public BigDecimal getTotalPriceAud() {
        return totalPriceAud;
    }

    public void setTotalPriceAud(BigDecimal totalPriceAud) {
        this.totalPriceAud = totalPriceAud;
    }

    public BigDecimal getTotalPriceRmb() {
        return totalPriceRmb;
    }

    public void setTotalPriceRmb(BigDecimal totalPriceRmb) {
        this.totalPriceRmb = totalPriceRmb;
    }

    public BigDecimal getTotalPriceUsd() {
        return totalPriceUsd;
    }

    public void setTotalPriceUsd(BigDecimal totalPriceUsd) {
        this.totalPriceUsd = totalPriceUsd;
    }

    public BigDecimal getTotalPriceGp20Aud() {
        return totalPriceGp20Aud;
    }

    public void setTotalPriceGp20Aud(BigDecimal totalPriceGp20Aud) {
        this.totalPriceGp20Aud = totalPriceGp20Aud;
    }

    public BigDecimal getTotalPriceGp20Rmb() {
        return totalPriceGp20Rmb;
    }

    public void setTotalPriceGp20Rmb(BigDecimal totalPriceGp20Rmb) {
        this.totalPriceGp20Rmb = totalPriceGp20Rmb;
    }

    public BigDecimal getTotalPriceGp20Usd() {
        return totalPriceGp20Usd;
    }

    public void setTotalPriceGp20Usd(BigDecimal totalPriceGp20Usd) {
        this.totalPriceGp20Usd = totalPriceGp20Usd;
    }

    public BigDecimal getTotalPriceGp40Aud() {
        return totalPriceGp40Aud;
    }

    public void setTotalPriceGp40Aud(BigDecimal totalPriceGp40Aud) {
        this.totalPriceGp40Aud = totalPriceGp40Aud;
    }

    public BigDecimal getTotalPriceGp40Rmb() {
        return totalPriceGp40Rmb;
    }

    public void setTotalPriceGp40Rmb(BigDecimal totalPriceGp40Rmb) {
        this.totalPriceGp40Rmb = totalPriceGp40Rmb;
    }

    public BigDecimal getTotalPriceGp40Usd() {
        return totalPriceGp40Usd;
    }

    public void setTotalPriceGp40Usd(BigDecimal totalPriceGp40Usd) {
        this.totalPriceGp40Usd = totalPriceGp40Usd;
    }

    public BigDecimal getTotalPriceHq40Aud() {
        return totalPriceHq40Aud;
    }

    public void setTotalPriceHq40Aud(BigDecimal totalPriceHq40Aud) {
        this.totalPriceHq40Aud = totalPriceHq40Aud;
    }

    public BigDecimal getTotalPriceHq40Rmb() {
        return totalPriceHq40Rmb;
    }

    public void setTotalPriceHq40Rmb(BigDecimal totalPriceHq40Rmb) {
        this.totalPriceHq40Rmb = totalPriceHq40Rmb;
    }

    public BigDecimal getTotalPriceHq40Usd() {
        return totalPriceHq40Usd;
    }

    public void setTotalPriceHq40Usd(BigDecimal totalPriceHq40Usd) {
        this.totalPriceHq40Usd = totalPriceHq40Usd;
    }

    public BigDecimal getTotalPriceLclAud() {
        return totalPriceLclAud;
    }

    public void setTotalPriceLclAud(BigDecimal totalPriceLclAud) {
        this.totalPriceLclAud = totalPriceLclAud;
    }

    public BigDecimal getTotalPriceLclRmb() {
        return totalPriceLclRmb;
    }

    public void setTotalPriceLclRmb(BigDecimal totalPriceLclRmb) {
        this.totalPriceLclRmb = totalPriceLclRmb;
    }

    public BigDecimal getTotalPriceLclUsd() {
        return totalPriceLclUsd;
    }

    public void setTotalPriceLclUsd(BigDecimal totalPriceLclUsd) {
        this.totalPriceLclUsd = totalPriceLclUsd;
    }

    public BigDecimal getTotalPriceOtherAud() {
        return totalPriceOtherAud;
    }

    public void setTotalPriceOtherAud(BigDecimal totalPriceOtherAud) {
        this.totalPriceOtherAud = totalPriceOtherAud;
    }

    public BigDecimal getTotalPriceOtherRmb() {
        return totalPriceOtherRmb;
    }

    public void setTotalPriceOtherRmb(BigDecimal totalPriceOtherRmb) {
        this.totalPriceOtherRmb = totalPriceOtherRmb;
    }

    public BigDecimal getTotalPriceOtherUsd() {
        return totalPriceOtherUsd;
    }

    public void setTotalPriceOtherUsd(BigDecimal totalPriceOtherUsd) {
        this.totalPriceOtherUsd = totalPriceOtherUsd;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public Date getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public Date getStartTime() {
        return startTime;
    }

    @Override
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @Override
    public Date getEndTime() {
        return endTime;
    }

    @Override
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    public String getCreatorId() {
        return creatorId;
    }

    @Override
    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    @Override
    public String getCreatorCnName() {
        return creatorCnName;
    }

    @Override
    public void setCreatorCnName(String creatorCnName) {
        this.creatorCnName = creatorCnName;
    }

    @Override
    public String getCreatorEnName() {
        return creatorEnName;
    }

    @Override
    public void setCreatorEnName(String creatorEnName) {
        this.creatorEnName = creatorEnName;
    }

    @Override
    public String getDepartmentId() {
        return departmentId;
    }

    @Override
    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public String getDepartmentCnName() {
        return departmentCnName;
    }

    @Override
    public void setDepartmentCnName(String departmentCnName) {
        this.departmentCnName = departmentCnName;
    }

    @Override
    public String getDepartmentEnName() {
        return departmentEnName;
    }

    @Override
    public void setDepartmentEnName(String departmentEnName) {
        this.departmentEnName = departmentEnName;
    }

    @Override
    public Integer getStatus() {
        return status;
    }

    @Override
    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public Integer getFlowStatus() {
        return flowStatus;
    }

    @Override
    public void setFlowStatus(Integer flowStatus) {
        this.flowStatus = flowStatus;
    }

    @Override
    public String getProcessInstanceId() {
        return processInstanceId;
    }

    @Override
    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    @Override
    public String getBusinessId() {
        return businessId;
    }

    @Override
    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    @Override
    public String getReviewerId() {
        return reviewerId;
    }

    @Override
    public void setReviewerId(String reviewerId) {
        this.reviewerId = reviewerId;
    }

    @Override
    public String getReviewerCnName() {
        return reviewerCnName;
    }

    @Override
    public void setReviewerCnName(String reviewerCnName) {
        this.reviewerCnName = reviewerCnName;
    }

    @Override
    public String getReviewerEnName() {
        return reviewerEnName;
    }

    @Override
    public void setReviewerEnName(String reviewerEnName) {
        this.reviewerEnName = reviewerEnName;
    }

    @Override
    public String getReviewerDepartmentId() {
        return reviewerDepartmentId;
    }

    @Override
    public void setReviewerDepartmentId(String reviewerDepartmentId) {
        this.reviewerDepartmentId = reviewerDepartmentId;
    }

    @Override
    public String getReviewerDepartmentCnName() {
        return reviewerDepartmentCnName;
    }

    @Override
    public void setReviewerDepartmentCnName(String reviewerDepartmentCnName) {
        this.reviewerDepartmentCnName = reviewerDepartmentCnName;
    }

    @Override
    public String getReviewerDepartmentEnName() {
        return reviewerDepartmentEnName;
    }

    @Override
    public void setReviewerDepartmentEnName(String reviewerDepartmentEnName) {
        this.reviewerDepartmentEnName = reviewerDepartmentEnName;
    }

    public int getApproved() {
        return approved;
    }

    public void setApproved(int approved) {
        this.approved = approved;
    }

    @Transient
    public String getServiceProviderName() {
        if(SessionUtils.isCnLang()){
            return  this.serviceProviderCnName;
        }else if(SessionUtils.isEnLang()){
            return this.serviceProviderEnName;
        }
        return null;
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

    @Override
    public Integer getHold() {
        return hold;
    }

    @Override
    public void setHold(Integer hold) {
        this.hold = hold;
    }
}
