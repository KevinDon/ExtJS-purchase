package com.newaim.purchase.archives.flow.finance.dao;


import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.archives.flow.finance.entity.BalanceRefund;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BalanceRefundDao extends BaseDao<BalanceRefund, String> {


    List<BalanceRefund> findByOrderId(String orderId);

    /**
     * 挂起指定订单的差额退款
     * @param orderIds
     */
    @Modifying
    @Query("UPDATE BalanceRefund set hold = 1 where orderId in ?1")
    void suspendByOrderIds(List<String> orderIds);

    @Modifying
    @Query("UPDATE BalanceRefund set hold = 1 where orderId in (select distinct o.id from PurchaseContract o, PurchaseContractDetail d where o.id = d.orderId and d.productId in ?1 and o.flagAsnStatus = 2)")
    void suspendByProductIds(List<String> productIds);

    List<BalanceRefund> findBySamplePaymentId(String samplePaymentId);

    List<BalanceRefund> findByBusinessId(String businessId);
}
