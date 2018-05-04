package com.newaim.purchase.archives.flow.shipping.dao.impl;

import com.newaim.core.jpa.BaseDaoCustomImpl;
import com.newaim.purchase.archives.flow.shipping.dao.ServiceProviderQuotationDaoCustom;
import com.newaim.purchase.archives.flow.shipping.entity.ServiceProviderQuotation;
import com.newaim.purchase.archives.flow.shipping.vo.ServiceProviderQuotationVo;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class ServiceProviderQuotationDaoImpl extends BaseDaoCustomImpl implements ServiceProviderQuotationDaoCustom{


    @Override
    public List<ServiceProviderQuotation> findQuotationsByParams(ServiceProviderQuotationVo params) {
        StringBuffer hql = new StringBuffer("select t from ServiceProviderQuotation t where 1 = 1 ");
        if(StringUtils.isNotBlank(params.getServiceProviderId())){
            hql.append(" and t.serviceProviderId = :serviceProviderId ");
        }
        if(params.getValidUntil() != null){
            hql.append(" and t.effectiveDate >= :effectiveDate and t.validUntil <= :validUntil ");
        }
        return list(hql.toString(), params);
    }
}
