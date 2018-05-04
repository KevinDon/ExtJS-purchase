package com.newaim.purchase.archives.product.dao.impl;

import com.google.common.collect.Maps;
import com.newaim.core.jpa.BaseDaoCustomImpl;
import com.newaim.purchase.archives.product.dao.ProductCertificateDaoCustom;
import com.newaim.purchase.archives.product.dao.ProductDaoCustom;
import com.newaim.purchase.archives.product.entity.Product;
import com.newaim.purchase.archives.product.entity.ProductCertificate;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.LinkedHashMap;

public class ProductCertificateDaoImpl extends BaseDaoCustomImpl implements ProductCertificateDaoCustom {

    @Override
    public List<ProductCertificate> findByProductIds( List<String> productIds) {
        StringBuilder hql = new StringBuilder("select t from ProductCertificate t where 1= 1");
        hql.append(" and exists (select 1 from ProductCertificateUnion u where u.certificateId = t.id and u.productId in :productIds)");
        LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
        params.put("productIds", productIds);
        return list(hql.toString(), params);
    }
}
