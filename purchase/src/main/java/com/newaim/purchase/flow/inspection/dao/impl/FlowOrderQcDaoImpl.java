package com.newaim.purchase.flow.inspection.dao.impl;

import com.google.common.collect.Maps;
import com.newaim.core.jpa.BaseDaoCustomImpl;
import com.newaim.purchase.flow.inspection.dao.FlowOrderQcDaoCustom;
import com.newaim.purchase.flow.inspection.entity.FlowOrderQc;

import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

public class FlowOrderQcDaoImpl extends BaseDaoCustomImpl implements FlowOrderQcDaoCustom{


    @Override
    public List<FlowOrderQc> findByOrderId(String orderId) {
        StringBuilder hql = new StringBuilder("select t from FlowOrderQc t ");
        hql.append(" where exists (select 1 from FlowOrderQcDetail d where d.businessId = t.id and d.orderId = :orderId)");
        Map<String, Object> params = Maps.newHashMap();
        params.put("orderId", orderId);
        return list(hql.toString(), params);
    }
}
