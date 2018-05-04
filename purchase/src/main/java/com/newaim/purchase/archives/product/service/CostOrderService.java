package com.newaim.purchase.archives.product.service;

import com.newaim.core.mapper.BeanMapper;
import com.newaim.purchase.archives.product.dao.CostOrderDao;
import com.newaim.purchase.archives.product.entity.CostOrder;
import com.newaim.purchase.archives.product.entity.CostProductCost;
import com.newaim.purchase.archives.product.vo.CostOrderVo;
import com.newaim.purchase.archives.product.vo.CostProductCostVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class CostOrderService {

    @Autowired
    private CostOrderDao costOrderDao;



    /**
     * 获取该成本下所有产品成本
     * @param orderId 成本id
     * @return
     */
    public List<CostOrderVo> listCostProductCostsByCostId(String costId) {
        List<CostOrder> costOrders = costOrderDao.findCostOrderByCostId(costId);
        return BeanMapper.mapList(costOrders, CostOrder.class, CostOrderVo.class);
    }



}
