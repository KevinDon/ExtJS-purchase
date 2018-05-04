package com.newaim.purchase.flow.shipping.service;

import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.flow.shipping.dao.FlowServiceProviderQuotationPortDao;
import com.newaim.purchase.flow.shipping.entity.FlowServiceProviderQuotationPort;
import com.newaim.purchase.flow.shipping.vo.FlowServiceProviderQuotationPortVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by bryan 2017/11/2.
 */
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class FlowServiceProviderQuotationPortService extends ServiceBase {

    @Autowired
    private FlowServiceProviderQuotationPortDao flowServiceProviderQuotationPortDao;


    /**
     * 根据业务id返回所有明细
     * @param businessId 业务ID
     * @return detailVo集合
     */
    public List<FlowServiceProviderQuotationPortVo> findPostsVoByBusinessId(String businessId){
        return BeanMapper.mapList(flowServiceProviderQuotationPortDao.findPortsByBusinessId(businessId), FlowServiceProviderQuotationPort.class, FlowServiceProviderQuotationPortVo.class);
    }

    /**
     * 根据业务id返回所有明细
     * @param businessId 业务ID
     * @return detailVo集合
     */
    public List<FlowServiceProviderQuotationPort> findPortsByBusinessId(String businessId){
        return flowServiceProviderQuotationPortDao.findPortsByBusinessId(businessId);
    }

    /**
     * 从发货计划中获取港口和柜型数量数据，转化为 FlowServiceProviderQuotationPort 对象
     * @param serviceProviderId
     * @param flowOrderShippingPlanId
     * @return
     */
    List<FlowServiceProviderQuotationPortVo> findPortVosForQuotation(String serviceProviderId, String flowOrderShippingPlanId){
        return BeanMapper.mapList(findPortsForQuotation(serviceProviderId, flowOrderShippingPlanId), FlowServiceProviderQuotationPort.class, FlowServiceProviderQuotationPortVo.class);
    }


    /**
     * 从发货计划中获取港口和柜型数量数据，转化为 FlowServiceProviderQuotationPort 对象
     * @param serviceProviderId
     * @param flowOrderShippingPlanId
     * @return
     */
    List<FlowServiceProviderQuotationPort> findPortsForQuotation(String serviceProviderId, String flowOrderShippingPlanId){
        return flowServiceProviderQuotationPortDao.findPortsForQuotation(serviceProviderId, flowOrderShippingPlanId);
    }

}
