package com.newaim.purchase.archives.vendor.vo;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;
import com.newaim.purchase.archives.vendor.entity.VendorProductCategoryUnion;

public class VendorProductCategoryUnionsVo implements Serializable{

	private List<VendorProductCategoryUnion> productCategory = Lists.newArrayList();

	public List<VendorProductCategoryUnion> getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(List<VendorProductCategoryUnion> productCategory) {
		this.productCategory = productCategory;
	}

	
}
