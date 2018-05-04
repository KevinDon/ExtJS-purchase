package com.newaim.purchase.archives.vendor.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.archives.vendor.entity.VendorProductCategoryUnion;

@Repository
public interface VendorProductCategoryUnionDao extends BaseDao<VendorProductCategoryUnion, String> {

	VendorProductCategoryUnion findUserRoleUnionById(String id);
	
	List<VendorProductCategoryUnion> findVendorProductCategoryUnionByVendorId(String vendorId);
	
	List<VendorProductCategoryUnion> findVendorProductCategoryUnionByProductCategoryId(String productCategoryId);

	void deleteVendorProductCategoryUnionByVendorId(String vendorId);
	
	void deleteVendorProductCategoryUnionByProductCategoryId(String productCategoryId);

}
