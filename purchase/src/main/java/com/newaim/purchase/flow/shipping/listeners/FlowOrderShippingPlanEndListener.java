package com.newaim.purchase.flow.shipping.listeners;

import com.newaim.core.mapper.BeanMapper;
import com.newaim.purchase.Constants;
import com.newaim.purchase.archives.flow.purchase.dao.PurchaseContractDao;
import com.newaim.purchase.archives.flow.purchase.entity.PurchaseContract;
import com.newaim.purchase.archives.flow.shipping.dao.OrderShippingPlanDao;
import com.newaim.purchase.archives.flow.shipping.dao.OrderShippingPlanDetailDao;
import com.newaim.purchase.flow.shipping.entity.FlowOrderShippingPlan;
import com.newaim.purchase.flow.shipping.entity.FlowOrderShippingPlanDetail;
import com.newaim.purchase.archives.flow.shipping.entity.OrderShippingPlan;
import com.newaim.purchase.archives.flow.shipping.entity.OrderShippingPlanDetail;
import com.newaim.purchase.flow.shipping.service.FlowOrderShippingPlanDetailService;
import com.newaim.purchase.flow.shipping.service.FlowOrderShippingPlanService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *发货计划正常结束监听器.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FlowOrderShippingPlanEndListener implements Serializable, ExecutionListener {

    @Autowired
    private FlowOrderShippingPlanService flowOrderShippingPlanService;

    @Autowired
    private FlowOrderShippingPlanDetailService flowOrderShippingPlanDetailService;

    @Autowired
    private OrderShippingPlanDao orderShippingPlanDao;

    @Autowired
    private OrderShippingPlanDetailDao orderShippingPlanDetailDao;

    @Autowired
    private PurchaseContractDao purchaseContractDao;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        String businessId = execution.getProcessBusinessKey();
        //1. 通过表单id获取相关对象
        FlowOrderShippingPlan flowOrderShippingPlan = flowOrderShippingPlanService.getFlowOrderShippingPlan(businessId);
        List<FlowOrderShippingPlanDetail> details = flowOrderShippingPlanDetailService.findDetailsByBusinessId(businessId);
        //2. 保存相关数据到正式业务数据表
        OrderShippingPlan orderShippingPlan = BeanMapper.map(flowOrderShippingPlan, OrderShippingPlan.class);
        orderShippingPlan.setId(null);
        orderShippingPlan.setBusinessId(flowOrderShippingPlan.getId());
        orderShippingPlan.setFlowStatus(Constants.FlowStatus.PASS.code);
        orderShippingPlan.setEndTime(new Date());
        orderShippingPlan.setHandlerId(flowOrderShippingPlan.getCreatorId());
        orderShippingPlan.setHandlerCnName(flowOrderShippingPlan.getCreatorCnName());
        orderShippingPlan.setHandlerEnName(flowOrderShippingPlan.getCreatorEnName());
        orderShippingPlanDao.save(orderShippingPlan);

        if(details != null){
            for (FlowOrderShippingPlanDetail detail: details) {
                //2.2 拷贝明细数据到业务表
                OrderShippingPlanDetail orderShippingPlanDetail = BeanMapper.map(detail, OrderShippingPlanDetail.class);
                orderShippingPlanDetail.setId(null);
                orderShippingPlanDetail.setOrderShippingPlanId(orderShippingPlan.getId());
                orderShippingPlanDetailDao.save(orderShippingPlanDetail);
                PurchaseContract order = purchaseContractDao.getOne(orderShippingPlanDetail.getOrderId());
                if(order != null){
                    order.setFlagOrderShippingPlanId(orderShippingPlan.getId());
                    order.setFlagOrderShippingPlanStatus(1);
                    order.setFlagOrderShippingPlanTime(new Date());
                    purchaseContractDao.save(order);
                }
            }
        }
    }
}
