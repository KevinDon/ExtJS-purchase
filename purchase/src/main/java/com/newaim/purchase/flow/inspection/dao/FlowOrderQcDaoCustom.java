package com.newaim.purchase.flow.inspection.dao;

import com.newaim.purchase.flow.inspection.entity.FlowOrderQc;

import java.util.List;

public interface FlowOrderQcDaoCustom {

    /**
     * 通过订单id查找
     * @param orderId
     * @return
     */
    List<FlowOrderQc> findByOrderId(String orderId);
}
