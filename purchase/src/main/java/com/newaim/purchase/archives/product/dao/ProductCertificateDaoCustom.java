package com.newaim.purchase.archives.product.dao;

import com.newaim.purchase.archives.product.entity.ProductCertificate;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ProductCertificateDaoCustom {

    List<ProductCertificate> findByProductIds(List<String> productIds);
}
