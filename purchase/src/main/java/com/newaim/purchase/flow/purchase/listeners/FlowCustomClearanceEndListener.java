package com.newaim.purchase.flow.purchase.listeners;



import com.newaim.core.mapper.BeanMapper;
import com.newaim.purchase.Constants;
import com.newaim.purchase.archives.flow.purchase.dao.*;
import com.newaim.purchase.archives.flow.purchase.entity.*;
import com.newaim.purchase.archives.flow.shipping.dao.OrderShippingPlanDao;
import com.newaim.purchase.archives.flow.shipping.entity.OrderShippingPlan;
import com.newaim.purchase.flow.purchase.dao.FlowCustomClearanceDao;
import com.newaim.purchase.flow.purchase.dao.FlowPurchaseContractDao;
import com.newaim.purchase.flow.purchase.entity.FlowCustomClearance;
import com.newaim.purchase.flow.purchase.entity.FlowCustomClearancePacking;
import com.newaim.purchase.flow.purchase.entity.FlowCustomClearancePackingDetail;
import com.newaim.purchase.flow.purchase.entity.FlowPurchaseContract;
import com.newaim.purchase.flow.purchase.service.FlowCustomClearancePackingDetailService;
import com.newaim.purchase.flow.purchase.service.FlowCustomClearancePackingService;
import com.newaim.purchase.flow.purchase.service.FlowCustomClearanceService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class FlowCustomClearanceEndListener implements Serializable, ExecutionListener {

    @Autowired
    private FlowCustomClearanceService flowCustomClearanceService;

    @Autowired
    private FlowCustomClearancePackingService flowCustomClearancePackingService;

    @Autowired
    private FlowCustomClearancePackingDetailService flowCustomClearancePackingDetailService;

    @Autowired
    private CustomClearanceDao customClearanceDao;

    @Autowired
    private FlowCustomClearanceDao flowCustomClearanceDao;

    @Autowired
    private CustomClearancePackingDao customClearancePackingDao;

    @Autowired
    private CustomClearancePackingDetailDao customClearancePackingDetailDao;

    @Autowired
    private PurchaseContractDao purchaseContractDao;

    @Autowired
    private FlowPurchaseContractDao flowPurchaseContractDao;

    @Autowired
    private OrderShippingPlanDao orderShippingPlanDao;

    @Autowired
    private PurchaseContractOtherDetailDao purchaseContractOtherDetailDao;

    @Override
    public void notify(DelegateExecution execution)throws Exception{
        String businessId = execution.getProcessBusinessKey();
        //1.通过表单Id获取相关对象
        FlowCustomClearance flowCustomClearance = flowCustomClearanceService.getFlowCustomClearance(businessId);
        List<FlowCustomClearancePacking> flowPackings = flowCustomClearancePackingService.findPackingsByBusinessId(businessId);

        //2. 保存相关数据到正式业务数据表
        CustomClearance customClearance = BeanMapper.map(flowCustomClearance,CustomClearance.class);
        customClearance.setId(null);
        customClearance.setBusinessId(flowCustomClearance.getId());
        customClearance.setFlowStatus(Constants.FlowStatus.PASS.code);
        customClearance.setEndTime(new Date());
        customClearance.setPhotos(flowCustomClearance.getPhotos());
        customClearance.setTotalPackingCbm(flowCustomClearance.getTotalPackingCbm());
        customClearanceDao.save(customClearance);
        flowCustomClearanceDao.save(flowCustomClearance);
        //实际订单货值总额
        BigDecimal totalAud = BigDecimal.ZERO;
        BigDecimal totalRmb = BigDecimal.ZERO;
        BigDecimal totalUsd = BigDecimal.ZERO;
        //2.2 拷贝明细数据到业务表
        if (flowPackings != null){
            for (FlowCustomClearancePacking flowPacking : flowPackings){
                CustomClearancePacking packing = BeanMapper.map(flowPacking, CustomClearancePacking.class);
                packing.setId(null);
                packing.setFlagWarehousePlanStatus(2);
                packing.setCustomClearanceId(customClearance.getId());
                customClearancePackingDao.save(packing);
                List<FlowCustomClearancePackingDetail> flowPackingDetails = flowCustomClearancePackingDetailService.findPackingDetailsByPackingId(flowPacking.getId());
                if (flowPackingDetails!= null){
                    for (FlowCustomClearancePackingDetail flowPackingDetail : flowPackingDetails) {
                        CustomClearancePackingDetail packingDetail = BeanMapper.map(flowPackingDetail, CustomClearancePackingDetail.class);
                        packingDetail.setId(null);
                        packingDetail.setPackingId(packing.getId());
                        Integer packingQty = packingDetail.getPackingQty();
                        if(packingQty != null){
                            if(packingDetail.getPriceAud() != null){
                                totalAud = totalAud.add(packingDetail.getPriceAud().multiply(BigDecimal.valueOf(packingQty)));
                            }
                            if(packingDetail.getPriceRmb() != null){
                                totalRmb = totalRmb.add(packingDetail.getPriceRmb().multiply(BigDecimal.valueOf(packingQty)));
                            }
                            if(packingDetail.getPriceUsd() != null){
                                totalUsd = totalUsd.add(packingDetail.getPriceUsd().multiply(BigDecimal.valueOf(packingQty)));
                            }
                        }
                        customClearancePackingDetailDao.save(packingDetail);
                    }
                }
            }
        }
        //根据orderId找到订单
        PurchaseContract order = purchaseContractDao.getOne(customClearance.getOrderId());

        if(order != null){
            order.setFlagCustomClearanceId(customClearance.getId());
            order.setFlagCustomClearanceStatus(1);
            order.setFlagCustomClearanceTime(new Date());
            order.setTotalPackingCbm(flowCustomClearance.getTotalPackingCbm());
            //调整尾款 = 调整总额 - 冲销金额 - 货值定金 + (其它费用)
            BigDecimal adjustBalanceAud = totalAud.subtract(order.getWriteOffAud()).subtract(order.getDepositAud());
            BigDecimal adjustBalanceRmb = totalRmb.subtract(order.getWriteOffRmb()).subtract(order.getDepositRmb());
            BigDecimal adjustBalanceUsd = totalUsd.subtract(order.getWriteOffUsd()).subtract(order.getDepositUsd());
            //调整后货值尾款＝调整总额 - 货值订金
            BigDecimal adjustValueBalanceAud = totalAud.subtract(order.getDepositAud());
            BigDecimal adjustValueBalanceRmb = totalRmb.subtract(order.getDepositRmb());
            BigDecimal adjustValueBalanceUsd = totalUsd.subtract(order.getDepositUsd());
            List<PurchaseContractOtherDetail> orderOtherDetails = purchaseContractOtherDetailDao.findOtherDetailsByOrderId(order.getId());
            if(orderOtherDetails != null && orderOtherDetails.size() > 0){
                for (int i = 0; i < orderOtherDetails.size(); i++) {
                    PurchaseContractOtherDetail orderOtherDetail = orderOtherDetails.get(i);
                    BigDecimal qty = BigDecimal.valueOf(orderOtherDetail.getQty());
                    if(orderOtherDetail.getSettlementType().intValue() == 2){
                        adjustBalanceAud = adjustBalanceAud.add(orderOtherDetail.getPriceAud().multiply(qty));
                        adjustBalanceRmb = adjustBalanceRmb.add(orderOtherDetail.getPriceRmb().multiply(qty));
                        adjustBalanceUsd = adjustBalanceUsd.add(orderOtherDetail.getPriceUsd().multiply(qty));
                    }else if(orderOtherDetail.getSettlementType().intValue() == 3){
                        if(order.getDepositType().intValue() == 1){
                            adjustBalanceAud = adjustBalanceAud.add(orderOtherDetail.getPriceAud().multiply(qty));
                            adjustBalanceRmb = adjustBalanceRmb.add(orderOtherDetail.getPriceRmb().multiply(qty));
                            adjustBalanceUsd = adjustBalanceUsd.add(orderOtherDetail.getPriceUsd().multiply(qty));
                        }else if(order.getDepositType().intValue() == 2){
                            adjustBalanceAud = adjustBalanceAud.add(orderOtherDetail.getPriceAud().multiply(qty).multiply(BigDecimal.ONE.subtract(order.getDepositRate())));
                            adjustBalanceRmb = adjustBalanceRmb.add(orderOtherDetail.getPriceRmb().multiply(qty).multiply(BigDecimal.ONE.subtract(order.getDepositRate())));
                            adjustBalanceUsd = adjustBalanceUsd.add(orderOtherDetail.getPriceUsd().multiply(qty).multiply(BigDecimal.ONE.subtract(order.getDepositRate())));
                        }
                    }
                }
            }
            order.setAdjustBalanceAud(adjustBalanceAud);
            order.setAdjustBalanceRmb(adjustBalanceRmb);
            order.setAdjustBalanceUsd(adjustBalanceUsd);
            order.setAdjustValueBalanceAud(adjustValueBalanceAud);
            order.setAdjustValueBalanceRmb(adjustValueBalanceRmb);
            order.setAdjustValueBalanceUsd(adjustValueBalanceUsd);
            purchaseContractDao.save(order);
            //如果该订单完成发货计划，而未完成成本计算，则将该发货计划的清关标记为1
            if ("1".equals(order.getFlagOrderShippingPlanStatus().toString()) && "2".equals(order.getFlagCostStatus().toString())) {
                OrderShippingPlan shippingPlan = orderShippingPlanDao.findOne(order.getFlagOrderShippingPlanId());
                shippingPlan.setFlagCustomClearanceStatus(1);
                shippingPlan.setFlagCustomClearanceId(customClearance.getId());
                shippingPlan.setFlagCustomClearanceTime(new Date());
                orderShippingPlanDao.save(shippingPlan);
            }

            if(order.getBusinessId() != null){
                FlowPurchaseContract flowOrder = flowPurchaseContractDao.findOne(order.getBusinessId());
                if(flowOrder != null){
                    flowOrder.setAdjustBalanceAud(adjustBalanceAud);
                    flowOrder.setAdjustBalanceRmb(adjustBalanceRmb);
                    flowOrder.setAdjustBalanceUsd(adjustBalanceUsd);
                    flowOrder.setAdjustValueBalanceAud(adjustValueBalanceAud);
                    flowOrder.setAdjustValueBalanceRmb(adjustValueBalanceRmb);
                    flowOrder.setAdjustValueBalanceUsd(adjustValueBalanceUsd);
                    flowPurchaseContractDao.save(flowOrder);
                }
            }
        }
    }

}