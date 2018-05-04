package com.newaim.purchase.archives.flow.inspection.dao;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.archives.flow.inspection.entity.OrderQc;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderQcDao extends BaseDao<OrderQc, String> {

    /**
     * 挂起指定订单的订单质检
     * @param orderIds
     */
    @Modifying
    @Query("UPDATE OrderQc set hold = 1 where orderId in ?1")
    void suspendByOrderIds(List<String> orderIds);

    @Modifying
    @Query("UPDATE OrderQc set hold = 1 where orderId in (select distinct o.id from PurchaseContract o, PurchaseContractDetail d where o.id = d.orderId and d.productId in ?1 and o.flagAsnStatus = 2)")
    void suspendByProductIds(List<String> productIds);

    /**
     * 通过订单id查找
     * @param orderId
     * @return
     */
    List<OrderQc> findByOrderId(String orderId);
}
