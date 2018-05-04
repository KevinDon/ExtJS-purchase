package com.newaim.purchase.flow.finance.vo;

import com.google.common.collect.Lists;
import com.newaim.purchase.admin.account.vo.UserVo;
import com.newaim.purchase.admin.system.vo.AttachmentVo;
import com.newaim.purchase.archives.flow.purchase.vo.CustomClearancePackingDetailVo;
import com.newaim.purchase.archives.flow.purchase.vo.PurchaseContractDetailVo;
import com.newaim.purchase.archives.vendor.vo.VendorVo;
import com.newaim.purchase.config.json.JsonMoney;

import javax.persistence.Transient;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class FlowFeeRegisterVo implements Serializable{

    private String id;
    private String title;

    /**订单ID*/
    private String orderId;
    private String orderNumber;
    private String orderTitle;

    /**供应商信息*/
    private String vendorId;
    private String vendorName;
    private String vendorCnName;
    private String vendorEnName;
    private VendorVo vendor;

    /**开户银行*/
    private String beneficiaryBank;
    private String beneficiaryBankAddress;

    /**收款公司中、英文名*/
    private String companyCnName;
    private String companyEnName;
    private String companyCnAddress;
    private String companyEnAddress;

    private String guaranteeLetter;//保函文件
    private String guaranteeLetterName;//保函文件文件
    private String contractGuaranteeLetter;//合同担保函
    private String contractGuaranteeLetterName;//合同担保函文件

    /**银行代号*/
    private String swiftCode;
    private String cnaps;

    /**银行账户*/
    private String bankAccount;
    /**费用类型*/
    private Integer feeType;
    /**结算货币*/
    private Integer currency;
    /**总金额、汇率*/
    private BigDecimal totalPriceAud;
    private BigDecimal totalPriceRmb;
    private BigDecimal totalPriceUsd;
    @JsonMoney
    private BigDecimal rateAudToRmb;
    @JsonMoney
    private BigDecimal rateAudToUsd;
    private BigDecimal depositAud;
    private BigDecimal depositRmb;
    private BigDecimal depositUsd;

    private Date dueDate;
    private Integer paymentStatus;
    private String remark;

    /**创建、更新、申请、完成时间*/
    private Date createdAt;
    private Date updatedAt;
    private Date startTime;
    private Date endTime;

    /**创建人、创建部门信息*/
    private String creatorId;
    private String creatorCnName;
    private String creatorEnName;
    private String departmentId;
    private String departmentCnName;
    private String departmentEnName;

    /**流程实例ID*/
    private String processInstanceId;
    private int approved;

    /**状态*/
    private Integer flowStatus;
    private Integer status;

    /**确认人、确认部门信息*/
    private String reviewerId;
    private String reviewerCnName;
    private String reviewerEnName;
    private String reviewerDepartmentId;
    private String reviewerDepartmentCnName;
    private String reviewerDepartmentEnName;

    /**当前处理人*/
    private String assigneeId;
    private String assigneeCnName;
    private String assigneeEnName;
    /**挂起状态*/
    private Integer hold;

    private List<FlowFeeRegisterDetailVo> details = Lists.newArrayList();
    private List<FlowFeeRegisterDetailVo> otherDetails = Lists.newArrayList();
    private List<AttachmentVo> attachments = Lists.newArrayList();
    private List<PurchaseContractDetailVo> contractDetails = Lists.newArrayList();
    private List<CustomClearancePackingDetailVo> customClearancePackingDetailVos = Lists.newArrayList();

    private List<UserVo> flowNextHandler = Lists.newArrayList();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
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

    public VendorVo getVendor() {
        return vendor;
    }

    public void setVendor(VendorVo vendor) {
        this.vendor = vendor;
    }

    public String getBeneficiaryBank() {
        return beneficiaryBank;
    }

    public void setBeneficiaryBank(String beneficiaryBank) {
        this.beneficiaryBank = beneficiaryBank;
    }

    public String getBeneficiaryBankAddress() {
        return beneficiaryBankAddress;
    }

    public void setBeneficiaryBankAddress(String beneficiaryBankAddress) {
        this.beneficiaryBankAddress = beneficiaryBankAddress;
    }

    public String getCompanyCnName() {
        return companyCnName;
    }

    public void setCompanyCnName(String companyCnName) {
        this.companyCnName = companyCnName;
    }

    public String getCompanyEnName() {
        return companyEnName;
    }

    public void setCompanyEnName(String companyEnName) {
        this.companyEnName = companyEnName;
    }

    public String getCompanyCnAddress() {
        return companyCnAddress;
    }

    public void setCompanyCnAddress(String companyCnAddress) {
        this.companyCnAddress = companyCnAddress;
    }

    public String getCompanyEnAddress() {
        return companyEnAddress;
    }

    public void setCompanyEnAddress(String companyEnAddress) {
        this.companyEnAddress = companyEnAddress;
    }

    public String getGuaranteeLetter() {
        return guaranteeLetter;
    }

    public void setGuaranteeLetter(String guaranteeLetter) {
        this.guaranteeLetter = guaranteeLetter;
    }

    public String getGuaranteeLetterName() {
        return guaranteeLetterName;
    }

    public void setGuaranteeLetterName(String guaranteeLetterName) {
        this.guaranteeLetterName = guaranteeLetterName;
    }

    public String getContractGuaranteeLetter() {
        return contractGuaranteeLetter;
    }

    public void setContractGuaranteeLetter(String contractGuaranteeLetter) {
        this.contractGuaranteeLetter = contractGuaranteeLetter;
    }

    public String getContractGuaranteeLetterName() {
        return contractGuaranteeLetterName;
    }

    public void setContractGuaranteeLetterName(String contractGuaranteeLetterName) {
        this.contractGuaranteeLetterName = contractGuaranteeLetterName;
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

    public Integer getFeeType() {
        return feeType;
    }

    public void setFeeType(Integer feeType) {
        this.feeType = feeType;
    }

    public Integer getCurrency() {
        return currency;
    }

    public void setCurrency(Integer currency) {
        this.currency = currency;
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

    public BigDecimal getDepositAud() {
        return depositAud;
    }

    public void setDepositAud(BigDecimal depositAud) {
        this.depositAud = depositAud;
    }

    public BigDecimal getDepositRmb() {
        return depositRmb;
    }

    public void setDepositRmb(BigDecimal depositRmb) {
        this.depositRmb = depositRmb;
    }

    public BigDecimal getDepositUsd() {
        return depositUsd;
    }

    public void setDepositUsd(BigDecimal depositUsd) {
        this.depositUsd = depositUsd;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Integer getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(Integer paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public int getApproved() {
        return approved;
    }

    public void setApproved(int approved) {
        this.approved = approved;
    }

    public Integer getFlowStatus() {
        return flowStatus;
    }

    public void setFlowStatus(Integer flowStatus) {
        this.flowStatus = flowStatus;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public String getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(String assigneeId) {
        this.assigneeId = assigneeId;
    }

    public String getAssigneeCnName() {
        return assigneeCnName;
    }

    public void setAssigneeCnName(String assigneeCnName) {
        this.assigneeCnName = assigneeCnName;
    }

    public String getAssigneeEnName() {
        return assigneeEnName;
    }

    public void setAssigneeEnName(String assigneeEnName) {
        this.assigneeEnName = assigneeEnName;
    }

    public Integer getHold() {
        return hold;
    }

    public void setHold(Integer hold) {
        this.hold = hold;
    }

    public List<FlowFeeRegisterDetailVo> getDetails() {
        return details;
    }

    public void setDetails(List<FlowFeeRegisterDetailVo> details) {
        this.details = details;
    }

    public List<FlowFeeRegisterDetailVo> getOtherDetails() {
        return otherDetails;
    }

    public void setOtherDetails(List<FlowFeeRegisterDetailVo> otherDetails) {
        this.otherDetails = otherDetails;
    }

    public List<AttachmentVo> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentVo> attachments) {
        this.attachments = attachments;
    }

    public List<PurchaseContractDetailVo> getContractDetails() {
        return contractDetails;
    }

    public void setContractDetails(List<PurchaseContractDetailVo> contractDetails) {
        this.contractDetails = contractDetails;
    }

    public List<UserVo> getFlowNextHandler() {
        return flowNextHandler;
    }

    public void setFlowNextHandler(List<UserVo> flowNextHandler) {
        this.flowNextHandler = flowNextHandler;
    }

    public List<CustomClearancePackingDetailVo> getCustomClearancePackingDetailVos() {
        return customClearancePackingDetailVos;
    }

    public void setCustomClearancePackingDetailVos(List<CustomClearancePackingDetailVo> customClearancePackingDetailVos) {
        this.customClearancePackingDetailVos = customClearancePackingDetailVos;
    }
}
