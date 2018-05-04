package com.newaim.purchase.archives.flow.shipping.dao.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.newaim.core.jpa.BaseDaoCustomImpl;
import com.newaim.purchase.archives.flow.shipping.dao.ServiceProviderQuotationPortDaoCustom;
import com.newaim.purchase.archives.flow.shipping.entity.ServiceProviderQuotationPort;
import com.newaim.purchase.archives.flow.shipping.vo.SpQuotationForOrderPlanVo;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ServiceProviderQuotationPortDaoImpl extends BaseDaoCustomImpl implements ServiceProviderQuotationPortDaoCustom {



    @Override
    public List<ServiceProviderQuotationPort> findQuotationPortsForOrderPlan(List<SpQuotationForOrderPlanVo> queryParams) {
        StringBuffer hql = new StringBuffer("select t from ServiceProviderQuotationPort t where 1 = 1 ");
        Map<String, Object> params = Maps.newHashMap();
        List<String> originPortIds = Lists.newArrayList();
        if(queryParams != null && queryParams.size() > 0){
            hql.append(" and t.serviceProviderQuotationId in( select p.serviceProviderQuotationId from ServiceProviderQuotationPort p ");
            hql.append(", ServiceProviderQuotation q where q.id =p.serviceProviderQuotationId and q.status = 1 and (");
            Set<String> countSet = Sets.newHashSet();
            for (int i = 0; i < queryParams.size(); i++) {
                SpQuotationForOrderPlanVo vo = queryParams.get(i);
                originPortIds.add(vo.getOriginPortId());
                if(i > 0){
                    hql.append("or");
                }
                hql.append("(p.originPortId = :originPortId").append(i).append(" and p.destinationPortId = :destinationPortId").append(i);
                hql.append(" and q.effectiveDate <= :etd").append(i).append(" and q.validUntil >=:etd").append(i);
                if("1".equals(vo.getContainerType())){
                    hql.append(" and p.priceGp20Aud is not null and p.priceGp20Aud >0 ");
                }else if ("2".equals(vo.getContainerType())){
                    hql.append(" and p.priceGp40Aud is not null and p.priceGp40Aud >0");
                }else if ("3".equals(vo.getContainerType())){
                    hql.append(" and p.priceHq40Aud is not null and p.priceHq40Aud >0 ");
                }else{
                    hql.append(" and p.priceLclAud is not null and p.priceLclAud >0 ");
                }
                hql.append(")");
                params.put("originPortId" + i, vo.getOriginPortId());
                params.put("destinationPortId" + i, vo.getDestinationPortId());
                params.put("etd" + i, vo.getEtd());
                countSet.add(vo.getOriginPortId() + ":" + vo.getDestinationPortId());
            }
            hql.append(") group by p.serviceProviderQuotationId, p.destinationPortId having count(1) = :count");
            hql.append(")");
            params.put("count", Long.valueOf(countSet.size()));
        }
        params.put("originPortIds", originPortIds);
        hql.append(" and t.originPortId in :originPortIds ");
        return list(hql.toString(), params);
    }
}
