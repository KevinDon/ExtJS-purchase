package com.newaim.purchase.archives.service_provider.vo;

import com.google.common.collect.Lists;
import com.newaim.purchase.admin.system.vo.AttachmentVo;
import com.newaim.purchase.archives.finance.vo.BankAccountVo;
import com.newaim.purchase.archives.product.vo.ProductForVendorVo;
import com.newaim.purchase.desktop.email.vo.ContactsVo;
import com.newaim.purchase.desktop.reports.vo.ReportsVo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


public class ServiceProviderVo implements Serializable{

    private String id;
    private String code;
    private String name;
    private String cnName;
    private String enName;

    private Integer type;

    private String categoryId;
    private String categoryName;
    private String categoryCnName;
    private String categoryEnName;

    private String buyerId;
    private String buyerName;
    private String buyerCnName;
    private String buyerEnName;

    private String director;

    private String address;

    private String postcode;

    private String abn;

    private String website;

    private String rating;

    private String photos;

    private String files;

    private Integer source;

    private Integer currency;

    private String paymentProvision;

    private String dynContent;

    private String dynFields1;

    private String dynFields2;

    private Integer status;

    private BankAccountVo bank;

    private String creatorId;
    private String creatorName;
    private String creatorCnName;
    private String creatorEnName;

    private Date createdAt;
    private Date updatedAt;

    private String departmentId;
    private String departmentName;
    private String departmentCnName;
    private String departmentEnName;

    private List<ProductForVendorVo> products;
    /**附件*/
    private List<AttachmentVo> attachments = Lists.newArrayList();
    /**图片ID*/
    private String images;
    /**联系人*/
    private List<ContactsVo> contacts = Lists.newArrayList();
    /**图片原型*/
    private List<AttachmentVo> imagesDoc = Lists.newArrayList();
    /**港口*/
    private List<ServiceProviderPortVo> ports = Lists.newArrayList();
    /**收费项目*/
    private List<ServiceProviderChargeItemVo> chargeItems = Lists.newArrayList();
    //相关报告
    private List<ReportsVo> reports = Lists.newArrayList();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryCnName() {
        return categoryCnName;
    }

    public void setCategoryCnName(String categoryCnName) {
        this.categoryCnName = categoryCnName;
    }

    public String getCategoryEnName() {
        return categoryEnName;
    }

    public void setCategoryEnName(String categoryEnName) {
        this.categoryEnName = categoryEnName;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
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

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getAbn() {
        return abn;
    }

    public void setAbn(String abn) {
        this.abn = abn;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public String getFiles() {
        return files;
    }

    public void setFiles(String files) {
        this.files = files;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Integer getCurrency() {
        return currency;
    }

    public void setCurrency(Integer currency) {
        this.currency = currency;
    }

    public String getPaymentProvision() {
        return paymentProvision;
    }

    public void setPaymentProvision(String paymentProvision) {
        this.paymentProvision = paymentProvision;
    }

    public String getDynContent() {
        return dynContent;
    }

    public void setDynContent(String dynContent) {
        this.dynContent = dynContent;
    }

    public String getDynFields1() {
        return dynFields1;
    }

    public void setDynFields1(String dynFields1) {
        this.dynFields1 = dynFields1;
    }

    public String getDynFields2() {
        return dynFields2;
    }

    public void setDynFields2(String dynFields2) {
        this.dynFields2 = dynFields2;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public BankAccountVo getBank() {
        return bank;
    }

    public void setBank(BankAccountVo bank) {
        this.bank = bank;
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

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
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

    public List<ProductForVendorVo> getProducts() {
        return products;
    }

    public void setProducts(List<ProductForVendorVo> products) {
        this.products = products;
    }

    public List<AttachmentVo> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentVo> attachments) {
        this.attachments = attachments;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public List<ContactsVo> getContacts() {
        return contacts;
    }

    public void setContacts(List<ContactsVo> contacts) {
        this.contacts = contacts;
    }

    public List<AttachmentVo> getImagesDoc() {
        return imagesDoc;
    }

    public void setImagesDoc(List<AttachmentVo> imagesDoc) {
        this.imagesDoc = imagesDoc;
    }

    public List<ServiceProviderPortVo> getPorts() {
        return ports;
    }

    public void setPorts(List<ServiceProviderPortVo> ports) {
        this.ports = ports;
    }

    public List<ServiceProviderChargeItemVo> getChargeItems() {
        return chargeItems;
    }

    public void setChargeItems(List<ServiceProviderChargeItemVo> chargeItems) {
        this.chargeItems = chargeItems;
    }

    public List<ReportsVo> getReports() {
        return reports;
    }

    public void setReports(List<ReportsVo> reports) {
        this.reports = reports;
    }
}
