package com.newaim.purchase.archives.vendor.entity;

import com.newaim.core.utils.SessionUtils;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Mark on 2017/9/15.
 */
@Entity
@Table(name = "na_vendor")
public class Vendor implements Serializable {

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.newaim.core.jpa.IdGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name = "prefix", value = "VD")})
    private String id;

    private String code;

    private String cnName;
    private String enName;

    private String categoryId;
    private String categoryCnName;
    private String categoryEnName;

    private String buyerId;
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

    private Integer orderSerialNumber;

    private String paymentProvision;

    private String dynContent;

    private String dynFields1;

    private String dynFields2;

    private Integer status;

    private String creatorId;
    private String creatorCnName;
    private String creatorEnName;

    private Date createdAt;
    private Date updatedAt;//更新时间

    private String departmentId;
    private String departmentCnName;
    private String departmentEnName;
    private String sellerPhone;
    private String sellerFax;
    private String sellerEmail;

    private Integer type;

    private Integer balancePaymentTerm;
    private Integer balanceCreditTerm;
    private String tradeTerm;

    @Transient
    private String images;

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

    @Transient
    public String getName() {
        if(SessionUtils.isCnLang()){
            return  this.cnName;
        }else if(SessionUtils.isEnLang()){
            return this.enName;
        }
        return null;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
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

    @Transient
    public String getCategoryName() {
        if(SessionUtils.isCnLang()){
            return  this.categoryCnName;
        }else if(SessionUtils.isEnLang()){
            return this.categoryEnName;
        }
        return null;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
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

    @Transient
    public String getBuyerName() {
        if(SessionUtils.isCnLang()){
            return  this.buyerCnName;
        }else if(SessionUtils.isEnLang()){
            return this.buyerEnName;
        }
        return null;
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

    public Integer getOrderSerialNumber() {
        return orderSerialNumber;
    }

    public void setOrderSerialNumber(Integer orderSerialNumber) {
        this.orderSerialNumber = orderSerialNumber;
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

    @Transient
    public String getCreatorName() {
        if(SessionUtils.isCnLang()){
            return  this.creatorCnName;
        }else if(SessionUtils.isEnLang()){
            return this.creatorEnName;
        }
        return null;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}

    public String getSellerPhone() {
        return sellerPhone;
    }

    public void setSellerPhone(String sellerPhone) {
        this.sellerPhone = sellerPhone;
    }

    public String getSellerFax() {
        return sellerFax;
    }

    public void setSellerFax(String sellerFax) {
        this.sellerFax = sellerFax;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
    }

    public Integer getBalancePaymentTerm() {
        return balancePaymentTerm;
    }

    public void setBalancePaymentTerm(Integer balancePaymentTerm) {
        this.balancePaymentTerm = balancePaymentTerm;
    }

    public Integer getBalanceCreditTerm() {
        return balanceCreditTerm;
    }

    public void setBalanceCreditTerm(Integer balanceCreditTerm) {
        this.balanceCreditTerm = balanceCreditTerm;
    }

    public String getTradeTerm() {
        return tradeTerm;
    }

    public void setTradeTerm(String tradeTerm) {
        this.tradeTerm = tradeTerm;
    }
}
