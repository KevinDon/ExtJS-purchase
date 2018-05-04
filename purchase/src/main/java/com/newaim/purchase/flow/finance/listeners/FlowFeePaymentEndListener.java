package com.newaim.purchase.flow.finance.listeners;

import com.newaim.core.mapper.BeanMapper;
import com.newaim.purchase.Constants;
import com.newaim.purchase.archives.finance.dao.BankAccountDao;
import com.newaim.purchase.archives.finance.entity.BankAccount;
import com.newaim.purchase.archives.finance.service.BankAccountService;
import com.newaim.purchase.archives.flow.finance.dao.BalanceRefundDao;
import com.newaim.purchase.archives.flow.finance.dao.FeePaymentDao;
import com.newaim.purchase.archives.flow.finance.dao.FeeRegisterDao;
import com.newaim.purchase.archives.flow.finance.entity.BalanceRefund;
import com.newaim.purchase.archives.flow.finance.entity.FeePayment;
import com.newaim.purchase.archives.flow.finance.entity.FeeRegister;
import com.newaim.purchase.archives.flow.purchase.dao.PurchaseBalanceRefundUnionDao;
import com.newaim.purchase.archives.flow.purchase.dao.PurchaseContractDao;
import com.newaim.purchase.archives.flow.purchase.entity.PurchaseBalanceRefundUnion;
import com.newaim.purchase.archives.flow.purchase.entity.PurchaseContract;
import com.newaim.purchase.flow.finance.dao.FlowBalanceRefundDao;
import com.newaim.purchase.flow.finance.dao.FlowFeeRegisterDao;
import com.newaim.purchase.flow.finance.entity.FlowBalanceRefund;
import com.newaim.purchase.flow.finance.entity.FlowFeePayment;
import com.newaim.purchase.flow.finance.entity.FlowFeeRegister;
import com.newaim.purchase.flow.finance.service.FlowFeePaymentService;
import com.newaim.purchase.flow.purchase.dao.FlowPurchaseContractDao;
import com.newaim.purchase.flow.purchase.entity.FlowPurchaseContract;
import com.newaim.purchase.flow.workflow.listeners.CommonEndListener;
import org.activiti.engine.delegate.DelegateExecution;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 费用支付正常流程结束监听器
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FlowFeePaymentEndListener extends CommonEndListener {

    @Autowired
    private FlowFeePaymentService flowFeePaymentService;

    @Autowired
    private FeePaymentDao feePaymentDao;

    @Autowired
    private FeeRegisterDao feeRegisterDao;

    @Autowired
    private FlowFeeRegisterDao flowFeeRegisterDao;

    @Autowired
    private PurchaseContractDao purchaseContractDao;

    @Autowired
    private FlowPurchaseContractDao flowPurchaseContractDao;

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private BankAccountDao bankAccountDao;

    @Autowired
    private BalanceRefundDao balanceRefundDao;

    @Autowired
    private FlowBalanceRefundDao flowBalanceRefundDao;

    @Autowired
    private PurchaseBalanceRefundUnionDao purchaseBalanceRefundUnionDao;

    @Override
    public void notify(DelegateExecution execution)throws RuntimeException{
        String businessId = execution.getProcessBusinessKey();
        //1. 通过表单id获取相关对象
        FlowFeePayment flowFeePayment = flowFeePaymentService.getFlowFeePayment(businessId);
        //2. 保存相关数据到正式业务数据表
        FeePayment feePayment = BeanMapper.map(flowFeePayment,FeePayment.class);
        feePayment.setId(null);
        feePayment.setBusinessId(flowFeePayment.getId());
        feePayment.setFlowStatus(Constants.FlowStatus.PASS.code);
        feePayment.setEndTime(new Date());
        feePayment.setHold(Constants.HoldStatus.UN_HOLD.code);
        feePaymentDao.save(feePayment);

        //费用登记的支付状态改为已支付
        FeeRegister feeRegister = feeRegisterDao.findOne(feePayment.getFeeRegisterId());
        if(feeRegister != null){
            feeRegister.setPaymentStatus(Constants.FeePaymentStatus.COMPLETE.code);
            feeRegisterDao.save(feeRegister);
        }
        //费用登记流程，支付状态改为已支付
        FlowFeeRegister flowFeeRegister =flowFeeRegisterDao.findOne(feeRegister.getBusinessId());
        if (flowFeeRegister != null){
            flowFeeRegister.setPaymentStatus(Constants.FeePaymentStatus.COMPLETE.code);
            flowFeeRegisterDao.save(flowFeeRegister);

            //供应商押金类型时
            if(Constants.FeeType.VENDOR_DEPOSIT.code.equals(flowFeeRegister.getFeeType())){
                BankAccount bankAccount = bankAccountService.getBankAccountByVendorId(flowFeeRegister.getVendorId());
                if(bankAccount != null){
                    bankAccount.setDepositType(1);
                    bankAccount.setDepositRate(BigDecimal.ZERO);
                    bankAccountDao.save(bankAccount);
                }
            }

            //订单相关
            if(StringUtils.isNotBlank(flowFeeRegister.getOrderId())){
                PurchaseContract order = purchaseContractDao.getOne(flowFeeRegister.getOrderId());
                if(order != null){
                    //尾款支付
                    if(Constants.FeeType.CONTRACT_BALANCE.code.equals(flowFeeRegister.getFeeType())){
                        order.setFlagFeePaymentId(feePayment.getId());
                        order.setFlagFeePaymentStatus(1);
                        order.setFlagFeePaymentTime(new Date());
                        //设置冲销单据的扣款状态
                        List<PurchaseBalanceRefundUnion> unions =  purchaseBalanceRefundUnionDao.findByPurchaseContractBusinessId(order.getBusinessId());
                        if(unions != null && unions.size() > 0){
                            for (int i = 0; i < unions.size(); i++) {
                                PurchaseBalanceRefundUnion union = unions.get(i);
                                BalanceRefund balanceRefund = balanceRefundDao.findOne(union.getBalanceRefundId());
                                balanceRefund.setChargebackStatus(1);
                                balanceRefundDao.save(balanceRefund);
                                FlowBalanceRefund flowBalanceRefund = flowBalanceRefundDao.findOne(balanceRefund.getBusinessId());
                                if(flowBalanceRefund != null){
                                    flowBalanceRefund.setChargebackStatus(1);
                                    flowBalanceRefundDao.save(flowBalanceRefund);
                                }
                            }
                        }

                    }
                    //电放费
                    if(Constants.FeeType.ELECTRONIC_PROCESSING_FEE.code.equals(flowFeeRegister.getFeeType())){
                        order.setElectronicProcessingFeeAud(flowFeeRegister.getTotalPriceAud());
                        order.setElectronicProcessingFeeRmb(flowFeeRegister.getTotalPriceRmb());
                        order.setElectronicProcessingFeeUsd(flowFeeRegister.getTotalPriceUsd());
                        FlowPurchaseContract flowOrder = flowPurchaseContractDao.findOne(order.getBusinessId());
                        if(flowOrder != null){
                            flowOrder.setElectronicProcessingFeeAud(flowFeeRegister.getTotalPriceAud());
                            flowOrder.setElectronicProcessingFeeRmb(flowFeeRegister.getTotalPriceRmb());
                            flowOrder.setElectronicProcessingFeeUsd(flowFeeRegister.getTotalPriceUsd());
                            flowPurchaseContractDao.save(flowOrder);
                        }
                    }
                    purchaseContractDao.save(order);
                }
            }
        }

    }

}
