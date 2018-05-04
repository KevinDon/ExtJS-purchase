package com.newaim.purchase.flow.purchase.service;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.purchase.Constants;
import com.newaim.purchase.admin.system.entity.Attachment;
import com.newaim.purchase.admin.system.service.AttachmentService;
import com.newaim.purchase.admin.system.vo.AttachmentVo;
import com.newaim.purchase.archives.flow.purchase.dao.PurchaseContractDao;
import com.newaim.purchase.archives.flow.purchase.dao.PurchasePlanDetailDao;
import com.newaim.purchase.archives.flow.purchase.entity.PurchaseContract;
import com.newaim.purchase.archives.flow.purchase.entity.PurchasePlanDetail;
import com.newaim.purchase.archives.flow.purchase.service.PurchaseContractService;
import com.newaim.purchase.constants.ConstantsAttachment;
import com.newaim.purchase.flow.purchase.dao.FlowCustomClearanceDao;
import com.newaim.purchase.flow.purchase.dao.FlowCustomClearancePackingDao;
import com.newaim.purchase.flow.purchase.dao.FlowCustomClearancePackingDetailDao;
import com.newaim.purchase.flow.purchase.entity.FlowCustomClearance;
import com.newaim.purchase.flow.purchase.entity.FlowCustomClearancePacking;
import com.newaim.purchase.flow.purchase.entity.FlowCustomClearancePackingDetail;
import com.newaim.purchase.flow.purchase.vo.FlowCustomClearancePackingDetailVo;
import com.newaim.purchase.flow.purchase.vo.FlowCustomClearancePackingVo;
import com.newaim.purchase.flow.purchase.vo.FlowCustomClearanceVo;
import com.newaim.purchase.flow.workflow.service.FlowServiceBase;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.LinkedHashMap;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class FlowCustomClearanceService extends FlowServiceBase {

    private static final String FILE_MODULE_NAME =  ConstantsAttachment.Status.FlowCustomClearance.code;

    @Autowired
    private FlowCustomClearanceDao flowCustomClearanceDao;

    @Autowired
    private FlowCustomClearancePackingDetailDao flowCustomClearancePackingDetailDao;

    @Autowired
    private FlowCustomClearancePackingDao flowCustomClearancePackingDao;

    @Autowired
    private FlowCustomClearancePackingService flowCustomClearancePackingService;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private PurchaseContractService purchaseContractService;

    @Autowired
    private PurchaseContractDao purchaseContractDao;

    @Autowired
    private PurchasePlanDetailDao purchasePlanDetailDao;

    public Page<FlowCustomClearanceVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<FlowCustomClearance> spec = buildSpecification(params);
        Page<FlowCustomClearance> p = flowCustomClearanceDao.findAll(spec, pageRequest);
        Page<FlowCustomClearanceVo> page = p.map(flowCustomClearance -> convertToFlowCustomClearanceVo(flowCustomClearance));
        return page;
    }

    private FlowCustomClearanceVo convertToFlowCustomClearanceVo(FlowCustomClearance flowCustomClearance){
        FlowCustomClearanceVo vo = BeanMapper.map(flowCustomClearance, FlowCustomClearanceVo.class);
        return vo;
    }

    public FlowCustomClearance getFlowCustomClearance(String id){
        return flowCustomClearanceDao.findOne(id);
    }


    /**
     * 获取信息
     * @param id
     * @return
     */
    public FlowCustomClearanceVo get(String id){
        FlowCustomClearanceVo vo =convertToFlowCustomClearanceVo(getFlowCustomClearance(id));
        vo.setAttachments(attachmentService.listByBusinessIdAndModuleName(id, FILE_MODULE_NAME) );
        vo.setDetails(flowCustomClearancePackingService.findPackingsVoByBusinessId(id));
        //获取photo附件
        vo.setImagesDoc(attachmentService.listByBusinessIdAndModuleName(id, ConstantsAttachment.Status.FlowCustomClearance_Photo.code));
        if (vo.getImagesDoc() != null) {
            List<String> list = Lists.newArrayList();
            for (AttachmentVo atta : vo.getImagesDoc()) {
                list.add(atta.getDocumentId());
            }
            vo.setImages(StringUtils.join(list.toArray(), ","));
        }

        if(StringUtils.isNotBlank(vo.getOrderId())){
            PurchaseContract pc = purchaseContractService.getPurchaseContract(vo.getOrderId());
            if(pc != null){
                vo.setNextHandlerId(pc.getCreatorId());
            }
        }
        return vo;
    }

    private Specification<FlowCustomClearance> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<FlowCustomClearance> spec = DynamicSpecifications.bySearchFilter(filters.values(), FlowCustomClearance.class);
        return spec;
    }

    /**
     * 新建，设置创建人信息，供应商，附件，绑定details数据
     * @param flowCustomClearance
     * @param details
     * @param attachments
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public FlowCustomClearance add(FlowCustomClearance flowCustomClearance, List<FlowCustomClearancePacking> details, List<Attachment> attachments){
        //设置创建人信息
        setFlowCreatorInfo(flowCustomClearance);
        flowCustomClearance = flowCustomClearanceDao.save(flowCustomClearance);
        //保存Detail信息
        saveDetails(flowCustomClearance,details);

        //保存附件
        attachmentService.save(attachments, FILE_MODULE_NAME, flowCustomClearance.getId());

        //保存图片附件
        setPhotosFromImages(flowCustomClearance);

        return flowCustomClearance;
    }

    /**
     * 修改，设置供应商、更新时间，重新绑定details数据
     * @param flowCustomClearance
     * @param details
     * @param attachments
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public FlowCustomClearance update(FlowCustomClearance flowCustomClearance, List<FlowCustomClearancePacking> details, List<Attachment> attachments){
        //设置更新时间
        flowCustomClearance.setUpdatedAt(new Date());
        //先删除packingList明细
        List<FlowCustomClearancePackingVo> packings = flowCustomClearancePackingService.findPackingsVoByBusinessId(flowCustomClearance.getId());
        if(packings != null){
            for(int i=0;i<packings.size();i++) {
                FlowCustomClearancePackingVo packing = packings.get(i);
                flowCustomClearancePackingDetailDao.deleteByPackingId(packing.getId());
            }
        }
        //再删除detail明细
        flowCustomClearancePackingDao.deleteByBusinessId(flowCustomClearance.getId());
        saveDetails(flowCustomClearance,details);


        //更新附件
        attachmentService.save(attachments, FILE_MODULE_NAME, flowCustomClearance.getId());

        //保存图片附件
        setPhotosFromImages(flowCustomClearance);

        return flowCustomClearanceDao.save(flowCustomClearance);
    }

    /**
     * 复制另存
     */
    @Transactional(rollbackFor = Exception.class)
    public FlowCustomClearance saveAs(FlowCustomClearance flowCustomClearance, List<FlowCustomClearancePacking> details, List<Attachment> attachments){
        flowCustomClearanceDao.clear();
        //设置创建人信息
        setFlowCreatorInfo(flowCustomClearance);
        //清理信息
        cleanInfoForSaveAs(flowCustomClearance);
        //保存
        flowCustomClearance = flowCustomClearanceDao.save(flowCustomClearance);
        saveDetails(flowCustomClearance,details);
        //保存附件
        attachmentService.save(attachments, FILE_MODULE_NAME, flowCustomClearance.getId());

        //保存图片附件
        setPhotosFromImages(flowCustomClearance);

        return flowCustomClearance;
    }

    /**
     * 保存时,跟Packing表建立关联关系
     */
    private void saveDetails(FlowCustomClearance flowCustomClearance, List<FlowCustomClearancePacking> details){
        //循环保存detail和packingDetails信息
        if (details != null){
            List<FlowCustomClearancePackingDetail> allPackingDetails = Lists.newArrayList();
            for(int i=0;i<details.size();i++) {
                FlowCustomClearancePacking packing = details.get(i);
                //设置装柜单的订单信息
                packing.setOrderId(flowCustomClearance.getOrderId());
                packing.setOrderNumber(flowCustomClearance.getOrderNumber());
                packing.setOrderTitle(flowCustomClearance.getOrderTitle());
                //设置对方的订单号
                packing.setCiNumber(flowCustomClearance.getCiNumber());
                packing.setBusinessId(flowCustomClearance.getId());
                packing.setHold(Constants.HoldStatus.UN_HOLD.code);
                flowCustomClearancePackingDao.save(packing);
                for (int j = 0; j < packing.getPackingDetails().size(); j++) {
                    FlowCustomClearancePackingDetail packingDetail = packing.getPackingDetails().get(j);
                    packingDetail.setPackingId(packing.getId());
                    flowCustomClearancePackingDetailDao.save(packingDetail);
                    allPackingDetails.add(packingDetail);
                }
            }


            //对sku做合并计算比较
            Map<String, List<FlowCustomClearancePackingDetail>> temp = Maps.newHashMap();
            for (int i = 0; i < allPackingDetails.size(); i++) {
                FlowCustomClearancePackingDetail detail = allPackingDetails.get(i);
                //额外添加的产品不计入
                if(detail.getOrderQty() != null ){
                    String productId = detail.getProductId();
                    if(temp.containsKey(productId)){
                        temp.get(productId).add(detail);
                    }else{
                        List<FlowCustomClearancePackingDetail> newDetails = Lists.newArrayList();
                        newDetails.add(detail);
                        temp.put(productId, newDetails);
                    }
                }
            }

            Map<String,FlowCustomClearancePackingDetailVo> qtyMap = Maps.newHashMap();
            for (String productId : temp.keySet()) {
                List<FlowCustomClearancePackingDetail> packingDetails = temp.get(productId);

                BigDecimal totalPackingQty = BigDecimal.ZERO;
                BigDecimal totalSrcPackingQty = BigDecimal.ZERO;
                Integer orderQty = packingDetails.get(0).getOrderQty();
                for (int i = 0; i < packingDetails.size(); i++) {
                    FlowCustomClearancePackingDetail detail = packingDetails.get(i);
                    totalPackingQty = totalPackingQty.add(BigDecimal.valueOf(detail.getPackingQty()));
                    totalSrcPackingQty = totalSrcPackingQty.add(BigDecimal.valueOf(detail.getSrcPackingQty() != null ? detail.getSrcPackingQty() : 0));
                }
                if(totalPackingQty.subtract(totalSrcPackingQty).intValue() != orderQty){
                    FlowCustomClearancePackingDetailVo qtyDetail = new FlowCustomClearancePackingDetailVo();
                    qtyDetail.setPackingQty(totalPackingQty.intValue());
                    qtyDetail.setSrcPackingQty(totalSrcPackingQty.intValue());
                    qtyMap.put(productId, qtyDetail);
                }

                Integer diffQty = 0;
                if(totalSrcPackingQty.equals(BigDecimal.ZERO)){
                    //新增时,如果订单中sku数量与清关中的sku件数不一致
                    diffQty = orderQty - totalPackingQty.intValue();
                }else{
                    //编辑时,sku数量有变动
                    diffQty = totalSrcPackingQty.subtract(totalPackingQty).intValue();
                }
                //原装柜数量超出计划数量
                if(totalSrcPackingQty.intValue() > orderQty){
                    diffQty = diffQty - (totalSrcPackingQty.intValue() - orderQty);
                    if(diffQty < 0){
                        diffQty =  0;
                    }
                }
                //如有有差异()
                if(diffQty != 0){
                    List<PurchasePlanDetail> planDetails = purchasePlanDetailDao.findByOrderIdAndProductId(flowCustomClearance.getOrderId(), productId);
                    for (int i = 0; i < planDetails.size(); i++) {
                        if(diffQty != 0){
                            PurchasePlanDetail planDetail = planDetails.get(i);
                            Integer planOrderQty = planDetail.getOrderQty();
                            Integer alreadyOrderQty = planDetail.getAlreadyOrderQty();
                            Integer qty = planOrderQty - (alreadyOrderQty - diffQty);
                            if(qty >= 0){
                                planDetail.setAlreadyOrderQty(alreadyOrderQty - diffQty);
                                break;
                            }else{
                                planDetail.setAlreadyOrderQty(planOrderQty);
                                diffQty = diffQty + qty;
                            }
                            purchasePlanDetailDao.save(planDetail);
                        }else{
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * 写photos附件
     *
     * @param flowCustomClearance
     */
    public void setPhotosFromImages(FlowCustomClearance flowCustomClearance) {
        if (flowCustomClearance.getImages() != null && StringUtils.isNotBlank(flowCustomClearance.getImages())) {
            List<Attachment> attachments = Lists.newArrayList();
            String[] images = flowCustomClearance.getImages().split(",");
            for (String image : images) {
                Attachment atta = new Attachment();
                atta.setDocumentId(image);
                attachments.add(atta);
            }
            attachments = attachmentService.save(attachments, ConstantsAttachment.Status.FlowCustomClearance_Photo.code, flowCustomClearance.getId());

            //回写photos
            if (attachments.size() > 0) {
                List<String> attaIds = Lists.newArrayList();
                for (Attachment atta : attachments) {
                    attaIds.add(atta.getId());
                }
                flowCustomClearance.setPhotos(StringUtils.join(attaIds.toArray(), ","));
            }

        } else {
            attachmentService.deleteByBusinessId(flowCustomClearance.getId(), ConstantsAttachment.Status.FlowCustomClearance_Photo.code);
            flowCustomClearance.setPhotos(null);
        }
    }

    /**
     * 冻结
     * @param flowId
     */
    @Transactional(rollbackFor = Exception.class)
    public void hold(String flowId){
        FlowCustomClearance flow = flowCustomClearanceDao.findOne(flowId);
        flow.setHold(Constants.HoldStatus.HOLD.code);
        flowCustomClearanceDao.save(flow);
    }

    /**
     * 解除冻结
     * @param flowId
     */
    @Transactional(rollbackFor = Exception.class)
    public void unHold(String flowId){
        FlowCustomClearance flow = flowCustomClearanceDao.findOne(flowId);
        flow.setHold(Constants.HoldStatus.UN_HOLD.code);
        flowCustomClearanceDao.save(flow);
    }


    /**
     * 标记删除状态
     * @param flowCustomClearance
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(FlowCustomClearance flowCustomClearance){
        flowCustomClearance.setUpdatedAt(new Date());
        flowCustomClearance.setStatus(Constants.Status.DELETED.code);
        flowCustomClearanceDao.save(flowCustomClearance);
    }

    public List<FlowCustomClearance> findByOrderIds(List<String> orderIds){
        return flowCustomClearanceDao.findByOrderIds(orderIds);
    }
}
