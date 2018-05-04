package com.newaim.purchase.archives.product.service;

import com.newaim.core.mapper.BeanMapper;
import com.newaim.purchase.archives.product.dao.CostProductDao;
import com.newaim.purchase.archives.product.dao.TariffDao;
import com.newaim.purchase.archives.product.entity.CostProduct;
import com.newaim.purchase.archives.product.entity.Tariff;
import com.newaim.purchase.archives.product.vo.CostProductVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class CostProductService {

    @Autowired
    private CostProductDao costProductDao;

    @Autowired
    private TariffDao tariffDao;

    /**
     * 获取该成本下所有产品
     * @param costId 成本id
     * @return
     */
    public List<CostProductVo> listCostProductsByCostId(String costId) {
        List<CostProduct> data = costProductDao.findCostProductsByCostId(costId);
        List<CostProductVo> result = BeanMapper.mapList(data, CostProduct.class, CostProductVo.class);
        if(result != null && result.size() > 0){
            for (int i = 0; i < result.size(); i++) {
                CostProductVo costProductVo = result.get(i);
                if(StringUtils.isNotBlank(costProductVo.getHsCode())){
                    Tariff tariff = tariffDao.findTopByItemCode(costProductVo.getHsCode());
                    if(tariff != null){
                        costProductVo.setDutyRate(tariff.getDutyRate());
                    }
                }
            }
        }
        return result;
    }

}
