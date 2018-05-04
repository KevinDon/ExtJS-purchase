package com.newaim.purchase.flow.finance.dao;

import com.newaim.purchase.flow.finance.entity.FlowPurchaseContractDeposit;

import java.util.List;

public interface FlowPurchaseContractDepositDaoCustom {


    /**
     * 通过订单id查找
     * @param orderId
     * @return
     */
    List<FlowPurchaseContractDeposit> findByOrderId(String orderId);
}
