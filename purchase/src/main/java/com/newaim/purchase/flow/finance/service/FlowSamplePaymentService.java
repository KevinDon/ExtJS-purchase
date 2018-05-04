package com.newaim.purchase.flow.finance.service;


import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.purchase.Constants;
import com.newaim.purchase.admin.system.entity.Attachment;
import com.newaim.purchase.admin.system.service.AttachmentService;
import com.newaim.purchase.archives.flow.finance.dao.BalanceRefundDao;
import com.newaim.purchase.archives.flow.finance.dao.SamplePaymentDao;
import com.newaim.purchase.archives.flow.finance.entity.BalanceRefund;
import com.newaim.purchase.archives.flow.finance.entity.SamplePayment;
import com.newaim.purchase.archives.flow.purchase.service.SampleOtherDetailService;
import com.newaim.purchase.archives.vendor.service.VendorService;
import com.newaim.purchase.constants.ConstantsAttachment;
import com.newaim.purchase.flow.finance.dao.FlowBalanceRefundDao;
import com.newaim.purchase.flow.finance.dao.FlowSamplePaymentDao;
import com.newaim.purchase.flow.finance.dao.FlowSamplePaymentDetailDao;
import com.newaim.purchase.flow.finance.entity.FlowBalanceRefund;
import com.newaim.purchase.flow.finance.entity.FlowSamplePayment;
import com.newaim.purchase.flow.finance.entity.FlowSamplePaymentDetail;
import com.newaim.purchase.flow.finance.vo.FlowSamplePaymentVo;
import com.newaim.purchase.flow.workflow.service.FlowServiceBase;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.LinkedHashMap;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class FlowSamplePaymentService extends FlowServiceBase {

    @Autowired
    private FlowSamplePaymentDao flowSamplePaymentDao;

    @Autowired
    private FlowSamplePaymentDetailDao flowSamplePaymentDetailDao;

    @Autowired
    private VendorService vendorService;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private FlowSamplePaymentDetailService flowSamplePaymentDetailService;

    @Autowired
    private SampleOtherDetailService sampleOtherDetailService;

    @Autowired
    private FlowBalanceRefundDao flowBalanceRefundDao;

    @Autowired
    private BalanceRefundDao balanceRefundDao;

    @Autowired
    private SamplePaymentDao samplePaymentDao;

    /**
     * 查询所有样品付款信息
     * @param params
     * @param pageNumber
     * @param pageSize
     * @param sort
     * @return
     */
    public Page<FlowSamplePaymentVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<FlowSamplePayment> spec = buildSpecification(params);
        Page<FlowSamplePayment> p = flowSamplePaymentDao.findAll(spec, pageRequest);
        Page<FlowSamplePaymentVo> page = p.map(new Converter<FlowSamplePayment, FlowSamplePaymentVo>() {
            @Override
            public FlowSamplePaymentVo convert(FlowSamplePayment flowSamplePayment) {
                return convertToFlowSamplePaymentVo(flowSamplePayment);
            }
        });
        return page;
    }

    /**
     * entity转换为Vo
     * @param flowSamplePayment
     * @return
     */
    private FlowSamplePaymentVo convertToFlowSamplePaymentVo(FlowSamplePayment flowSamplePayment){
        FlowSamplePaymentVo vo = BeanMapper.map(flowSamplePayment, FlowSamplePaymentVo.class);
        return vo;
    }

    public FlowSamplePayment getFlowSamplePayment(String id){
        return flowSamplePaymentDao.findOne(id);
    }

    /**
     * 根据样品申请Id查找样品付款单
     * @return
     */
    public FlowSamplePayment findBySampleBusinessId(String sampleBusinessId){
        return flowSamplePaymentDao.findBySampleBusinessId(sampleBusinessId);
    }

    /**
     * 根据ID获取样品付款信息
     * @param id
     * @return
     */
    public FlowSamplePaymentVo get(String id){
        FlowSamplePaymentVo vo =convertToFlowSamplePaymentVo(getFlowSamplePayment(id));
        if(StringUtils.isNotBlank(vo.getVendorId())){
            vo.setVendor(vendorService.get(vo.getVendorId()));
        }
        vo.setDetails(flowSamplePaymentDetailService.findDetailsVoByBusinessId(id));
        vo.setOtherDetails(sampleOtherDetailService.findOtherDetailVosBySampleId(vo.getSampleId()));
        vo.setAttachments(attachmentService.listByBusinessId(id));
        return vo;
    }

    /**
     * 动态创建关联
     * @param searchParams
     * @return
     */
    private Specification<FlowSamplePayment> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<FlowSamplePayment> spec = DynamicSpecifications.bySearchFilter(filters.values(), FlowSamplePayment.class);
        return spec;
    }

    @Transactional(rollbackFor = Exception.class)
    public FlowSamplePayment add(FlowSamplePayment flowSamplePayment, List<FlowSamplePaymentDetail> details, List<Attachment> attachments){
        setFlowCreatorInfo(flowSamplePayment);
        setFlowVendorInfo(flowSamplePayment);
        setFlowSamplePaymentPriceInfo(flowSamplePayment, details);
        flowSamplePayment = flowSamplePaymentDao.save(flowSamplePayment);
        saveDetails(flowSamplePayment.getId(), details);
        //保存附件
        attachmentService.save(attachments, ConstantsAttachment.Status.FlowSamplePayment.code, flowSamplePayment.getId());
        return flowSamplePayment;
    }

    @Transactional(rollbackFor = Exception.class)
    public FlowSamplePayment update(FlowSamplePayment flowSamplePayment, List<FlowSamplePaymentDetail> details, List<Attachment> attachments){
        //设置供应商信息
        setFlowVendorInfo(flowSamplePayment);
        setFlowSamplePaymentPriceInfo(flowSamplePayment, details);
        //设置更新时间
        flowSamplePayment.setUpdatedAt(new Date());
        //删除之前明细，重新绑定数据
        flowSamplePaymentDetailDao.deleteByBusinessId(flowSamplePayment.getId());
        saveDetails(flowSamplePayment.getId(),details);
        //保存附件
        attachmentService.save(attachments, ConstantsAttachment.Status.FlowSamplePayment.code, flowSamplePayment.getId());
        return flowSamplePaymentDao.save(flowSamplePayment);
    }

    /**
     * 出纳环节单独保存附件
     * @param flowSamplePayment
     * @param attachments
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public FlowSamplePayment updateFile(FlowSamplePayment flowSamplePayment, List<Attachment> attachments){
        //保存附件
        attachmentService.save(attachments, ConstantsAttachment.Status.FlowSamplePayment.code, flowSamplePayment.getId());
        return flowSamplePaymentDao.save(flowSamplePayment);
    }

    /**
     * 复制另存
     */
    @Transactional(rollbackFor = Exception.class)
    public FlowSamplePayment saveAs(FlowSamplePayment flowSamplePayment, List<FlowSamplePaymentDetail> details, List<Attachment> attachments){
        flowSamplePaymentDao.clear();
        //创建人信息
        setFlowCreatorInfo(flowSamplePayment);
        //设置供应商信息
        setFlowVendorInfo(flowSamplePayment);
        //设置样品费用信息
        setFlowSamplePaymentPriceInfo(flowSamplePayment, details);
        //清理信息
        cleanInfoForSaveAs(flowSamplePayment);
        flowSamplePayment = flowSamplePaymentDao.save(flowSamplePayment);
        saveDetails(flowSamplePayment.getId(), details);
        //保存附件
        attachmentService.save(attachments, ConstantsAttachment.Status.FlowSamplePayment.code, flowSamplePayment.getId());
        return flowSamplePayment;
    }

    /**
     * 保存时建立关联关系
     */
    private void saveDetails(String businessId, List<FlowSamplePaymentDetail> details){
        if(details != null){
            for (int i = 0; i < details.size(); i++) {
                details.get(i).setBusinessId(businessId);
            }
            flowSamplePaymentDetailDao.save(details);
        }
    }


    /**
     * 设置样品金额信息
     */
    private void setFlowSamplePaymentPriceInfo(FlowSamplePayment flowSamplePayment,List<FlowSamplePaymentDetail> details) {
        BigDecimal totalSampleFeeAud = BigDecimal.ZERO;
        BigDecimal totalSampleFeeRmb = BigDecimal.ZERO;
        BigDecimal totalSampleFeeUsd = BigDecimal.ZERO;
        if (details != null) {
            for (FlowSamplePaymentDetail detail : details) {
                if (detail.getSampleFeeAud() != null && detail.getQty() != null) {
                    totalSampleFeeAud = totalSampleFeeAud.add(detail.getSampleFeeAud().multiply(BigDecimal.valueOf(detail.getQty())));
                }
                if (detail.getSampleFeeRmb() != null && detail.getQty() != null) {
                    totalSampleFeeRmb = totalSampleFeeRmb.add(detail.getSampleFeeRmb().multiply(BigDecimal.valueOf(detail.getQty())));
                }
                if (detail.getSampleFeeUsd() != null && detail.getQty() != null) {
                    totalSampleFeeUsd = totalSampleFeeUsd.add(detail.getSampleFeeUsd().multiply(BigDecimal.valueOf(detail.getQty())));
                }
            }
        }
        flowSamplePayment.setTotalSampleFeeAud(totalSampleFeeAud);
        flowSamplePayment.setTotalSampleFeeRmb(totalSampleFeeRmb);
        flowSamplePayment.setTotalSampleFeeUsd(totalSampleFeeUsd);
    }

    /**
     * 冻结
     * @param flowId
     */
    @Transactional(rollbackFor = Exception.class)
    public void hold(String flowId){
        FlowSamplePayment flow = flowSamplePaymentDao.findOne(flowId);
        flow.setHold(Constants.HoldStatus.HOLD.code);
        flowSamplePaymentDao.save(flow);
    }

    /**
     * 解除冻结
     * @param flowId
     */
    @Transactional(rollbackFor = Exception.class)
    public void unHold(String flowId){
        FlowSamplePayment flow = flowSamplePaymentDao.findOne(flowId);
        flow.setHold(Constants.HoldStatus.UN_HOLD.code);
        flowSamplePaymentDao.save(flow);
    }

    /**
     * 作废
     * @param flowId
     */
    @Transactional(rollbackFor = Exception.class)
    public void cancel(String flowId){
        FlowSamplePayment flowSamplePayment = flowSamplePaymentDao.findOne(flowId);
        flowSamplePayment.setStatus(Constants.Status.CANCELED.code);
        flowSamplePaymentDao.save(flowSamplePayment);
        List<SamplePayment> samplePayments = samplePaymentDao.findByBusinessId(flowId);
        if(samplePayments != null && samplePayments.size() > 0){
            for (int i = 0; i < samplePayments.size(); i++) {
                SamplePayment samplePayment = samplePayments.get(i);
                samplePayment.setStatus(Constants.Status.CANCELED.code);
                samplePaymentDao.save(samplePayment);

                List<FlowBalanceRefund> flowBalanceRefunds = flowBalanceRefundDao.findBySamplePaymentId(samplePayment.getId());
                if(flowBalanceRefunds != null && flowBalanceRefunds.size() > 0){
                    for (int j = 0; j < flowBalanceRefunds.size(); j++) {
                        FlowBalanceRefund flowBalanceRefund = flowBalanceRefunds.get(j);
                        flowBalanceRefund.setStatus(Constants.Status.CANCELED.code);
                        flowBalanceRefundDao.save(flowBalanceRefund);
                    }
                }
                List<BalanceRefund> balanceRefunds = balanceRefundDao.findBySamplePaymentId(samplePayment.getId());
                if(balanceRefunds != null && balanceRefunds.size() > 0){
                    for (int j = 0; j < balanceRefunds.size(); j++) {
                        BalanceRefund balanceRefund = balanceRefunds.get(j);
                        balanceRefund.setStatus(Constants.Status.CANCELED.code);
                        balanceRefundDao.save(balanceRefund);
                    }
                }
            }
        }
    }

    /**
     * 删除，改为删除状态
     * @param flowSamplePayment
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(FlowSamplePayment flowSamplePayment){
        flowSamplePayment.setUpdatedAt(new Date());
        flowSamplePayment.setStatus(Constants.Status.DELETED.code);
        flowSamplePaymentDao.save(flowSamplePayment);
    }

}
