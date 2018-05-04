package com.newaim.purchase.flow.finance.dao;


import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.flow.finance.entity.FlowBalanceRefund;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlowBalanceRefundDao extends BaseDao<FlowBalanceRefund, String> {
    /**
     * 挂起指定订单的差额退款
     * @param orderIds
     */
    @Modifying
    @Query("UPDATE FlowBalanceRefund set hold = 1 where orderId in ?1")
    void suspendByOrderIds(List<String> orderIds);

    @Modifying
    @Query("UPDATE FlowBalanceRefund set hold = 1 where orderId in (select distinct o.id from PurchaseContract o, PurchaseContractDetail d where o.id = d.orderId and d.productId in ?1 and o.flagAsnStatus = 2)")
    void suspendByProductIds(List<String> productIds);

    List<FlowBalanceRefund> findBySamplePaymentId(String samplePaymentId);
}
