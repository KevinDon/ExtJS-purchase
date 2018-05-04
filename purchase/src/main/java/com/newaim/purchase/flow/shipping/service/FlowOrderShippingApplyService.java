package com.newaim.purchase.flow.shipping.service;


import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.purchase.Constants;
import com.newaim.purchase.archives.flow.purchase.dao.PurchaseContractDao;
import com.newaim.purchase.archives.flow.purchase.entity.PurchaseContract;
import com.newaim.purchase.archives.flow.shipping.dao.OrderShippingApplyDao;
import com.newaim.purchase.archives.flow.shipping.dao.OrderShippingApplyDetailDao;
import com.newaim.purchase.archives.flow.shipping.dao.OrderShippingPlanDao;
import com.newaim.purchase.archives.flow.shipping.entity.OrderShippingApply;
import com.newaim.purchase.archives.flow.shipping.entity.OrderShippingPlan;
import com.newaim.purchase.flow.shipping.dao.FlowOrderShippingApplyDao;
import com.newaim.purchase.flow.shipping.dao.FlowOrderShippingApplyDetailDao;
import com.newaim.purchase.flow.shipping.dao.FlowOrderShippingPlanDao;
import com.newaim.purchase.flow.shipping.entity.FlowOrderShippingApply;
import com.newaim.purchase.flow.shipping.entity.FlowOrderShippingApplyDetail;
import com.newaim.purchase.flow.shipping.entity.FlowOrderShippingPlan;
import com.newaim.purchase.flow.shipping.vo.FlowOrderShippingApplyVo;
import com.newaim.purchase.flow.workflow.service.FlowServiceBase;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class FlowOrderShippingApplyService extends FlowServiceBase {

    @Autowired
    private FlowOrderShippingApplyDao flowOrderShippingApplyDao;

    @Autowired
    private FlowOrderShippingApplyDetailDao flowOrderShippingApplyDetailDao;

    @Autowired
    private FlowOrderShippingPlanDao flowOrderShippingPlanDao;

    @Autowired
    private FlowOrderShippingApplyDetailService flowOrderShippingApplyDetailService;

    @Autowired
    private OrderShippingPlanDao orderShippingPlanDao;

    @Autowired
    private OrderShippingApplyDao orderShippingApplyDao;

    @Autowired
    private OrderShippingApplyDetailDao orderShippingApplyDetailDao;

    @Autowired
    private PurchaseContractDao purchaseContractDao;

    /**
     * 分页查询List
     * @param params
     * @param pageNumber
     * @param pageSize
     * @param sort
     * @return
     */
    public Page<FlowOrderShippingApplyVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<FlowOrderShippingApply> spec = buildSpecification(params);
        Page<FlowOrderShippingApply> p = flowOrderShippingApplyDao.findAll(spec, pageRequest);
        Page<FlowOrderShippingApplyVo> page = p.map(new Converter<FlowOrderShippingApply, FlowOrderShippingApplyVo>() {
            @Override
            public FlowOrderShippingApplyVo convert(FlowOrderShippingApply flowOrderShippingApply) {
            	FlowOrderShippingApplyVo vo = convertToFlowOrderShippingApplyVo(flowOrderShippingApply);
                vo.setDetails(flowOrderShippingApplyDetailService.findDetailsVoByBusinessId(vo.getId()));
                return vo;
            }
        });
        return page;
    }

    /**
     * 将entity转换为Vo
     * @param flowOrderShippingApply
     * @return
     */
    private FlowOrderShippingApplyVo convertToFlowOrderShippingApplyVo(FlowOrderShippingApply flowOrderShippingApply){
        FlowOrderShippingApplyVo vo = BeanMapper.map(flowOrderShippingApply, FlowOrderShippingApplyVo.class);
        return vo;
    }

    public FlowOrderShippingApply getFlowOrderShippingApply(String id){
        return flowOrderShippingApplyDao.findOne(id);
    }

    /**
     * 根据ID获取主表和details信息
     * @param id
     * @return
     */
    public FlowOrderShippingApplyVo get(String id){
        FlowOrderShippingApplyVo vo = convertToFlowOrderShippingApplyVo(getFlowOrderShippingApply(id));
        vo.setDetails(flowOrderShippingApplyDetailService.findDetailsVoByBusinessId(id));
        return vo;
    }

    private Specification<FlowOrderShippingApply> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<FlowOrderShippingApply> spec = DynamicSpecifications.bySearchFilter(filters.values(), FlowOrderShippingApply.class);
        return spec;
    }

    /**
     * 新建，设置创建人信息，绑定details数据
     * @param flowOrderShippingApply
     * @param details
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public FlowOrderShippingApply add(FlowOrderShippingApply flowOrderShippingApply, List<FlowOrderShippingApplyDetail> details){
        //設置創建人信息
        setFlowCreatorInfo(flowOrderShippingApply);

        flowOrderShippingApplyDao.save(flowOrderShippingApply);
        saveDetails(flowOrderShippingApply, details);
        flowOrderShippingApplyDao.save(flowOrderShippingApply);
        return flowOrderShippingApply;
    }

    /**
     * 更新，设置更新时间，重新绑定details数据
     * @param flowOrderShippingApply
     * @param details
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public FlowOrderShippingApply update(FlowOrderShippingApply flowOrderShippingApply, List<FlowOrderShippingApplyDetail> details){
        //设置更新时间
        flowOrderShippingApply.setUpdatedAt(new Date());
        //删除之前明细，重新绑定数据
        flowOrderShippingApplyDetailDao.deleteByBusinessId(flowOrderShippingApply.getId());
        saveDetails(flowOrderShippingApply, details);
        return flowOrderShippingApplyDao.save(flowOrderShippingApply);
    }

    /**
     * 复制另存
     */
    @Transactional(rollbackFor = Exception.class)
    public FlowOrderShippingApply saveAs(FlowOrderShippingApply flowOrderShippingApply, List<FlowOrderShippingApplyDetail> details){
        flowOrderShippingApplyDao.clear();
        //設置創建人信息
        setFlowCreatorInfo(flowOrderShippingApply);
        //清理信息
        cleanInfoForSaveAs(flowOrderShippingApply);
        flowOrderShippingApply = flowOrderShippingApplyDao.save(flowOrderShippingApply);
        saveDetails(flowOrderShippingApply, details);
        flowOrderShippingApplyDao.save(flowOrderShippingApply);
        return flowOrderShippingApply;
    }


    /**
     * 保存时建立关联关系
     */
    private void saveDetails(FlowOrderShippingApply flowOrderShippingApply, List<FlowOrderShippingApplyDetail> details){
        if(details != null && details.size() > 0){
            StringBuilder orderNumbers = new StringBuilder();
            for (int i = 0; i < details.size(); i++) {
                details.get(i).setBusinessId(flowOrderShippingApply.getId());
                if(i > 0){
                    orderNumbers.append(",");
                }
                orderNumbers.append(details.get(i).getOrderNumber());
            }
            flowOrderShippingApply.setOrderNumbers(orderNumbers.toString());
            flowOrderShippingApplyDetailDao.save(details);
        }
    }

    /**
     * 冻结
     * @param flowId
     */
    @Transactional(rollbackFor = Exception.class)
    public void hold(String flowId){
        FlowOrderShippingApply flow = flowOrderShippingApplyDao.findOne(flowId);
        flow.setHold(Constants.HoldStatus.HOLD.code);
        flowOrderShippingApplyDao.save(flow);
    }

    /**
     * 解除冻结
     * @param flowId
     */
    @Transactional(rollbackFor = Exception.class)
    public void unHold(String flowId){
        FlowOrderShippingApply flow = flowOrderShippingApplyDao.findOne(flowId);
        flow.setHold(Constants.HoldStatus.UN_HOLD.code);
        flowOrderShippingApplyDao.save(flow);
    }

    /**
     * 刪除（改变状态并非真正删除）
     * @param flowOrderShippingApply
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(FlowOrderShippingApply flowOrderShippingApply){
        flowOrderShippingApply.setUpdatedAt(new Date());
        flowOrderShippingApply.setStatus(Constants.Status.DELETED.code);
        flowOrderShippingApplyDao.save(flowOrderShippingApply);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateOrderShippingStatusByBusinessId(String businessId, Integer status){
        List<FlowOrderShippingApplyDetail> details = flowOrderShippingApplyDetailDao.findDetailsByBusinessId(businessId);
        for (int i = 0; i < details.size(); i++) {
            FlowOrderShippingApplyDetail detail = details.get(i);
            OrderShippingPlan orderShippingPlan = orderShippingPlanDao.findOne(detail.getOrderShippingPlanId());
            orderShippingPlan.setFlagOrderShippingApplyStatus(1);
            orderShippingPlanDao.save(orderShippingPlan);
        }
    }

    public List<FlowOrderShippingApply> findByOrderShippingPlanBusinessId(String orderShippingPlanBusinessId){
        return flowOrderShippingApplyDao.findByOrderShippingPlanBusinessId(orderShippingPlanBusinessId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void cancel(String businessId){
        FlowOrderShippingApply flowOrderShippingApply = flowOrderShippingApplyDao.findOne(businessId);
        flowOrderShippingApply.setStatus(Constants.Status.CANCELED.code);
        flowOrderShippingApplyDao.save(flowOrderShippingApply);

        List<FlowOrderShippingApplyDetail> flowOrderShippingApplyDetails = flowOrderShippingApplyDetailDao.findDetailsByBusinessId(businessId);
        if(flowOrderShippingApplyDetails != null && flowOrderShippingApplyDetails.size()>0){
            for (int i = 0; i < flowOrderShippingApplyDetails.size(); i++) {
                //修改对应的业务表发货确认记录状态
                List<OrderShippingApply> orderShippingApplies = orderShippingApplyDao.findByBusinessId(flowOrderShippingApplyDetails.get(i).getBusinessId());
                if(orderShippingApplies != null && orderShippingApplies.size()>0){
                    for (int j=0; j<orderShippingApplies.size(); j++){
                        orderShippingApplies.get(j).setStatus(Constants.Status.CANCELED.code);
                    }
                    orderShippingApplyDao.save(orderShippingApplies);
                }

                //修改业务表发货计划标记位
                OrderShippingPlan orderShippingPlan = orderShippingPlanDao.getOne(flowOrderShippingApplyDetails.get(i).getOrderShippingPlanId());
                if(orderShippingPlan != null) {
                    orderShippingPlan.setFlagOrderShippingApplyStatus(2);
                    orderShippingPlan.setFlagOrderShippingApplyId(null);
                    orderShippingPlan.setFlagOrderShippingApplyTime(null);
                    orderShippingPlanDao.save(orderShippingPlan);
                }

                //修改流程表发货计划标记位
                FlowOrderShippingPlan flowOrderShippingPlan = flowOrderShippingPlanDao.getOne(flowOrderShippingApplyDetails.get(i).getOrderShippingPlanBusinessId());
                if(flowOrderShippingPlan != null) {
                    flowOrderShippingPlan.setFlagOrderShippingApplyStatus(2);
                    flowOrderShippingPlan.setFlagOrderShippingApplyId(null);
                    flowOrderShippingPlan.setFlagOrderShippingApplyTime(null);
                    flowOrderShippingPlanDao.save(flowOrderShippingPlan);
                }

                //修改业务表订单标记
                PurchaseContract order = purchaseContractDao.findOne(flowOrderShippingApplyDetails.get(i).getOrderId());
                if(order != null) {
                    order.setFlagOrderShippingApplyStatus(2);
                    order.setFlagOrderShippingApplyId(null);
                    order.setFlagOrderShippingApplyTime(null);
                    purchaseContractDao.save(order);
                }

            }
        }

    }
}
