package com.newaim.purchase.flow.purchase.listeners;

import com.newaim.core.mapper.BeanMapper;
import com.newaim.purchase.Constants;
import com.newaim.purchase.archives.flow.purchase.dao.PurchaseBalanceRefundUnionDao;
import com.newaim.purchase.archives.flow.purchase.dao.PurchasePlanDao;
import com.newaim.purchase.archives.flow.purchase.dao.PurchasePlanDetailDao;
import com.newaim.purchase.archives.flow.purchase.entity.PurchaseBalanceRefundUnion;
import com.newaim.purchase.archives.flow.purchase.entity.PurchasePlan;
import com.newaim.purchase.archives.flow.purchase.entity.PurchasePlanDetail;
import com.newaim.purchase.archives.product.dao.ProductVendorPropDao;
import com.newaim.purchase.archives.product.entity.ProductVendorProp;
import com.newaim.purchase.desktop.reports.service.ReportsService;
import com.newaim.purchase.flow.purchase.entity.FlowPurchasePlan;
import com.newaim.purchase.flow.purchase.entity.FlowPurchasePlanDetail;
import com.newaim.purchase.flow.purchase.service.FlowPurchasePlanDetailService;
import com.newaim.purchase.flow.purchase.service.FlowPurchasePlanService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 采购计划流正常结束监听器.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FlowPurchasePlanEndListener implements Serializable, ExecutionListener{

    @Autowired
    private FlowPurchasePlanService flowPurchasePlanService;

    @Autowired
    private FlowPurchasePlanDetailService flowPurchasePlanDetailService;

    @Autowired
    private PurchasePlanDao purchasePlanDao;

    @Autowired
    private PurchasePlanDetailDao purchasePlanDetailDao;

    @Autowired
    private ProductVendorPropDao productVendorPropDao;

    @Autowired
    private ReportsService reportsService;

    @Autowired
    private PurchaseBalanceRefundUnionDao purchaseBalanceRefundUnionDao;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        String businessId = execution.getProcessBusinessKey();
        //1. 通过表单id获取相关对象
        FlowPurchasePlan flowPurchasePlan = flowPurchasePlanService.getFlowPurchasePlan(businessId);
        List<FlowPurchasePlanDetail> details = flowPurchasePlanDetailService.findDetailsByBusinessId(businessId);
        //2. 保存相关数据到正式业务数据表
        PurchasePlan purchasePlan = BeanMapper.map(flowPurchasePlan, PurchasePlan.class);
        purchasePlan.setId(null);
        purchasePlan.setBusinessId(flowPurchasePlan.getId());
        purchasePlan.setFlowStatus(Constants.FlowStatus.PASS.code);
        purchasePlan.setEndTime(new Date());
        purchasePlanDao.save(purchasePlan);

        if(details != null){
            for (FlowPurchasePlanDetail detail: details) {
                //2.2 拷贝明细数据到业务表
                PurchasePlanDetail purchasePlanDetail = BeanMapper.map(detail, PurchasePlanDetail.class);
                purchasePlanDetail.setPurchasePlanId(purchasePlan.getId());
                purchasePlanDetail.setHold(Constants.HoldStatus.UN_HOLD.code);
                purchasePlanDetailDao.save(purchasePlanDetail);
                //采购计划流程通过时，将报价、报价汇率、报价币种更新到产品属性
                ProductVendorProp prop = productVendorPropDao.findProductVendorPropByProductId(purchasePlanDetail.getProductId());
                if(prop!=null) {
                    prop.setIsApply(1);
                    prop.setRateAudToRmb(purchasePlanDetail.getRateAudToRmb());
                    prop.setRateAudToUsd(purchasePlanDetail.getRateAudToUsd());
                    prop.setCurrency(purchasePlanDetail.getCurrency());
                    prop.setQuotationPriceAud(purchasePlanDetail.getPriceAud());
                    prop.setQuotationPriceRmb(purchasePlanDetail.getPriceRmb());
                    prop.setQuotationPriceUsd(purchasePlanDetail.getPriceUsd());
                    prop.setQuotationRateAudToRmb(purchasePlanDetail.getRateAudToRmb());
                    prop.setQuotationRateAudToUsd(purchasePlanDetail.getRateAudToUsd());
                    prop.setQuotationCurrency(purchasePlanDetail.getCurrency());
                    productVendorPropDao.save(prop);
                }
            }
        }

        //更新相关报告的确认人
        reportsService.saveConfirmedByBusinessId(flowPurchasePlan.getId());

        //采购差额退款关联表更新
        List<PurchaseBalanceRefundUnion> purchaseBalanceRefundUnions = purchaseBalanceRefundUnionDao.findByPurchasePlanBusinessId(businessId);
        if(purchaseBalanceRefundUnions != null && purchaseBalanceRefundUnions.size() > 0){
            for (int i = 0; i < purchaseBalanceRefundUnions.size(); i++) {
                purchaseBalanceRefundUnions.get(i).setPurchasePlanId(purchasePlan.getId());
            }
            purchaseBalanceRefundUnionDao.save(purchaseBalanceRefundUnions);
        }
    }
}
