package com.newaim.purchase.flow.finance.listeners;


import com.newaim.core.mapper.BeanMapper;
import com.newaim.purchase.Constants;
import com.newaim.purchase.archives.flow.finance.dao.PurchaseContractDepositDao;
import com.newaim.purchase.archives.flow.finance.entity.PurchaseContractDeposit;
import com.newaim.purchase.archives.flow.purchase.dao.PurchaseContractDao;
import com.newaim.purchase.archives.flow.purchase.entity.PurchaseContract;
import com.newaim.purchase.flow.finance.entity.FlowPurchaseContractDeposit;
import com.newaim.purchase.flow.finance.service.FlowPurchaseContractDepositService;
import com.newaim.purchase.flow.purchase.dao.FlowPurchaseContractDao;
import com.newaim.purchase.flow.purchase.entity.FlowPurchaseContract;
import com.newaim.purchase.flow.workflow.listeners.CommonEndListener;
import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 合同定金正常流程结束监听器
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FlowPurchaseContractDepositEndListener extends CommonEndListener {

    @Autowired
    private FlowPurchaseContractDepositService flowPurchaseContractDepositService;

    @Autowired
    private PurchaseContractDepositDao purchaseContractDepositDao;

    @Autowired
    private PurchaseContractDao purchaseContractDao;

    @Autowired
    private FlowPurchaseContractDao flowPurchaseContractDao;

    @Override
    public void notify(DelegateExecution execution)throws RuntimeException{
        String businessId = execution.getProcessBusinessKey();
        //1. 通过表单id获取相关对象
        FlowPurchaseContractDeposit flowPurchaseContractDeposit = flowPurchaseContractDepositService.getFlowPurchaseContractDeposit(businessId);
        //2. 保存相关数据到正式业务数据表
        PurchaseContractDeposit pcd = BeanMapper.map(flowPurchaseContractDeposit,PurchaseContractDeposit.class);
        pcd.setId(null);
        pcd.setBusinessId(flowPurchaseContractDeposit.getId());
        pcd.setFlowStatus(Constants.FlowStatus.PASS.code);
        pcd.setEndTime(new Date());
        purchaseContractDepositDao.save(pcd);
        //合同定金通过后，在采购合同标记合同定金通过，实收总定金回写到正式采购合同
        PurchaseContract order = purchaseContractDao.findOne(pcd.getOrderId());
        FlowPurchaseContract flowOrder = flowPurchaseContractDao.findOne(order.getBusinessId());
        if(order != null){
            order.setPaymentRateAudToRmb(pcd.getPaymentRateAudToRmb());
            order.setPaymentRateAudToUsd(pcd.getPaymentRateAudToUsd());
            order.setPaymentDepositAud(pcd.getPaymentAud());
            order.setPaymentDepositRmb(pcd.getPaymentRmb());
            order.setPaymentDepositUsd(pcd.getPaymentUsd());
            order.setFlagContractDepositId(pcd.getId());
            order.setFlagContractDepositStatus(1);
            order.setFlagContractDepositTime(new Date());
            purchaseContractDao.save(order);
        }
        //合同定金通过后，实收总定金、汇率回写到采购合同流程
        if (flowOrder !=null){
            flowOrder.setPaymentRateAudToRmb(pcd.getPaymentRateAudToRmb());
            flowOrder.setPaymentRateAudToUsd(pcd.getPaymentRateAudToUsd());
            flowOrder.setPaymentDepositAud(pcd.getPaymentAud());
            flowOrder.setPaymentDepositRmb(pcd.getPaymentRmb());
            flowOrder.setPaymentDepositUsd(pcd.getPaymentUsd());
            flowPurchaseContractDao.save(flowOrder);
        }
    }
}
