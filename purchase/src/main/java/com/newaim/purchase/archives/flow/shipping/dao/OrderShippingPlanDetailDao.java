package com.newaim.purchase.archives.flow.shipping.dao;


import com.newaim.purchase.archives.flow.shipping.entity.OrderShippingPlanDetail;
import com.newaim.purchase.flow.shipping.entity.FlowOrderShippingPlanDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public interface OrderShippingPlanDetailDao extends JpaRepository<OrderShippingPlanDetail, String>, JpaSpecificationExecutor<OrderShippingPlanDetail> {

    /**
     * 根据业务ID查找明细结合
     * @param orderShippingPlanId
     * @return 明细集合
     */
    List<OrderShippingPlanDetail> findDetailsByOrderShippingPlanId(String orderShippingPlanId);

    /**
     * 通过订单id查询第一条发货计划明细
     * @param orderId 订单id
     * @return 发货计划明细
     */
    OrderShippingPlanDetail findTopByOrderId(String orderId);


    /**
     * 用于在成本计算选择发货计划是获取订单详情
     * @param orderShippingPlanId 返货计划id
     * @return 发货计划明细
     */
    @Query( nativeQuery = true,value ="SELECT spd.* \n" +
            "FROM na_order_shipping_plan sp\n" +
            "left join na_order_shipping_plan_detail spd on sp.id = spd.order_shipping_plan_id\n" +
            "inner join na_purchase_contract pc on spd.order_id = pc.id\n" +
            "WHERE pc.flag_custom_clearance_status = 1 and ( pc.flag_cost_status = 2 or  pc.Flag_cost_status is null) and sp.id = ?" )
    List<OrderShippingPlanDetail> getNotCostDetail( String orderShippingPlanId);
}
