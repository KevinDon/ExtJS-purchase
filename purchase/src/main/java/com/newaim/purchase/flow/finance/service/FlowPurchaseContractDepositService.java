package com.newaim.purchase.flow.finance.service;

import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.purchase.Constants;
import com.newaim.purchase.admin.system.entity.Attachment;
import com.newaim.purchase.admin.system.service.AttachmentService;
import com.newaim.purchase.archives.flow.finance.dao.PurchaseContractDepositDao;
import com.newaim.purchase.archives.flow.finance.entity.PurchaseContractDeposit;
import com.newaim.purchase.archives.flow.purchase.dao.PurchaseContractDao;
import com.newaim.purchase.archives.flow.purchase.entity.PurchaseContract;
import com.newaim.purchase.archives.flow.purchase.service.PurchaseContractDetailService;
import com.newaim.purchase.archives.flow.purchase.service.PurchaseContractOtherDetailService;
import com.newaim.purchase.archives.vendor.service.VendorService;
import com.newaim.purchase.constants.ConstantsAttachment;
import com.newaim.purchase.flow.finance.dao.FlowPurchaseContractDepositDao;
import com.newaim.purchase.flow.finance.entity.FlowPurchaseContractDeposit;
import com.newaim.purchase.flow.finance.vo.FlowPurchaseContractDepositVo;
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
public class FlowPurchaseContractDepositService extends FlowServiceBase {

    @Autowired
    private FlowPurchaseContractDepositDao flowPurchaseContractDepositDao;

    @Autowired
    private PurchaseContractDepositDao purchaseContractDepositDao;

    @Autowired
    private VendorService vendorService;

    @Autowired
    private PurchaseContractDetailService purchaseContractDetailService;

    @Autowired
    private PurchaseContractOtherDetailService purchaseContractOtherDetailService;

    @Autowired
    private PurchaseContractDao purchaseContractDao;

    @Autowired
    private AttachmentService attachmentService;

    /**
     * 分页查询所有信息
     * @param params
     * @param pageNumber
     * @param pageSize
     * @param sort
     * @return
     */
    public Page<FlowPurchaseContractDepositVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<FlowPurchaseContractDeposit> spec = buildSpecification(params);
        Page<FlowPurchaseContractDeposit> p = flowPurchaseContractDepositDao.findAll(spec, pageRequest);
        Page<FlowPurchaseContractDepositVo> page = p.map(new Converter<FlowPurchaseContractDeposit, FlowPurchaseContractDepositVo>() {
            @Override
            public FlowPurchaseContractDepositVo convert(FlowPurchaseContractDeposit deposit) {
                return convertToFlowPurchaseContractDepositVo(deposit);
            }
        });
        return page;
    }

    /**
     * entity转换为Vo
     * @param deposit
     * @return
     */
    private FlowPurchaseContractDepositVo convertToFlowPurchaseContractDepositVo(FlowPurchaseContractDeposit deposit){
        FlowPurchaseContractDepositVo vo = BeanMapper.map(deposit, FlowPurchaseContractDepositVo.class);
        return vo;
    }

    /**
     *动态建立关联
     * @param searchParams
     * @return
     */
    private Specification<FlowPurchaseContractDeposit> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<FlowPurchaseContractDeposit> spec = DynamicSpecifications.bySearchFilter(filters.values(), FlowPurchaseContractDeposit.class);
        return spec;
    }

    public FlowPurchaseContractDeposit getFlowPurchaseContractDeposit(String id){
        return flowPurchaseContractDepositDao.findOne(id);
    }

    public FlowPurchaseContractDepositVo get(String id){
        FlowPurchaseContractDepositVo vo = convertToFlowPurchaseContractDepositVo(getFlowPurchaseContractDeposit(id));
        if (StringUtils.isNotBlank(vo.getVendorId())){
            vo.setVendor(vendorService.get(vo.getVendorId()));
        }
        vo.setPurchaseContractDetail(purchaseContractDetailService.findDetailVosByOrderId(vo.getOrderId()));
        vo.setPurchaseContractOtherDetails(purchaseContractOtherDetailService.findOtherDetailVosByOrderId(vo.getOrderId()));
        vo.setAttachments(attachmentService.listByBusinessId(id));
        return vo;
    }


    @Transactional(rollbackFor = Exception.class)
    public FlowPurchaseContractDeposit add(FlowPurchaseContractDeposit flowPurchaseContractDeposit,List<Attachment> attachments){
        //设置创建人信息
        setFlowCreatorInfo(flowPurchaseContractDeposit);
        //设置供应商信息
        setFlowVendorInfo(flowPurchaseContractDeposit);
        flowPurchaseContractDeposit = flowPurchaseContractDepositDao.save(flowPurchaseContractDeposit);
        //保存附件
        attachmentService.save(attachments, ConstantsAttachment.Status.FlowPurchaseContractDeposit.code, flowPurchaseContractDeposit.getId());
        return flowPurchaseContractDeposit;
    }

    @Transactional(rollbackFor = Exception.class)
    public FlowPurchaseContractDeposit update(FlowPurchaseContractDeposit flowPurchaseContractDeposit,List<Attachment> attachments){
        //设置供应商信息
        setFlowVendorInfo(flowPurchaseContractDeposit);
        //设置更新时间
        flowPurchaseContractDeposit.setUpdatedAt(new Date());
        //保存附件
        attachmentService.save(attachments, ConstantsAttachment.Status.FlowPurchaseContractDeposit.code, flowPurchaseContractDeposit.getId());
        return flowPurchaseContractDepositDao.save(flowPurchaseContractDeposit);
    }

    /**
     * 出纳环节单独保存附件
     * @param flowPurchaseContractDeposit
     * @param attachments
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public FlowPurchaseContractDeposit updateFile(FlowPurchaseContractDeposit flowPurchaseContractDeposit,List<Attachment> attachments){
        //保存附件
        attachmentService.save(attachments, ConstantsAttachment.Status.FlowPurchaseContractDeposit.code, flowPurchaseContractDeposit.getId());
        return flowPurchaseContractDepositDao.save(flowPurchaseContractDeposit);
    }

    @Transactional(rollbackFor = Exception.class)
    public FlowPurchaseContractDeposit saveAs(FlowPurchaseContractDeposit flowPurchaseContractDeposit,List<Attachment> attachments){
        flowPurchaseContractDepositDao.clear();
        //设置创建人信息
        setFlowCreatorInfo(flowPurchaseContractDeposit);
        //设置供应商信息
        setFlowVendorInfo(flowPurchaseContractDeposit);
        //清理信息
        cleanInfoForSaveAs(flowPurchaseContractDeposit);
        flowPurchaseContractDeposit = flowPurchaseContractDepositDao.save(flowPurchaseContractDeposit);
        //保存附件
        attachmentService.save(attachments, ConstantsAttachment.Status.FlowPurchaseContractDeposit.code, flowPurchaseContractDeposit.getId());
        return flowPurchaseContractDeposit;
    }

    /**
     * 冻结
     * @param flowId
     */
    @Transactional(rollbackFor = Exception.class)
    public void hold(String flowId){
        FlowPurchaseContractDeposit flow = flowPurchaseContractDepositDao.findOne(flowId);
        flow.setHold(Constants.HoldStatus.HOLD.code);
        flowPurchaseContractDepositDao.save(flow);
    }

    /**
     * 解除冻结
     * @param flowId
     */
    @Transactional(rollbackFor = Exception.class)
    public void unHold(String flowId){
        FlowPurchaseContractDeposit flow = flowPurchaseContractDepositDao.findOne(flowId);
        flow.setHold(Constants.HoldStatus.UN_HOLD.code);
        flowPurchaseContractDepositDao.save(flow);
    }

    /**
     * 删除，标记删除为状态
     * @param flowPurchaseContractDeposit
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(FlowPurchaseContractDeposit flowPurchaseContractDeposit){
        flowPurchaseContractDeposit.setUpdatedAt(new Date());
        flowPurchaseContractDeposit.setStatus(Constants.Status.DELETED.code);
        flowPurchaseContractDepositDao.save(flowPurchaseContractDeposit);
    }

    /**
     * 作废定金
     * @param id
     */
    @Transactional(rollbackFor = Exception.class)
    public void cancel(String id){
        FlowPurchaseContractDeposit flowPurchaseContractDeposit = flowPurchaseContractDepositDao.findOne(id);
        if(flowPurchaseContractDeposit != null){
            flowPurchaseContractDeposit.setStatus(Constants.Status.CANCELED.code);
            flowPurchaseContractDepositDao.save(flowPurchaseContractDeposit);
            //清空订单标记位
            PurchaseContract order = purchaseContractDao.findOne(flowPurchaseContractDeposit.getOrderId());
            if(order != null){
                order.setFlagContractDepositId(null);
                order.setFlagContractDepositStatus(2);
                order.setFlagContractDepositTime(null);
                purchaseContractDao.save(order);
            }
        }
        //作废正式数据
        List<PurchaseContractDeposit> deposits = purchaseContractDepositDao.findByBusinessId(id);
        if(deposits != null && deposits.size() > 0){
            for (int i = 0; i < deposits.size(); i++) {
                PurchaseContractDeposit deposit = deposits.get(i);
                deposit.setStatus(Constants.Status.CANCELED.code);
                purchaseContractDepositDao.save(deposit);
            }
        }
    }

    /**
     * 查找合同定金单
     * @param orderId
     * @return
     */
    public FlowPurchaseContractDeposit findByOrderId(String orderId){
        List<FlowPurchaseContractDeposit> data = flowPurchaseContractDepositDao.findByOrderId(orderId);
        return data != null && data.size() > 0 ? data.get(0) : null;
    }


}
