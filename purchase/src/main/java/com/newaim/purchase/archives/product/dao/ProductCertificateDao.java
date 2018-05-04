package com.newaim.purchase.archives.product.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.archives.product.entity.ProductCertificate;

@Repository
public interface ProductCertificateDao extends BaseDao<ProductCertificate, String>,ProductCertificateDaoCustom {

    List<ProductCertificate> findByBusinessIdAndStatus(String businessId, Integer status);

	@Query("select t from ProductCertificate t where t.businessId = :businessId and t.status !=3")
	List<ProductCertificate> findByBusinessId(@Param("businessId") String businessId);
}
