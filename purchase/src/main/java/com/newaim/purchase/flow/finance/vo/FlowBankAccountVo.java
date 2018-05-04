package com.newaim.purchase.flow.finance.vo;

import com.google.common.collect.Lists;
import com.newaim.purchase.admin.account.vo.UserVo;
import com.newaim.purchase.admin.system.vo.AttachmentVo;
import com.newaim.purchase.archives.vendor.vo.VendorVo;
import com.newaim.purchase.config.json.JsonMoney;

import java.beans.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class FlowBankAccountVo implements Serializable{
    private String id;

    private String vendorId;
    private String vendorName;
    private String vendorCnName;
    private String vendorEnName;
    private Integer type;
    private String beneficiaryCnName;
    private String beneficiaryEnName;
    private String companyCnName;
    private String companyEnName;
    private String companyCnAddress;
    private String companyEnAddress;

    private String beneficiaryBank;

    private String beneficiaryBankAddress;

    private String swiftCode;

    private String cnaps;

    private String bankAccount;

    private Integer currency;
    private Integer depositType;
    private BigDecimal depositRate;

    private String guaranteeLetter;
    private String contractGuaranteeLetter;

    private Integer status;

    private String creatorId;
    private String creatorName;
    private String creatorCnName;
    private String creatorEnName;

    private Date createdAt;
    private Date updatedAt;

    private Integer flowStatus;
    private String processInstanceId;
    private Date startTime;
    private Date endTime;

    private String departmentId;
    private String departmentName;
    private String departmentCnName;
    private String departmentEnName;

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

    private VendorVo vendor;
    private AttachmentVo guaranteeLetterFile;//保函文件
    private AttachmentVo contractGuaranteeLetterFile;//合同履约担保函
    /**审批*/
    private int approved;
    /**挂起状态*/
    private Integer hold;

    private List<UserVo> flowNextHandler = Lists.newArrayList();

    public String getServiceName(){
        return this.getVendorName();
    }

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

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVendorCnName() {
        return vendorCnName;
    }

    public void setVendorCnName(String vendorCnName){
        this.vendorCnName = vendorCnName;
    }

    public String getVendorEnName(){
        return vendorEnName;
    }

    public void setVendorEnName(String vendorEnName){
        this.vendorEnName = vendorEnName;
    }

    public String getBeneficiaryCnName() {
        return beneficiaryCnName;
    }

    public void setBeneficiaryCnName(String beneficiaryCnName) {
        this.beneficiaryCnName = beneficiaryCnName;
    }

    public String getBeneficiaryEnName() {
        return beneficiaryEnName;
    }

    public void setBeneficiaryEnName(String beneficiaryEnName) {
        this.beneficiaryEnName = beneficiaryEnName;
    }

    public String getCompanyCnName() { return companyCnName; }

    public void setCompanyCnName(String companyCnName){ this.companyCnName = companyCnName; }

    public String getCompanyEnName() { return companyEnName; }

    public void setCompanyEnName(String companyEnName){ this.companyEnName = companyEnName; }

    public String getCompanyCnAddress(){
        return companyCnAddress;
    }

    public void setCompanyCnAddress(String companyCnAddress){
        this.companyCnAddress = companyCnAddress;
    }

    public String getCompanyEnAddress(){
        return companyEnAddress;
    }

    public void setCompanyEnAddress(String companyEnAddress){
        this.companyEnAddress = companyEnAddress;
    }

    public String getBankAccount(){
        return bankAccount;
    }

    public void setBankAccount(String bankAccount){
        this.bankAccount = bankAccount;
    }

    public String getBeneficiaryBank(){
        return beneficiaryBank;
    }

    public void setBeneficiaryBank(String beneficiaryBank){
        this.beneficiaryBank = beneficiaryBank;
    }

    public String getSwiftCode(){
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode){
        this.swiftCode = swiftCode;
    }

    public  String getCnaps() { return cnaps; }

    public  void setCnaps(String cnaps) { this.cnaps = cnaps; }

    public String getBeneficiaryBankAddress(){
        return beneficiaryBankAddress;
    }

    public void setBeneficiaryBankAddress(String beneficiaryBankAddress){
        this.beneficiaryBankAddress = beneficiaryBankAddress;
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

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreatorCnName() { return creatorCnName; }

    public void setCreatorCnName(String creatorCnName){ this.creatorCnName = creatorCnName; }

    public String getCreatorEnName() { return creatorEnName; }

    public void setCreatorEnName(String creatorEnName) { this.creatorEnName = creatorEnName; }

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

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
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

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public Integer getCurrency() {
        return currency;
    }

    public void setCurrency(Integer currency) {
        this.currency = currency;
    }

    public Integer getDepositType() {
        return depositType;
    }

    public void setDepositType(Integer depositType) {
        this.depositType = depositType;
    }

    public BigDecimal getDepositRate() {
        return depositRate;
    }

    public void setDepositRate(BigDecimal depositRate) {
        this.depositRate = depositRate;
    }

    public AttachmentVo getGuaranteeLetterFile() {
        return guaranteeLetterFile;
    }

    public void setGuaranteeLetterFile(AttachmentVo guaranteeLetterFile) {
        this.guaranteeLetterFile = guaranteeLetterFile;
    }

    public AttachmentVo getContractGuaranteeLetterFile() {
        return contractGuaranteeLetterFile;
    }

    public void setContractGuaranteeLetterFile(AttachmentVo contractGuaranteeLetterFile) {
        this.contractGuaranteeLetterFile = contractGuaranteeLetterFile;
    }

    public int getApproved() {
        return approved;
    }

    public void setApproved(int approved) {
        this.approved = approved;
    }

    public VendorVo getVendor() {
        return vendor;
    }

    public void setVendor(VendorVo vendor) {
        this.vendor = vendor;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getGuaranteeLetter() {
        return guaranteeLetter;
    }

    public void setGuaranteeLetter(String guaranteeLetter) {
        this.guaranteeLetter = guaranteeLetter;
    }

    public String getContractGuaranteeLetter() {
        return contractGuaranteeLetter;
    }

    public void setContractGuaranteeLetter(String contractGuaranteeLetter) {
        this.contractGuaranteeLetter = contractGuaranteeLetter;
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

    public List<UserVo> getFlowNextHandler() {
        return flowNextHandler;
    }

    public void setFlowNextHandler(List<UserVo> flowNextHandler) {
        this.flowNextHandler = flowNextHandler;
    }

    public Integer getHold() {
        return hold;
    }

    public void setHold(Integer hold) {
        this.hold = hold;
    }
}
