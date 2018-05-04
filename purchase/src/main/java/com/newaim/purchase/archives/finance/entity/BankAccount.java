package com.newaim.purchase.archives.finance.entity;

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
@Table(name = "na_bank_account")
public class BankAccount implements Serializable{

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.newaim.core.jpa.IdGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name = "prefix", value = "BKAT")})
    private String id;

    private String vendorId;
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
    private String guaranteeLetter;//保函文件
    private String contractGuaranteeLetter;//合同担保函

    private Integer status;
    private String creatorId;
    private String creatorCnName;
    private String creatorEnName;
    private Date createdAt;
    private Date updatedAt;
    private String departmentId;
    private String departmentCnName;
    private String departmentEnName;

    private String businessId;
    private String processInstanceId;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    @Transient
    public String getVendorName() {
        if(SessionUtils.isCnLang()){
            return  this.getVendorCnName();
        }else if(SessionUtils.isEnLang()){
            return this.getVendorEnName();
        }
        return null;
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
}
