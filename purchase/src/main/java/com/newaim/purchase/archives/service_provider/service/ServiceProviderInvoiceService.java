package com.newaim.purchase.archives.service_provider.service;

import com.google.common.collect.Lists;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.purchase.archives.flow.finance.entity.FeeRegister;
import com.newaim.purchase.archives.flow.finance.service.FeePaymentService;
import com.newaim.purchase.archives.flow.finance.service.FeeRegisterDetailService;
import com.newaim.purchase.archives.flow.finance.service.FeeRegisterService;
import com.newaim.purchase.archives.flow.finance.service.PurchaseContractDepositService;
import com.newaim.purchase.archives.flow.finance.vo.FeePaymentVo;
import com.newaim.purchase.archives.flow.finance.vo.FeeRegisterDetailVo;
import com.newaim.purchase.archives.flow.finance.vo.PurchaseContractDepositVo;
import com.newaim.purchase.archives.flow.purchase.service.CustomClearancePackingService;
import com.newaim.purchase.archives.flow.purchase.service.PurchaseContractDetailService;
import com.newaim.purchase.archives.flow.purchase.service.PurchaseContractOtherDetailService;
import com.newaim.purchase.archives.flow.purchase.service.PurchaseContractService;
import com.newaim.purchase.archives.flow.purchase.vo.*;
import com.newaim.purchase.archives.flow.shipping.service.OrderShippingPlanService;
import com.newaim.purchase.archives.flow.shipping.service.ServiceProviderQuotationPortService;
import com.newaim.purchase.archives.flow.shipping.service.ServiceProviderQuotationService;
import com.newaim.purchase.archives.flow.shipping.vo.OrderShippingPlanDetailVo;
import com.newaim.purchase.archives.flow.shipping.vo.ServiceProviderQuotationChargeItemVo;
import com.newaim.purchase.archives.flow.shipping.vo.ServiceProviderQuotationPortVo;
import com.newaim.purchase.archives.flow.shipping.vo.ServiceProviderQuotationVo;
import com.newaim.purchase.archives.product.dao.ProductVendorPropDao;
import com.newaim.purchase.archives.product.dao.TariffDao;
import com.newaim.purchase.archives.product.entity.ProductVendorProp;
import com.newaim.purchase.archives.product.service.ProductService;
import com.newaim.purchase.archives.product.vo.CostChargeItemVo;
import com.newaim.purchase.archives.product.vo.CostProductVo;
import com.newaim.purchase.archives.product.vo.ProductVo;
import com.newaim.purchase.archives.product.vo.TariffVo;
import com.newaim.purchase.archives.service_provider.vo.ServiceProviderInvoiceVo;
import com.newaim.purchase.flow.shipping.entity.FlowOrderShippingPlan;
import com.newaim.purchase.flow.shipping.service.FlowOrderShippingPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Mark on 2017/11/25.
 */
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class ServiceProviderInvoiceService {

    @Autowired
    private FlowOrderShippingPlanService flowOrderShippingPlanService;

    @Autowired
    private ServiceProviderQuotationService serviceProviderQuotationService;

    @Autowired
    private PurchaseContractService purchaseContractService;

    @Autowired
    private CustomClearancePackingService customClearancePackingService;

    @Autowired
    private PurchaseContractDepositService purchaseContractDepositService;

    @Autowired
    private FeePaymentService feePaymentService;

    @Autowired
    private PurchaseContractOtherDetailService purchaseContractOtherDetailService;
    @Autowired
    private FeeRegisterDetailService feeRegisterDetailService;

    @Autowired
    private ServiceProviderQuotationPortService serviceProviderQuotationPortService;
    @Autowired
    private PurchaseContractDetailService purchaseContractDetailService;
    @Autowired
    private ProductVendorPropDao productVendorPropDao;
    @Autowired
    private TariffDao tariffDao;
    @Autowired
    private OrderShippingPlanService orderShippingPlanService;
    @Autowired
    private FeeRegisterService feeRegisterService;
    @Autowired
    private ProductService productService;

    public ServiceProviderInvoiceVo getByOrderShippingPlanId(String orderShippingPlanId ,String orderShippingPlanBusinessId){

        ServiceProviderInvoiceVo vo = new ServiceProviderInvoiceVo();

        FlowOrderShippingPlan orderShippingPlan = flowOrderShippingPlanService.getFlowOrderShippingPlan(orderShippingPlanBusinessId);
        //获取询价id
        String serviceProviderQuotationId = orderShippingPlan.getServiceProviderQuotationId();
        //获取发运计划对应的海运报价
        List<ServiceProviderQuotationPortVo> ports = serviceProviderQuotationPortService.listPorts(orderShippingPlanId);
        for (ServiceProviderQuotationPortVo port : ports) {
            if (port.getPriceGp20InsuranceAud() == null) {
                port.setPriceGp20InsuranceAud(BigDecimal.valueOf(0));
            }
            if (port.getPriceGp40InsuranceAud() == null) {
                port.setPriceGp40InsuranceAud(BigDecimal.valueOf(0));
            }
            if (port.getPriceHq40InsuranceAud() == null) {
                port.setPriceHq40InsuranceAud(BigDecimal.valueOf(0));
            }
            if (port.getPriceLclInsuranceAud() == null) {
                port.setPriceLclInsuranceAud(BigDecimal.valueOf(0));
            }
        }
        //获取询价信息
        ServiceProviderQuotationVo quotation = serviceProviderQuotationService.get(serviceProviderQuotationId);
        vo.setCurrency(quotation.getCurrency());
        vo.setRateAudToRmb(quotation.getRateAudToRmb());
        vo.setRateAudToUsd(quotation.getRateAudToUsd());
        vo.setCurrencyAdjustment(quotation.getCurrencyAdjustment());
        vo.setEffectiveDate(quotation.getEffectiveDate());
        vo.setValidUntil(quotation.getValidUntil());
        vo.setType(quotation.getType());
        vo.setServiceProviderId(quotation.getServiceProviderId());
        vo.setServiceProviderCnName(quotation.getServiceProviderCnName());
        vo.setServiceProviderEnName(quotation.getServiceProviderEnName());
        vo.setQuotationId(quotation.getId());
        vo.setPorts(ports);
        List<PurchaseContractVo> orders = Lists.newArrayList();
        List<FeeRegisterDetailVo> FeeRegisterDetails = Lists.newArrayList();
//        HashMap<String, List<PurchaseContractDetailVo>> skuCount = new HashMap<>();
        HashMap<String, List<CostProductVo>> skuCount = new HashMap<>();
         List<CostProductVo> costProducts = Lists.newArrayList();




        //订单信息
//        List<FlowOrderShippingPlanDetailVo> planDetails = flowOrderShippingPlanDetailService.findDetailsVoByBusinessId(orderShippingPlanBusinessId);
        List<OrderShippingPlanDetailVo> planDetails = orderShippingPlanService.getDetail(orderShippingPlanId);

        List<CostProductVo> costProductVoList = Lists.newArrayList();
        List<PurchaseContractOtherDetailVo> orderOtherDetails = Lists.newArrayList();
        List<String> orderIds = Lists.newArrayList();
        HashSet<String> vendorIds = new HashSet<>();
        List<PurchaseContractDepositVo> deposits = Lists.newArrayList();
        //判断发货计划明细是否为空
        if(planDetails != null){
            //根据发运计划明细找到对应的订单，获取订单id，订单明细，订单订金等信息
            for (int i = 0; i < planDetails.size(); i++) {
                OrderShippingPlanDetailVo planDetail = planDetails.get(i);
                String orderId = planDetail.getOrderId();
                PurchaseContractVo order = purchaseContractService.get(orderId);
                List<PurchaseContractDetailVo> detailVos = purchaseContractDetailService.findDetailVosByOrderId(orderId);
                order.setDetails(detailVos);
                vendorIds.add(order.getVendorId());
                List<PurchaseContractDepositVo> depositvos = purchaseContractDepositService.findDepositvoByOrderId(orderId);
                for (PurchaseContractDepositVo depositvo : depositvos) {
                    deposits.add(depositvo);
                }
                List<PurchaseContractOtherDetailVo> otherDetails = purchaseContractOtherDetailService.findOtherDepositByOrderId(orderId);
                for (PurchaseContractOtherDetailVo otherDetail : otherDetails) {
                	otherDetail.setOrderNumber(order.getOrderNumber());
                    orderOtherDetails.add(otherDetail);
                }
                List<PurchaseContractDetailVo> details = order.getDetails();


//                //产品明细
//                for (PurchaseContractDetailVo detail : details) {
//                    String sku = detail.getSku();
//                    CostProductVo costProductVo = new CostProductVo();
//                    costProductVo.setSku(detail.getSku());
//                    costProductVo.setOrderQty(detail.getOrderQty());
//                    costProductVo.setProductId(detail.getProductId());
//                    costProductVo.setPriceAud(detail.getPriceAud());
//                    costProductVo.setPriceRmb(detail.getPriceRmb());
//                    costProductVo.setPriceUsd(detail.getPriceUsd());
//                    costProductVo.setOrderId(detail.getOrderId());
//                    costProductVo.setOrderNumber(order.getOrderNumber());
////                    costProductVo.setOrderValueAud(detail.getOrderValueAud());
////                    costProductVo.setOrderValueRmb(detail.getOrderValueRmb());
////                    costProductVo.setOrderValueUsd(detail.getOrderValueUsd());
//                    costProductVoList.add(costProductVo);
//                    if (skuCount.containsKey(sku)) {
//                        List<PurchaseContractDetailVo> contractDetail = skuCount.get(sku);
//                        contractDetail.add(detail);
//                    } else {
//                         List<PurchaseContractDetailVo> contractDetail = Lists.newArrayList();
//                        contractDetail.add(detail);
//                        skuCount.put(sku, contractDetail);
//                    }
//
//                }
                orders.add(order);
                orderIds.add(order.getId());
            }
        }
        List<FeePaymentVo> balancesVoByOrders = Lists.newArrayList();
        //根据订单id获取费用支付
        if (orderIds.size() > 0) {
            balancesVoByOrders = feePaymentService.findBalancesVoByOrderIds(orderIds);
            for (FeePaymentVo balancesVoByOrder : balancesVoByOrders) {
                String orderNumber = balancesVoByOrder.getOrderNumber();
                List<FeeRegisterDetailVo> feeRegisterDetail = feeRegisterDetailService.findDetailsByFeeRegisterIdAndApplyCost(balancesVoByOrder.getFeeRegisterId(), 1);
                FeeRegister feeRegister = feeRegisterService.getFeeRegister(balancesVoByOrder.getFeeRegisterId());
                Integer paymentStatus = feeRegister.getPaymentStatus();
                if (paymentStatus ==(Integer) 3) {
                    for (FeeRegisterDetailVo feeRegisterDetailVo : feeRegisterDetail) {
                        feeRegisterDetailVo.setOrderNumber(orderNumber);
                        //加入实付汇率
                        feeRegisterDetailVo.setPaymentRateAudToRmb(balancesVoByOrder.getPaymentRateAudToRmb());
                        feeRegisterDetailVo.setPaymentRateAudToUsd(balancesVoByOrder.getPaymentRateAudToUsd());
                        FeeRegisterDetails.add(feeRegisterDetailVo);
                    }
                }

            }
        }

//        for (Map.Entry<String, List<PurchaseContractDetailVo>> entry : skuCount.entrySet()) {
//            CostProductVo costProduct = new CostProductVo();
//            Integer num = 0;
//            BigDecimal priceAud = null;
//            BigDecimal priceRmb = null;
//            for (PurchaseContractDetailVo contractDetail : entry.getValue()) {
//                priceAud = contractDetail.getPriceAud();
//                priceRmb = contractDetail.getPriceRmb();
//                costProduct.setSku(entry.getKey());
//                costProduct.setProductId(contractDetail.getProductId());
//                costProduct.setPriceAud(priceAud);
//                costProduct.setPriceRmb(priceRmb);
//                num += contractDetail.getOrderQty();
//            }
//            costProduct.setOrderQty(num);
//            costProduct.setTotalPriceAud(priceAud.multiply(BigDecimal.valueOf(num)));
//            costProduct.setTotalPriceRmb(priceRmb.multiply(BigDecimal.valueOf(num)));
//            costProducts.add(costProduct);
//        }

        //获取海运柜及明细
        List<CustomClearancePackingVo> packingVos = customClearancePackingService.findPackingsVoByOrderIds(orderIds);
        for (CustomClearancePackingVo packingVo : packingVos) {
            List<CustomClearancePackingDetailVo> ccpdvs = packingVo.getDetails();
            for(CustomClearancePackingDetailVo ccpdv: ccpdvs) {
                ProductVo product = productService.get(ccpdv.getProductId());
                String sku = ccpdv.getSku();
                CostProductVo costProductVo = new CostProductVo();
                costProductVo.setSku(ccpdv.getSku());
                costProductVo.setOrderQty(ccpdv.getPackingQty());
                costProductVo.setProductId(ccpdv.getProductId());
                costProductVo.setPriceAud(ccpdv.getPriceAud());
                costProductVo.setPriceRmb(ccpdv.getPriceRmb());
                costProductVo.setPriceUsd(ccpdv.getPriceUsd());
                costProductVo.setMasterCartonCbm(ccpdv.getUnitCbm());
                costProductVo.setOrderId(packingVo.getOrderId());
                costProductVo.setOrderNumber(packingVo.getOrderNumber());
                costProductVo.setOrderTitle(packingVo.getOrderTitle());
                costProductVo.setPcsPerCarton(product.getProp().getPcsPerCarton());
                costProductVo.setSalesPriceAud(product.getProp().getTargetBinAud());
                costProductVo.setSalesPriceRmb(product.getProp().getTargetBinRmb());
                costProductVo.setSalesPriceUsd(product.getProp().getTargetBinUsd());
                costProductVo.setVendorCnName(product.getProp().getVendorCnName());
                costProductVo.setVendorEnName(product.getProp().getVendorEnName());
                costProductVo.setVendorId(product.getProp().getVendorId());
                costProductVo.setHsCode(product.getProp().getHsCode());
                costProductVo.setDutyRate(product.getProp().getDutyRate());
//                    costProductVo.setOrderValueAud(detail.getOrderValueAud());
//                    costProductVo.setOrderValueRmb(detail.getOrderValueRmb());
//                    costProductVo.setOrderValueUsd(detail.getOrderValueUsd());
                costProductVoList.add(costProductVo);
                if (skuCount.containsKey(sku)) {
                    List<CostProductVo> contractDetail = skuCount.get(sku);
                    contractDetail.add(costProductVo);
                } else {
                    List<CostProductVo> contractDetail = Lists.newArrayList();
                    contractDetail.add(costProductVo);
                    skuCount.put(sku, contractDetail);
                }
            }
        }

//        for (Map.Entry<String, List<CostProductVo>> entry : skuCount.entrySet()) {
//            CostProductVo costProduct = new CostProductVo();
//            Integer num = 0;
//            BigDecimal priceAud = null;
//            BigDecimal priceRmb = null;
//            for (CostProductVo contractDetail : entry.getValue()) {
//                priceAud = contractDetail.getPriceAud();
//                priceRmb = contractDetail.getPriceRmb();
//                costProduct.setSku(entry.getKey());
//                costProduct.setProductId(contractDetail.getProductId());
//                costProduct.setPriceAud(priceAud);
//                costProduct.setPriceRmb(priceRmb);
//                num += contractDetail.getOrderQty();
//            }
//            costProduct.setOrderQty(num);
//            costProduct.setTotalPriceAud(priceAud.multiply(BigDecimal.valueOf(num)));
//            costProduct.setTotalPriceRmb(priceRmb.multiply(BigDecimal.valueOf(num)));
//            costProducts.add(costProduct);
//        }

        //海关编码
        Map<String, List<CostProductVo>> hsCount = new HashMap<>();
        //关税
        List<TariffVo> tariffs = Lists.newArrayList();

        for (CostProductVo costProduct : costProducts) {
            ProductVendorProp prop = productVendorPropDao.findProductVendorPropByProductId(costProduct.getProductId());
            //从产品档案获取目标售价，与采购数量相乘，得到目标售价
            costProduct.setSalesPriceAud((prop.getTargetBinAud()==null? BigDecimal.valueOf(0) :prop.getTargetBinAud()).multiply(BigDecimal.valueOf(costProduct.getOrderQty())));
            costProduct.setSalesPriceRmb((prop.getTargetBinRmb()==null? BigDecimal.valueOf(0) :prop.getTargetBinAud()).multiply(BigDecimal.valueOf(costProduct.getOrderQty())));
            costProduct.setSalesPriceUsd((prop.getTargetBinUsd()==null? BigDecimal.valueOf(0) :prop.getTargetBinUsd()).multiply(BigDecimal.valueOf(costProduct.getOrderQty())));
            String hsCode = prop.getHsCode();
            if (hsCode != null) {
                if (hsCount.containsKey(hsCode)) {
                    List<CostProductVo> costProductVos = hsCount.get(hsCode);
                    costProductVos.add(costProduct);
                } else {
                    List<CostProductVo> costProductVos = Lists.newArrayList();
                    costProductVos.add(costProduct);
                    hsCount.put(hsCode, costProductVos);
                }
            } else {
                TariffVo tariffVo = new TariffVo();
                tariffVo.setSku(costProduct.getSku());
                tariffVo.setOrderQty(costProduct.getOrderQty());
                tariffVo.setTotalPriceUsd(costProduct.getSalesPriceUsd());
                tariffVo.setTotalPriceRmb(costProduct.getSalesValueRmb());
                tariffVo.setTotalPriceAud(costProduct.getSalesPriceAud());
                tariffs.add(tariffVo);
            }
        }

        //获取海关编码
        for (Map.Entry<String, List<CostProductVo>> entry : hsCount.entrySet()) {
            TariffVo tariffVo = BeanMapper.map(tariffDao.findTopByItemCode(entry.getKey()), TariffVo.class);
            BigDecimal salePriceAud = BigDecimal.valueOf(0);
            BigDecimal salePriceRmb = BigDecimal.valueOf(0);
            BigDecimal salePriceUsd = BigDecimal.valueOf(0);
            Integer orderQty = 0;
            for (CostProductVo costProductVo : entry.getValue()) {
                salePriceAud = salePriceAud.add(costProductVo.getSalesPriceAud()) ;
                salePriceRmb = salePriceRmb.add(costProductVo.getSalesPriceRmb());
                salePriceUsd = salePriceUsd.add(costProductVo.getSalesPriceUsd());
                orderQty += costProductVo.getOrderQty();
            }
            tariffVo.setTotalPriceAud(salePriceAud);
            tariffVo.setTotalPriceRmb(salePriceRmb);
            tariffVo.setTotalPriceUsd(salePriceUsd);
            tariffVo.setOrderQty(orderQty);
            tariffs.add(tariffVo);
        }

        //将按照柜数收费的本地询价拆分为哥哥柜型的报价
        List<ServiceProviderQuotationChargeItemVo> chargeItems = quotation.getChargeItems();
        List<CostChargeItemVo> costChargeItems = Lists.newArrayList();
        for (ServiceProviderQuotationChargeItemVo chargeItem : chargeItems) {
            if (!chargeItem.getUnitCnName().equals("每个货柜")) {
                CostChargeItemVo costChargeItem = new CostChargeItemVo();
                QuotationChargeItemVoToCostChargeItem(chargeItem, costChargeItem);
                costChargeItem.setPriceAud(chargeItem.getPriceGp20Aud());
                costChargeItem.setPriceRmb(chargeItem.getPriceGp20Rmb());
                costChargeItem.setPriceUsd(chargeItem.getPriceGp20Usd());
                costChargeItems.add(costChargeItem);
            } else {
                CostChargeItemVo costChargeItem1 = new CostChargeItemVo();
                QuotationChargeItemVoToCostChargeItem(chargeItem, costChargeItem1);
                costChargeItem1.setContainerType(1);
                costChargeItem1.setPriceUsd(chargeItem.getPriceGp20Usd());
                costChargeItem1.setPriceRmb(chargeItem.getPriceGp20Rmb());
                costChargeItem1.setPriceAud(chargeItem.getPriceGp20Aud());
                costChargeItems.add(costChargeItem1);
                CostChargeItemVo costChargeItem2 = new CostChargeItemVo();
                QuotationChargeItemVoToCostChargeItem(chargeItem, costChargeItem2);
                costChargeItem2.setContainerType(2);
                costChargeItem2.setPriceUsd(chargeItem.getPriceGp40Usd());
                costChargeItem2.setPriceRmb(chargeItem.getPriceGp40Rmb());
                costChargeItem2.setPriceAud(chargeItem.getPriceGp40Aud());
                costChargeItems.add(costChargeItem2);

                CostChargeItemVo costChargeItem3 = new CostChargeItemVo();
                QuotationChargeItemVoToCostChargeItem(chargeItem, costChargeItem3);
                costChargeItem3.setContainerType(3);
                costChargeItem3.setPriceUsd(chargeItem.getPriceHq40Usd());
                costChargeItem3.setPriceRmb(chargeItem.getPriceHq40Rmb());
                costChargeItem3.setPriceAud(chargeItem.getPriceHq40Aud());
                costChargeItems.add(costChargeItem3);

                CostChargeItemVo costChargeItem4 = new CostChargeItemVo();
                QuotationChargeItemVoToCostChargeItem(chargeItem, costChargeItem4);
                costChargeItem4.setContainerType(4);
                costChargeItem4.setPriceUsd(chargeItem.getPriceLclUsd());
                costChargeItem4.setPriceRmb(chargeItem.getPriceLclRmb());
                costChargeItem4.setPriceAud(chargeItem.getPriceLclAud());
                costChargeItems.add(costChargeItem4);
            }
        }
        List<CostChargeItemVo> collect = costChargeItems.stream().filter(costChargeItemVo ->null != costChargeItemVo.getPriceAud()).sorted(Comparator.comparing(CostChargeItemVo::getItemCnName)).collect(Collectors.toList());



        vo.setCostChargeItems(collect);//本地费用
        vo.setTariffs(tariffs);//关税
        vo.setOtherDetails(orderOtherDetails);//订单支付 1
        vo.setCostProducts(costProductVoList);//产品成本
        vo.setVendorNumber(vendorIds.size());//供应商数量（已去重）
        vo.setFeeRegisterDetails(FeeRegisterDetails);//订单支付
        vo.setPackings(packingVos);//海运费用
        vo.setOrders(orders);//并且和packings一起用来计算每个订单的柜数
        vo.setOrderDeposits(deposits);//订单支付
        vo.setOrderBalances(balancesVoByOrders);//订单支付
        return vo;
    }

    private void QuotationChargeItemVoToCostChargeItem(ServiceProviderQuotationChargeItemVo sv, CostChargeItemVo cv) {
        cv.setCurrency(sv.getCurrency());
        cv.setItemCnName(sv.getItemCnName());
        cv.setItemEnName(sv.getItemEnName());
        cv.setItemId(sv.getItemId());
        cv.setUnitCnName(sv.getUnitCnName());
        cv.setUnitEnName(sv.getUnitEnName());
        cv.setUnitId(sv.getUnitId());
    }
}

