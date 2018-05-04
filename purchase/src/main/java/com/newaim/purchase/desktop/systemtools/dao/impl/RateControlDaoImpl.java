package com.newaim.purchase.desktop.systemtools.dao.impl;

import com.google.common.collect.Maps;
import com.newaim.core.jpa.BaseDaoCustomImpl;
import com.newaim.purchase.desktop.systemtools.dao.RateControlDaoCustom;
import com.newaim.purchase.desktop.systemtools.entity.RateControl;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class RateControlDaoImpl extends BaseDaoCustomImpl implements RateControlDaoCustom {

    @Override
    public List<RateControl> listNewRate() {

        Map<String, Object> params = Maps.newHashMap();
        params.put("effectiveDate", new Date());
        params.put("status", 1);

        StringBuilder hql = new StringBuilder("SELECT * FROM na_rate_control main WHERE ");
        hql.append("main.effective_date<=:effectiveDate AND main.status=:status ");
        hql.append("ORDER BY main.effective_date DESC, main.creator_id DESC ");
        hql.append("LIMIT 1");

        return listBySql(hql.toString(), params, RateControl.class);
    }
}
