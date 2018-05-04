package com.newaim.purchase.archives.flow.shipping.dao.impl;

import com.google.common.collect.Maps;
import com.newaim.core.jpa.BaseDaoCustomImpl;
import com.newaim.purchase.archives.flow.shipping.dao.OrderShippingPlanDaoCustom;
import com.newaim.purchase.archives.flow.shipping.entity.OrderShippingPlan;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

public class OrderShippingPlanDaoImpl extends BaseDaoCustomImpl implements OrderShippingPlanDaoCustom {


    @Override
    public List<OrderShippingPlan> findByOrderId(String orderId) {
        Map<String, Object> params = Maps.newHashMap();

        if(StringUtils.isBlank(orderId)){
            params.put("orderId", orderId);
        }else{
            params.put("orderId", "");
        }

        StringBuilder hql = new StringBuilder("select t from OrderShippingPlan t ");
        hql.append(" where exists (select 1 from OrderShippingPlanDetail d where d.orderBusinessId = t.id and d.orderId = :orderId)");

        return list(hql.toString(), params);
    }
}
