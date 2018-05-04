package com.newaim.purchase.archives.flow.shipping.dao;

import com.newaim.purchase.archives.flow.shipping.entity.OrderShippingPlan;

import java.util.List;

public interface OrderShippingPlanDaoCustom  {

    List<OrderShippingPlan> findByOrderId(String orderId);
}
