package com.newaim.purchase.flow.inspection.listeners;

import com.google.common.collect.Lists;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.LocaleMessageSource;
import com.newaim.purchase.Constants;
import com.newaim.purchase.archives.flow.finance.dao.FeePaymentDao;
import com.newaim.purchase.archives.flow.finance.dao.FeeRegisterDao;
import com.newaim.purchase.archives.flow.finance.dao.PurchaseContractDepositDao;
import com.newaim.purchase.archives.flow.inspection.dao.ComplianceApplyDao;
import com.newaim.purchase.archives.flow.inspection.dao.OrderQcDao;
import com.newaim.purchase.archives.flow.inspection.entity.ComplianceApply;
import com.newaim.purchase.archives.flow.purchase.dao.CustomClearanceDao;
import com.newaim.purchase.archives.flow.purchase.dao.CustomClearancePackingDao;
import com.newaim.purchase.archives.flow.purchase.dao.ProductQuotationDao;
import com.newaim.purchase.archives.flow.purchase.dao.PurchaseContractDao;
import com.newaim.purchase.archives.flow.purchase.dao.PurchasePlanDetailDao;
import com.newaim.purchase.archives.flow.purchase.service.ProductQuotationService;
import com.newaim.purchase.archives.flow.shipping.dao.AsnDao;
import com.newaim.purchase.archives.flow.shipping.dao.OrderShippingApplyDao;
import com.newaim.purchase.archives.flow.shipping.dao.OrderShippingPlanDao;
import com.newaim.purchase.archives.flow.shipping.dao.WarehousePlanDao;
import com.newaim.purchase.archives.product.dao.ProductDao;
import com.newaim.purchase.archives.product.entity.Product;
import com.newaim.purchase.archives.product.entity.ProductVendorProp;
import com.newaim.purchase.desktop.message.helper.Msg;
import com.newaim.purchase.desktop.reports.dao.ReportsProductDao;
import com.newaim.purchase.desktop.reports.entity.ReportsProduct;
import com.newaim.purchase.desktop.reports.service.ReportsService;
import com.newaim.purchase.flow.finance.dao.FlowFeePaymentDao;
import com.newaim.purchase.flow.finance.dao.FlowFeeRegisterDao;
import com.newaim.purchase.flow.finance.dao.FlowPurchaseContractDepositDao;
import com.newaim.purchase.flow.finance.dao.FlowSamplePaymentDao;
import com.newaim.purchase.flow.inspection.dao.FlowComplianceApplyDetailDao;
import com.newaim.purchase.flow.inspection.dao.FlowOrderQcDao;
import com.newaim.purchase.flow.inspection.dao.FlowProductCertificationDao;
import com.newaim.purchase.flow.inspection.dao.FlowSampleQcDao;
import com.newaim.purchase.flow.inspection.entity.FlowComplianceApply;
import com.newaim.purchase.flow.inspection.entity.FlowComplianceApplyDetail;
import com.newaim.purchase.flow.inspection.service.FlowComplianceApplyDetailService;
import com.newaim.purchase.flow.inspection.service.FlowComplianceApplyService;
import com.newaim.purchase.flow.purchase.dao.FlowCustomClearanceDao;
import com.newaim.purchase.flow.purchase.dao.FlowCustomClearancePackingDao;
import com.newaim.purchase.flow.purchase.dao.FlowNewProductDao;
import com.newaim.purchase.flow.purchase.dao.FlowProductQuotationDetailDao;
import com.newaim.purchase.flow.purchase.dao.FlowPurchaseContractDao;
import com.newaim.purchase.flow.purchase.dao.FlowPurchasePlanDetailDao;
import com.newaim.purchase.flow.purchase.dao.FlowSampleDao;
import com.newaim.purchase.flow.shipping.dao.FlowAsnDao;
import com.newaim.purchase.flow.shipping.dao.FlowOrderShippingApplyDao;
import com.newaim.purchase.flow.shipping.dao.FlowOrderShippingPlanDao;
import com.newaim.purchase.flow.shipping.dao.FlowWarehousePlanDao;
import com.newaim.purchase.flow.workflow.listeners.CommonEndListener;
import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 安检申请流正常结束监听器.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FlowComplianceApplyEndListener extends CommonEndListener{

    @Autowired
    private FlowComplianceApplyService flowComplianceApplyService;

    @Autowired
    private FlowComplianceApplyDetailService flowComplianceApplyDetailService;

    @Autowired
    private FlowComplianceApplyDetailDao flowComplianceApplyDetailDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private LocaleMessageSource localeMessageSource;

    @Autowired
    private ProductQuotationService productQuotationService;

    @Autowired
    private ReportsService reportsService;

    @Autowired
    private ReportsProductDao reportsProductDao;

    /**产品相关*/
    @Autowired
    private ComplianceApplyDao complianceApplyDao;
    @Autowired
    private ProductQuotationDao productQuotationDao;
    @Autowired
    private PurchaseContractDao purchaseContractDao;
    @Autowired
    private PurchasePlanDetailDao purchasePlanDetailDao;

    @Autowired
    private FlowSamplePaymentDao flowSamplePaymentDao;
    @Autowired
    private FlowProductCertificationDao flowProductCertificationDao;
    @Autowired
    private FlowSampleQcDao flowSampleQcDao;
    @Autowired
    private FlowNewProductDao flowNewProductDao;
    @Autowired
    private FlowProductQuotationDetailDao flowProductQuotationDetailDao;
    @Autowired
    private FlowPurchaseContractDao flowPurchaseContractDao;
    @Autowired
    private FlowPurchasePlanDetailDao flowPurchasePlanDetailDao;
    @Autowired
    private FlowSampleDao flowSampleDao;

    @Autowired
    private FeeRegisterDao feeRegisterDao;
    @Autowired
    private FeePaymentDao feePaymentDao;
    @Autowired
    private PurchaseContractDepositDao purchaseContractDepositDao;
    @Autowired
    private OrderQcDao orderQcDao;
    @Autowired
    private CustomClearanceDao customClearanceDao;
    @Autowired
    private CustomClearancePackingDao customClearancePackingDao;
    @Autowired
    private AsnDao asnDao;
    @Autowired
    private OrderShippingApplyDao orderShippingApplyDao;
    @Autowired
    private OrderShippingPlanDao orderShippingPlanDao;
    @Autowired
    private WarehousePlanDao warehousePlanDao;
    @Autowired
    private FlowFeeRegisterDao flowFeeRegisterDao;
    @Autowired
    private FlowFeePaymentDao flowFeePaymentDao;
    @Autowired
    private FlowPurchaseContractDepositDao flowPurchaseContractDepositDao;
    @Autowired
    private FlowOrderQcDao flowOrderQcDao;
    @Autowired
    private FlowCustomClearanceDao flowCustomClearanceDao;
    @Autowired
    private FlowCustomClearancePackingDao flowCustomClearancePackingDao;
    @Autowired
    private FlowAsnDao flowAsnDao;
    @Autowired
    private FlowOrderShippingApplyDao flowOrderShippingApplyDao;
    @Autowired
    private FlowOrderShippingPlanDao flowOrderShippingPlanDao;
    @Autowired
    private FlowWarehousePlanDao flowWarehousePlanDao;


    @Override
    public void notify(DelegateExecution execution) throws Exception {
        String businessId = execution.getProcessBusinessKey();
        //1. 通过表单id获取相关对象
        FlowComplianceApply flowComplianceApply = flowComplianceApplyService.getFlowComplianceApply(businessId);
        List<FlowComplianceApplyDetail> details = flowComplianceApplyDetailService.findDetailsByBusinessId(businessId);
        //2. 修改产品的质检状态为完成，并根据安检状态和开发状态来转换新品
        if(details != null){
            //2. 1 记录所有立即封存、售完停售的产品ID
            List<String> pids = Lists.newArrayList();
            for (FlowComplianceApplyDetail detail: details) {
                //复制明细表信息
                ComplianceApply complianceApply = BeanMapper.map(detail, ComplianceApply.class);
                //复制主表信息
                setBusinessObject(complianceApply, flowComplianceApply);
                //设置明细信息
                complianceApply.setVendorId(flowComplianceApply.getVendorId());
                complianceApply.setVendorCnName(flowComplianceApply.getVendorCnName());
                complianceApply.setVendorEnName(flowComplianceApply.getVendorEnName());
                complianceApply.setHandlerId(flowComplianceApply.getHandlerId());
                complianceApply.setHandlerCnName(flowComplianceApply.getHandlerCnName());
                complianceApply.setHandlerEnName(flowComplianceApply.getHandlerEnName());
                complianceApply.setHandlerDepartmentId(flowComplianceApply.getHandlerDepartmentId());
                complianceApply.setHandlerDepartmentCnName(flowComplianceApply.getHandlerDepartmentCnName());
                complianceApply.setHandlerDepartmentEnName(flowComplianceApply.getHandlerDepartmentEnName());
                complianceApply.setHandledAt(flowComplianceApply.getHandledAt());
                complianceApply.setFlowStatus(Constants.FlowStatus.PASS.code);
                complianceApply.setHold(Constants.HoldStatus.UN_HOLD.code);
                ReportsProduct reportsProduct = reportsProductDao.findByReportsIdAndProductId(businessId, detail.getProductId());
                if(reportsProduct != null){
                    complianceApply.setRiskRating(reportsProduct.getCplRiskRating());
                    detail.setNewPrevRiskRating(complianceApply.getRiskRating());
                }
                String productId = detail.getProductId();
                //查询对应新品信息ID
                Product product = productDao.findOne(productId);
                ProductVendorProp prop = product.getProp();
                if(prop != null){
                    complianceApply.setPrevRiskRating(prop.getRiskRating());
                    detail.setPrevRiskRating(complianceApply.getPrevRiskRating());
                }
                complianceApply.setEndTime(new Date());
                complianceApplyDao.save(complianceApply);
                if(prop != null){
                    //设置安检通过状态
                    prop.setFlagComplianceStatus(Constants.NewProductStatus.COMPLIANCE_STATUS_PASS.code);
                    //安检通过后，风险等级回写到产品档案
                    prop.setRiskRating(detail.getNewPrevRiskRating());
                    prop.setFlagComplianceId(complianceApply.getId());
                    prop.setFlagComplianceTime(new Date());
                    //新品转换
                    if(Product.NORMAL_PRODUCT.equals(product.getNewProduct())
                            && Constants.NewProductStatus.QC_STATUS_PASS.code.equals(prop.getFlagComplianceStatus())
                            && Constants.NewProductStatus.DEV_STATUS_PASS.code.equals(prop.getFlagQcStatus())){
                        //开发，安检通过时设置为正常产品

                        Msg.send(complianceApply.getCreatorId(), localeMessageSource.getMsgNewProductConvertTitle(product.getSku()), localeMessageSource.getMsgNewProductConvertContent(product.getSku()));
                    }
                    //保存状态
                    if(Constants.RiskRating.RED.code.equals(detail.getNewPrevRiskRating()) || Constants.RiskRating.BLACK.code.equals(detail.getNewPrevRiskRating())){
                        //风险级别为红黑时，禁用产品
                        product.setStatus(Constants.Status.DISABLED.code);
                        productQuotationService.disableProductStatus(productId);
                    }
                    productDao.save(product);
                }
                //立即封存
                if(Constants.RiskRating.BLACK.code.equals(detail.getNewPrevRiskRating())){
                    if(!pids.contains(productId)){
                        pids.add(productId);
                    }
                }

                flowComplianceApplyDetailDao.save(detail);

            }
            //更新相关报告的确认人
            reportsService.saveConfirmedByBusinessId(flowComplianceApply.getId());


            //挂起相关数据
            if(pids.size() > 0){
                // 挂起产品相关单据
                feeRegisterDao.suspendByProductIds(pids);
                feePaymentDao.suspendByProductIds(pids);
                purchaseContractDepositDao.suspendByProductIds(pids);
                orderQcDao.suspendByProductIds(pids);
                customClearanceDao.suspendByProductIds(pids);
                customClearancePackingDao.suspendByProductIds(pids);
                asnDao.suspendByProductIds(pids);
                orderShippingApplyDao.suspendByProductIds(pids);
                orderShippingPlanDao.suspendByProductIds(pids);
                warehousePlanDao.suspendByProductIds(pids);
                flowFeeRegisterDao.suspendByProductIds(pids);
                flowFeePaymentDao.suspendByProductIds(pids);
                flowPurchaseContractDepositDao.suspendByProductIds(pids);
                flowOrderQcDao.suspendByProductIds(pids);
                flowCustomClearanceDao.suspendByProductIds(pids);
                flowCustomClearancePackingDao.suspendByProductIds(pids);
                flowAsnDao.suspendByProductIds(pids);
                flowOrderShippingApplyDao.suspendByProductIds(pids);
                flowOrderShippingPlanDao.suspendByProductIds(pids);
                flowWarehousePlanDao.suspendByProductIds(pids);
                flowPurchaseContractDao.suspendByProductIds(pids);
                purchaseContractDao.suspendByProductIds(pids);
                //采购计划: 未完成,未确认订单
                purchasePlanDetailDao.suspendByProductIds(pids);
                flowPurchasePlanDetailDao.suspendByProductIds(pids);

                //样品付款: 未完成支付的挂起
                flowSamplePaymentDao.suspendByProductIds(pids);
                //样品质检: 未完成质检的挂起
                flowSampleQcDao.suspendByProductIds(pids);
                //新品开发：未完成的申请单
                flowNewProductDao.suspendByProductIds(pids);
                //产品证书：未完成申请单
                flowProductCertificationDao.suspendByProductIds(pids);

                //只冻结有效期内的询价
                flowProductQuotationDetailDao.suspendByProductIds(pids);
                productQuotationDao.suspendByProductIds(pids);
                //样品：冻结未完成申请单据
                flowSampleDao.suspendByProductIds(pids);
            }

        }
    }
}
