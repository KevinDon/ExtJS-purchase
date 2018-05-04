package com.newaim.purchase.flow.finance.service;

import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.purchase.Constants;
import com.newaim.purchase.admin.system.entity.Attachment;
import com.newaim.purchase.admin.system.service.AttachmentService;
import com.newaim.purchase.archives.finance.dao.BankAccountDao;
import com.newaim.purchase.archives.finance.service.BankAccountService;
import com.newaim.purchase.archives.flow.finance.dao.BalanceRefundDao;
import com.newaim.purchase.archives.flow.finance.dao.FeePaymentDao;
import com.newaim.purchase.archives.flow.finance.dao.FeeRegisterDao;
import com.newaim.purchase.archives.flow.finance.entity.BalanceRefund;
import com.newaim.purchase.archives.flow.finance.entity.FeePayment;
import com.newaim.purchase.archives.flow.finance.entity.FeeRegister;
import com.newaim.purchase.archives.flow.finance.service.FeeRegisterService;
import com.newaim.purchase.archives.flow.purchase.dao.PurchaseBalanceRefundUnionDao;
import com.newaim.purchase.archives.flow.purchase.dao.PurchaseContractDao;
import com.newaim.purchase.archives.flow.purchase.entity.PurchaseBalanceRefundUnion;
import com.newaim.purchase.archives.flow.purchase.entity.PurchaseContract;
import com.newaim.purchase.archives.vendor.entity.Vendor;
import com.newaim.purchase.constants.ConstantsAttachment;
import com.newaim.purchase.flow.finance.dao.FlowBalanceRefundDao;
import com.newaim.purchase.flow.finance.dao.FlowFeePaymentDao;
import com.newaim.purchase.flow.finance.dao.FlowFeeRegisterDao;
import com.newaim.purchase.flow.finance.entity.FlowBalanceRefund;
import com.newaim.purchase.flow.finance.entity.FlowFeePayment;
import com.newaim.purchase.flow.finance.entity.FlowFeeRegister;
import com.newaim.purchase.flow.finance.vo.FlowFeePaymentVo;
import com.newaim.purchase.flow.purchase.dao.FlowPurchaseContractDao;
import com.newaim.purchase.flow.purchase.entity.FlowPurchaseContract;
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

import java.util.Date;
import java.util.List;
import java.util.LinkedHashMap;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class FlowFeePaymentService extends FlowServiceBase {

    @Autowired
    private FlowFeePaymentDao flowFeePaymentDao;

    @Autowired
    private FeeRegisterService feeRegisterService;

    @Autowired
    private FlowFeeRegisterService flowFeeRegisterService;

    @Autowired
    private FeePaymentDao feePaymentDao;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private FeeRegisterDao feeRegisterDao;

    @Autowired
    private FlowFeeRegisterDao flowFeeRegisterDao;

    @Autowired
    private PurchaseContractDao purchaseContractDao;

    @Autowired
    private FlowPurchaseContractDao flowPurchaseContractDao;

    @Autowired
    private PurchaseBalanceRefundUnionDao purchaseBalanceRefundUnionDao;

    @Autowired
    private BalanceRefundDao balanceRefundDao;

    @Autowired
    private FlowBalanceRefundDao flowBalanceRefundDao;

    /**
     * 分页查询
     * @param params
     * @param pageNumber
     * @param pageSize
     * @param sort
     * @return
     */
    public Page<FlowFeePaymentVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<FlowFeePayment> spec = buildSpecification(params);
        Page<FlowFeePayment> p = flowFeePaymentDao.findAll(spec, pageRequest);
        Page<FlowFeePaymentVo> page = p.map(new Converter<FlowFeePayment, FlowFeePaymentVo>() {
            @Override
            public FlowFeePaymentVo convert(FlowFeePayment flowFeePayment) {
                return convertToFlowFeePaymentVo(flowFeePayment);
            }
        });
        return page;
    }

    /**
     * 转换实体类为Vo
     * @param flowFeePayment
     * @return
     */
    private FlowFeePaymentVo convertToFlowFeePaymentVo(FlowFeePayment flowFeePayment){
        FlowFeePaymentVo vo = BeanMapper.map(flowFeePayment, FlowFeePaymentVo.class);
        return vo;
    }

    /**
     * 动态建立关系
     * @param searchParams
     * @return
     */
    private Specification<FlowFeePayment> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<FlowFeePayment> spec = DynamicSpecifications.bySearchFilter(filters.values(), FlowFeePayment.class);
        return spec;
    }

    /**
     * 通过ID获取费用支付数据
     * @param id
     * @return
     */
    public FlowFeePayment getFlowFeePayment(String id){
        return flowFeePaymentDao.findOne(id);
    }

    /**
     * 通过ID获取费用支付
     * @param id
     * @return
     */
    public FlowFeePaymentVo get(String id){
        FlowFeePaymentVo vo = convertToFlowFeePaymentVo(getFlowFeePayment(id));
        vo.setAttachments(attachmentService.listByBusinessId(id));
        if (StringUtils.isNotBlank(vo.getFeeRegisterBusinessId())){
           vo.setFlowFeeRegisterVo(flowFeeRegisterService.get(vo.getFeeRegisterBusinessId()));
        }
        return vo;
    }

    /**
     * 新建，设置创建人信息，附件
     * @param flowFeePayment
     * @param attachments
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public FlowFeePayment add(FlowFeePayment flowFeePayment, List<Attachment> attachments){
        //设置创建人信息
        setFlowCreatorInfo(flowFeePayment);
        //设置供应商信息
        setFlowVendorInfo(flowFeePayment);
        //设置费用登记信息
        setFeeRegistrationInfo(flowFeePayment);
        flowFeePayment = flowFeePaymentDao.save(flowFeePayment);
        //保存附件
        attachmentService.save(attachments, ConstantsAttachment.Status.FlowFeePayment.code, flowFeePayment.getId());
        return flowFeePayment;
    }

    /**
     * 更新
     * @param flowFeePayment
     * @param attachments
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public FlowFeePayment update(FlowFeePayment flowFeePayment,List<Attachment> attachments){
        //设置供应商信息
        setFlowVendorInfo(flowFeePayment);
        //设置更新时间
        flowFeePayment.setUpdatedAt(new Date());
        //设置支付时间
        flowFeePayment.setPaymentDate(new Date());
        //设置费用登记信息
        setFeeRegistrationInfo(flowFeePayment);
        //保存附件
        attachmentService.save(attachments, ConstantsAttachment.Status.FlowFeePayment.code, flowFeePayment.getId());
        return flowFeePaymentDao.save(flowFeePayment);
    }

    /**
     * 出纳环节修改保存附件
     * @param flowFeePayment
     * @param attachments
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public FlowFeePayment updateFile(FlowFeePayment flowFeePayment,List<Attachment> attachments){
        //保存附件
        attachmentService.save(attachments, ConstantsAttachment.Status.FlowFeePayment.code, flowFeePayment.getId());
        return flowFeePaymentDao.save(flowFeePayment);
    }

    /**
     * 另存
     * @param flowFeePayment
     * @param attachments
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public FlowFeePayment saveAs(FlowFeePayment flowFeePayment,List<Attachment> attachments){
        flowFeePaymentDao.clear();
        //设置创建人信息
        setFlowCreatorInfo(flowFeePayment);
        //设置供应商信息
        setFlowVendorInfo(flowFeePayment);
        //设置费用登记信息
        setFeeRegistrationInfo(flowFeePayment);
        //清理信息
        cleanInfoForSaveAs(flowFeePayment);
        flowFeePayment = flowFeePaymentDao.save(flowFeePayment);
        //保存附件
        attachmentService.save(attachments, ConstantsAttachment.Status.FlowFeePayment.code, flowFeePayment.getId());
        return flowFeePayment;
    }

    /**
     * 设置费用登记信息,应付总金额、汇率、结算币种、最迟支付时间
     * @param flowFeePayment
     */
    public void setFeeRegistrationInfo(FlowFeePayment flowFeePayment){
        if (StringUtils.isNotBlank(flowFeePayment.getFeeRegisterId())){
            FeeRegister feeRegister = feeRegisterService.getFeeRegister(flowFeePayment.getFeeRegisterId());
            if (feeRegister != null){
                flowFeePayment.setFeeRegisterId(feeRegister.getId());
                flowFeePayment.setTotalPriceAud(feeRegister.getTotalPriceAud());
                flowFeePayment.setTotalPriceRmb(feeRegister.getTotalPriceRmb());
                flowFeePayment.setTotalPriceUsd(feeRegister.getTotalPriceUsd());
                flowFeePayment.setCurrency(feeRegister.getCurrency());
                flowFeePayment.setDueDate(feeRegister.getDueDate());
                flowFeePayment.setOrderNumber(feeRegister.getOrderNumber());
                flowFeePayment.setOrderTitle(feeRegister.getOrderTitle());
            }
        }
    }

    /**
     * 冻结
     * @param flowId
     */
    @Transactional(rollbackFor = Exception.class)
    public void hold(String flowId){
        FlowFeePayment flow = flowFeePaymentDao.findOne(flowId);
        flow.setHold(Constants.HoldStatus.HOLD.code);
        flowFeePaymentDao.save(flow);
    }

    /**
     * 解除冻结
     * @param flowId
     */
    @Transactional(rollbackFor = Exception.class)
    public void unHold(String flowId){
        FlowFeePayment flow = flowFeePaymentDao.findOne(flowId);
        flow.setHold(Constants.HoldStatus.UN_HOLD.code);
        flowFeePaymentDao.save(flow);
    }

    /**
     * 删除，改变状态
     * @param flowFeePayment
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(FlowFeePayment flowFeePayment){
        flowFeePayment.setUpdatedAt(new Date());
        flowFeePayment.setStatus(Constants.Status.DELETED.code);
        flowFeePaymentDao.save(flowFeePayment);
    }
    /**
     * 作废
     * @param flowId
     */
    @Transactional(rollbackFor = Exception.class)
    public void cancel(String flowId){
        FlowFeePayment flowFeePayment = flowFeePaymentDao.findOne(flowId);
        flowFeePayment.setStatus(Constants.Status.CANCELED.code);
        flowFeePaymentDao.save(flowFeePayment);
        if(StringUtils.isNotBlank(flowFeePayment.getFeeRegisterBusinessId())){
            FlowFeeRegister flowFeeRegister = flowFeeRegisterDao.findOne(flowFeePayment.getFeeRegisterBusinessId());
            if(flowFeeRegister != null){
                flowFeeRegister.setPaymentStatus(Constants.FeePaymentStatus.UNPAID.code);
                flowFeeRegisterDao.save(flowFeeRegister);
            }
        }
        if(StringUtils.isNotBlank(flowFeePayment.getFeeRegisterId())){
            FeeRegister feeRegister = feeRegisterDao.findOne(flowFeePayment.getFeeRegisterId());
            if(feeRegister != null){
                feeRegister.setPaymentStatus(Constants.FeePaymentStatus.UNPAID.code);
                feeRegisterDao.save(feeRegister);
            }

            //供应商押金类型时
            if(Constants.FeeType.VENDOR_DEPOSIT.code.equals(feeRegister.getFeeType())){
                //TODO
                //BankAccount bankAccount = bankAccountService.getBankAccountByVendorId(feeRegister.getVendorId());
            }

            //订单相关
            if(StringUtils.isNotBlank(feeRegister.getOrderId())){
                PurchaseContract order = purchaseContractDao.findOne(feeRegister.getOrderId());
                if(order != null){
                    //尾款支付
                    if(Constants.FeeType.CONTRACT_BALANCE.code.equals(feeRegister.getFeeType())){
                        order.setFlagFeePaymentId(null);
                        order.setFlagFeePaymentStatus(2);
                        order.setFlagFeePaymentTime(null);
                        //设置冲销单据的扣款状态
                        List<PurchaseBalanceRefundUnion> unions =  purchaseBalanceRefundUnionDao.findByPurchaseContractBusinessId(order.getBusinessId());
                        if(unions != null && unions.size() > 0){
                            for (int i = 0; i < unions.size(); i++) {
                                PurchaseBalanceRefundUnion union = unions.get(i);
                                BalanceRefund balanceRefund = balanceRefundDao.findOne(union.getBalanceRefundId());
                                balanceRefund.setChargebackStatus(2);
                                balanceRefundDao.save(balanceRefund);
                                FlowBalanceRefund flowBalanceRefund = flowBalanceRefundDao.findOne(balanceRefund.getBusinessId());
                                if(flowBalanceRefund != null){
                                    flowBalanceRefund.setChargebackStatus(2);
                                    flowBalanceRefundDao.save(flowBalanceRefund);
                                }
                            }
                        }

                    }
                    //电放费
                    if(Constants.FeeType.ELECTRONIC_PROCESSING_FEE.code.equals(feeRegister.getFeeType())){
                        order.setElectronicProcessingFeeAud(null);
                        order.setElectronicProcessingFeeRmb(null);
                        order.setElectronicProcessingFeeUsd(null);
                        FlowPurchaseContract flowOrder = flowPurchaseContractDao.findOne(order.getBusinessId());
                        if(flowOrder != null){
                            flowOrder.setElectronicProcessingFeeAud(null);
                            flowOrder.setElectronicProcessingFeeRmb(null);
                            flowOrder.setElectronicProcessingFeeUsd(null);
                            flowPurchaseContractDao.save(flowOrder);
                        }
                    }
                    purchaseContractDao.save(order);
                }
            }
        }
        List<FeePayment> feePayments = feePaymentDao.findByBusinessId(flowId);
        if(feePayments != null && feePayments.size() > 0){
            for (int i = 0; i < feePayments.size(); i++) {
                FeePayment feePayment = feePayments.get(i);
                feePayment.setStatus(Constants.Status.CANCELED.code);
                feePaymentDao.save(feePayment);
            }
        }
    }


}
