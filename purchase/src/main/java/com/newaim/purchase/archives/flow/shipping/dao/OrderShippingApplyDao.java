package com.newaim.purchase.archives.flow.shipping.dao;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.archives.flow.shipping.entity.OrderShippingApply;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderShippingApplyDao extends BaseDao<OrderShippingApply, String> {
    /**
     * 挂起指定订单的发货计划
     * @param orderIds
     */
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE na_order_shipping_apply t set hold = 1 where EXISTS\n" +
            "(SELECT 1 from na_order_shipping_apply_detail d where d.order_shipping_apply_id = t.id and d.order_id in ?1)")
    void suspendByOrderIds(List<String> orderIds);

    @Modifying
    @Query(nativeQuery = true, value = "UPDATE na_order_shipping_apply t set hold = 1 where EXISTS\n" +
            "(SELECT 1 from na_order_shipping_apply_detail d where d.order_shipping_apply_id = t.id and d.order_id in (select distinct o.id from na_purchase_contract o,na_purchase_contract_detail d where o.id = d.order_id and d.product_id in ?1 and o.flag_asn_status = 2))")
    void suspendByProductIds(List<String> orderIds);

    List<OrderShippingApply> findByBusinessId(String businessId);

}
