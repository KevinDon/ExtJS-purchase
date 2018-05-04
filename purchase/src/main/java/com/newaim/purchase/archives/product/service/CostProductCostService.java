package com.newaim.purchase.archives.product.service;

import com.newaim.core.mapper.BeanMapper;
import com.newaim.purchase.archives.product.dao.CostDao;
import com.newaim.purchase.archives.product.dao.CostProductCostDao;
import com.newaim.purchase.archives.product.entity.Cost;
import com.newaim.purchase.archives.product.entity.CostProductCost;
import com.newaim.purchase.archives.product.vo.CostProductCostVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class CostProductCostService {

    @Autowired
    private CostProductCostDao costProductCostDao;

    @Autowired
    private CostDao costDao;

    /**
     * 获取该成本下所有产品成本
     * @param costId 成本id
     * @return
     */
    public List<CostProductCostVo> listCostProductCostsByCostId(String costId) {
        List<CostProductCost> data = costProductCostDao.findCostProductCostsByCostId(costId);
        return BeanMapper.mapList(data, CostProductCost.class, CostProductCostVo.class);
    }

    /**
     * 通过发货计划ID和产品ID查找产品成本
     * @param orderShippingPlanId
     * @param productId
     * @return
     */
    public CostProductCost findByOrderShippingPlanIdAndProductId(String orderShippingPlanId, String productId){
        Cost cost = costDao.findTopByOrderShippingPlanIdOrderByCreatedAtDesc(orderShippingPlanId);
        if(cost != null){
            List<CostProductCost> costProductCosts = costProductCostDao.findCostProductCostsByCostId(cost.getId());
            if(costProductCosts != null && costProductCosts.size() > 0){
                for (CostProductCost cpc: costProductCosts) {
                    if(StringUtils.equals(cpc.getProductId(), productId)){
                        return cpc;
                    }
                }
            }
        }
        return null;
    }
}
