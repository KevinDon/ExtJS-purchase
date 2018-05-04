package com.newaim.purchase.archives.product.dao;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.archives.product.entity.ProductVendorProp;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductVendorPropDao extends BaseDao<ProductVendorProp, String> {

	/** 通过供应商id查找
	 * @param vendorId
	 * @return
	 */
	List<ProductVendorProp> findProductVendorPropByVendorId(String vendorId);

	/** 通过ID查找
	 * @param id
	 * @return
	 */
	ProductVendorProp findProductVendorPropById(String id);


	ProductVendorProp findProductVendorPropByProductId(String productId);

	@Modifying
	@Query("update ProductVendorProp set currency= :currency where vendorId = :vendorId")
	void batchUpdateCurrency(@Param("vendorId") String vendorId, @Param("currency") Integer currency);



}
