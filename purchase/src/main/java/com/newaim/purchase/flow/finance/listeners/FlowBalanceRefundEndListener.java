package com.newaim.purchase.flow.finance.listeners;


import com.newaim.core.mapper.BeanMapper;
import com.newaim.purchase.Constants;
import com.newaim.purchase.archives.flow.finance.dao.BalanceRefundDao;
import com.newaim.purchase.archives.flow.finance.dao.BalanceRefundDetailDao;
import com.newaim.purchase.archives.flow.finance.entity.BalanceRefund;
import com.newaim.purchase.archives.flow.finance.entity.BalanceRefundDetail;
import com.newaim.purchase.archives.flow.purchase.dao.PurchaseContractDao;
import com.newaim.purchase.archives.flow.purchase.entity.PurchaseContract;
import com.newaim.purchase.flow.finance.entity.FlowBalanceRefund;
import com.newaim.purchase.flow.finance.entity.FlowBalanceRefundDetail;
import com.newaim.purchase.flow.finance.service.FlowBalanceRefundDetailService;
import com.newaim.purchase.flow.finance.service.FlowBalanceRefundService;
import com.newaim.purchase.flow.workflow.listeners.CommonEndListener;
import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class FlowBalanceRefundEndListener extends CommonEndListener {

    @Autowired
    private FlowBalanceRefundService flowBalanceRefundService;

    @Autowired
    private FlowBalanceRefundDetailService flowBalanceRefundDetailService;

    @Autowired
    private BalanceRefundDao balanceRefundDao;

    @Autowired
    private PurchaseContractDao purchaseContractDao;

    @Autowired
    private BalanceRefundDetailDao balanceRefundDetailDao;

    @Override
    public void notify(DelegateExecution execution)throws RuntimeException{
        String businessId = execution.getProcessBusinessKey();
        //1. 通过表单id获取相关对象
        FlowBalanceRefund flowBalanceRefund = flowBalanceRefundService.getFlowBalanceRefund(businessId);
        List<FlowBalanceRefundDetail> details = flowBalanceRefundDetailService.findDetailsByBusinessId(businessId);
        //2. 保存相关数据到正式业务数据表
        BalanceRefund balanceRefund = BeanMapper.map(flowBalanceRefund,BalanceRefund.class);
        balanceRefund.setId(null);
        balanceRefund.setBusinessId(flowBalanceRefund.getId());
        balanceRefund.setChargebackStatus(2);
        balanceRefund.setFlowStatus(Constants.FlowStatus.PASS.code);
        balanceRefund.setEndTime(new Date());
        balanceRefundDao.save(balanceRefund);
        //设置差额退款通过后在订单的标记
        if(balanceRefund.getType() == 1){
            PurchaseContract order = purchaseContractDao.findOne(balanceRefund.getOrderId());
            if(order != null){
                order.setFlagBalanceRefundId(balanceRefund.getId());
                order.setFlagBalanceRefundStatus(1);
                order.setFlagBalanceRefundTime(new Date());
                purchaseContractDao.save(order);
            }
        }
        if (details!=null){
            for (FlowBalanceRefundDetail detail : details){
                //2.2 拷贝明细数据到业务表
                BalanceRefundDetail balanceRefundDetail = BeanMapper.map(detail,BalanceRefundDetail.class);
                balanceRefundDetail.setBalanceRefundId(balanceRefund.getId());
                balanceRefundDetailDao.save(balanceRefundDetail);
            }
        }
    }
}
