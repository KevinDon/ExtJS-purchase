package com.newaim.purchase.flow.finance.dao;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.flow.finance.entity.FlowPurchaseContractDeposit;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlowPurchaseContractDepositDao extends BaseDao<FlowPurchaseContractDeposit, String>,FlowPurchaseContractDepositDaoCustom {

    /**
     * 挂起指定订单的定金支付
     * @param orderIds
     */
    @Modifying
    @Query("UPDATE FlowPurchaseContractDeposit set hold = 1 where orderId in ?1")
    void suspendByOrderIds(List<String> orderIds);

    @Modifying
    @Query("UPDATE FlowPurchaseContractDeposit set hold = 1 where orderId in (select distinct o.id from PurchaseContract o, PurchaseContractDetail d where o.id = d.orderId and d.productId in ?1 and o.flagAsnStatus = 2)")
    void suspendByProductIds(List<String> productIds);

    @Query("select t from FlowPurchaseContractDeposit t where (t.status = 0 or t.status = 1) and t.orderId = :orderId")
    List<FlowPurchaseContractDeposit> findDepositByOrderId(@Param("orderId") String orderId);
}
