package com.newaim.purchase.flow.shipping.listeners;

import com.newaim.core.mapper.BeanMapper;
import com.newaim.purchase.Constants;
import com.newaim.purchase.archives.flow.shipping.dao.OrderShippingPlanDao;
import com.newaim.purchase.archives.flow.shipping.dao.OrderShippingPlanDetailDao;
import com.newaim.purchase.archives.flow.shipping.dao.ServiceProviderQuotationChargeItemDao;
import com.newaim.purchase.archives.flow.shipping.dao.ServiceProviderQuotationDao;
import com.newaim.purchase.archives.flow.shipping.dao.ServiceProviderQuotationOrderUnionDao;
import com.newaim.purchase.archives.flow.shipping.dao.ServiceProviderQuotationPortDao;
import com.newaim.purchase.archives.flow.shipping.entity.OrderShippingPlan;
import com.newaim.purchase.archives.flow.shipping.entity.OrderShippingPlanDetail;
import com.newaim.purchase.archives.flow.shipping.entity.ServiceProviderQuotation;
import com.newaim.purchase.archives.flow.shipping.entity.ServiceProviderQuotationChargeItem;
import com.newaim.purchase.archives.flow.shipping.entity.ServiceProviderQuotationOrderUnion;
import com.newaim.purchase.archives.flow.shipping.entity.ServiceProviderQuotationPort;
import com.newaim.purchase.flow.shipping.entity.FlowServiceProviderQuotation;
import com.newaim.purchase.flow.shipping.entity.FlowServiceProviderQuotationChargeItem;
import com.newaim.purchase.flow.shipping.entity.FlowServiceProviderQuotationPort;
import com.newaim.purchase.flow.shipping.service.FlowServiceProviderQuotationChargeItemService;
import com.newaim.purchase.flow.shipping.service.FlowServiceProviderQuotationPortService;
import com.newaim.purchase.flow.shipping.service.FlowServiceProviderQuotationService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 发货确认正常结束监听器
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FlowServiceProviderQuotationEndListener implements Serializable, ExecutionListener {

    @Autowired
    private FlowServiceProviderQuotationService flowServiceProviderQuotationService;

    @Autowired
    private FlowServiceProviderQuotationPortService flowServiceProviderQuotationPortService;

    @Autowired
    private FlowServiceProviderQuotationChargeItemService flowServiceProviderQuotationChargeItemService;

    @Autowired
    private ServiceProviderQuotationDao serviceProviderQuotationDao;

    @Autowired
    private ServiceProviderQuotationPortDao serviceProviderQuotationPortDao;

    @Autowired
    private ServiceProviderQuotationChargeItemDao serviceProviderQuotationChargeItemDao;

    @Autowired
    private OrderShippingPlanDao orderShippingPlanDao;

    @Autowired
    private OrderShippingPlanDetailDao orderShippingPlanDetailDao;

    @Autowired
    private ServiceProviderQuotationOrderUnionDao serviceProviderQuotationOrderUnionDao;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        String businessId = execution.getProcessBusinessKey();
        //1. 通过表单id获取相关对象
        FlowServiceProviderQuotation flowServiceProviderQuotation = flowServiceProviderQuotationService.getFlowServiceProviderQuotation(businessId);
        List<FlowServiceProviderQuotationPort> ports = flowServiceProviderQuotationPortService.findPortsByBusinessId(businessId);
        List<FlowServiceProviderQuotationChargeItem> chargeItems = flowServiceProviderQuotationChargeItemService.findChargeItemsByBusinessId(businessId);
        //2. 保存相关数据到正式业务数据表
        ServiceProviderQuotation serviceProviderQuotation = BeanMapper.map(flowServiceProviderQuotation, ServiceProviderQuotation.class);
        serviceProviderQuotation.setId(null);
        serviceProviderQuotation.setBusinessId(flowServiceProviderQuotation.getId());
        serviceProviderQuotation.setFlowStatus(Constants.FlowStatus.PASS.code);
        serviceProviderQuotation.setEndTime(new Date());
        serviceProviderQuotationDao.save(serviceProviderQuotation);
        if(StringUtils.isNotBlank(serviceProviderQuotation.getFlowOrderShippingPlanId())){
            //如果有关联采购计划，则记录采购计划下订单与报价的关联关系
            OrderShippingPlan orderShippingPlan = orderShippingPlanDao.getByBusinessId(serviceProviderQuotation.getFlowOrderShippingPlanId());
            if(orderShippingPlan != null){
                List<OrderShippingPlanDetail> details = orderShippingPlanDetailDao.findDetailsByOrderShippingPlanId(orderShippingPlan.getId());
                if(details != null){
                    for (int i = 0; i < details.size(); i++) {
                        OrderShippingPlanDetail detail = details.get(i);
                        ServiceProviderQuotationOrderUnion union = new ServiceProviderQuotationOrderUnion();
                        union.setOrderId(detail.getOrderId());
                        union.setServiceProviderQuotationId(serviceProviderQuotation.getId());
                        serviceProviderQuotationOrderUnionDao.save(union);
                    }
                }
            }
        }
        if(ports != null){
            for (FlowServiceProviderQuotationPort flowPort: ports) {
                //2.2 拷贝明细数据到业务表
                ServiceProviderQuotationPort port = BeanMapper.map(flowPort, ServiceProviderQuotationPort.class);
                port.setServiceProviderQuotationId(serviceProviderQuotation.getId());
                serviceProviderQuotationPortDao.save(port);
            }
        }
        if(chargeItems != null){
            for (FlowServiceProviderQuotationChargeItem flowChargeItem: chargeItems) {
                //2.2 拷贝明细数据到业务表
                ServiceProviderQuotationChargeItem chargeItem = BeanMapper.map(flowChargeItem, ServiceProviderQuotationChargeItem.class);
                chargeItem.setServiceProviderQuotationId(serviceProviderQuotation.getId());
                serviceProviderQuotationChargeItemDao.save(chargeItem);
            }
        }
    }
}
