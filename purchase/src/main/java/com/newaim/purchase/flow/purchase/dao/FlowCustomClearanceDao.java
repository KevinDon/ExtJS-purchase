package com.newaim.purchase.flow.purchase.dao;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.flow.purchase.entity.FlowCustomClearance;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlowCustomClearanceDao extends BaseDao<FlowCustomClearance, String> {

    /**
     * 挂起指定订单的清关申请
     * @param orderIds
     */
    @Modifying
    @Query("UPDATE FlowCustomClearance set hold = 1 where orderId in ?1")
    void suspendByOrderIds(List<String> orderIds);

    @Modifying
    @Query("UPDATE FlowCustomClearance set hold = 1 where orderId in (select distinct o.id from PurchaseContract o, PurchaseContractDetail d where o.id = d.orderId and d.productId in ?1 and o.flagAsnStatus = 2)")
    void suspendByProductIds(List<String> productIds);

    @Query(nativeQuery = true, value = "select t.* from na_flow_custom_clearance t where (t.status = 0 or t.status = 1) and exists(\n" +
            "  select 1 from na_flow_custom_clearance_packing d where d.business_id = t.id and d.order_id in :orderIds\n" +
            ")")
    List<FlowCustomClearance> findByOrderIds(@Param("orderIds") List<String> orderIds);

}
