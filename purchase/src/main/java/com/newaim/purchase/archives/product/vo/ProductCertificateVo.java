package com.newaim.purchase.archives.product.vo;

import com.google.common.collect.Lists;
import com.newaim.purchase.admin.system.vo.AttachmentVo;
import com.newaim.purchase.archives.vendor.vo.VendorVo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ProductCertificateVo implements Serializable{

    private String id;

    private String sku;

    private VendorVo vendor = null;
    private String vendorId;
    private String vendorName;
    private String vendorCnName;
    private String vendorEnName;

    private String relevantStandard;//相关标准

    private String description;//描述

    private String certificateNumber;

    private Date effectiveDate;

    private Date validUntil;

    private String fileId;
    private String certificateFile;//证书文件

    private Integer status;//状态

    private String creatorId;//创建人ID
    private String creatorCnName;
    private String creatorEnName;

    private Date createdAt;//创建时间
    private Date updatedAt;//更新时间

    private String departmentId;//部门ID
    private String departmentCnName;
    private String departmentEnName;
    private String businessId;

    //报告文件
    private AttachmentVo certificateAttach;

    private List<ProductCertificateUnionVo> details = Lists.newArrayList();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public VendorVo getVendor() {
        return vendor;
    }

    public void setVendor(VendorVo vendor) {
        this.vendor = vendor;
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
    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getRelevantStandard() {
        return relevantStandard;
    }

    public void setRelevantStandard(String relevantStandard) {
        this.relevantStandard = relevantStandard;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
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

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getCertificateFile() {
        return certificateFile;
    }

    public void setCertificateFile(String certificateFile) {
        this.certificateFile = certificateFile;
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

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public AttachmentVo getCertificateAttach() {
        return certificateAttach;
    }

    public void setCertificateAttach(AttachmentVo certificateAttach) {
        this.certificateAttach = certificateAttach;
    }

    public List<ProductCertificateUnionVo> getDetails() {
        return details;
    }

    public void setDetails(List<ProductCertificateUnionVo> details) {
        this.details = details;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }
}
