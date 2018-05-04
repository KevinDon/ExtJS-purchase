package com.newaim.purchase.archives.product.service;

import com.newaim.core.mapper.BeanMapper;
import com.newaim.purchase.archives.product.dao.CostPortDao;
import com.newaim.purchase.archives.product.entity.CostPort;
import com.newaim.purchase.archives.product.vo.CostPortVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class CostPortService {

    @Autowired
    private CostPortDao costPortDao;

    /**
     * 获取该成本下所有海运费
     * @param costId 成本id
     * @return
     */
    public List<CostPortVo> listCostPortsByCostId(String costId) {
        List<CostPort> data = costPortDao.findCostPortsByCostId(costId);
        return BeanMapper.mapList(data, CostPort.class, CostPortVo.class);
    }
}
