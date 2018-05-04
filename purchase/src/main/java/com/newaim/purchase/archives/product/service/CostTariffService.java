package com.newaim.purchase.archives.product.service;

import com.newaim.core.mapper.BeanMapper;
import com.newaim.purchase.archives.product.dao.CostTariffDao;
import com.newaim.purchase.archives.product.entity.CostTariff;
import com.newaim.purchase.archives.product.vo.CostTariffVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class CostTariffService {

    @Autowired
    private CostTariffDao costTariffDao;

    /**
     * 获取该成本下所有海运费
     * @param costId 成本id
     * @return
     */
    public List<CostTariffVo> listPortsByCostId(String costId) {
        List<CostTariff> data = costTariffDao.findCostTariffByCostId(costId);
        return BeanMapper.mapList(data, CostTariff.class, CostTariffVo.class);
    }
}
