package com.newaim.purchase.flow.shipping.listeners;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.purchase.Constants;
import com.newaim.purchase.archives.flow.purchase.dao.CustomClearancePackingDao;
import com.newaim.purchase.archives.flow.purchase.dao.PurchaseContractDao;
import com.newaim.purchase.archives.flow.purchase.entity.CustomClearancePacking;
import com.newaim.purchase.archives.flow.purchase.entity.PurchaseContract;
import com.newaim.purchase.archives.flow.shipping.dao.AsnDao;
import com.newaim.purchase.archives.flow.shipping.dao.AsnPackingDao;
import com.newaim.purchase.archives.flow.shipping.dao.AsnPackingDetailDao;
import com.newaim.purchase.archives.flow.shipping.entity.Asn;
import com.newaim.purchase.archives.flow.shipping.entity.AsnPacking;
import com.newaim.purchase.archives.flow.shipping.entity.AsnPackingDetail;
import com.newaim.purchase.archives.product.dao.ProductDao;
import com.newaim.purchase.archives.product.entity.Product;
import com.newaim.purchase.flow.finance.dao.FlowBalanceRefundDao;
import com.newaim.purchase.flow.finance.dao.FlowBalanceRefundDetailDao;
import com.newaim.purchase.flow.finance.entity.FlowBalanceRefund;
import com.newaim.purchase.flow.finance.entity.FlowBalanceRefundDetail;
import com.newaim.purchase.flow.finance.service.FlowBalanceRefundService;
import com.newaim.purchase.flow.shipping.entity.FlowAsn;
import com.newaim.purchase.flow.shipping.entity.FlowAsnPacking;
import com.newaim.purchase.flow.shipping.entity.FlowAsnPackingDetail;
import com.newaim.purchase.flow.shipping.service.FlowAsnPackingDetailService;
import com.newaim.purchase.flow.shipping.service.FlowAsnPackingService;
import com.newaim.purchase.flow.shipping.service.FlowAsnService;
import com.newaim.purchase.flow.workflow.listeners.CommonEndListener;

/**
 * 收货通知正常结束监听器
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FlowAsnEndListener extends CommonEndListener {

    @Autowired
    private FlowAsnService flowAsnService;
    
    @Autowired
    private FlowBalanceRefundService flowBalanceRefundService;

    @Autowired
    private FlowAsnPackingService flowAsnPackingService;

    @Autowired
    private FlowAsnPackingDetailService flowAsnPackingDetailService;

    @Autowired
    private AsnDao AsnDao;

    @Autowired
    private AsnPackingDao asnPackingDao;

    @Autowired
    private CustomClearancePackingDao customClearancePackingDao;

    @Autowired
    private PurchaseContractDao purchaseContractDao;

    @Autowired
    private AsnPackingDetailDao asnPackingDetailDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private FlowBalanceRefundDao flowBalanceRefundDao;

    @Autowired
    private FlowBalanceRefundDetailDao flowBalanceRefundDetailDao;

    @Override
    public void notify(DelegateExecution execution)throws Exception{
        String businessId = execution.getProcessBusinessKey();
        //1. 通过表单id获取相关对象
        FlowAsn flowAsn = flowAsnService.getFlowAsn(businessId);
        List<FlowAsnPacking> flowPackings = flowAsnPackingService.findPackingsByBusinessId(businessId);
        //2. 保存相关数据到正式业务数据表
        Asn asn = BeanMapper.map(flowAsn,Asn.class);
        asn.setId(null);
        asn.setBusinessId(flowAsn.getId());
        asn.setFlowStatus(Constants.FlowStatus.PASS.code);
        asn.setEndTime(new Date());
        AsnDao.save(asn);
        //2.2拷贝明细数据到业务表
        if (flowPackings != null){
            for (FlowAsnPacking flowAsnPacking : flowPackings){
                AsnPacking asnPacking = BeanMapper.map(flowAsnPacking,AsnPacking.class);
                asnPacking.setId(null);
                asnPacking.setAsnId(asn.getId());
                asnPackingDao.save(asnPacking);
                CustomClearancePacking packing = customClearancePackingDao.findOne(asnPacking.getCcPackingId());
                //2.3 拷贝明细数据到业务表
                List<FlowAsnPackingDetail> flowPackingDetails = flowAsnPackingDetailService.findPackingDetailsByAsnPackingId(flowAsnPacking.getId());
                if (flowPackingDetails!= null){
                    for (FlowAsnPackingDetail flowPackingDetail : flowPackingDetails) {
                        AsnPackingDetail packingDetail = BeanMapper.map(flowPackingDetail, AsnPackingDetail.class);
                        packingDetail.setId(null);
                        packingDetail.setAsnId(asn.getId());
                        packingDetail.setAsnPackingId(packing.getId());
                        Product product = productDao.findOne(packingDetail.getProductId());
                        if(product != null){
                            if(product.getFlagFirst() == null || product.getFlagFirst() == 1){
                                product.setFlagFirst(2);
                                productDao.save(product);
                            }
                        }
                        asnPackingDetailDao.saveAndFlush(packingDetail);
                    }
                }
            }
        }
        //检查订单ASN情况,全部做完则在订单中标记
//        List<CustomClearancePacking> notAsnPackings = customClearancePackingDao.findNotAsnPackingsByOrderId(asn.getOrderId());
//        if(notAsnPackings == null || notAsnPackings.isEmpty()){
        List<CustomClearancePacking> customClearancePackings =  customClearancePackingDao.findPackingsByOrderId(asn.getOrderId());
        List<FlowAsn> passFlowAsns = flowAsnService.findPassFlowAsnByOrderId(asn.getOrderId());
        int passFlowAsnsSize =0;
        if(passFlowAsns==null){
        	passFlowAsnsSize = 0;
        }else{
        	passFlowAsnsSize = passFlowAsns.size();
        }
        if(customClearancePackings!=null&&passFlowAsns!=null&&customClearancePackings.size()==passFlowAsnsSize+1){
        	
        	String asnIds = flowAsn.getId();
        	String asnNumbers = flowAsn.getAsnNumber();
        	for(FlowAsn obj:passFlowAsns){
        		asnIds = asnIds +","+ obj.getId();
        		asnNumbers = asnNumbers +","+ obj.getAsnNumber();
        	}
        	
            PurchaseContract order = purchaseContractDao.findOne(asn.getOrderId());
            order.setFlagAsnStatus(1);
            order.setFlagAsnId(asn.getId());
            order.setFlagAsnTime(new Date());
            order.setFlagCompleteId(asn.getId());
            order.setFlagCompleteStatus(1);
            order.setFlagCompleteTime(new Date());
            purchaseContractDao.save(order);

            List<AsnPackingDetail> asnPackingDetails = asnPackingDetailDao.findPackDetailsByOrderId(asn.getOrderId());
            if(asnPackingDetails != null && asnPackingDetails.size() > 0){

                Map<String, List<AsnPackingDetail>> asnPackingDetailsMap = Maps.newHashMap();
                for (int i = 0; i < asnPackingDetails.size(); i++) {
                    AsnPackingDetail detail = asnPackingDetails.get(i);
                    if(asnPackingDetailsMap.containsKey(detail.getProductId())){
                        asnPackingDetailsMap.get(detail.getProductId()).add(detail);
                    }else{
                        List<AsnPackingDetail> details = Lists.newArrayList();
                        details.add(detail);
                        asnPackingDetailsMap.put(detail.getProductId(), details);
                    }
                }
                List<FlowBalanceRefundDetail> flowBalanceRefundDetails = Lists.newArrayList();
                BigDecimal totalFeeAud = BigDecimal.ZERO;
                BigDecimal totalFeeRmb = BigDecimal.ZERO;
                BigDecimal totalFeeUsd = BigDecimal.ZERO;
                for (String productId : asnPackingDetailsMap.keySet()) {
                    //对每组数据做差额对比
                    List<AsnPackingDetail> details = asnPackingDetailsMap.get(productId);
                    Integer expectedQty = 0;
                    Integer receivedQty = 0;
                    for (int i = 0; i < details.size(); i++) {
                        AsnPackingDetail detail = details.get(i);
                        expectedQty += detail.getExpectedQty();
                        if(detail.getReceivedQty() != null){
                            receivedQty += detail.getReceivedQty();
                        }
                    }
                    //有差额
                    if (!expectedQty.equals(receivedQty)){
                        BigDecimal diffQty = BigDecimal.valueOf(expectedQty - receivedQty);
                        AsnPackingDetail detail = details.get(0);
                        BigDecimal totalDiffAud = detail.getPriceAud().multiply(diffQty);
                        BigDecimal totalDiffRmb = detail.getPriceRmb().multiply(diffQty);
                        BigDecimal totalDiffUsd = detail.getPriceUsd().multiply(diffQty);
                        FlowBalanceRefundDetail flowBalanceRefundDetail = new FlowBalanceRefundDetail();
                        flowBalanceRefundDetail.setAsnId(asnIds);
                        flowBalanceRefundDetail.setAsnNumber(asnNumbers);
                        flowBalanceRefundDetail.setRemark("create by " +asnNumbers);
                        flowBalanceRefundDetail.setCurrency(detail.getCurrency());
                        flowBalanceRefundDetail.setRateAudToUsd(detail.getRateAudToUsd());
                        flowBalanceRefundDetail.setRateAudToRmb(detail.getRateAudToRmb());
                        flowBalanceRefundDetail.setProductId(detail.getProductId());
                        flowBalanceRefundDetail.setSku(detail.getSku());
                        flowBalanceRefundDetail.setPriceAud(detail.getPriceAud());
                        flowBalanceRefundDetail.setPriceRmb(detail.getPriceRmb());
                        flowBalanceRefundDetail.setPriceUsd(detail.getPriceUsd());
                        flowBalanceRefundDetail.setExpectedQty(expectedQty);
                        flowBalanceRefundDetail.setReceivedQty(receivedQty);
                        flowBalanceRefundDetail.setDiffQty(diffQty.intValue());
                        flowBalanceRefundDetail.setDiffAud(totalDiffAud);
                        flowBalanceRefundDetail.setDiffRmb(totalDiffRmb);
                        flowBalanceRefundDetail.setDiffUsd(totalDiffUsd);
                        flowBalanceRefundDetail.setType(1);
                        flowBalanceRefundDetail.setShippingOrderId(order.getFlagOrderShippingPlanId());
                        flowBalanceRefundDetails.add(flowBalanceRefundDetail);
                        totalFeeAud = totalFeeAud.add(totalDiffAud);
                        totalFeeRmb = totalFeeRmb.add(totalDiffRmb);
                        totalFeeUsd = totalFeeUsd.add(totalDiffUsd);
                    }
                }

                if(flowBalanceRefundDetails.size() > 0){
                    //差额退款单
                    FlowBalanceRefund flowBalanceRefund = new FlowBalanceRefund();
                    flowBalanceRefund.setTotalFeeAud(totalFeeAud);
                    flowBalanceRefund.setTotalFeeRmb(totalFeeRmb);
                    flowBalanceRefund.setTotalFeeUsd(totalFeeUsd);
                    flowBalanceRefund.setVendorId(order.getVendorId());
                    flowBalanceRefund.setVendorCnName(order.getVendorCnName());
                    flowBalanceRefund.setVendorEnName(order.getVendorEnName());
                    flowBalanceRefund.setOrderId(order.getId());
                    flowBalanceRefund.setOrderBusinessId(order.getBusinessId());
                    flowBalanceRefund.setOrderNumber(order.getOrderNumber());
                    flowBalanceRefund.setOrderTitle(order.getOrderTitle());
                    flowBalanceRefund.setCurrency(order.getCurrency());
                    flowBalanceRefund.setRateAudToRmb(order.getRateAudToRmb());
                    flowBalanceRefund.setRateAudToUsd(order.getRateAudToUsd());
                    flowBalanceRefund.setType(1);
                    flowBalanceRefund.setChargebackStatus(2);
                    flowBalanceRefund.setCreatedAt(new Date());
                    flowBalanceRefund.setCreatorId(order.getCreatorId());
                    flowBalanceRefund.setCreatorCnName(order.getCreatorCnName());
                    flowBalanceRefund.setCreatorEnName(order.getCreatorEnName());
                    flowBalanceRefund.setDepartmentId(order.getDepartmentId());
                    flowBalanceRefund.setDepartmentCnName(order.getDepartmentCnName());
                    flowBalanceRefund.setDepartmentEnName(order.getDepartmentEnName());
                    flowBalanceRefund.setStatus(Constants.Status.DRAFT.code);
                    flowBalanceRefund.setHold(Constants.HoldStatus.UN_HOLD.code);
                    flowBalanceRefundService.addForAsn(flowBalanceRefund,flowBalanceRefundDetails);
                }
            }
        }

    }

}
