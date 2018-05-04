package com.newaim.purchase.flow.shipping.dao;

import com.newaim.purchase.flow.shipping.entity.FlowServiceProviderQuotationChargeItem;
import com.newaim.purchase.flow.shipping.entity.FlowServiceProviderQuotationPort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlowServiceProviderQuotationChargeItemDao extends JpaRepository<FlowServiceProviderQuotationChargeItem, String>, JpaSpecificationExecutor<FlowServiceProviderQuotationChargeItem> {

    /**
     * 根据业务id删除数据
     * @param businessId
     */
    void deleteByBusinessId(String businessId);

    /**
     * 根据业务ID查找明细结合
     * @param businessId 业务id
     * @return 明细集合
     */
    List<FlowServiceProviderQuotationChargeItem> findChargeItemsByBusinessId(String businessId);



    /**
     * 从发货计划中获取港口和柜型数量数据，转化为 FlowServiceProviderQuotationPort 对象
     * @param serviceProviderId
     * @param flowOrderShippingPlanId
     * @return
     */
    @Query(nativeQuery = true, value = "SELECT p.id,d.business_id,p.origin_port_id,p.origin_port_cn_name,p.origin_port_en_name,p.destination_port_id,p.destination_port_cn_name,p.destination_port_en_name,\n" +
            "  d.id as flow_order_shipping_plan_id,\n" +
            "  0 currency,0 as rate_aud_to_rmb,0 as rate_aud_to_usd,\n" +
            "  0 as price_gp20_aud,0 as price_gp20_rmb,0 as price_gp20_usd,\n" +
            "  0 as price_gp40_aud,0 as price_gp40_rmb,0 as price_gp40_usd,\n" +
            "  0 as price_hq40_aud,0 as price_hq40_rmb,0 as price_hq40_usd,\n" +
            "  0 as price_lcl_aud,0 as price_lcl_rmb,0 as price_lcl_usd,\n" +
            "  0 as price_other_aud,0 as price_other_rmb,0 as price_other_usd,\n" +
            "  sum(CASE when d.container_type = 1 then d.container_qty else 0 end) as gp20_qty,\n" +
            "  sum(CASE when d.container_type = 2 then d.container_qty else 0 end) as gp40_qty,\n" +
            "  sum(CASE when d.container_type = 3 then d.container_qty else 0 end) as hq40_qty,\n" +
            "  sum(CASE when d.container_type = 4 then d.container_qty else 0 end) as lcl_cbm,\n" +
            "  0 as prev_rate_aud_to_rmb,0 as prev_rate_aud_to_usd,\n" +
            "  0 as prev_price_gp20_aud,0 as prev_price_gp20_rmb,0 as prev_price_gp20_usd,\n" +
            "  0 as prev_price_gp40_aud,0 as prev_price_gp40_rmb,0 as prev_price_gp40_usd,\n" +
            "  0 as prev_price_hq40_aud,0 as prev_price_hq40_rmb,0 as prev_price_hq40_usd,\n" +
            "  0 as prev_price_lcl_aud,0 as prev_price_lcl_rmb,0 as prev_price_lcl_usd,\n" +
            "  0 as prev_price_other_aud,0 as prev_price_other_rmb,0 as prev_price_other_usd,\n" +
            "  sum(CASE when d.container_type = 1 then d.container_qty else 0 end) as prev_gp20_qty,\n" +
            "  sum(CASE when d.container_type = 2 then d.container_qty else 0 end) as prev_gp40_qty,\n" +
            "  sum(CASE when d.container_type = 3 then d.container_qty else 0 end) as prev_hq40_qty,\n" +
            "  sum(CASE when d.container_type = 4 then d.container_qty else 0 end) as prev_lcl_cbm\n" +
            "from na_flow_order_shipping_plan_detail d\n" +
            "LEFT JOIN na_service_provider_port p on d.origin_port_id = p.origin_port_id\n" +
            "WHERE d.business_id = :flowOrderShippingPlanId and p.service_provider_id=:serviceProviderId \n" +
            "GROUP BY p.id,d.business_id,d.id,p.origin_port_id,p.origin_port_cn_name,p.origin_port_en_name,p.destination_port_id,p.destination_port_cn_name,p.destination_port_en_name\n" +
            "ORDER BY p.origin_port_id")
    List<FlowServiceProviderQuotationChargeItem> findChargeItemsForQuotation(@Param("serviceProviderId") String serviceProviderId, @Param("flowOrderShippingPlanId") String flowOrderShippingPlanId);
}
