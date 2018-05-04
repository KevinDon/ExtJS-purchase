package com.newaim.purchase.flow.shipping.service;

import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.purchase.Constants;
import com.newaim.purchase.archives.flow.purchase.dao.PurchaseContractDao;
import com.newaim.purchase.archives.flow.purchase.entity.PurchaseContract;
import com.newaim.purchase.archives.flow.shipping.dao.OrderShippingPlanDao;
import com.newaim.purchase.archives.flow.shipping.dao.OrderShippingPlanDetailDao;
import com.newaim.purchase.archives.flow.shipping.entity.OrderShippingPlan;
import com.newaim.purchase.archives.flow.shipping.entity.OrderShippingPlanDetail;
import com.newaim.purchase.archives.flow.shipping.service.ServiceProviderQuotationService;
import com.newaim.purchase.flow.shipping.dao.FlowOrderShippingPlanDao;
import com.newaim.purchase.flow.shipping.dao.FlowOrderShippingPlanDetailDao;
import com.newaim.purchase.flow.shipping.entity.FlowOrderShippingPlan;
import com.newaim.purchase.flow.shipping.entity.FlowOrderShippingPlanDetail;
import com.newaim.purchase.flow.shipping.vo.FlowOrderShippingPlanVo;
import com.newaim.purchase.flow.workflow.service.FlowServiceBase;
import org.springframework.beans.factory.annotation.Autowired;
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
public class FlowOrderShippingPlanService extends FlowServiceBase {

    @Autowired
    private FlowOrderShippingPlanDao flowOrderShippingPlanDao;

    @Autowired
    private OrderShippingPlanDao orderShippingPlanDao;

    @Autowired
    private OrderShippingPlanDetailDao orderShippingPlanDetailDao;

    @Autowired
    private FlowOrderShippingPlanDetailDao flowOrderShippingPlanDetailDao;

    @Autowired
    private FlowOrderShippingPlanDetailService flowOrderShippingPlanDetailService;

    @Autowired
    private ServiceProviderQuotationService serviceProviderQuotationService;

    @Autowired
    private PurchaseContractDao purchaseContractDao;


    /**
     * 分页查询所有信息
     *
     * @param params
     * @param pageNumber
     * @param pageSize
     * @param sort
     * @return
     */
    public Page<FlowOrderShippingPlanVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort) {
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<FlowOrderShippingPlan> spec = buildSpecification(params);
        Page<FlowOrderShippingPlan> p = flowOrderShippingPlanDao.findAll(spec, pageRequest);
        Page<FlowOrderShippingPlanVo> page = p.map(flowOrderShippingPlan -> convertToFlowOrderShippingPlanVo(flowOrderShippingPlan));
        return page;
    }

    /**
     * 分页查询所有信息
     *
     * @param params
     * @param pageNumber
     * @param pageSize
     * @param sort
     * @return
     */
    public Page<FlowOrderShippingPlanVo> listForDialog(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort) {
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<FlowOrderShippingPlan> spec = buildSpecification(params);
        Page<FlowOrderShippingPlan> p = flowOrderShippingPlanDao.findAll(spec, pageRequest);
        Page<FlowOrderShippingPlanVo> page = p.map(flowOrderShippingPlan -> {
            FlowOrderShippingPlanVo vo = convertToFlowOrderShippingPlanVo(flowOrderShippingPlan);
            vo.setDetails(flowOrderShippingPlanDetailService.findDetailsVoByBusinessId(vo.getId()));
            return vo;
        });
        return page;
    }
    /**
     * 将entity转换为Vo
     *
     * @param flowOrderShippingPlan
     * @return
     */
    private FlowOrderShippingPlanVo convertToFlowOrderShippingPlanVo(FlowOrderShippingPlan flowOrderShippingPlan) {
        FlowOrderShippingPlanVo vo = BeanMapper.map(flowOrderShippingPlan, FlowOrderShippingPlanVo.class);
        return vo;
    }

    public FlowOrderShippingPlan getFlowOrderShippingPlan(String id) {
        return flowOrderShippingPlanDao.findOne(id);
    }

    /**
     * 根据ID获取主表和details信息
     *
     * @param id
     * @return
     */
    public FlowOrderShippingPlanVo get(String id) {
        FlowOrderShippingPlanVo vo = convertToFlowOrderShippingPlanVo(getFlowOrderShippingPlan(id));
        vo.setDetails(flowOrderShippingPlanDetailService.findDetailsVoByBusinessId(id));
        vo.setQuotations(serviceProviderQuotationService.findQuotationVosByFlowOrderShippingPlanId(id));
        return vo;
    }


    private Specification<FlowOrderShippingPlan> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<FlowOrderShippingPlan> spec = DynamicSpecifications.bySearchFilter(filters.values(), FlowOrderShippingPlan.class);
        return spec;
    }

    /**
     * 新建，设置创建人信息、绑定details数据
     *
     * @param flowOrderShippingPlan
     * @param details
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public FlowOrderShippingPlan add(FlowOrderShippingPlan flowOrderShippingPlan, List<FlowOrderShippingPlanDetail> details) {
        //設置創建人信息
        setFlowCreatorInfo(flowOrderShippingPlan);
        //设置服务商信息
        setFlowServiceProviderInfo(flowOrderShippingPlan);
        setTotalContainerQty(flowOrderShippingPlan,details);
        flowOrderShippingPlan.setFlagOrderShippingApplyStatus(2);
        flowOrderShippingPlan.setFlagCostStatus(2);
        flowOrderShippingPlanDao.save(flowOrderShippingPlan);
        saveDetails(flowOrderShippingPlan, details);
        flowOrderShippingPlanDao.save(flowOrderShippingPlan);
        return flowOrderShippingPlan;
    }

    /**
     * 更新，设置更新时间、处理人信息，重新绑定details数据
     *
     * @param flowOrderShippingPlan
     * @param details
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public FlowOrderShippingPlan update(FlowOrderShippingPlan flowOrderShippingPlan, List<FlowOrderShippingPlanDetail> details) {
        //设置更新时间
        flowOrderShippingPlan.setUpdatedAt(new Date());
        //设置服务商信息
        setFlowServiceProviderInfo(flowOrderShippingPlan);
        setTotalContainerQty(flowOrderShippingPlan,details);
        flowOrderShippingPlan.setFlagOrderShippingApplyStatus(2);
        flowOrderShippingPlan.setFlagCostStatus(2);
        //删除之前明细，重新绑定数据
        flowOrderShippingPlanDetailDao.deleteByBusinessId(flowOrderShippingPlan.getId());
        saveDetails(flowOrderShippingPlan, details);
        return flowOrderShippingPlanDao.save(flowOrderShippingPlan);
    }

    /**
     * 复制另存，初始化数据，设置创建人信息，处理人、绑定details数据
     *
     * @param flowOrderShippingPlan
     * @param details
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public FlowOrderShippingPlan saveAs(FlowOrderShippingPlan flowOrderShippingPlan, List<FlowOrderShippingPlanDetail> details) {
        flowOrderShippingPlanDao.clear();
        //設置創建人信息
        setFlowCreatorInfo(flowOrderShippingPlan);
        //设置服务商信息
        setFlowServiceProviderInfo(flowOrderShippingPlan);
        setTotalContainerQty(flowOrderShippingPlan,details);
        flowOrderShippingPlan.setFlagOrderShippingApplyStatus(2);
        flowOrderShippingPlan.setFlagCostStatus(2);
        //清理信息
        cleanInfoForSaveAs(flowOrderShippingPlan);
        flowOrderShippingPlanDao.save(flowOrderShippingPlan);
        saveDetails(flowOrderShippingPlan, details);
        flowOrderShippingPlanDao.save(flowOrderShippingPlan);
        return flowOrderShippingPlan;
    }

    /**
     * 更新流程状态
     */
    @Transactional(rollbackFor = Exception.class)
    public FlowOrderShippingPlan updateFlowCompleteStatus(String id) {
        FlowOrderShippingPlan flowOrderShippingPlan = getFlowOrderShippingPlan(id);
        flowOrderShippingPlan.setFlowStatus(Constants.FlowStatus.PASS.code);
        flowOrderShippingPlan.setStatus(Constants.Status.NORMAL.code);
        return flowOrderShippingPlanDao.save(flowOrderShippingPlan);
    }

    /**
     * 保存时建立关联关系
     */
    private void saveDetails(FlowOrderShippingPlan flowOrderShippingPlan, List<FlowOrderShippingPlanDetail> details) {
        if (details != null && details.size() > 0) {
            StringBuilder orderNumbers = new StringBuilder();
            for (int i = 0; i < details.size(); i++) {
                FlowOrderShippingPlanDetail detail = details.get(i);
                detail.setBusinessId(flowOrderShippingPlan.getId());
                detail.setServiceProviderId(flowOrderShippingPlan.getServiceProviderId());
                detail.setServiceProviderCnName(flowOrderShippingPlan.getServiceProviderCnName());
                detail.setServiceProviderEnName(flowOrderShippingPlan.getServiceProviderEnName());
                if(i > 0){
                    orderNumbers.append(",");
                }
                orderNumbers.append(detail.getOrderNumber());
            }
            flowOrderShippingPlan.setOrderNumbers(orderNumbers.toString());
            flowOrderShippingPlanDetailDao.save(details);
        }
    }

    /**
     * 设置总柜数
     * @param flowOrderShippingPlan
     * @param details
     */
    private void setTotalContainerQty(FlowOrderShippingPlan flowOrderShippingPlan,List<FlowOrderShippingPlanDetail> details){
        BigDecimal totalContainerQty = BigDecimal.ZERO;
        if (details!=null){
            for (FlowOrderShippingPlanDetail detail : details){
                if (detail.getContainerQty()!=null){
                    totalContainerQty = totalContainerQty.add(detail.getContainerQty());
                }
            }
        }
        flowOrderShippingPlan.setTotalContainerQty(totalContainerQty);
    }

    /**
     * 冻结
     * @param flowId
     */
    @Transactional(rollbackFor = Exception.class)
    public void hold(String flowId){
        FlowOrderShippingPlan flow = flowOrderShippingPlanDao.findOne(flowId);
        flow.setHold(Constants.HoldStatus.HOLD.code);
        flowOrderShippingPlanDao.save(flow);
    }

    /**
     * 解除冻结
     * @param flowId
     */
    @Transactional(rollbackFor = Exception.class)
    public void unHold(String flowId){
        FlowOrderShippingPlan flow = flowOrderShippingPlanDao.findOne(flowId);
        flow.setHold(Constants.HoldStatus.UN_HOLD.code);
        flowOrderShippingPlanDao.save(flow);
    }

    @Transactional(rollbackFor = Exception.class)
    public void cancel(String businessId){
        FlowOrderShippingPlan flowOrderShippingPlan = flowOrderShippingPlanDao.findOne(businessId);
        flowOrderShippingPlan.setStatus(Constants.Status.CANCELED.code);
        flowOrderShippingPlanDao.save(flowOrderShippingPlan);
        if(flowOrderShippingPlan.getFlagOrderShippingApplyStatus() == null || flowOrderShippingPlan.getFlagOrderShippingApplyStatus() == 2){
            OrderShippingPlan orderShippingPlan = orderShippingPlanDao.getByBusinessId(businessId);
            if(orderShippingPlan != null){
                orderShippingPlan.setStatus(Constants.Status.CANCELED.code);
                orderShippingPlanDao.save(orderShippingPlan);
                List<OrderShippingPlanDetail> details = orderShippingPlanDetailDao.findDetailsByOrderShippingPlanId(orderShippingPlan.getId());
                for (int i = 0; i < details.size(); i++) {
                    PurchaseContract order = purchaseContractDao.findOne(details.get(i).getOrderId());
                    order.setFlagOrderShippingPlanStatus(2);
                    purchaseContractDao.save(order);
                }
            }
        }
    }

    /**
     * 刪除（改变状态并非真正删除）
     * @param flowOrderShippingPlan
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(FlowOrderShippingPlan flowOrderShippingPlan){
        flowOrderShippingPlan.setUpdatedAt(new Date());
        flowOrderShippingPlan.setStatus(Constants.Status.DELETED.code);
        flowOrderShippingPlanDao.save(flowOrderShippingPlan);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateOrderShippingStatusByBusinessId(String businessId, Integer status){
        List<FlowOrderShippingPlanDetail> details = flowOrderShippingPlanDetailService.findDetailsByBusinessId(businessId);
        for (int i = 0; i < details.size(); i++) {
            FlowOrderShippingPlanDetail detail = details.get(i);
            PurchaseContract order = purchaseContractDao.findOne(detail.getOrderId());
            order.setFlagOrderShippingPlanStatus(status);
            purchaseContractDao.save(order);
        }
    }

    public List<FlowOrderShippingPlan> findByServiceProviderQuotationBusinessId(String serviceProviderQuotationBusinessId){
        return flowOrderShippingPlanDao.findByServiceProviderQuotationBusinessId(serviceProviderQuotationBusinessId);
    }
}
