package com.newaim.purchase.flow.purchase.listeners;

import com.google.common.collect.Lists;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.purchase.Constants;
import com.newaim.purchase.archives.flow.purchase.dao.PurchaseBalanceRefundUnionDao;
import com.newaim.purchase.archives.flow.purchase.dao.PurchaseContractDao;
import com.newaim.purchase.archives.flow.purchase.dao.PurchaseContractDetailDao;
import com.newaim.purchase.archives.flow.purchase.dao.PurchaseContractOtherDetailDao;
import com.newaim.purchase.archives.flow.purchase.entity.PurchaseBalanceRefundUnion;
import com.newaim.purchase.archives.flow.purchase.entity.PurchaseContract;
import com.newaim.purchase.archives.flow.purchase.entity.PurchaseContractDetail;
import com.newaim.purchase.archives.flow.purchase.entity.PurchaseContractOtherDetail;
import com.newaim.purchase.archives.product.dao.ProductDao;
import com.newaim.purchase.archives.product.entity.Product;
import com.newaim.purchase.desktop.message.helper.Msg;
import com.newaim.purchase.flow.finance.dao.FlowPurchaseContractDepositDao;
import com.newaim.purchase.flow.finance.entity.FlowPurchaseContractDeposit;
import com.newaim.purchase.flow.inspection.dao.FlowOrderQcDao;
import com.newaim.purchase.flow.inspection.dao.FlowOrderQcDetailDao;
import com.newaim.purchase.flow.inspection.entity.FlowOrderQc;
import com.newaim.purchase.flow.inspection.entity.FlowOrderQcDetail;
import com.newaim.purchase.flow.purchase.dao.FlowPurchaseContractDao;
import com.newaim.purchase.flow.purchase.entity.FlowPurchaseContract;
import com.newaim.purchase.flow.purchase.entity.FlowPurchaseContractDetail;
import com.newaim.purchase.flow.purchase.entity.FlowPurchaseContractOtherDetail;
import com.newaim.purchase.flow.purchase.service.FlowPurchaseContractDetailService;
import com.newaim.purchase.flow.purchase.service.FlowPurchaseContractOtherDetailService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 采购合同申请正常结束监听器
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FlowPurchaseContractEndListener implements Serializable, ExecutionListener {

    @Autowired
    private FlowPurchaseContractDao flowPurchaseContractDao;

    @Autowired
    private FlowPurchaseContractDetailService flowPurchaseContractDetailService;

    @Autowired
    private FlowPurchaseContractOtherDetailService flowPurchaseContractOtherDetailService;

    @Autowired
    private PurchaseContractDao purchaseContractDao;

    @Autowired
    private PurchaseContractDetailDao purchaseContractDetailDao;

    @Autowired
    private PurchaseContractOtherDetailDao purchaseContractOtherDetailDao;

    @Autowired
    private FlowPurchaseContractDepositDao flowPurchaseContractDepositDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private FlowOrderQcDao flowOrderQcDao;

    @Autowired
    private FlowOrderQcDetailDao flowOrderQcDetailDao;

    @Autowired
    private PurchaseBalanceRefundUnionDao purchaseBalanceRefundUnionDao;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        String businessId = execution.getProcessBusinessKey();
        //1. 通过表单id获取相关对象
        FlowPurchaseContract flowPurchaseContract = flowPurchaseContractDao.findOne(businessId);
        List<FlowPurchaseContractDetail> details = flowPurchaseContractDetailService.findDetailsByBusinessId(businessId);
        List<FlowPurchaseContractOtherDetail> otherDetails = flowPurchaseContractOtherDetailService.findOtherDetailsByBusinessId(businessId);
        //2. 保存相关数据到正式业务数据表
        PurchaseContract purchaseContract = BeanMapper.map(flowPurchaseContract, PurchaseContract.class);
        purchaseContract.setId(null);
        purchaseContract.setBusinessId(flowPurchaseContract.getId());
        purchaseContract.setFlowStatus(Constants.FlowStatus.PASS.code);
        purchaseContract.setEndTime(new Date());
        purchaseContract.setFlagFeePaymentStatus(2);
        purchaseContract.setFlagWarehousePlanStatus(2);
        purchaseContract.setFlagAsnStatus(2);
        purchaseContract.setFlagCostStatus(2);
        purchaseContract.setFlagFeePaymentStatus(2);
        if(BigDecimal.ZERO.equals(purchaseContract.getDepositAud())){
            purchaseContract.setFlagContractDepositStatus(1);
        }else{
            purchaseContract.setFlagContractDepositStatus(2);
        }
        purchaseContract.setFlagBalanceRefundStatus(2);
        purchaseContract.setFlagOrderQcStatus(2);
        purchaseContract.setFlagOrderShippingPlanStatus(2);
        purchaseContract.setFlagOrderShippingApplyStatus(2);
        purchaseContract.setFlagCustomClearanceStatus(2);
        purchaseContractDao.save(purchaseContract);

        //相关差额退款单关联
        List<PurchaseBalanceRefundUnion> unions =  purchaseBalanceRefundUnionDao.findByPurchaseContractBusinessId(businessId);
        if(unions != null && unions.size() > 0){
            for (int i = 0; i < unions.size(); i++) {
                PurchaseBalanceRefundUnion union = unions.get(i);
                union.setPurchaseContractId(purchaseContract.getId());
                purchaseBalanceRefundUnionDao.save(union);
            }
        }
        //其它定金费用
        BigDecimal otherAud = BigDecimal.ZERO;
        BigDecimal otherRmb = BigDecimal.ZERO;
        BigDecimal otherUsd = BigDecimal.ZERO;
        if(otherDetails != null && otherDetails.size() > 0){
            for (int i = 0; i < otherDetails.size(); i++) {
                FlowPurchaseContractOtherDetail otherDetail = otherDetails.get(i);
                BigDecimal qty = BigDecimal.valueOf(otherDetail.getQty());
                if(otherDetail.getSettlementType() == 1){
                    //合同定金类支付
                    otherAud = otherAud.add(otherDetail.getPriceAud().multiply(qty));
                    otherRmb = otherRmb.add(otherDetail.getPriceRmb().multiply(qty));
                    otherUsd = otherUsd.add(otherDetail.getPriceUsd().multiply(qty));
                }else if(otherDetail.getSettlementType() == 3){
                    //按定金比率分摊
                    if(purchaseContract.getDepositType().intValue() == 2){
                        otherAud = otherAud.add(otherDetail.getPriceAud().multiply(qty).multiply(purchaseContract.getDepositRate()));
                        otherRmb = otherRmb.add(otherDetail.getPriceRmb().multiply(qty).multiply(purchaseContract.getDepositRate()));
                        otherUsd = otherUsd.add(otherDetail.getPriceUsd().multiply(qty).multiply(purchaseContract.getDepositRate()));
                    }
                }
                PurchaseContractOtherDetail purchaseContractOtherDetail = BeanMapper.map(otherDetails.get(i), PurchaseContractOtherDetail.class);
                purchaseContractOtherDetail.setOrderId(purchaseContract.getId());
                purchaseContractOtherDetailDao.save(purchaseContractOtherDetail);
            }
        }
        if (details != null && details.size() > 0) {
            boolean neededQc = false;
            List<Product> products = Lists.newArrayList();
            for (FlowPurchaseContractDetail detail : details) {
                //2.2 拷贝明细数据到业务表
                PurchaseContractDetail purchaseContractDetail = BeanMapper.map(detail, PurchaseContractDetail.class);
                purchaseContractDetail.setOrderId(purchaseContract.getId());
                purchaseContractDetailDao.save(purchaseContractDetail);

                //2.3 标记单个产品的质检index, 产品前两单必检
                Product product = productDao.findOne(detail.getProductId());
                if (product != null) {
                    Integer qcIndex = product.getQcIndex();
                    int newQcIndex = qcIndex != null ? qcIndex + 1 : 1;
                    product.setQcIndex(newQcIndex);
                    if (!neededQc) {
                        Integer riskRating = product.getProp().getRiskRating();
                        if (Constants.RiskRating.YELLOW.code.equals(riskRating)) {
                            //初次或逢5抽1
                            if (newQcIndex - 2 <= 0 || newQcIndex - 2 >= 5) {
                                neededQc = true;
                            }
                        } else if (Constants.RiskRating.BLUE.code.equals(riskRating)) {
                            //初次或逢3抽1
                            if (newQcIndex - 2 <= 0 || newQcIndex - 2 >= 3) {
                                neededQc = true;
                            }
                        } else if (riskRating.equals(Constants.RiskRating.ORANGE.code)) {
                            //全检
                            neededQc = true;
                        } else if(riskRating.equals(Constants.RiskRating.GREEN.code)){
                            //免检前两单必检
                            if(newQcIndex - 2 <= 0){
                                neededQc = true;
                            }
                        }
                    }
                    products.add(product);
                }
            }

            if (neededQc) {
                //设置质检标记
                purchaseContract.setIsNeededQc(2);
                purchaseContractDao.save(purchaseContract);
                flowPurchaseContract.setIsNeededQc(2);
                flowPurchaseContractDao.save(flowPurchaseContract);
                if (products != null && products.size() > 0) {
                    for (int i = 0; i < products.size(); i++) {
                        //质检index清零
                        Integer qcIndex = products.get(i).getQcIndex();
                        if(qcIndex > 2){
                            products.get(i).setQcIndex(2);
                        }
                    }
                }
                //创建质检流
                FlowOrderQc flowOrderQc = new FlowOrderQc();
                flowOrderQc.setCreatorId(purchaseContract.getCreatorId());
                flowOrderQc.setCreatorCnName(purchaseContract.getCreatorCnName());
                flowOrderQc.setCreatorEnName(purchaseContract.getCreatorEnName());
                flowOrderQc.setCreatedAt(new Date());
                flowOrderQc.setDepartmentId(purchaseContract.getDepartmentId());
                flowOrderQc.setDepartmentCnName(purchaseContract.getDepartmentCnName());
                flowOrderQc.setDepartmentEnName(purchaseContract.getDepartmentEnName());
                flowOrderQc.setVendorId(purchaseContract.getVendorId());
                flowOrderQc.setVendorCnName(purchaseContract.getVendorCnName());
                flowOrderQc.setVendorEnName(purchaseContract.getVendorEnName());
                flowOrderQc.setStatus(Constants.Status.DRAFT.code);
                flowOrderQc.setHold(Constants.HoldStatus.UN_HOLD.code);
                flowOrderQc.setOrderNumber(purchaseContract.getOrderNumber());
                flowOrderQc.setOrderTitle(purchaseContract.getOrderTitle());
                flowOrderQcDao.save(flowOrderQc);
                //质检明细
                FlowOrderQcDetail flowOrderQcDetail = new FlowOrderQcDetail();
                flowOrderQcDetail.setBusinessId(flowOrderQc.getId());
                flowOrderQcDetail.setOrderId(purchaseContract.getId());
                flowOrderQcDetail.setOrderNumber(purchaseContract.getOrderNumber());
                flowOrderQcDetail.setOrderTitle(purchaseContract.getOrderTitle());
                flowOrderQcDetail.setCurrency(purchaseContract.getCurrency());
                flowOrderQcDetail.setRateAudToRmb(purchaseContract.getRateAudToRmb());
                flowOrderQcDetail.setRateAudToUsd(purchaseContract.getRateAudToUsd());
                flowOrderQcDetail.setTotalPriceAud(purchaseContract.getTotalPriceAud());
                flowOrderQcDetail.setTotalPriceRmb(purchaseContract.getTotalPriceRmb());
                flowOrderQcDetail.setTotalPriceUsd(purchaseContract.getTotalPriceUsd());
                flowOrderQcDetail.setTotalOrderQty(purchaseContract.getTotalOrderQty());
                flowOrderQcDetail.setDepositAud(purchaseContract.getDepositAud());
                flowOrderQcDetail.setDepositRmb(purchaseContract.getDepositRmb());
                flowOrderQcDetail.setDepositUsd(purchaseContract.getDepositUsd());
                flowOrderQcDetailDao.save(flowOrderQcDetail);
                Msg.send(flowOrderQc.getCreatorId(), "已成功创建订单质检单", "已成功创建订单质检单");
            }else{
                //标记免检
                purchaseContract.setIsNeededQc(3);
                //标记为已通过订单质检
                purchaseContract.setFlagOrderQcStatus(1);
                purchaseContractDao.save(purchaseContract);
                flowPurchaseContract.setIsNeededQc(3);
                flowPurchaseContractDao.save(flowPurchaseContract);
            }
            //保存qcIndex
            productDao.save(products);
        }
        //当采购合同流程通过自动生成合同定金单
        if (purchaseContract.getDepositAud().add(otherAud).doubleValue() > 0) {
            FlowPurchaseContractDeposit deposit = new FlowPurchaseContractDeposit();
            deposit.setOrderId(purchaseContract.getId());
            deposit.setOrderNumber(purchaseContract.getOrderNumber());
            deposit.setOrderTitle(purchaseContract.getOrderTitle());
            deposit.setVendorId(purchaseContract.getVendorId());
            deposit.setVendorCnName(purchaseContract.getVendorCnName());
            deposit.setVendorEnName(purchaseContract.getVendorEnName());
            deposit.setDepositRate(purchaseContract.getDepositRate());
            deposit.setTotalValueDepositAud(purchaseContract.getDepositAud());
            deposit.setTotalValueDepositRmb(purchaseContract.getDepositRmb());
            deposit.setTotalValueDepositUsd(purchaseContract.getDepositUsd());
            deposit.setRateAudToRmb(purchaseContract.getRateAudToRmb());
            deposit.setRateAudToUsd(purchaseContract.getRateAudToUsd());
            deposit.setTotalOtherAud(otherAud);
            deposit.setTotalOtherRmb(otherRmb);
            deposit.setTotalOtherUsd(otherUsd);
            deposit.setPayableAud(purchaseContract.getDepositAud().add(otherAud));
            deposit.setPayableRmb(purchaseContract.getDepositRmb().add(otherRmb));
            deposit.setPayableUsd(purchaseContract.getDepositUsd().add(otherUsd));
            deposit.setPaymentAud(BigDecimal.ZERO);
            deposit.setPaymentRmb(BigDecimal.ZERO);
            deposit.setPaymentUsd(BigDecimal.ZERO);
            deposit.setPaymentRateAudToRmb(purchaseContract.getRateAudToRmb());
            deposit.setPaymentRateAudToUsd(purchaseContract.getRateAudToUsd());
            deposit.setCurrency(purchaseContract.getCurrency());
            deposit.setStatus(Constants.Status.DRAFT.code);
            deposit.setCreatedAt(new Date());
            deposit.setCreatorId(flowPurchaseContract.getCreatorId());
            deposit.setCreatorCnName(flowPurchaseContract.getCreatorCnName());
            deposit.setCreatorEnName(flowPurchaseContract.getCreatorEnName());
            deposit.setDepartmentId(flowPurchaseContract.getDepartmentId());
            deposit.setDepartmentCnName(flowPurchaseContract.getDepartmentCnName());
            deposit.setDepartmentEnName(flowPurchaseContract.getDepartmentEnName());
            deposit.setFlowStatus(null);
            deposit.setLatestPaymentTime(null);
            deposit.setRemark(null);
            deposit.setHold(Constants.HoldStatus.UN_HOLD.code);
            flowPurchaseContractDepositDao.save(deposit);
            Msg.send(flowPurchaseContract.getCreatorId(), "已成功创建合同定金单", "已成功创建合同定金单");
            Msg.send(deposit.getCreatorId(), "已成功创建合同定金单", "请及时处理合同定金申请单");
        }
    }
}
