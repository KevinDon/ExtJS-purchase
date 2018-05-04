package com.newaim.purchase.archives.vendor.vo;

import com.google.common.collect.Lists;
import com.newaim.purchase.admin.system.vo.AttachmentVo;
import com.newaim.purchase.archives.finance.vo.BankAccountVo;
import com.newaim.purchase.archives.flow.purchase.vo.PurchaseContractVo;
import com.newaim.purchase.archives.product.vo.ProductForVendorVo;
import com.newaim.purchase.desktop.email.vo.ContactsVo;
import com.newaim.purchase.desktop.reports.vo.ReportsVo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class VendorVo implements Serializable{

    private String id;

    private String code;
    private String name;
    private String cnName;
    private String enName;

    private String categoryId;

    private String categoryName;
    private String categoryCnName;
    private String categoryEnName;
    //采購員名稱
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

    private Integer orderSerialNumber;

    private String paymentProvision;

    private String dynContent;

    private String dynFields1;

    private String dynFields2;

    private Integer status;

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

    private String sellerPhone;
    private String sellerFax;
    private String sellerEmail;

    private Integer type;
    private BankAccountVo bank;

    private Integer depositType;
    private BigDecimal depositRate;

    private Integer balancePaymentTerm;
    private Integer balanceCreditTerm;
    private String tradeTerm;
    
    private List<VendorProductCategoryUnionVo> productCategory;
    private List<ProductForVendorVo> products;
    private List<AttachmentVo> attachments = Lists.newArrayList();
    //图片原型
    private List<AttachmentVo> imagesDoc = Lists.newArrayList();
    //图片ID
    private String images;
    //联系人
    private List<ContactsVo> contacts = Lists.newArrayList();

    private List<ReportsVo> reports = Lists.newArrayList();

    private List<PurchaseContractVo> orders = Lists.newArrayList();

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

    public String getCategoryId() {
        return categoryId;
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

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
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

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
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

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
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

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

	public BankAccountVo getBank() {
		return bank;
	}

	public void setBank(BankAccountVo bank) {
		this.bank = bank;
	}

	public List<VendorProductCategoryUnionVo> getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(List<VendorProductCategoryUnionVo> productCategory) {
		this.productCategory = productCategory;
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

	public List<AttachmentVo> getImagesDoc() {
		return imagesDoc;
	}

	public void setImagesDoc(List<AttachmentVo> imagesDoc) {
		this.imagesDoc = imagesDoc;
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
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

    public List<ReportsVo> getReports() {
        return reports;
    }

    public void setReports(List<ReportsVo> reports) {
        this.reports = reports;
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

    public List<PurchaseContractVo> getOrders() {
        return orders;
    }

    public void setOrders(List<PurchaseContractVo> orders) {
        this.orders = orders;
    }
}
