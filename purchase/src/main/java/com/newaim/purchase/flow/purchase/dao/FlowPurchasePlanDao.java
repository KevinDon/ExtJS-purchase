package com.newaim.purchase.flow.purchase.dao;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.flow.purchase.entity.FlowPurchasePlan;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlowPurchasePlanDao extends BaseDao<FlowPurchasePlan, String> {

    @Query(nativeQuery = true, value = "select t.* from na_flow_purchase_plan t where (t.status = 0 or t.status = 1) and exists\n" +
            " (select 1 from na_flow_purchase_plan_detail d where d.business_id = t.id and d.product_quotation_business_id = :productQuotationBusinessId )")
    List<FlowPurchasePlan> findByProductQuotationBusinessId(@Param("productQuotationBusinessId") String productQuotationBusinessId);

}
