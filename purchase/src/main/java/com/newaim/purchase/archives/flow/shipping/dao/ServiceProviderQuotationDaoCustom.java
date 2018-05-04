package com.newaim.purchase.archives.flow.shipping.dao;

import com.newaim.purchase.archives.flow.shipping.entity.ServiceProviderQuotation;
import com.newaim.purchase.archives.flow.shipping.vo.ServiceProviderQuotationVo;
import com.newaim.purchase.archives.flow.shipping.vo.SpQuotationForOrderPlanVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceProviderQuotationDaoCustom {

    /**
     * 查询所有与指定发货计划相关的报价
     * @param params 条件对象
     * @return
     */
    List<ServiceProviderQuotation> findQuotationsByParams(ServiceProviderQuotationVo params);

}
