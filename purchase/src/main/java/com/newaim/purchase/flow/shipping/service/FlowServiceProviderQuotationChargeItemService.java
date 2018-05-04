package com.newaim.purchase.flow.shipping.service;

import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.flow.shipping.dao.FlowServiceProviderQuotationChargeItemDao;
import com.newaim.purchase.flow.shipping.entity.FlowServiceProviderQuotationChargeItem;
import com.newaim.purchase.flow.shipping.vo.FlowServiceProviderQuotationChargeItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by bryan 2017/11/2.
 */
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class FlowServiceProviderQuotationChargeItemService extends ServiceBase {

    @Autowired
    private FlowServiceProviderQuotationChargeItemDao flowServiceProviderQuotationChargeItemDao;

    /**
     * 根据业务id返回所有明细
     * @param businessId 业务ID
     * @return detailVo集合
     */
    public List<FlowServiceProviderQuotationChargeItemVo> findChargeItemsVoByBusinessId(String businessId){
        return BeanMapper.mapList(flowServiceProviderQuotationChargeItemDao.findChargeItemsByBusinessId(businessId), FlowServiceProviderQuotationChargeItem.class, FlowServiceProviderQuotationChargeItemVo.class);
    }

    /**
     * 根据业务id返回所有明细
     * @param businessId 业务ID
     * @return detailVo集合
     */
    public List<FlowServiceProviderQuotationChargeItem> findChargeItemsByBusinessId(String businessId){
        return flowServiceProviderQuotationChargeItemDao.findChargeItemsByBusinessId(businessId);
    }
}
