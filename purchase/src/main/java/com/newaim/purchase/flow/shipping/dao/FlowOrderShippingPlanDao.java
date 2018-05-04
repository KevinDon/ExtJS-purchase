package com.newaim.purchase.flow.shipping.dao;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.flow.shipping.entity.FlowOrderShippingPlan;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlowOrderShippingPlanDao extends BaseDao<FlowOrderShippingPlan, String>, FlowOrderShippingPlanDaoCustom {

    /**
     * 挂起指定订单的发货计划
     * @param orderIds
     */
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE na_flow_order_shipping_plan t set hold = 1 where EXISTS\n" +
            "(SELECT 1 from na_flow_order_shipping_plan_detail d where d.business_id = t.id and d.order_id in ?1)")
    void suspendByOrderIds(List<String> orderIds);

    @Modifying
    @Query(nativeQuery = true, value = "UPDATE na_flow_order_shipping_plan t set hold = 1 where EXISTS\n" +
            "(SELECT 1 from na_flow_order_shipping_plan_detail d where d.business_id = t.id and d.order_id in (select distinct o.id from na_purchase_contract o,na_purchase_contract_detail d where o.id = d.order_id and d.product_id in ?1 and o.flag_asn_status = 2))")
    void suspendByProductIds(List<String> orderIds);

    @Query(nativeQuery = true, value = "select t.* from na_flow_order_shipping_plan t where (t.status = 0 or t.status = 1) and exists\n" +
            " (select 1 from na_service_provider_quotation q where q.id = t.service_provider_quotation_id and q.business_id = :serviceProviderQuotationBusinessId )")
    List<FlowOrderShippingPlan> findByServiceProviderQuotationBusinessId(@Param("serviceProviderQuotationBusinessId") String serviceProviderQuotationBusinessId);

    @Query("select t from FlowOrderShippingPlan t where (t.status = 0 or t.status = 1) and exists (select 1 from FlowOrderShippingPlanDetail d where d.businessId = t.id and d.orderId = :orderId)")
    List<FlowOrderShippingPlan> findByOrderId(@Param("orderId") String orderId);
}
