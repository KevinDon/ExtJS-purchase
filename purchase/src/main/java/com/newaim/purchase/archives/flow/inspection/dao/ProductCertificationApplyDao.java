package com.newaim.purchase.archives.flow.inspection.dao;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.archives.flow.inspection.entity.ProductCertificationApply;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCertificationApplyDao extends BaseDao<ProductCertificationApply, String> {

    /**
     * 挂起指定产品的产品认证
     * @param productIds
     */
    @Modifying
    @Query("UPDATE ProductCertificationApply set hold = 1 where productId in ?1")
    void suspendByProductIds(List<String> productIds);
}
