package com.newaim.purchase.archives.flow.shipping.dao;


import com.newaim.purchase.archives.flow.shipping.entity.ServiceProviderQuotationPort;
import com.newaim.purchase.archives.flow.shipping.vo.SpQuotationForOrderPlanVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceProviderQuotationPortDaoCustom{
    /**
     * 根据条件查询港口报价
     * @param queryParams 查询参数
     * @return
     */
    List<ServiceProviderQuotationPort> findQuotationPortsForOrderPlan(List<SpQuotationForOrderPlanVo> queryParams);
}
