package com.newaim.purchase.archives.product.service;

import com.newaim.core.mapper.BeanMapper;
import com.newaim.purchase.archives.product.dao.CostChargeItemDao;
import com.newaim.purchase.archives.product.entity.CostChargeItem;
import com.newaim.purchase.archives.product.vo.CostChargeItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class CostChargeItemService {

    @Autowired
    private CostChargeItemDao costChargeItemDao;

    /**
     * 获取该成本下所有本地費用
     * @param costId 成本id
     * @return
     */
    public List<CostChargeItemVo> listCostChargeItemsByCostId(String costId) {
        List<CostChargeItem> data = costChargeItemDao.findCostChargeItemsByCostId(costId);
        return BeanMapper.mapList(data, CostChargeItem.class, CostChargeItemVo.class);
    }
}
