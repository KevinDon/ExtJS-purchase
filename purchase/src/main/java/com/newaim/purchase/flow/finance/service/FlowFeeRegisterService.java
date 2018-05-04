package com.newaim.purchase.flow.finance.service;


import com.google.common.collect.Lists;
import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.purchase.Constants;
import com.newaim.purchase.admin.system.entity.Attachment;
import com.newaim.purchase.admin.system.service.AttachmentService;
import com.newaim.purchase.admin.system.vo.AttachmentVo;
import com.newaim.purchase.archives.flow.purchase.dao.PurchaseContractDao;
import com.newaim.purchase.archives.flow.purchase.entity.PurchaseContract;
import com.newaim.purchase.archives.flow.purchase.service.*;
import com.newaim.purchase.archives.flow.purchase.vo.CustomClearancePackingDetailVo;
import com.newaim.purchase.archives.flow.purchase.vo.CustomClearancePackingVo;
import com.newaim.purchase.archives.flow.purchase.vo.CustomClearanceVo;
import com.newaim.purchase.archives.vendor.service.VendorService;
import com.newaim.purchase.constants.ConstantsAttachment;
import com.newaim.purchase.flow.finance.dao.FlowFeeRegisterDao;
import com.newaim.purchase.flow.finance.dao.FlowFeeRegisterDetailDao;
import com.newaim.purchase.flow.finance.entity.FlowFeeRegister;
import com.newaim.purchase.flow.finance.entity.FlowFeeRegisterDetail;
import com.newaim.purchase.flow.finance.vo.FlowFeeRegisterVo;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.LinkedHashMap;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class FlowFeeRegisterService extends FlowServiceBase {

    @Autowired
    private FlowFeeRegisterDao flowFeeRegisterDao;

    @Autowired
    private FlowFeeRegisterDetailDao flowFeeRegisterDetailDao;

    @Autowired
    private VendorService vendorService;

    @Autowired
    private FlowFeeRegisterDetailService flowFeeRegisterDetailService;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private PurchaseContractDao purchaseContractDao;

    @Autowired
    private PurchaseContractDetailService purchaseContractDetailService;

    @Autowired
    private CustomClearanceService customClearanceService;

    @Autowired
    private CustomClearancePackingService customClearancePackingService;

    @Autowired
    private CustomClearancePackingDetailService customClearancePackingDetailService;


    /**
     * 分页查询
     * @param params
     * @param pageNumber
     * @param pageSize
     * @param sort
     * @return
     */
    public Page<FlowFeeRegisterVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<FlowFeeRegister> spec = buildSpecification(params);
        Page<FlowFeeRegister> p = flowFeeRegisterDao.findAll(spec, pageRequest);
        Page<FlowFeeRegisterVo> page = p.map(new Converter<FlowFeeRegister, FlowFeeRegisterVo>() {
            @Override
            public FlowFeeRegisterVo convert(FlowFeeRegister flowFeeRegister) {
                return convertToFlowFeeRegisterVo(flowFeeRegister);
            }
        });
        return page;
    }

    /**
     * 转换实体类为Vo
     * @param flowFeeRegister
     * @return
     */
    private FlowFeeRegisterVo convertToFlowFeeRegisterVo(FlowFeeRegister flowFeeRegister){
        FlowFeeRegisterVo vo = BeanMapper.map(flowFeeRegister, FlowFeeRegisterVo.class);
        return vo;
    }

    public FlowFeeRegister getFlowFeeRegister(String id){
        return flowFeeRegisterDao.findOne(id);
    }

    /**
     * 根据费用登记流程ID，获取明细信息
     * @param id
     * @return
     */
    public FlowFeeRegisterVo get(String id){
        FlowFeeRegisterVo vo =convertToFlowFeeRegisterVo(getFlowFeeRegister(id));
        vo.setAttachments(attachmentService.listByBusinessId(id) );
        if(StringUtils.isNotBlank(vo.getVendorId())){
            vo.setVendor(vendorService.get(vo.getVendorId()));

        }
        vo.setDetails(flowFeeRegisterDetailService.findDetailsVoByBusinessIdAndType(id, 1));
        vo.setOtherDetails(flowFeeRegisterDetailService.findDetailsVoByBusinessIdAndType(id, 2));
        //合同尾款，先判断是否做过清关申请
        if (StringUtils.isNotBlank(vo.getOrderId())) {
            PurchaseContract order = purchaseContractDao.findOne(vo.getOrderId());
            if ("3".equals(vo.getFeeType().toString()) && "1".equals(order.getFlagCustomClearanceStatus().toString())) {
                //如果通过清关申请，则调用清关的明细数据
                List<CustomClearancePackingDetailVo> detailVos = new ArrayList<>();
                CustomClearanceVo customClearanceVo = customClearanceService.getCustomClearanceVo(order.getFlagCustomClearanceId());
                if (customClearanceVo != null) {
                    List<CustomClearancePackingVo> packingVos = customClearancePackingService.findPackingsVoByCustomClearanceId(customClearanceVo.getId());
                    for (int i = 0; i < packingVos.size(); i++) {
                        //通过装柜ID查找装柜明细
                        List<CustomClearancePackingDetailVo> vos = customClearancePackingDetailService.findPackingDetailsVoByPackingId(packingVos.get(i).getId());
                        for (CustomClearancePackingDetailVo detailVo : vos) {
                            detailVos.add(detailVo);
                        }
                    }
                }
                vo.setCustomClearancePackingDetailVos(customClearancePackingDetailService.findMergeDetailVosByPackingId(detailVos));
            } else {
                //否则调用采购合同的明细数据
                vo.setContractDetails(purchaseContractDetailService.findDetailVosByOrderId(vo.getOrderId()));
            }
        }
        if(StringUtils.isNotBlank(vo.getGuaranteeLetter())){
            AttachmentVo attachmentVo = attachmentService.get(vo.getGuaranteeLetter());
            if(attachmentVo != null && attachmentVo.getDocument() != null){
                vo.setGuaranteeLetterName(attachmentVo.getDocument().getName());
            }
        }
        if(StringUtils.isNotBlank(vo.getContractGuaranteeLetter())){
            AttachmentVo attachmentVo = attachmentService.get(vo.getContractGuaranteeLetter());
            if(attachmentVo != null && attachmentVo.getDocument() != null){
                vo.setContractGuaranteeLetterName(attachmentVo.getDocument().getName());
            }
        }
        return vo;
    }

    /**
     * 新建，设置创建人，供应商，价格，订单信息
     */
    @Transactional(rollbackFor = Exception.class)
    public FlowFeeRegister add(FlowFeeRegister flowFeeRegister, List<FlowFeeRegisterDetail> details, List<FlowFeeRegisterDetail> otherDetails, List<Attachment> attachments){
        //设置创建人信息
        setFlowCreatorInfo(flowFeeRegister);
        //设置供应商信息
        setFlowVendorInfo(flowFeeRegister);
        //设置费用信息
        setFlowFeeRegisterPriceInfo(flowFeeRegister, details, otherDetails);
        //设置支付状态
        flowFeeRegister.setPaymentStatus(Constants.FeePaymentStatus.UNPAID.code);
        flowFeeRegister = flowFeeRegisterDao.save(flowFeeRegister);
        saveDetails(flowFeeRegister.getId(), details, otherDetails);
        //保存附件
        attachmentService.save(attachments, ConstantsAttachment.Status.FlowFeeRegister.code, flowFeeRegister.getId());
        return flowFeeRegister;
    }

    /**
     * 更新，设置创建人，供应商，价格，订单信息
     */
    @Transactional(rollbackFor = Exception.class)
    public FlowFeeRegister update(FlowFeeRegister flowFeeRegister, List<FlowFeeRegisterDetail> details, List<FlowFeeRegisterDetail> otherDetails, List<Attachment> attachments){
        //设置供应商信息
        setFlowVendorInfo(flowFeeRegister);
        setFlowFeeRegisterPriceInfo(flowFeeRegister, details, otherDetails);
        //设置更新时间
        flowFeeRegister.setUpdatedAt(new Date());
        //删除之前明细，重新绑定数据
        flowFeeRegisterDetailDao.deleteByBusinessId(flowFeeRegister.getId());
        saveDetails(flowFeeRegister.getId(), details, otherDetails);
        //保存附件
        attachmentService.save(attachments, ConstantsAttachment.Status.FlowFeeRegister.code, flowFeeRegister.getId());
        return flowFeeRegisterDao.save(flowFeeRegister);
    }

    /**
     * 复制另存，设置创建人，供应商，价格，订单信息
     */
    @Transactional(rollbackFor = Exception.class)
    public FlowFeeRegister saveAs(FlowFeeRegister flowFeeRegister, List<FlowFeeRegisterDetail> details, List<FlowFeeRegisterDetail> otherDetails, List<Attachment> attachments){
        flowFeeRegisterDao.clear();
        //设置创建人信息
        setFlowCreatorInfo(flowFeeRegister);
        //设置供应商信息
        setFlowVendorInfo(flowFeeRegister);
        //设置费用信息
        setFlowFeeRegisterPriceInfo(flowFeeRegister, details, otherDetails);
        //清理信息
        cleanInfoForSaveAs(flowFeeRegister);
        flowFeeRegister.setPaymentStatus(Constants.FeePaymentStatus.UNPAID.code);
        flowFeeRegister = flowFeeRegisterDao.save(flowFeeRegister);
        saveDetails(flowFeeRegister.getId(), details, otherDetails);
        //保存附件
        attachmentService.save(attachments, ConstantsAttachment.Status.FlowFeeRegister.code, flowFeeRegister.getId());
        return flowFeeRegister;
    }

    /**
     * 保存时建立关联关系
     */
    private void saveDetails(String businessId, List<FlowFeeRegisterDetail> details, List<FlowFeeRegisterDetail> otherDetails){
        List<FlowFeeRegisterDetail> allDetails = Lists.newArrayList();
        if(details != null){
            allDetails.addAll(details);
        }
        if(otherDetails != null){
            allDetails.addAll(otherDetails);
        }
        for (int i = 0; i < allDetails.size(); i++) {
            allDetails.get(i).setBusinessId(businessId);
        }
        flowFeeRegisterDetailDao.save(allDetails);
    }

    /**
     * 设置样品金额信息
     */
    private void setFlowFeeRegisterPriceInfo(FlowFeeRegister flowFeeRegister,List<FlowFeeRegisterDetail> details, List<FlowFeeRegisterDetail> otherDetails){
        List<FlowFeeRegisterDetail> allDetails = Lists.newArrayList();
        if(details != null){
            allDetails.addAll(details);
        }
        if(otherDetails != null){
            allDetails.addAll(otherDetails);
        }
        //费用类型为押金时不处理总金额
        if(Constants.FeeType.VENDOR.code.equals(flowFeeRegister.getFeeType())
                || Constants.FeeType.SERVICE_PROVIDER.code.equals(flowFeeRegister.getFeeType())
                || Constants.FeeType.ELECTRONIC_PROCESSING_FEE.code.equals(flowFeeRegister.getFeeType())){
            BigDecimal totalPriceAud = BigDecimal.ZERO;
            BigDecimal totalPriceRmb = BigDecimal.ZERO;
            BigDecimal totalPriceUsd = BigDecimal.ZERO;
            if (allDetails != null) {
                for (FlowFeeRegisterDetail detail : allDetails) {
                    BigDecimal qty = BigDecimal.valueOf(detail.getQty() != null ? detail.getQty() : 0);

                    if (detail.getPriceAud() != null) {
                        totalPriceAud = totalPriceAud.add(detail.getPriceAud().multiply(qty));
                    }
                    if (detail.getPriceRmb() != null) {
                        totalPriceRmb = totalPriceRmb.add(detail.getPriceRmb().multiply(qty));
                    }
                    if (detail.getPriceUsd() != null) {
                        totalPriceUsd = totalPriceUsd.add(detail.getPriceUsd().multiply(qty));
                    }
                }
            }
            flowFeeRegister.setTotalPriceAud(totalPriceAud);
            flowFeeRegister.setTotalPriceRmb(totalPriceRmb);
            flowFeeRegister.setTotalPriceUsd(totalPriceUsd);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(FlowFeeRegister flowFeeRegister){
        flowFeeRegister.setUpdatedAt(new Date());
        flowFeeRegister.setStatus(Constants.Status.DELETED.code);
        flowFeeRegisterDao.save(flowFeeRegister);
    }

    /**
     * 冻结
     * @param flowId
     */
    @Transactional(rollbackFor = Exception.class)
    public void hold(String flowId){
        FlowFeeRegister flow = flowFeeRegisterDao.findOne(flowId);
        flow.setHold(Constants.HoldStatus.HOLD.code);
        flowFeeRegisterDao.save(flow);
    }

    /**
     * 解除冻结
     * @param flowId
     */
    @Transactional(rollbackFor = Exception.class)
    public void unHold(String flowId){
        FlowFeeRegister flow = flowFeeRegisterDao.findOne(flowId);
        flow.setHold(Constants.HoldStatus.UN_HOLD.code);
        flowFeeRegisterDao.save(flow);
    }

    private Specification<FlowFeeRegister> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<FlowFeeRegister> spec = DynamicSpecifications.bySearchFilter(filters.values(), FlowFeeRegister.class);
        return spec;
    }

}
