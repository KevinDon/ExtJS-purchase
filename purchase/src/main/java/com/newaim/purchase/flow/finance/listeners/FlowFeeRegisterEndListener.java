package com.newaim.purchase.flow.finance.listeners;

import com.newaim.core.mapper.BeanMapper;
import com.newaim.purchase.Constants;
import com.newaim.purchase.archives.flow.finance.dao.FeeRegisterDao;
import com.newaim.purchase.archives.flow.finance.dao.FeeRegisterDetailDao;
import com.newaim.purchase.archives.flow.finance.entity.FeeRegister;
import com.newaim.purchase.archives.flow.finance.entity.FeeRegisterDetail;
import com.newaim.purchase.flow.finance.entity.FlowFeeRegister;
import com.newaim.purchase.flow.finance.entity.FlowFeeRegisterDetail;
import com.newaim.purchase.flow.finance.service.FlowFeeRegisterDetailService;
import com.newaim.purchase.flow.finance.service.FlowFeeRegisterService;
import com.newaim.purchase.flow.workflow.listeners.CommonEndListener;
import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class FlowFeeRegisterEndListener extends CommonEndListener {

    @Autowired
    private FlowFeeRegisterService flowFeeRegisterService;

    @Autowired
    private FlowFeeRegisterDetailService flowFeeRegisterDetailService;

    @Autowired
    private FeeRegisterDao feeRegisterDao;

    @Autowired
    private FeeRegisterDetailDao feeRegisterDetailDao;


    @Override
    public void notify(DelegateExecution execution)throws RuntimeException{
        String businessId = execution.getProcessBusinessKey();
        //1. 通过表单id获取相关对象
        FlowFeeRegister flowFeeRegister = flowFeeRegisterService.getFlowFeeRegister(businessId);
        List<FlowFeeRegisterDetail> details = flowFeeRegisterDetailService.findDetailsByBusinessId(businessId);
        //2. 保存相关数据到正式业务数据表
        FeeRegister feeRegister = BeanMapper.map(flowFeeRegister,FeeRegister.class);
        feeRegister.setId(null);
        feeRegister.setBusinessId(flowFeeRegister.getId());
        feeRegister.setPaymentStatus(Constants.FeePaymentStatus.UNPAID.code);
        feeRegister.setFlowStatus(Constants.FlowStatus.PASS.code);
        feeRegister.setEndTime(new Date());
        feeRegisterDao.save(feeRegister);

        if (details!=null){
            for (FlowFeeRegisterDetail detail : details){
                //2.2 拷贝明细数据到业务表
                FeeRegisterDetail feeRegisterDetail = BeanMapper.map(detail,FeeRegisterDetail.class);
                feeRegisterDetail.setFeeRegisterId(feeRegister.getId());
                feeRegisterDetailDao.save(feeRegisterDetail);
            }
        }
    }
}
