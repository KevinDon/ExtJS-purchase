package com.newaim.purchase.archives.product.service;


import com.google.common.collect.Lists;
import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.admin.account.vo.UserDepartmentVo;
import com.newaim.purchase.admin.account.vo.UserVo;
import com.newaim.purchase.admin.system.entity.Attachment;
import com.newaim.purchase.admin.system.service.AttachmentService;
import com.newaim.purchase.archives.flow.finance.service.FeePaymentService;
import com.newaim.purchase.archives.flow.finance.service.FeeRegisterDetailService;
import com.newaim.purchase.archives.flow.finance.service.PurchaseContractDepositService;
import com.newaim.purchase.archives.flow.finance.vo.FeePaymentVo;
import com.newaim.purchase.archives.flow.finance.vo.FeeRegisterDetailVo;
import com.newaim.purchase.archives.flow.finance.vo.PurchaseContractDepositVo;
import com.newaim.purchase.archives.flow.purchase.dao.CustomClearancePackingDao;
import com.newaim.purchase.archives.flow.purchase.dao.PurchaseContractDao;
import com.newaim.purchase.archives.flow.purchase.entity.CustomClearancePacking;
import com.newaim.purchase.archives.flow.purchase.entity.PurchaseContract;
import com.newaim.purchase.archives.flow.purchase.service.PurchaseContractOtherDetailService;
import com.newaim.purchase.archives.flow.purchase.vo.PurchaseContractOtherDetailVo;
import com.newaim.purchase.archives.flow.shipping.dao.OrderShippingPlanDao;
import com.newaim.purchase.archives.flow.shipping.dao.OrderShippingPlanDetailDao;
import com.newaim.purchase.archives.flow.shipping.entity.OrderShippingPlan;
import com.newaim.purchase.archives.flow.shipping.entity.OrderShippingPlanDetail;
import com.newaim.purchase.archives.flow.shipping.service.OrderShippingPlanService;
import com.newaim.purchase.archives.flow.shipping.vo.OrderShippingPlanDetailVo;
import com.newaim.purchase.archives.product.dao.*;
import com.newaim.purchase.archives.product.entity.*;
import com.newaim.purchase.archives.product.vo.CostFeeDetailVo;
import com.newaim.purchase.archives.product.vo.CostVo;
import com.newaim.purchase.constants.ConstantsAttachment;
import com.newaim.purchase.flow.purchase.dao.FlowPurchaseContractDao;
import com.newaim.purchase.flow.purchase.entity.FlowPurchaseContract;
import com.newaim.purchase.flow.shipping.dao.FlowOrderShippingPlanDao;
import com.newaim.purchase.flow.shipping.entity.FlowOrderShippingPlan;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class CostService extends ServiceBase {

    private static final String FILE_MODULE_NAME = ConstantsAttachment.Status.Cost.code;

    @Autowired
    private CostDao costDao;

    @Autowired
    private CostProductDao costProductDao;

    @Autowired
    private CostPortDao costPortDao;

    @Autowired
    private CostChargeItemDao costChargeItemDao;

    @Autowired
    private CostTariffDao costTariffDao;

    @Autowired
    private CostProductCostDao costProductCostDao;

    @Autowired
    private CostProductService costProductService;

    @Autowired
    private CostPortService costPortService;

    @Autowired
    private CostChargeItemService costChargeItemService;

    @Autowired
    private CostTariffService costTariffService;

    @Autowired
    private CostProductCostService costProductCostService;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private OrderShippingPlanDetailDao orderShippingPlanDetailDao;

    @Autowired
    private FlowPurchaseContractDao flowPurchaseContractDao;

    @Autowired
    private PurchaseContractDao purchaseContractDao;

    @Autowired
    private OrderShippingPlanDao orderShippingPlanDao;

    @Autowired
    private FlowOrderShippingPlanDao flowOrderShippingPlanDao;

    @Autowired
    private CostOrderDao costOrderDao;
    @Autowired
    private CustomClearancePackingDao customClearancePackingDao;
    @Autowired
    private OrderShippingPlanService orderShippingPlanService;
    @Autowired
    private PurchaseContractOtherDetailService purchaseContractOtherDetailService;
    @Autowired
    private PurchaseContractDepositService purchaseContractDepositService;

    @Autowired
    private FeeRegisterDetailService feeRegisterDetailService;
    @Autowired
    private FeePaymentService feePaymentService;
    @Autowired
    private CostOrderService costOrderService;
    /**
     * 分页查询
     * @param params
     * @param pageNumber
     * @param pageSize
     * @param sort
     * @return
     */
    public Page<CostVo> listCost(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<Cost> spec = buildSpecification(params);
        Page<Cost> p = costDao.findAll(spec, pageRequest);
        Page<CostVo> page = p.map(cost -> convertToVo(cost));
        return page;
    }

    public Cost getCost(String id){
        return costDao.findOne(id);
    }

    /**
     * 根据ID获取附件、银行信息、产品类型、图片附件、联系人信息
     * @param id
     * @return
     */
    public CostVo get(String id){
        CostVo vo = convertToVo(getCost(id));
        vo.setAttachments(attachmentService.listByBusinessId(id));
        vo.setCostProducts(costProductService.listCostProductsByCostId(id));
        vo.setCostPorts(costPortService.listCostPortsByCostId(id));
        vo.setCostChargeItems(costChargeItemService.listCostChargeItemsByCostId(id));
        vo.setCostProductCosts(costProductCostService.listCostProductCostsByCostId(id));
        vo.setCostTariffs(costTariffService.listPortsByCostId(id));
        vo.setCostOrders(costOrderService.listCostProductCostsByCostId(id));
        return vo;
    }

    /**
     * 将entity转换为Vo
     * @param cost
     * @return
     */
    private CostVo convertToVo(Cost cost){
        CostVo vo = BeanMapper.map(cost, CostVo.class);
        return vo;

    }
    /**
     * 新增
     */
    @Transactional(rollbackFor = Exception.class)
    public void add(Cost cost, List<Attachment> attachments, List<CostProduct> costProducts,
                    List<CostPort> costPorts, List<CostChargeItem> costChargeItems,
                    List<CostTariff> costTariffs, List<CostProductCost> costProductCosts,
                    List<CostOrder> costOrders){
        //设置创建人相关信息
        setCreatorInfo(cost);
        //拼接order numbers
        cost.setOrderNumbers(joinOrderNumber(costOrders));

        cost = costDao.save(cost);

        //保存附件
        attachmentService.save(attachments, FILE_MODULE_NAME, cost.getId());
        saveCostProducts(cost.getId(), costProducts);
        saveCostPorts(cost.getId(), costPorts);
        saveCostChargeItems(cost.getId(), costChargeItems);
        saveCostTariffs(cost.getId(), costTariffs);
        saveCostProductCosts(cost.getId(), costProductCosts);
        saveCostOrder(cost.getId(),costOrders);
        //同步实际发货时间和到岸时间到合同
        updateOrderInfo(cost);
        //更新发货计划的成本计算状态
        updateOrderShippingPlanCostStatus(cost,1);
        updatePackingCostStatus(cost,costOrders,1);
    }

    /**
     * 修改成本表下面的业务装箱单的成本计算状态位改为1
     * @param cost
     * @param costOrders
     */
    private void updatePackingCostStatus(Cost cost,List<CostOrder> costOrders,int costStatus) {
        if (null != costOrders) {
            for (CostOrder costOrder : costOrders) {
                String orderId = costOrder.getOrderId();
                List<CustomClearancePacking> packings = customClearancePackingDao.findPackingsByOrderId(orderId);
                for (CustomClearancePacking packing : packings) {
                    packing.setFlagCostStatus(costStatus);
                    packing.setFlagCostId(cost.getId());
                    packing.setFlagCostTime(new Date());
                }
                customClearancePackingDao.save(packings);
            }

        }
    }

    /**
     * 保存
     * @param cost
     * @param attachments
     * @param costProducts
     * @param costPorts
     * @param costChargeItems
     * @param costTariffs
     * @param costProductCosts
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(Cost cost, List<Attachment> attachments, List<CostProduct> costProducts,
                       List<CostPort> costPorts, List<CostChargeItem> costChargeItems,
                       List<CostTariff> costTariffs, List<CostProductCost> costProductCosts){


        cost.setUpdatedAt(new Date());

        costDao.save(cost);

        //保存附件
        attachmentService.save(attachments, FILE_MODULE_NAME, cost.getId());
        saveCostProducts(cost.getId(), costProducts);
        saveCostPorts(cost.getId(), costPorts);
        saveCostChargeItems(cost.getId(), costChargeItems);
        saveCostTariffs(cost.getId(), costTariffs);
        saveCostProductCosts(cost.getId(), costProductCosts);

        //同步实际发货时间和到岸时间到合同
        updateOrderInfo(cost);
        //更新发货计划的成本计算状态
        updateOrderShippingPlanCostStatus(cost,1);
    }

    /**
     * 复制另存
     */
    @Transactional(rollbackFor = Exception.class)
    public Cost saveAs(Cost cost, List<Attachment> attachments, List<CostProduct> costProducts,
                       List<CostPort> costPorts, List<CostChargeItem> costChargeItems,
                       List<CostTariff> costTariffs, List<CostProductCost> costProductCosts,
                       List<CostOrder> costOrders){
        costDao.clear();
        //设置创建人相关信息
        setCreatorInfo(cost);
        //拼接order numbers
        cost.setOrderNumbers(joinOrderNumber(costOrders));

        costDao.save(cost);
        //保存附件
        attachmentService.save(attachments, FILE_MODULE_NAME, cost.getId());
        saveCostProducts(cost.getId(), costProducts);
        saveCostPorts(cost.getId(), costPorts);
        saveCostChargeItems(cost.getId(), costChargeItems);
        saveCostTariffs(cost.getId(), costTariffs);
        saveCostProductCosts(cost.getId(), costProductCosts);
        saveCostOrder(cost.getId(),costOrders);
        //同步实际发货时间和到岸时间到合同
        updateOrderInfo(cost);

        //更新发货计划的成本计算状态
        updateOrderShippingPlanCostStatus(cost,1);

        return cost;
    }

    /**
     * 修改发运计划
     * @param cost
     * @param costStatus
     */
    private void updateOrderShippingPlanCostStatus(Cost cost,int costStatus){
        OrderShippingPlan orderShippingPlan = orderShippingPlanDao.findOne(cost.getOrderShippingPlanId());


        if(orderShippingPlan != null){
            //修改该发运计划下的合同 成本计算标记
            List<OrderShippingPlanDetailVo> planDetails = orderShippingPlanService.getDetail(orderShippingPlan.getId());
            for (OrderShippingPlanDetailVo planDetail : planDetails) {
                PurchaseContract order = purchaseContractDao.findOne(planDetail.getOrderId());
                order.setFlagCostStatus(costStatus);
                order.setFlagCostId(cost.getId());
                order.setFlagCostTime(new Date());
                purchaseContractDao.save(order);
            }

            Date now = new Date();
            orderShippingPlan.setFlagCostId(cost.getId());
            orderShippingPlan.setFlagCostStatus(costStatus);
            orderShippingPlan.setFlagCostTime(now);
            orderShippingPlanDao.save(orderShippingPlan);
            FlowOrderShippingPlan flowOrderShippingPlan = flowOrderShippingPlanDao.findOne(orderShippingPlan.getBusinessId());
            if(flowOrderShippingPlan != null){
                flowOrderShippingPlan.setFlagCostId(cost.getId());
                flowOrderShippingPlan.setFlagCostStatus(costStatus);
                flowOrderShippingPlan.setFlagCostTime(now);
                flowOrderShippingPlanDao.save(flowOrderShippingPlan);
            }
        }
    }

    /**
     * 同步实际发货时间和到岸时间到合同（包括申请单）
     * @param cost
     */
    private void updateOrderInfo(Cost cost){
        List<OrderShippingPlanDetail> planDetails = orderShippingPlanDetailDao.findDetailsByOrderShippingPlanId(cost.getOrderShippingPlanId());
        if(planDetails != null && planDetails.size() > 0){
            for (int i = 0; i < planDetails.size(); i++) {
                OrderShippingPlanDetail planDetail = planDetails.get(i);
                PurchaseContract order = purchaseContractDao.findOne(planDetail.getOrderId());
                if(order != null){
                    order.setRetd(cost.getRetd());
                    order.setReta(cost.getReta());
                    purchaseContractDao.save(order);
                    FlowPurchaseContract flowOrder = flowPurchaseContractDao.findOne(order.getBusinessId());
                    if(flowOrder != null){
                        flowOrder.setRetd(cost.getRetd());
                        flowOrder.setReta(cost.getReta());
                        flowPurchaseContractDao.save(flowOrder);
                    }
                }
            }
        }
    }

    private void saveCostProducts(String costId, List<CostProduct> costProducts){
    	if(costId!=null){
    		costProductDao.deleteByCostId(costId);
    	}
        if(costProducts != null && costProducts.size() > 0){
            for (int i = 0; i < costProducts.size(); i++) {
                costProducts.get(i).setCostId(costId);
            }
            costProductDao.save(costProducts);
        }
    }

    private void saveCostPorts(String costId, List<CostPort> costPorts){
    	if(costId!=null){
    		costPortDao.deleteByCostId(costId);
    	}
        if(costPorts != null && costPorts.size() > 0){
            for (int i = 0; i < costPorts.size(); i++) {
                costPorts.get(i).setCostId(costId);
            }
            costPortDao.save(costPorts);
        }
    }

    private void saveCostChargeItems(String costId, List<CostChargeItem> costChargeItems){
    	if(costId!=null){
    		costChargeItemDao.deleteByCostId(costId);
    	}
        if(costChargeItems != null && costChargeItems.size() > 0){
            for (int i = 0; i < costChargeItems.size(); i++) {
                costChargeItems.get(i).setCostId(costId);
            }
            costChargeItemDao.save(costChargeItems);
        }
    }

    private void saveCostTariffs(String costId, List<CostTariff> costTariffs){
    	if(costId!=null){
    		costTariffDao.deleteByCostId(costId);
    	}
        if(costTariffs != null && costTariffs.size() > 0){
            for (int i = 0; i < costTariffs.size(); i++) {
                costTariffs.get(i).setCostId(costId);
            }
            costTariffDao.save(costTariffs);
        }
    }

    private void saveCostProductCosts(String costId, List<CostProductCost> costProductCosts){
    	if(costId!=null){
    		costProductCostDao.deleteByCostId(costId);
    	}
        if(costProductCosts != null && costProductCosts.size() > 0){
            UserVo user = SessionUtils.currentUserVo();
            for (int i = 0; i < costProductCosts.size(); i++) {
                CostProductCost costProductCost = costProductCosts.get(i);
                costProductCost.setCostId(costId);
//                costProductCost.setRateAudToRmb(user.getAudToRmb());
//                costProductCost.setRateAudToUsd(user.getAudToUsd());
//                costProductCost.setPriceCostRmb(costProductCost.getPriceCostAud().multiply(user.getAudToRmb()));
//                costProductCost.setPriceCostUsd(costProductCost.getPriceCostAud().multiply(user.getAudToUsd()));
//                costProductCost.setPortFeeRmb(costProductCost.getPortFeeAud().multiply(user.getAudToRmb()));
//                costProductCost.setPortFeeUsd(costProductCost.getPortFeeAud().multiply(user.getAudToUsd()));
//                costProductCost.setChargeItemFeeRmb(costProductCost.getChargeItemFeeAud().multiply(user.getAudToRmb()));
//                costProductCost.setChargeItemFeeUsd(costProductCost.getChargeItemFeeAud().multiply(user.getAudToUsd()));
//                costProductCost.setTariffRmb(costProductCost.getTariffAud().multiply(user.getAudToRmb()));
//                costProductCost.setTariffUsd(costProductCost.getTariffAud().multiply(user.getAudToUsd()));
//                costProductCost.setCustomProcessingFeeRmb(costProductCost.getCustomProcessingFeeAud().multiply(user.getAudToRmb()));
//                costProductCost.setCustomProcessingFeeUsd(costProductCost.getCustomProcessingFeeAud().multiply(user.getAudToUsd()));
//                costProductCost.setOtherFeeRmb(costProductCost.getOtherFeeAud().multiply(user.getAudToRmb()));
//                costProductCost.setOtherFeeUsd(costProductCost.getOtherFeeAud().multiply(user.getAudToUsd()));
//                costProductCost.setGstRmb(costProductCost.getGstAud().multiply(user.getAudToRmb()));
//                costProductCost.setGstUsd(costProductCost.getGstAud().multiply(user.getAudToUsd()));
//                costProductCost.setTotalCostRmb(costProductCost.getTotalCostAud().multiply(user.getAudToRmb()));
//                costProductCost.setTotalCostUsd(costProductCost.getTotalCostAud().multiply(user.getAudToUsd()));
            }
            costProductCostDao.save(costProductCosts);
        }
    }

    /**
     * 设置服务商创建人信息，同时设置所属部门和采购员信息，用于新增时
     * @param cost
     */
    private void setCreatorInfo(Cost cost){
        UserVo user = SessionUtils.currentUserVo();
        if (StringUtils.isBlank(cost.getId())){
            cost.setCreatedAt(new Date());
            cost.setStatus(1);
        }else{
            cost.setUpdatedAt(new Date());
        }
        cost.setCreatorId(user.getId());
        cost.setCreatorCnName(user.getCnName());
        cost.setCreatorEnName(user.getEnName());
        UserDepartmentVo department = user.getDepartment();
        if(department != null){
            cost.setDepartmentId(department.getId());
            cost.setDepartmentCnName(department.getCnName());
            cost.setDepartmentEnName(department.getEnName());
        }
    }



    /**
     * 确认
     * @param id
     */
    @Transactional(rollbackFor = Exception.class)
    public void confirmCost(String id){
        Cost serviceProvider = costDao.findOne(id);
        serviceProvider.setStatus(1);
        serviceProvider.setUpdatedAt(new Date());
        costDao.save(serviceProvider);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteCost(String id){
        //清除发货计划中成本计算标记位
        Cost cost = costDao.findOne(id);
        if(StringUtils.isNotBlank(cost.getOrderShippingPlanId())){
            OrderShippingPlan orderShippingPlan = orderShippingPlanDao.findOne(cost.getOrderShippingPlanId());
            if(orderShippingPlan != null){
                orderShippingPlan.setFlagCostStatus(2);
                orderShippingPlan.setFlagCostId(null);
                orderShippingPlan.setFlagCostTime(null);
                orderShippingPlanDao.save(orderShippingPlan);
                FlowOrderShippingPlan flowOrderShippingPlan = flowOrderShippingPlanDao.findOne(orderShippingPlan.getBusinessId());
                if(flowOrderShippingPlan != null){
                    flowOrderShippingPlan.setFlagCostStatus(2);
                    flowOrderShippingPlan.setFlagCostId(null);
                    flowOrderShippingPlan.setFlagCostTime(null);
                    flowOrderShippingPlanDao.save(flowOrderShippingPlan);
                }
            }
        }
        //更新发货计划的成本计算状态
        updateOrderShippingPlanCostStatus(cost,2);
        List<CostOrder> costOrders = costOrderDao.findCostOrderByCostId(id);
        for(CostOrder obj:costOrders){
            PurchaseContract order = purchaseContractDao.findOne(obj.getOrderId());
            order.setFlagCostStatus(2);
            order.setFlagCostId("deleteCost");
            order.setFlagCostTime(new Date());
            purchaseContractDao.save(order);
        }
        updatePackingCostStatus(cost,costOrders,2);
        //删除相关cost表的数据
        costProductDao.deleteByCostId(id);
        costPortDao.deleteByCostId(id);
        costChargeItemDao.deleteByCostId(id);
        costTariffDao.deleteByCostId(id);
        costProductCostDao.deleteByCostId(id);
        costOrderDao.deleteByCostId(id);
        //删除全部关联附件
        attachmentService.deleteByBusinessId(id);
        
        costDao.delete(id);
    }

    /**
     * 根据ID修改状态为“删除”
     * @param id
     */
    @Transactional(rollbackFor = Exception.class)
    public void setDelete(String id){
        Cost sp = getCost(id);
        sp.setStatus(3);
        costDao.save(sp);
    }

    /**
     * 创建动态查询条件组合.
     */
    private Specification<Cost> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<Cost> spec = DynamicSpecifications.bySearchFilter(filters.values(), Cost.class);
        return spec;
    }

    private void saveCostOrder(String costId, List<CostOrder> costOrders) {
        if(costId!=null) {
            costOrderDao.deleteByCostId(costId);
        }
        if (costOrders != null && costOrders.size() > 0) {
            UserVo user = SessionUtils.currentUserVo();
            for (CostOrder costOrder : costOrders) {
                costOrder.setCostId(costId);
//                costOrder.setRateAudToRmb(user.getAudToRmb());
//                costOrder.setRateAudToUsd(user.getAudToUsd());
            }
            costOrderDao.save(costOrders);
        }
    }

    //当进入成本计算编辑页面时，用来获取编辑时导入订单支付数据的
    public CostFeeDetailVo getFee(String costId) {
        List<CostOrder> costOrder = costOrderDao.findCostOrderByCostId(costId);
        List<PurchaseContractOtherDetailVo> orderOtherDetails = Lists.newArrayList();
        List<PurchaseContractDepositVo> deposits = Lists.newArrayList();
        List<FeeRegisterDetailVo> listFeeRegisterDetails = Lists.newArrayList();

//        for (CostOrder order : costOrder) {
//            List<PurchaseContractOtherDetailVo> otherDetails = purchaseContractOtherDetailService.findOtherDepositByOrderId(order.getOrderId());
//
//            for (PurchaseContractOtherDetailVo otherDetail : otherDetails) {
//                otherDetail.setOrderNumber(order.getOrderNumber());
//                orderOtherDetails.add(otherDetail);
//            }
//            List<PurchaseContractDepositVo> depositvos = purchaseContractDepositService.findDepositvoByOrderId(order.getOrderId());
//            for (PurchaseContractDepositVo depositvo : depositvos) {
//                deposits.add(depositvo);
//            }
//        }

        List<String> orderIds = costOrder.stream().map(CostOrder::getOrderId).collect(Collectors.toList());
        List<FeePaymentVo> balancesVoByOrders = feePaymentService.findBalancesVoByOrderIds(orderIds);
        for (FeePaymentVo balancesVoByOrder : balancesVoByOrders) {
            List<FeeRegisterDetailVo> feeRegisterDetail = feeRegisterDetailService.findDetailsByFeeRegisterIdAndApplyCost(balancesVoByOrder.getFeeRegisterId(),1);
            for (FeeRegisterDetailVo feeRegisterDetailVo : feeRegisterDetail) {
                feeRegisterDetailVo.setOrderNumber(balancesVoByOrder.getOrderNumber());
                //加入实付汇率
                feeRegisterDetailVo.setPaymentRateAudToRmb(balancesVoByOrder.getPaymentRateAudToRmb());
                feeRegisterDetailVo.setPaymentRateAudToUsd(balancesVoByOrder.getPaymentRateAudToUsd());
                listFeeRegisterDetails.add(feeRegisterDetailVo);
            }
        }
        CostFeeDetailVo costFeeDetailVo = new CostFeeDetailVo();
//        costFeeDetailVo.setBalances(balancesVoByOrders);
//        costFeeDetailVo.setDeposits(deposits);
        costFeeDetailVo.setFeeRegisterDetails(listFeeRegisterDetails);
//        costFeeDetailVo.setOrderOtherDetail(orderOtherDetails);
        return costFeeDetailVo;
    }

    private String joinOrderNumber(List<CostOrder> costOrders){
        String orderNumbers = "";
        if(costOrders.size()>0){
            for(CostOrder costOrder: costOrders){
                orderNumbers += ","+costOrder.getOrderNumber();
            }
            orderNumbers = StringUtils.substring(orderNumbers, 1);
        }
        return orderNumbers;
    }
}
