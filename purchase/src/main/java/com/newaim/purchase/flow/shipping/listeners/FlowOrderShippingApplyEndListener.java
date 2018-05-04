package com.newaim.purchase.flow.shipping.listeners;

import com.newaim.core.mapper.BeanMapper;
import com.newaim.purchase.Constants;
import com.newaim.purchase.archives.flow.purchase.dao.PurchaseContractDao;
import com.newaim.purchase.archives.flow.purchase.entity.PurchaseContract;
import com.newaim.purchase.archives.flow.shipping.dao.OrderShippingApplyDao;
import com.newaim.purchase.archives.flow.shipping.dao.OrderShippingApplyDetailDao;
import com.newaim.purchase.archives.flow.shipping.dao.OrderShippingPlanDao;
import com.newaim.purchase.archives.flow.shipping.entity.OrderShippingPlan;
import com.newaim.purchase.flow.purchase.dao.FlowPurchaseContractDao;
import com.newaim.purchase.flow.purchase.entity.FlowPurchaseContract;
import com.newaim.purchase.flow.shipping.dao.FlowOrderShippingPlanDao;
import com.newaim.purchase.flow.shipping.entity.FlowOrderShippingApply;
import com.newaim.purchase.flow.shipping.entity.FlowOrderShippingApplyDetail;
import com.newaim.purchase.archives.flow.shipping.entity.OrderShippingApply;
import com.newaim.purchase.archives.flow.shipping.entity.OrderShippingApplyDetail;
import com.newaim.purchase.flow.shipping.entity.FlowOrderShippingPlan;
import com.newaim.purchase.flow.shipping.service.FlowOrderShippingApplyDetailService;
import com.newaim.purchase.flow.shipping.service.FlowOrderShippingApplyService;
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
public class FlowOrderShippingApplyEndListener implements Serializable, ExecutionListener {

    @Autowired
    private FlowOrderShippingApplyService flowOrderShippingApplyService;

    @Autowired
    private FlowOrderShippingApplyDetailService flowOrderShippingApplyDetailService;

    @Autowired
    private OrderShippingApplyDao orderShippingApplyDao;

    @Autowired
    private OrderShippingApplyDetailDao orderShippingApplyDetailDao;

    @Autowired
    private PurchaseContractDao purchaseContractDao;

    @Autowired
    private FlowPurchaseContractDao flowPurchaseContractDao;

    @Autowired
    private OrderShippingPlanDao orderShippingPlanDao;

    @Autowired
    private FlowOrderShippingPlanDao flowOrderShippingPlanDao;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        String businessId = execution.getProcessBusinessKey();
        //1. 通过表单id获取相关对象
        FlowOrderShippingApply flowOrderShippingApply = flowOrderShippingApplyService.getFlowOrderShippingApply(businessId);
        List<FlowOrderShippingApplyDetail> details = flowOrderShippingApplyDetailService.findDetailsByBusinessId(businessId);
        //2. 保存相关数据到正式业务数据表
        OrderShippingApply orderShippingApply = BeanMapper.map(flowOrderShippingApply, OrderShippingApply.class);
        orderShippingApply.setId(null);
        orderShippingApply.setBusinessId(flowOrderShippingApply.getId());
        orderShippingApply.setFlowStatus(Constants.FlowStatus.PASS.code);
        orderShippingApply.setEndTime(new Date());
        orderShippingApplyDao.save(orderShippingApply);

        if(details != null){
            for (FlowOrderShippingApplyDetail detail: details) {
                //2.2 拷贝明细数据到业务表
                OrderShippingApplyDetail orderShippingApplyDetail = BeanMapper.map(detail, OrderShippingApplyDetail.class);
                orderShippingApplyDetail.setOrderShippingApplyId(orderShippingApply.getId());
                orderShippingApplyDetailDao.save(orderShippingApplyDetail);
                //发货确认通过后，把发货计划的标记状态回写到正式发货计划
                OrderShippingPlan orderShippingPlan = orderShippingPlanDao.getOne(detail.getOrderShippingPlanId());
                if (orderShippingPlan != null){
                    FlowOrderShippingPlan flowOrderShippingPlan = flowOrderShippingPlanDao.findOne(orderShippingPlan.getBusinessId());
                    flowOrderShippingPlan.setFlagOrderShippingApplyStatus(1);
                    flowOrderShippingPlan.setFlagFlowOrderShippingApplyId(flowOrderShippingApply.getId());
                    flowOrderShippingPlan.setFlagOrderShippingApplyId(orderShippingApply.getId());
                    flowOrderShippingPlan.setFlagOrderShippingApplyTime(new Date());
                    orderShippingPlan.setFlagOrderShippingApplyStatus(1);
                    orderShippingPlan.setFlagOrderShippingApplyId(orderShippingApply.getId());
                    orderShippingPlan.setFlagOrderShippingApplyTime(new Date());
                    orderShippingPlan.setFlagFlowOrderShippingApplyId(flowOrderShippingApply.getId());
                    orderShippingPlanDao.save(orderShippingPlan);
                }
                //发货确认通过后，发货确认的标记回写到正式采购订单
                PurchaseContract order = purchaseContractDao.getOne(orderShippingApplyDetail.getOrderId());
                if(order != null){
                    order.setFlagOrderShippingApplyId(orderShippingApply.getId());
                    order.setFlagOrderShippingApplyStatus(1);
                    order.setFlagOrderShippingApplyTime(new Date());
                    order.setContainerType(detail.getContainerType());
                    order.setContainerQty(detail.getContainerQty());
                    order.setReadyDate(detail.getReadyDate());
                    order.setEtd(detail.getEtd());
                    order.setEta(detail.getEta());
                    purchaseContractDao.save(order);
                    if(StringUtils.isNotBlank(order.getBusinessId())){
                        FlowPurchaseContract flowOrder = flowPurchaseContractDao.findOne(order.getBusinessId());
                        flowOrder.setContainerType(detail.getContainerType());
                        flowOrder.setContainerQty(detail.getContainerQty());
                        flowOrder.setReadyDate(detail.getReadyDate());
                        flowOrder.setEtd(detail.getEtd());
                        flowOrder.setEta(detail.getEta());
                        flowPurchaseContractDao.save(flowOrder);
                    }
                }
            }
        }
    }
}
