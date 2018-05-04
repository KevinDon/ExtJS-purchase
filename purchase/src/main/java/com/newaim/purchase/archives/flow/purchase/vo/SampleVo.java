package com.newaim.purchase.archives.flow.purchase.vo;

import com.google.common.collect.Lists;
import com.newaim.purchase.config.json.JsonMoney;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class SampleVo implements Serializable {

    private String id;

    private String vendorId;
    private String vendorCnName;
    private String vendorEnName;
    private String skus;//产品编码
    private Integer currency;
    private String terapeakResearch;//TP报告
    private String productReport;//分析报告
    @JsonMoney
    private BigDecimal rateAudToRmb;
    @JsonMoney
    private BigDecimal rateAudToUsd;
    private Integer sampleFeeRefund;
    private String sampleReceiver;
    private Date startTime;
    private Date endTime;
    /**采购员信息*/
    private String buyerId;
    private String buyerCnName;
    private String buyerEnName;
    private String creatorId;
    private String creatorCnName;
    private String creatorEnName;
    private String departmentId;
    private String departmentCnName;
    private String departmentEnName;
    private Date createdAt;
    private Date updatedAt;
    private Integer status;
    private Integer flowStatus;
    private String processInstanceId;
    private String businessId;
    private String reviewerId;
    private String reviewerCnName;
    private String reviewerEnName;
    private String reviewerDepartmentId;
    private String reviewerDepartmentCnName;
    private String reviewerDepartmentEnName;
    /**样品总金额*/
    private BigDecimal totalSampleFeeAud;
    private BigDecimal totalSampleFeeRmb;
    private BigDecimal totalSampleFeeUsd;
    private BigDecimal totalOtherAud;
    private BigDecimal totalOtherRmb;
    private BigDecimal totalOtherUsd;
    /**挂起状态*/
    private Integer hold;
    private String remark;
    private List<SampleDetailVo> details = Lists.newArrayList();
    private List<SampleOtherDetailVo> otherDetails = Lists.newArrayList();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendorCnName() {
        return vendorCnName;
    }

    public void setVendorCnName(String vendorCnName) {
        this.vendorCnName = vendorCnName;
    }

    public String getVendorEnName() {
        return vendorEnName;
    }

    public void setVendorEnName(String vendorEnName) {
        this.vendorEnName = vendorEnName;
    }

    public String getSkus() {
        return skus;
    }

    public void setSkus(String skus) {
        this.skus = skus;
    }

    public Integer getCurrency() {
        return currency;
    }

    public void setCurrency(Integer currency) {
        this.currency = currency;
    }

    public String getTerapeakResearch() {
        return terapeakResearch;
    }

    public void setTerapeakResearch(String terapeakResearch) {
        this.terapeakResearch = terapeakResearch;
    }

    public String getProductReport() {
        return productReport;
    }

    public void setProductReport(String productReport) {
        this.productReport = productReport;
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

    public Integer getSampleFeeRefund() {
        return sampleFeeRefund;
    }

    public void setSampleFeeRefund(Integer sampleFeeRefund) {
        this.sampleFeeRefund = sampleFeeRefund;
    }

    public String getSampleReceiver() {
        return sampleReceiver;
    }

    public void setSampleReceiver(String sampleReceiver) {
        this.sampleReceiver = sampleReceiver;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getBuyerCnName() {
        return buyerCnName;
    }

    public void setBuyerCnName(String buyerCnName) {
        this.buyerCnName = buyerCnName;
    }

    public String getBuyerEnName() {
        return buyerEnName;
    }

    public void setBuyerEnName(String buyerEnName) {
        this.buyerEnName = buyerEnName;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getFlowStatus() {
        return flowStatus;
    }

    public void setFlowStatus(Integer flowStatus) {
        this.flowStatus = flowStatus;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(String reviewerId) {
        this.reviewerId = reviewerId;
    }

    public String getReviewerCnName() {
        return reviewerCnName;
    }

    public void setReviewerCnName(String reviewerCnName) {
        this.reviewerCnName = reviewerCnName;
    }

    public String getReviewerEnName() {
        return reviewerEnName;
    }

    public void setReviewerEnName(String reviewerEnName) {
        this.reviewerEnName = reviewerEnName;
    }

    public String getReviewerDepartmentId() {
        return reviewerDepartmentId;
    }

    public void setReviewerDepartmentId(String reviewerDepartmentId) {
        this.reviewerDepartmentId = reviewerDepartmentId;
    }

    public String getReviewerDepartmentCnName() {
        return reviewerDepartmentCnName;
    }

    public void setReviewerDepartmentCnName(String reviewerDepartmentCnName) {
        this.reviewerDepartmentCnName = reviewerDepartmentCnName;
    }

    public String getReviewerDepartmentEnName() {
        return reviewerDepartmentEnName;
    }

    public void setReviewerDepartmentEnName(String reviewerDepartmentEnName) {
        this.reviewerDepartmentEnName = reviewerDepartmentEnName;
    }

    public Integer getHold() {
        return hold;
    }

    public void setHold(Integer hold) {
        this.hold = hold;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<SampleDetailVo> getDetails() {
        return details;
    }

    public void setDetails(List<SampleDetailVo> details) {
        this.details = details;
    }

    public List<SampleOtherDetailVo> getOtherDetails() {
        return otherDetails;
    }

    public void setOtherDetails(List<SampleOtherDetailVo> otherDetails) {
        this.otherDetails = otherDetails;
    }

    public BigDecimal getTotalSampleFeeAud() {
        return totalSampleFeeAud;
    }

    public void setTotalSampleFeeAud(BigDecimal totalSampleFeeAud) {
        this.totalSampleFeeAud = totalSampleFeeAud;
    }

    public BigDecimal getTotalSampleFeeRmb() {
        return totalSampleFeeRmb;
    }

    public void setTotalSampleFeeRmb(BigDecimal totalSampleFeeRmb) {
        this.totalSampleFeeRmb = totalSampleFeeRmb;
    }

    public BigDecimal getTotalSampleFeeUsd() {
        return totalSampleFeeUsd;
    }

    public void setTotalSampleFeeUsd(BigDecimal totalSampleFeeUsd) {
        this.totalSampleFeeUsd = totalSampleFeeUsd;
    }

    public BigDecimal getTotalOtherAud() {
        return totalOtherAud;
    }

    public void setTotalOtherAud(BigDecimal totalOtherAud) {
        this.totalOtherAud = totalOtherAud;
    }

    public BigDecimal getTotalOtherRmb() {
        return totalOtherRmb;
    }

    public void setTotalOtherRmb(BigDecimal totalOtherRmb) {
        this.totalOtherRmb = totalOtherRmb;
    }

    public BigDecimal getTotalOtherUsd() {
        return totalOtherUsd;
    }

    public void setTotalOtherUsd(BigDecimal totalOtherUsd) {
        this.totalOtherUsd = totalOtherUsd;
    }
}
