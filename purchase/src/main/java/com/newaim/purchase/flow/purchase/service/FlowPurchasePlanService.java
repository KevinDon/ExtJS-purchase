package com.newaim.purchase.flow.purchase.service;


import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.purchase.Constants;
import com.newaim.purchase.admin.system.entity.Attachment;
import com.newaim.purchase.admin.system.service.AttachmentService;
import com.newaim.purchase.archives.flow.purchase.dao.PurchaseBalanceRefundUnionDao;
import com.newaim.purchase.archives.flow.purchase.dao.PurchasePlanDao;
import com.newaim.purchase.archives.flow.purchase.entity.PurchaseBalanceRefundUnion;
import com.newaim.purchase.archives.flow.purchase.entity.PurchasePlan;
import com.newaim.purchase.archives.flow.purchase.service.PurchaseBalanceRefundUnionService;
import com.newaim.purchase.archives.flow.purchase.service.PurchaseContractService;
import com.newaim.purchase.archives.vendor.service.VendorService;
import com.newaim.purchase.constants.ConstantsAttachment;
import com.newaim.purchase.flow.purchase.dao.FlowPurchasePlanDao;
import com.newaim.purchase.flow.purchase.dao.FlowPurchasePlanDetailDao;
import com.newaim.purchase.flow.purchase.entity.FlowPurchasePlan;
import com.newaim.purchase.flow.purchase.entity.FlowPurchasePlanDetail;
import com.newaim.purchase.flow.purchase.vo.FlowPurchasePlanVo;
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
public class FlowPurchasePlanService extends FlowServiceBase {

    private static final String FILE_MODULE_NAME = ConstantsAttachment.Status.FlowPurchasePlan.code;

    @Autowired
    private FlowPurchasePlanDao flowPurchasePlanDao;

    @Autowired
    private PurchasePlanDao purchasePlanDao;

    @Autowired
    private FlowPurchasePlanDetailDao flowPurchasePlanDetailDao;

    @Autowired
    private FlowPurchasePlanDetailService flowPurchasePlanDetailService;

    @Autowired
    private VendorService vendorService;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private PurchaseBalanceRefundUnionDao purchaseBalanceRefundUnionDao;

    @Autowired
    private PurchaseBalanceRefundUnionService purchaseBalanceRefundUnionService;

    @Autowired
    private PurchaseContractService purchaseContractService;

    /**
     * 分页查询List
     * @param params
     * @param pageNumber
     * @param pageSize
     * @param sort
     * @return
     */
    public Page<FlowPurchasePlanVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<FlowPurchasePlan> spec = buildSpecification(params);
        Page<FlowPurchasePlan> p = flowPurchasePlanDao.findAll(spec, pageRequest);
        Page<FlowPurchasePlanVo> page = p.map(new Converter<FlowPurchasePlan, FlowPurchasePlanVo>() {
            @Override
            public FlowPurchasePlanVo convert(FlowPurchasePlan flowPurchasePlan) {
                return convertToFlowPurchasePlanVo(flowPurchasePlan);
            }
        });
        return page;
    }

    /**
     * 将entity转换成Vo
     * @param flowPurchasePlan
     * @return
     */
    private FlowPurchasePlanVo convertToFlowPurchasePlanVo(FlowPurchasePlan flowPurchasePlan){
        FlowPurchasePlanVo vo = BeanMapper.map(flowPurchasePlan, FlowPurchasePlanVo.class);
        return vo;
    }

    public FlowPurchasePlan getFlowPurchasePlan(String id){
        return flowPurchasePlanDao.findOne(id);
    }

    /**
     * 根据ID获得附件、供应商、detail信息
     * @param id
     * @return
     */
    public FlowPurchasePlanVo get(String id){
        FlowPurchasePlanVo vo = convertToFlowPurchasePlanVo(getFlowPurchasePlan(id));
        vo.setAttachments(attachmentService.listByBusinessId(id));
        if (StringUtils.isNotBlank(vo.getVendorId())){
            vo.setVendor(vendorService.get(vo.getVendorId()));
        }
        vo.setDetails(flowPurchasePlanDetailService.findDetailsVoByBusinessId(id));
        vo.setPurchaseBalanceRefundUnions(purchaseBalanceRefundUnionService.findVoByPurchasePlanBusinessId(id));
        vo.setOrders(purchaseContractService.findVoByPurchasePlanBusinessId(id));
        return vo;
    }

    /**
     * 新建，需要设置创建人，供应商，价格信息，绑定details、附件
     * @param flowPurchasePlan
     * @param details
     * @param attachments
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public FlowPurchasePlan add(FlowPurchasePlan flowPurchasePlan, List<FlowPurchasePlanDetail> details, List<PurchaseBalanceRefundUnion> purchaseBalanceRefundUnions, List<Attachment> attachments){
        //设置创建人信息
        setFlowCreatorInfo(flowPurchasePlan);
        //设置供应商信息
        setFlowVendorInfo(flowPurchasePlan);
        flowPurchasePlanDao.save(flowPurchasePlan);
        saveDetails(flowPurchasePlan.getId(), details);
        //新增差额关联数据
        savePurchaseBalanceRefundUnionsInfo(flowPurchasePlan, purchaseBalanceRefundUnions);
        //保存附件
        attachmentService.save(attachments, FILE_MODULE_NAME, flowPurchasePlan.getId());

        return flowPurchasePlan;
    }

    /**
     * 更新,details需重新绑定
     * @param flowPurchasePlan
     * @param details
     */
    @Transactional(rollbackFor = Exception.class)
    public FlowPurchasePlan update(FlowPurchasePlan flowPurchasePlan, List<FlowPurchasePlanDetail> details, List<PurchaseBalanceRefundUnion> purchaseBalanceRefundUnions,List<Attachment> attachments){
        //设置供应商信息
        setFlowVendorInfo(flowPurchasePlan);
        //设置更新时间
        flowPurchasePlan.setUpdatedAt(new Date());
        //删除之前明细，重新绑定数据
        flowPurchasePlanDetailDao.deleteByBusinessId(flowPurchasePlan.getId());
        saveDetails(flowPurchasePlan.getId(), details);
        //保存差额关联时，先清除之前明细
        purchaseBalanceRefundUnionDao.deleteByPurchasePlanBusinessId(flowPurchasePlan.getId());
        savePurchaseBalanceRefundUnionsInfo(flowPurchasePlan, purchaseBalanceRefundUnions);
        //更新附件
        attachmentService.save(attachments, FILE_MODULE_NAME, flowPurchasePlan.getId());

        return flowPurchasePlanDao.save(flowPurchasePlan);
    }

    /**
     * 保存另存
     * @param flowPurchasePlan
     */
    @Transactional(rollbackFor = Exception.class)
    public FlowPurchasePlan saveAs(FlowPurchasePlan flowPurchasePlan, List<FlowPurchasePlanDetail> details, List<PurchaseBalanceRefundUnion> purchaseBalanceRefundUnions,List<Attachment> attachments){
        flowPurchasePlanDao.clear();
        //设置创建人信息
        setFlowCreatorInfo(flowPurchasePlan);
        //设置供应商信息
        setFlowVendorInfo(flowPurchasePlan);
        //清理信息
        cleanInfoForSaveAs(flowPurchasePlan);
        flowPurchasePlanDao.save(flowPurchasePlan);
        saveDetails(flowPurchasePlan.getId(), details);
        //新增差额关联数据
        savePurchaseBalanceRefundUnionsInfo(flowPurchasePlan, purchaseBalanceRefundUnions);
        //保存附件
        attachmentService.save(attachments, FILE_MODULE_NAME, flowPurchasePlan.getId());
        return flowPurchasePlan;
    }

    private void savePurchaseBalanceRefundUnionsInfo(FlowPurchasePlan flowPurchasePlan, List<PurchaseBalanceRefundUnion> purchaseBalanceRefundUnions){
        if(purchaseBalanceRefundUnions != null && purchaseBalanceRefundUnions.size() > 0){
            for (int i = 0; i < purchaseBalanceRefundUnions.size(); i++) {
                purchaseBalanceRefundUnions.get(i).setPurchasePlanBusinessId(flowPurchasePlan.getId());
            }
            purchaseBalanceRefundUnionDao.save(purchaseBalanceRefundUnions);
        }
    }

    /**
     * 保存时建立关联关系
     */
    private void saveDetails(String businessId, List<FlowPurchasePlanDetail> details){
        if(details != null){
            for (int i = 0; i < details.size(); i++) {
                details.get(i).setBusinessId(businessId);
            }
            flowPurchasePlanDetailDao.save(details);
        }
    }

    /**
     * 标记为删除状态
     * @param flowPurchasePlan
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(FlowPurchasePlan flowPurchasePlan){
        flowPurchasePlan.setUpdatedAt(new Date());
        flowPurchasePlan.setStatus(Constants.Status.DELETED.code);
        flowPurchasePlanDao.save(flowPurchasePlan);
    }

    /**
     * 冻结
     * @param flowId
     */
    @Transactional(rollbackFor = Exception.class)
    public void hold(String flowId){
        FlowPurchasePlan flow = flowPurchasePlanDao.findOne(flowId);
        flow.setHold(Constants.HoldStatus.HOLD.code);
        flowPurchasePlanDao.save(flow);
    }

    @Transactional(rollbackFor = Exception.class)
    public void cancel(String businessId){
        FlowPurchasePlan flowPurchasePlan = flowPurchasePlanDao.findOne(businessId);
        flowPurchasePlan.setStatus(Constants.Status.CANCELED.code);
        flowPurchasePlanDao.save(flowPurchasePlan);
        List<PurchasePlan> purchasePlans = purchasePlanDao.findByBusinessId(businessId);
        if(purchasePlans != null && purchasePlans.size() > 0){
            for (int i = 0; i < purchasePlans.size(); i++) {
                PurchasePlan purchasePlan = purchasePlans.get(i);
                purchasePlan.setStatus(Constants.Status.CANCELED.code);
            }
            purchasePlanDao.save(purchasePlans);
        }
    }

    /**
     * 解除冻结
     * @param flowId
     */
    @Transactional(rollbackFor = Exception.class)
    public void unHold(String flowId){
        FlowPurchasePlan flow = flowPurchasePlanDao.findOne(flowId);
        flow.setHold(Constants.HoldStatus.UN_HOLD.code);
        flowPurchasePlanDao.save(flow);
    }

    private Specification<FlowPurchasePlan> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<FlowPurchasePlan> spec = DynamicSpecifications.bySearchFilter(filters.values(), FlowPurchasePlan.class);
        return spec;
    }

    public List<FlowPurchasePlan> findByProductQuotationBusinessId(String productQuotationBusinessId){
        return flowPurchasePlanDao.findByProductQuotationBusinessId(productQuotationBusinessId);
    }


}
