package com.newaim.purchase.flow.finance.listeners;

import com.google.common.collect.Lists;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.purchase.Constants;
import com.newaim.purchase.archives.flow.finance.dao.SamplePaymentDao;
import com.newaim.purchase.archives.flow.finance.dao.SamplePaymentDetailDao;
import com.newaim.purchase.archives.flow.finance.entity.SamplePayment;
import com.newaim.purchase.archives.flow.finance.entity.SamplePaymentDetail;
import com.newaim.purchase.desktop.systemtools.service.RateControlService;
import com.newaim.purchase.desktop.systemtools.vo.RateControlVo;
import com.newaim.purchase.flow.finance.dao.FlowBalanceRefundDao;
import com.newaim.purchase.flow.finance.dao.FlowBalanceRefundDetailDao;
import com.newaim.purchase.flow.finance.entity.FlowBalanceRefund;
import com.newaim.purchase.flow.finance.entity.FlowBalanceRefundDetail;
import com.newaim.purchase.flow.finance.entity.FlowSamplePayment;
import com.newaim.purchase.flow.finance.entity.FlowSamplePaymentDetail;
import com.newaim.purchase.flow.finance.service.FlowSamplePaymentDetailService;
import com.newaim.purchase.flow.finance.service.FlowSamplePaymentService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 样品付款申请正常结束监听器
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FlowSamplePaymentEndListener implements Serializable, ExecutionListener {

    @Autowired
    private FlowSamplePaymentService flowSamplePaymentService;

    @Autowired
    private FlowSamplePaymentDetailService flowSamplePaymentDetailService;

    @Autowired
    private SamplePaymentDao samplePaymentDao;

    @Autowired
    private SamplePaymentDetailDao samplePaymentDetailDao;

    @Autowired
    private FlowBalanceRefundDao flowBalanceRefundDao;

    @Autowired
    private FlowBalanceRefundDetailDao flowBalanceRefundDetailDao;

    @Autowired
    private RateControlService rateControlService;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        String businessId = execution.getProcessBusinessKey();
        //1. 通过表单id获取相关对象
        FlowSamplePayment flowSamplePayment = flowSamplePaymentService.getFlowSamplePayment(businessId);
        List<FlowSamplePaymentDetail> details = flowSamplePaymentDetailService.findDetailsByBusinessId(businessId);
        //2. 保存相关数据到正式业务数据表
        SamplePayment samplePayment = BeanMapper.map(flowSamplePayment,SamplePayment.class);
        samplePayment.setId(null);
        samplePayment.setBusinessId(flowSamplePayment.getId());
        samplePayment.setFlowStatus(Constants.FlowStatus.PASS.code);
        samplePayment.setEndTime(new Date());
        samplePaymentDao.save(samplePayment);

        BigDecimal totalFeeAud = BigDecimal.ZERO;
        BigDecimal totalFeeRmb = BigDecimal.ZERO;
        BigDecimal totalFeeUsd = BigDecimal.ZERO;
        RateControlVo rate = rateControlService.listNewRate();
        List<FlowBalanceRefundDetail> balanceRefundDetails = Lists.newArrayList();
        if (details!=null){
            for (FlowSamplePaymentDetail detail : details){
                //2.2 拷贝明细数据到业务表
                SamplePaymentDetail samplePaymentDetail = BeanMapper.map(detail,SamplePaymentDetail.class);
                samplePaymentDetail.setSamplePaymentId(samplePayment.getId());
                samplePaymentDetailDao.save(samplePaymentDetail);
                if(samplePaymentDetail.getSampleFeeRefund() == 1 && samplePaymentDetail.getSampleFeeAud().doubleValue() > 0){
                    BigDecimal qty = BigDecimal.valueOf(samplePaymentDetail.getQty());
                    BigDecimal valueAud = BigDecimal.ZERO;
                    BigDecimal valueRmb = BigDecimal.ZERO;
                    BigDecimal valueUsd = BigDecimal.ZERO;
                    if(flowSamplePayment.getCurrency().intValue() == 1){
                        valueAud = samplePaymentDetail.getSampleFeeAud().multiply(qty);
                        valueRmb = samplePaymentDetail.getSampleFeeAud().multiply(rate.getRateAudToRmb()).multiply(qty);
                        valueUsd = samplePaymentDetail.getSampleFeeAud().multiply(rate.getRateAudToUsd()).multiply(qty);
                    }else if(flowSamplePayment.getCurrency().intValue() == 2){
                        valueAud = samplePaymentDetail.getSampleFeeRmb().divide(rate.getRateAudToRmb(), 4).multiply(qty);
                        valueRmb = samplePaymentDetail.getSampleFeeRmb().multiply(qty);
                        valueUsd = samplePaymentDetail.getSampleFeeRmb().divide(rate.getRateAudToRmb(), 4).multiply(rate.getRateAudToUsd()).multiply(qty);
                    }else if(flowSamplePayment.getCurrency().intValue() == 3){
                        valueAud = samplePaymentDetail.getSampleFeeUsd().divide(rate.getRateAudToUsd(), 4).multiply(qty);
                        valueRmb = samplePaymentDetail.getSampleFeeUsd().divide(rate.getRateAudToUsd(), 4).multiply(rate.getRateAudToRmb()).multiply(qty);
                        valueUsd = samplePaymentDetail.getSampleFeeUsd().multiply(qty);
                    }
                    totalFeeAud = totalFeeAud.add(valueAud);
                    totalFeeRmb = totalFeeRmb.add(valueRmb);
                    totalFeeUsd = totalFeeUsd.add(valueUsd);
                    //样品可退
                    FlowBalanceRefundDetail flowBalanceRefundDetail = new FlowBalanceRefundDetail();
                    flowBalanceRefundDetail.setCurrency(samplePaymentDetail.getCurrency());
                    flowBalanceRefundDetail.setRateAudToRmb(samplePaymentDetail.getRateAudToRmb());
                    flowBalanceRefundDetail.setRateAudToUsd(samplePaymentDetail.getRateAudToUsd());
                    flowBalanceRefundDetail.setPriceAud(samplePaymentDetail.getSampleFeeAud());
                    flowBalanceRefundDetail.setPriceRmb(samplePaymentDetail.getSampleFeeRmb());
                    flowBalanceRefundDetail.setPriceUsd(samplePaymentDetail.getSampleFeeUsd());
                    flowBalanceRefundDetail.setProductId(samplePaymentDetail.getProductId());
                    flowBalanceRefundDetail.setSku(samplePaymentDetail.getSku());
                    flowBalanceRefundDetail.setExpectedQty(samplePaymentDetail.getQty());
                    flowBalanceRefundDetail.setReceivedQty(0);
                    flowBalanceRefundDetail.setDiffQty(flowBalanceRefundDetail.getExpectedQty() - flowBalanceRefundDetail.getReceivedQty());
                    flowBalanceRefundDetail.setDiffAud(valueAud);
                    flowBalanceRefundDetail.setDiffRmb(valueRmb);
                    flowBalanceRefundDetail.setDiffUsd(valueUsd);
                    balanceRefundDetails.add(flowBalanceRefundDetail);
                }
            }
        }
        if(balanceRefundDetails.size() > 0){
            //差额退款单
            FlowBalanceRefund flowBalanceRefund = new FlowBalanceRefund();
            flowBalanceRefund.setTotalFeeAud(totalFeeAud);
            flowBalanceRefund.setTotalFeeRmb(totalFeeRmb);
            flowBalanceRefund.setTotalFeeUsd(totalFeeUsd);
            flowBalanceRefund.setVendorId(samplePayment.getVendorId());
            flowBalanceRefund.setVendorCnName(samplePayment.getVendorCnName());
            flowBalanceRefund.setVendorEnName(samplePayment.getVendorEnName());
            flowBalanceRefund.setSamplePaymentId(samplePayment.getId());
            flowBalanceRefund.setSamplePaymentBusinessId(businessId);
            flowBalanceRefund.setCurrency(samplePayment.getCurrency());
            flowBalanceRefund.setRateAudToRmb(rate.getRateAudToRmb());
            flowBalanceRefund.setRateAudToUsd(rate.getRateAudToUsd());
            flowBalanceRefund.setType(3);
            flowBalanceRefund.setCreatedAt(new Date());
            flowBalanceRefund.setCreatorId(samplePayment.getCreatorId());
            flowBalanceRefund.setCreatorCnName(samplePayment.getCreatorCnName());
            flowBalanceRefund.setCreatorEnName(samplePayment.getCreatorEnName());
            flowBalanceRefund.setDepartmentId(samplePayment.getDepartmentId());
            flowBalanceRefund.setDepartmentCnName(samplePayment.getDepartmentCnName());
            flowBalanceRefund.setDepartmentEnName(samplePayment.getDepartmentEnName());
            flowBalanceRefund.setStatus(Constants.Status.DRAFT.code);
            flowBalanceRefund.setHold(Constants.HoldStatus.UN_HOLD.code);
            flowBalanceRefund.setChargebackReason("样品扣款费");
            flowBalanceRefundDao.save(flowBalanceRefund);
            for (int i = 0; i < balanceRefundDetails.size(); i++) {
                balanceRefundDetails.get(i).setBusinessId(flowBalanceRefund.getId());
            }
            flowBalanceRefundDetailDao.save(balanceRefundDetails);
        }
    }
}
