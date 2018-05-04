package com.newaim.purchase.flow.inspection.dao;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.flow.inspection.entity.FlowProductCertification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlowProductCertificationDao extends BaseDao<FlowProductCertification, String> {
    /**
     * 挂起指定产品的产品认证申请
     * @param productIds
     */
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE na_flow_product_certification t set hold = 1 where (t.flow_status is null or t.flow_status != 2) and EXISTS\n" +
            "(SELECT 1 from na_flow_product_certification_detail d where d.business_id = t.id and d.product_id in ?1)")
    void suspendByProductIds(List<String> productIds);
}
