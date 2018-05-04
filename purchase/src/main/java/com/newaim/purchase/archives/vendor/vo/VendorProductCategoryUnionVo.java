package com.newaim.purchase.archives.vendor.vo;

import com.newaim.purchase.archives.product.vo.ProductCategoryVo;

import java.io.Serializable;

public class VendorProductCategoryUnionVo implements Serializable {

    private String id;
    private String vendorId;
    private String productCategoryId;
    private String alias;
    private Integer orderIndex;
    private VendorVo vendor;
    private ProductCategoryVo productCategory;

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

	public String getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(String productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Integer getOrderIndex() {
		return orderIndex;
	}

	public void setOrderIndex(Integer orderIndex) {
		this.orderIndex = orderIndex;
	}

	public VendorVo getVendor() {
		return vendor;
	}

	public void setVendor(VendorVo vendor) {
		this.vendor = vendor;
	}

	public ProductCategoryVo getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(ProductCategoryVo productCategory) {
		this.productCategory = productCategory;
	}

}
