package com.newaim.purchase.archives.product.dao;

import java.util.List;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.archives.product.entity.ProductForVendor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductForVendorDao extends BaseDao<ProductForVendor, String> {
	
	List<ProductForVendor> findProductForVendorByVendorId(String vendorId);

	ProductForVendor findProductForVendorById(String id);
	
}
