package com.newaim.purchase.flow.inspection.service;

import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.purchase.Constants;
import com.newaim.purchase.admin.account.vo.UserVo;
import com.newaim.purchase.admin.system.entity.Attachment;
import com.newaim.purchase.admin.system.service.AttachmentService;
import com.newaim.purchase.archives.flow.purchase.entity.PurchaseContract;
import com.newaim.purchase.archives.flow.purchase.service.PurchaseContractService;
import com.newaim.purchase.archives.flow.purchase.vo.PurchaseContractVo;
import com.newaim.purchase.archives.vendor.service.VendorService;
import com.newaim.purchase.constants.ConstantsAttachment;
import com.newaim.purchase.flow.inspection.dao.FlowOrderQcDao;
import com.newaim.purchase.flow.inspection.dao.FlowOrderQcDetailDao;
import com.newaim.purchase.flow.inspection.entity.FlowOrderQc;
import com.newaim.purchase.flow.inspection.entity.FlowOrderQcDetail;
import com.newaim.purchase.flow.inspection.vo.FlowOrderQcVo;
import com.newaim.purchase.flow.workflow.service.ActivitiService;
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
public class FlowOrderQcService extends FlowServiceBase {

    private static final String FILE_MODULE_NAME = ConstantsAttachment.Status.FlowOrderQc.code;

    @Autowired
    private FlowOrderQcDao flowOrderQcDao;

    @Autowired
    private FlowOrderQcDetailDao flowOrderQcDetailDao;

    @Autowired
    private FlowOrderQcDetailService flowOrderQcDetailService;

    @Autowired
    private PurchaseContractService purchaseContractService;

    @Autowired
    private VendorService vendorService;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private ActivitiService activitiService;

    /**
     * 分页查询订单质检的信息
     * @param params
     * @param pageNumber
     * @param pageSize
     * @param sort
     * @return
     */
    public Page<FlowOrderQcVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<FlowOrderQc> spec = buildSpecification(params);
        Page<FlowOrderQc> p = flowOrderQcDao.findAll(spec, pageRequest);
        Page<FlowOrderQcVo> page = p.map(new Converter<FlowOrderQc, FlowOrderQcVo>() {
            @Override
            public FlowOrderQcVo convert(FlowOrderQc flowOrderQc) {
                return convertToFlowOrderQcVo(flowOrderQc);
            }
        });
        return page;
    }

    /**
     * 将entity转换为Vo
     * @param flowOrderQc
     * @return
     */
    private FlowOrderQcVo convertToFlowOrderQcVo(FlowOrderQc flowOrderQc){
        FlowOrderQcVo vo = BeanMapper.map(flowOrderQc, FlowOrderQcVo.class);
        return vo;
    }

    public FlowOrderQc getFlowOrderQc(String id){
        return flowOrderQcDao.findOne(id);
    }

    /**
     * 根据ID获取附件、供应商信息、details信息
     * @param id
     * @return
     */
    public FlowOrderQcVo get(String id){
        FlowOrderQcVo vo =convertToFlowOrderQcVo(getFlowOrderQc(id));
        vo.setAttachments(attachmentService.listByBusinessId(id) );
        if(StringUtils.isNotBlank(vo.getVendorId())){
            vo.setVendor(vendorService.get(vo.getVendorId()));
        }
        vo.setDetails(flowOrderQcDetailService.findDetailsVoByBusinessId(id));
        return vo;
    }


    private Specification<FlowOrderQc> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<FlowOrderQc> spec = DynamicSpecifications.bySearchFilter(filters.values(), FlowOrderQc.class);
        return spec;
    }

    /**
     * 新建，需要设置创建人信息、供应商信息、附件、details信息
     * @param flowOrderQc
     * @param details
     * @param attachments
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public FlowOrderQc add(FlowOrderQc flowOrderQc, List<FlowOrderQcDetail> details, List<Attachment> attachments){
        //设置创建人信息
        setFlowCreatorInfo(flowOrderQc);
        setFlowVendorInfo(flowOrderQc);
        flowOrderQc = flowOrderQcDao.save(flowOrderQc);

        saveDetails(flowOrderQc.getId(), details);

        //保存附件
        attachmentService.save(attachments, FILE_MODULE_NAME, flowOrderQc.getId());
        return flowOrderQc;
    }

    /**
     * 修改，设置供应商信息、更新时间、重新绑定details数据、附件
     * @param o
     * @param details
     * @param attachments
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public FlowOrderQc update(FlowOrderQc o, List<FlowOrderQcDetail> details,List<Attachment> attachments){
        //设置供应商信息
        setFlowVendorInfo(o);
        //设置更新时间
        o.setUpdatedAt(new Date());

        // flowOrderQcDetailDao.save(details);
        saveDetails(o.getId(), details);

        //更新附件
        attachmentService.save(attachments, FILE_MODULE_NAME, o.getId());
        return flowOrderQcDao.save(o);
    }

    /**
     * 复制另存，初始化数据，设置创建人信息、供应商、绑定details数据、附件
     * @param flowOrderQc
     * @param details
     * @param attachments
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public FlowOrderQc saveAs(FlowOrderQc flowOrderQc, List<FlowOrderQcDetail> details,List<Attachment> attachments){
        flowOrderQcDao.clear();
        //设置创建人信息
        setFlowCreatorInfo(flowOrderQc);
        //设置供应商信息
        setFlowVendorInfo(flowOrderQc);
        //清理信息
        cleanInfoForSaveAs(flowOrderQc);
        flowOrderQc = flowOrderQcDao.save(flowOrderQc);

        saveDetails(flowOrderQc.getId(), details);
        //保存附件
        attachmentService.save(attachments, FILE_MODULE_NAME, flowOrderQc.getId());
        return flowOrderQc;
    }

    /**
     * 保存时建立关联关系
     */
    private void saveDetails(String businessId, List<FlowOrderQcDetail> details ){
        //删除之前明细，重新绑定数据
        flowOrderQcDetailDao.deleteByBusinessId(businessId);

        if(details != null){
            for (int i = 0; i < details.size(); i++) {
//                details.get(i).setBusinessId(businessId);
                saveDetails(businessId, details.get(i).getOrderId());
            }
//            flowOrderQcDetailDao.save(details);
        }
    }

    private void saveDetails(String businessId, String orderId ){

        if(orderId != null){
            FlowOrderQcDetail details = new FlowOrderQcDetail();
            PurchaseContractVo fpc = purchaseContractService.get(orderId);
            details.setBusinessId(businessId);
            details.setOrderId(orderId);
            details.setOrderNumber(fpc.getOrderNumber());
            details.setOrderTitle(fpc.getOrderTitle());
            details.setCurrency(fpc.getCurrency());
            details.setTotalOrderQty(fpc.getTotalOrderQty());
            details.setTotalPriceAud(fpc.getTotalPriceAud());
            details.setTotalPriceRmb(fpc.getTotalPriceRmb());
            details.setTotalPriceUsd(fpc.getTotalPriceUsd());
            details.setRateAudToRmb(fpc.getRateAudToRmb());
            details.setRateAudToUsd(fpc.getRateAudToUsd());
            details.setDepositAud(fpc.getDepositAud());
            details.setDepositRmb(fpc.getDepositRmb());
            details.setDepositUsd(fpc.getDepositUsd());

            flowOrderQcDetailDao.save(details);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void createAndStartFlowOrderQc(PurchaseContract order){
        //创建质检流
        FlowOrderQc flowOrderQc = new FlowOrderQc();
        flowOrderQc.setStatus(Constants.Status.DRAFT.code);
        flowOrderQc.setCreatorId(order.getCreatorId());
        flowOrderQc.setCreatorCnName(order.getCreatorCnName());
        flowOrderQc.setCreatorEnName(order.getCreatorEnName());
        flowOrderQc.setCreatedAt(new Date());
        flowOrderQc.setDepartmentId(order.getDepartmentId());
        flowOrderQc.setDepartmentCnName(order.getDepartmentCnName());
        flowOrderQc.setDepartmentEnName(order.getDepartmentEnName());
        flowOrderQc.setHold(Constants.HoldStatus.UN_HOLD.code);
        flowOrderQcDao.save(flowOrderQc);
        //质检明细
        FlowOrderQcDetail flowOrderQcDetail = new FlowOrderQcDetail();
        flowOrderQcDetail.setBusinessId(flowOrderQc.getId());
        flowOrderQcDetail.setOrderId(order.getId());
        flowOrderQcDetail.setOrderNumber(order.getOrderNumber());
        flowOrderQcDetail.setOrderTitle(order.getOrderTitle());
        flowOrderQcDetail.setCurrency(order.getCurrency());
        flowOrderQcDetail.setRateAudToRmb(order.getRateAudToRmb());
        flowOrderQcDetail.setRateAudToUsd(order.getRateAudToUsd());
        flowOrderQcDetail.setTotalPriceAud(order.getTotalPriceAud());
        flowOrderQcDetail.setTotalPriceRmb(order.getTotalPriceRmb());
        flowOrderQcDetail.setTotalPriceUsd(order.getTotalPriceUsd());
        flowOrderQcDetail.setTotalOrderQty(order.getTotalOrderQty());
        flowOrderQcDetail.setDepositAud(order.getDepositAud());
        flowOrderQcDetail.setDepositRmb(order.getDepositRmb());
        flowOrderQcDetail.setDepositUsd(order.getDepositUsd());
        flowOrderQcDetailDao.save(flowOrderQcDetail);
        UserVo user = userService.get(flowOrderQc.getCreatorId());
        //发起并提交流程
        activitiService.startWorkFlow(flowOrderQc, user);
    }

    /**
     * 冻结
     * @param flowId
     */
    @Transactional(rollbackFor = Exception.class)
    public void hold(String flowId){
        FlowOrderQc flow = flowOrderQcDao.findOne(flowId);
        flow.setHold(Constants.HoldStatus.HOLD.code);
        flowOrderQcDao.save(flow);
    }

    /**
     * 解除冻结
     * @param flowId
     */
    @Transactional(rollbackFor = Exception.class)
    public void unHold(String flowId){
        FlowOrderQc flow = flowOrderQcDao.findOne(flowId);
        flow.setHold(Constants.HoldStatus.UN_HOLD.code);
        flowOrderQcDao.save(flow);
    }

    /**
     * 删除
     * @param flowOrderQc
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(FlowOrderQc flowOrderQc){
        flowOrderQc.setUpdatedAt(new Date());
        flowOrderQc.setStatus(Constants.Status.DELETED.code);
        flowOrderQcDao.save(flowOrderQc);
    }


    /**
     * 查抄质检单
     * @param orderId
     * @return
     */
    public FlowOrderQc findByOrderId(String orderId){
        List<FlowOrderQc> data = flowOrderQcDao.findByOrderId(orderId);
        return data != null && data.size() > 0 ? data.get(0) : null;
    }

}


