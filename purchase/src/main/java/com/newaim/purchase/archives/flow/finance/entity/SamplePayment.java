package com.newaim.purchase.archives.flow.finance.entity;

import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.flow.workflow.entity.BusinessObject;
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
@Table(name = "na_sample_payment")
public class SamplePayment implements BusinessObject, Serializable {

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.newaim.core.jpa.IdGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name = "prefix", value = "SP")})
    private String id;

    private String vendorId;
    private String vendorCnName;//供应商名称
    private String vendorEnName;

    private String beneficiaryBank;//收款银行
    private String swiftCode;//银行代号
    private String cnaps;
    private String bankAccount;//银行账户
    private String beneficiary;//收款人
    private Integer currency;//结算货币
    private BigDecimal totalSampleFeeAud;//样品总金额
    private BigDecimal totalSampleFeeRmb;
    private BigDecimal totalSampleFeeUsd;
    private BigDecimal rateAudToRmb;
    private BigDecimal rateAudToUsd;
    private Integer paymentType;//费用类型

    private Date startTime;//申请时间
    private Date endTime;//完成时间
    private Date createdAt;//创建时间
    private Date updatedAt;//更新时间

    private String creatorId;//申请人ID
    private String creatorCnName;
    private String creatorEnName;

    private String departmentId;//部门ID
    private String departmentCnName;
    private String departmentEnName;

    private Integer status;
    private Integer flowStatus;//流程状态
    private String processInstanceId;//流程实例ID
    private String businessId;

    /**确认人、部门信息*/
    private String reviewerId;
    private String reviewerCnName;
    private String reviewerEnName;
    private String reviewerDepartmentId;
    private String reviewerDepartmentCnName;
    private String reviewerDepartmentEnName;

    /**挂起状态*/
    private Integer hold;

    private String sampleId;
    private String sampleBusinessId;
    private BigDecimal paymentTotalSampleFeeAud;
    private BigDecimal paymentTotalSampleFeeRmb;
    private BigDecimal paymentTotalSampleFeeUsd;
    private BigDecimal paymentRateAudToRmb;
    private BigDecimal paymentRateAudToUsd;
    /**
     * 审核
     */
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

    public String getBeneficiaryBank() {
        return beneficiaryBank;
    }

    public void setBeneficiaryBank(String beneficiaryBank) {
        this.beneficiaryBank = beneficiaryBank;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

    public String getCnaps() {
        return cnaps;
    }

    public void setCnaps(String cnaps) {
        this.cnaps = cnaps;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(String beneficiary) {
        this.beneficiary = beneficiary;
    }

    public Integer getCurrency() {
        return currency;
    }

    public void setCurrency(Integer currency) {
        this.currency = currency;
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

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
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

    @Override
    public Integer getHold() {
        return hold;
    }

    @Override
    public void setHold(Integer hold) {
        this.hold = hold;
    }

    public String getSampleId() {
        return sampleId;
    }

    public void setSampleId(String sampleId) {
        this.sampleId = sampleId;
    }

    public String getSampleBusinessId() {
        return sampleBusinessId;
    }

    public void setSampleBusinessId(String sampleBusinessId) {
        this.sampleBusinessId = sampleBusinessId;
    }

    public BigDecimal getPaymentTotalSampleFeeAud() {
        return paymentTotalSampleFeeAud;
    }

    public void setPaymentTotalSampleFeeAud(BigDecimal paymentTotalSampleFeeAud) {
        this.paymentTotalSampleFeeAud = paymentTotalSampleFeeAud;
    }

    public BigDecimal getPaymentTotalSampleFeeRmb() {
        return paymentTotalSampleFeeRmb;
    }

    public void setPaymentTotalSampleFeeRmb(BigDecimal paymentTotalSampleFeeRmb) {
        this.paymentTotalSampleFeeRmb = paymentTotalSampleFeeRmb;
    }

    public BigDecimal getPaymentTotalSampleFeeUsd() {
        return paymentTotalSampleFeeUsd;
    }

    public void setPaymentTotalSampleFeeUsd(BigDecimal paymentTotalSampleFeeUsd) {
        this.paymentTotalSampleFeeUsd = paymentTotalSampleFeeUsd;
    }

    public BigDecimal getPaymentRateAudToRmb() {
        return paymentRateAudToRmb;
    }

    public void setPaymentRateAudToRmb(BigDecimal paymentRateAudToRmb) {
        this.paymentRateAudToRmb = paymentRateAudToRmb;
    }

    public BigDecimal getPaymentRateAudToUsd() {
        return paymentRateAudToUsd;
    }

    public void setPaymentRateAudToUsd(BigDecimal paymentRateAudToUsd) {
        this.paymentRateAudToUsd = paymentRateAudToUsd;
    }

    @Transient
    public String getVendorName() {
        if(SessionUtils.isCnLang()){
            return  this.vendorCnName;
        }else if(SessionUtils.isEnLang()){
            return this.vendorEnName;
        }
        return null;
    }

    @Transient
    public String getCreatorName() {
        if(SessionUtils.isCnLang()){
            return  this.getCreatorCnName();
        }else if(SessionUtils.isEnLang()){
            return this.getCreatorEnName();
        }
        return null;
    }

    @Transient
    public String getDepartmentName() {
        if(SessionUtils.isCnLang()){
            return  this.getDepartmentCnName();
        }else if(SessionUtils.isEnLang()){
            return this.getDepartmentEnName();
        }
        return null;
    }

}
