package com.newaim.purchase.archives.product.dao;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.archives.product.entity.Cost;
import com.newaim.purchase.archives.service_provider.dao.ServiceProviderDaoCustom;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CostDao extends BaseDao<Cost, String>, ServiceProviderDaoCustom {


    /**
     * 通过发货计划查找最新成本
     * @param orderShippingPlanId
     * @return
     */
    Cost findTopByOrderShippingPlanIdOrderByCreatedAtDesc(String orderShippingPlanId);
}
