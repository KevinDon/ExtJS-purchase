package com.newaim.purchase.archives.flow.finance.dao;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.archives.flow.finance.entity.PurchaseContractDeposit;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseContractDepositDao extends BaseDao<PurchaseContractDeposit, String> {

    /**
     * 挂起指定订单的定金支付
     * @param orderIds
     */
    @Modifying
    @Query("UPDATE PurchaseContractDeposit set hold = 1 where orderId in ?1")
    void suspendByOrderIds(List<String> orderIds);

    @Modifying
    @Query("UPDATE PurchaseContractDeposit set hold = 1 where orderId in (select distinct o.id from PurchaseContract o, PurchaseContractDetail d where o.id = d.orderId and d.productId in ?1 and o.flagAsnStatus = 2)")
    void suspendByProductIds(List<String> productIds);

    List<PurchaseContractDeposit> findByOrderId(String orderId);

    List<PurchaseContractDeposit> findByOrderIdIn(List<String> orderIds);
    List<PurchaseContractDeposit> findByBusinessId(String businessId);



}
