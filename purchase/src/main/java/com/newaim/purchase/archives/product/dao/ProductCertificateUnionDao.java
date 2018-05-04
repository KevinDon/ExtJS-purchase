package com.newaim.purchase.archives.product.dao;

import com.newaim.purchase.archives.product.entity.ProductCertificateUnion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface ProductCertificateUnionDao extends JpaRepository<ProductCertificateUnion, String>, JpaSpecificationExecutor<ProductCertificateUnion> {

    void deleteByCertificateId(String certificateId);

    List<ProductCertificateUnion> findByCertificateId(String certificateId);

    List<ProductCertificateUnion> findByProductId(String productIds);

    List<ProductCertificateUnion> findProductCertificateUnionsByProductIdIn(String[] productIds);
}
