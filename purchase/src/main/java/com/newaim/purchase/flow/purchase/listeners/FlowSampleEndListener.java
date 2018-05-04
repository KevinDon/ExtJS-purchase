package com.newaim.purchase.flow.purchase.listeners;

import com.newaim.core.mapper.BeanMapper;
import com.newaim.purchase.Constants;
import com.newaim.purchase.archives.finance.entity.BankAccount;
import com.newaim.purchase.archives.finance.service.BankAccountService;
import com.newaim.purchase.archives.flow.purchase.dao.SampleDao;
import com.newaim.purchase.archives.flow.purchase.dao.SampleDetailDao;
import com.newaim.purchase.archives.flow.purchase.dao.SampleOtherDetailDao;
import com.newaim.purchase.archives.flow.purchase.entity.Sample;
import com.newaim.purchase.archives.flow.purchase.entity.SampleDetail;
import com.newaim.purchase.archives.flow.purchase.entity.SampleOtherDetail;
import com.newaim.purchase.archives.product.entity.Product;
import com.newaim.purchase.archives.product.service.ProductService;
import com.newaim.purchase.desktop.message.helper.Msg;
import com.newaim.purchase.flow.finance.dao.FlowSamplePaymentDao;
import com.newaim.purchase.flow.finance.dao.FlowSamplePaymentDetailDao;
import com.newaim.purchase.flow.finance.entity.FlowSamplePayment;
import com.newaim.purchase.flow.finance.entity.FlowSamplePaymentDetail;
import com.newaim.purchase.flow.inspection.dao.FlowSampleQcDao;
import com.newaim.purchase.flow.inspection.dao.FlowSampleQcDetailDao;
import com.newaim.purchase.flow.inspection.entity.FlowSampleQc;
import com.newaim.purchase.flow.inspection.entity.FlowSampleQcDetail;
import com.newaim.purchase.flow.purchase.entity.FlowSample;
import com.newaim.purchase.flow.purchase.entity.FlowSampleDetail;
import com.newaim.purchase.flow.purchase.entity.FlowSampleOtherDetail;
import com.newaim.purchase.flow.purchase.service.FlowSampleDetailService;
import com.newaim.purchase.flow.purchase.service.FlowSampleOtherDetailService;
import com.newaim.purchase.flow.purchase.service.FlowSampleService;
import com.newaim.purchase.flow.workflow.listeners.CommonEndListener;
import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *样品申请正常结束监听器.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FlowSampleEndListener extends CommonEndListener {


    @Autowired
    private FlowSampleService flowSampleService;

    @Autowired
    private FlowSampleDetailService flowSampleDetailService;

    @Autowired
    private FlowSampleOtherDetailService flowSampleOtherDetailService;

    @Autowired
    private SampleDao sampleDao;

    @Autowired
    private SampleDetailDao sampleDetailDao;

    @Autowired
    private SampleOtherDetailDao sampleOtherDetailDao;

    @Autowired
    private FlowSampleQcDao flowSampleQcDao;

    @Autowired
    private FlowSampleQcDetailDao flowSampleQcDetailDao;

    @Autowired
    private ProductService productService;

    @Autowired
    private FlowSamplePaymentDao flowSamplePaymentDao;

    @Autowired
    private FlowSamplePaymentDetailDao flowSamplePaymentDetailDao;

    @Autowired
    private BankAccountService bankAccountService;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        String businessId = execution.getProcessBusinessKey();
        //1. 通过表单id获取相关对象
        FlowSample flowSample = flowSampleService.getFlowSample(businessId);
        List<FlowSampleDetail> details = flowSampleDetailService.findDetailsByBusinessId(businessId);
        List<FlowSampleOtherDetail> otherDetails = flowSampleOtherDetailService.findOtherDetailsByBusinessId(businessId);
        //2. 保存相关数据到正式业务数据表
        Sample sample = BeanMapper.map(flowSample, Sample.class);
        sample.setId(null);
        sample.setEndTime(new Date());
        sample.setBusinessId(businessId);
        sample.setFlowStatus(Constants.FlowStatus.PASS.code);
        sampleDao.save(sample);
        //3.当样品申请通过后，自动生成样品质检单
        FlowSampleQc flowSampleQc = new FlowSampleQc();
        flowSampleQc.setVendorId(flowSample.getVendorId());
        flowSampleQc.setVendorCnName(flowSample.getVendorCnName());
        flowSampleQc.setVendorEnName(flowSample.getVendorEnName());
        flowSampleQc.setStatus(Constants.Status.DRAFT.code);
        flowSampleQc.setCreatedAt(new Date());
        flowSampleQc.setFlowStatus(null);
        flowSampleQc.setCreatorId(flowSample.getCreatorId());
        flowSampleQc.setCreatorCnName(flowSample.getCreatorCnName());
        flowSampleQc.setCreatorEnName(flowSample.getCreatorEnName());
        flowSampleQc.setDepartmentId(flowSample.getDepartmentId());
        flowSampleQc.setDepartmentCnName(flowSample.getDepartmentCnName());
        flowSampleQc.setDepartmentEnName(flowSample.getDepartmentEnName());
        flowSampleQc.setHold(Constants.HoldStatus.UN_HOLD.code);
        flowSampleQcDao.save(flowSampleQc);
        if(details != null && details.size() > 0){
            for (FlowSampleDetail detail: details) {
                //3.1样品申请流程通过，复制样品申请流程的数据
                SampleDetail sampleDetail = BeanMapper.map(detail, SampleDetail.class);
                sampleDetail.setId(null);
                sampleDetail.setSampleId(sample.getId());
                sampleDetailDao.save(sampleDetail);
                //3.2样品质检的明细复制样品申请明细
                FlowSampleQcDetail flowSampleQcDetail = new FlowSampleQcDetail();
                flowSampleQcDetail.setBusinessId(flowSampleQc.getId());
                flowSampleQcDetail.setProductId(detail.getProductId());
                flowSampleQcDetail.setPlace(detail.getSampleReceiver());
                Product product = productService.getProduct(flowSampleQcDetail.getProductId());
                flowSampleQcDetail.setProductName(product.getName());
                flowSampleQcDetail.setSku(product.getSku());
                flowSampleQcDetailDao.save(flowSampleQcDetail);
            }
        }
        if(otherDetails != null && otherDetails.size() > 0){
            for (FlowSampleOtherDetail otherDetail : otherDetails) {
                SampleOtherDetail sampleOtherDetail = BeanMapper.map(otherDetail, SampleOtherDetail.class);
                sampleOtherDetail.setId(null);
                sampleOtherDetail.setSampleId(sample.getId());
                sampleOtherDetailDao.save(sampleOtherDetail);
            }
        }
        Msg.send(flowSample.getCreatorId(),"已成功创建样品检验单","已成功创建样品检验单");
        //4.当样品申请通过后，自动生成样品付款单
        if (flowSample.getTotalSampleFeeAud().intValue() > 0){
            FlowSamplePayment flowSamplePayment = new FlowSamplePayment();
            flowSamplePayment.setSampleId(sample.getId());
            flowSamplePayment.setSampleBusinessId(businessId);
            BankAccount bankAccount = bankAccountService.getBankAccountByVendorId(flowSample.getVendorId());
            flowSamplePayment.setBeneficiary(bankAccount.getCompanyEnName());
            flowSamplePayment.setBeneficiaryBank(bankAccount.getBeneficiaryBank());
            flowSamplePayment.setBankAccount(bankAccount.getBankAccount());
            flowSamplePayment.setSwiftCode(bankAccount.getSwiftCode());
            flowSamplePayment.setCnaps(bankAccount.getCnaps());
            //设置样品付款单总金额、币种
            flowSamplePayment.setCurrency(flowSample.getCurrency());
            flowSamplePayment.setRateAudToRmb(flowSample.getRateAudToRmb());
            flowSamplePayment.setRateAudToUsd(flowSample.getRateAudToUsd());
            flowSamplePayment.setTotalSampleFeeAud(flowSample.getTotalSampleFeeAud());
            flowSamplePayment.setTotalSampleFeeRmb(flowSample.getTotalSampleFeeRmb());
            flowSamplePayment.setTotalSampleFeeUsd(flowSample.getTotalSampleFeeUsd());
            flowSamplePayment.setVendorId(flowSample.getVendorId());
            flowSamplePayment.setVendorCnName(flowSample.getVendorCnName());
            flowSamplePayment.setVendorEnName(flowSample.getVendorEnName());
            flowSamplePayment.setCreatedAt(new Date());
            flowSamplePayment.setStatus(Constants.Status.DRAFT.code);
            flowSamplePayment.setFlowStatus(null);
            flowSamplePayment.setCreatorId(flowSample.getCreatorId());
            flowSamplePayment.setCreatorCnName(flowSample.getCreatorCnName());
            flowSamplePayment.setCreatorEnName(flowSample.getCreatorEnName());
            flowSamplePayment.setDepartmentId(flowSample.getDepartmentId());
            flowSamplePayment.setDepartmentCnName(flowSample.getDepartmentCnName());
            flowSamplePayment.setDepartmentEnName(flowSample.getDepartmentEnName());
            flowSamplePayment.setHold(Constants.HoldStatus.UN_HOLD.code);
            flowSamplePayment.setPaymentTotalSampleFeeAud(BigDecimal.ZERO);
            flowSamplePayment.setPaymentTotalSampleFeeRmb(BigDecimal.ZERO);
            flowSamplePayment.setPaymentTotalSampleFeeUsd(BigDecimal.ZERO);
            flowSamplePaymentDao.save(flowSamplePayment);
            if (details != null){
                for (FlowSampleDetail detail: details) {
                    //4.1样品付款明细复制对应样品申请的
                    FlowSamplePaymentDetail flowSamplePaymentDetail = new FlowSamplePaymentDetail();
                    flowSamplePaymentDetail.setProductId(detail.getProductId());
                    flowSamplePaymentDetail.setBusinessId(flowSamplePayment.getId());
                    flowSamplePaymentDetail.setSampleFeeAud(detail.getSampleFeeAud());
                    flowSamplePaymentDetail.setSampleFeeRmb(detail.getSampleFeeRmb());
                    flowSamplePaymentDetail.setSampleFeeUsd(detail.getSampleFeeUsd());
                    flowSamplePaymentDetail.setCurrency(detail.getCurrency());
                    flowSamplePaymentDetail.setQty(detail.getQty());
                    flowSamplePaymentDetail.setSku(detail.getSku());
                    flowSamplePaymentDetail.setRateAudToRmb(detail.getRateAudToRmb());
                    flowSamplePaymentDetail.setRateAudToUsd(detail.getRateAudToUsd());
                    flowSamplePaymentDetail.setSampleFeeRefund(detail.getSampleFeeRefund());
                    flowSamplePaymentDetailDao.save(flowSamplePaymentDetail);
                }
            }
            Msg.send(flowSample.getCreatorId(),"已成功创建样品付款单","已成功创建样品付款单");
        }
    }
}
