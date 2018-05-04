package com.newaim.purchase.archives.service_provider.vo;


import com.google.common.collect.Lists;
import com.newaim.purchase.archives.flow.finance.vo.FeePaymentVo;
import com.newaim.purchase.archives.flow.finance.vo.FeeRegisterDetailVo;
import com.newaim.purchase.archives.flow.finance.vo.PurchaseContractDepositVo;
import com.newaim.purchase.archives.flow.purchase.entity.PurchaseContractOtherDetail;
import com.newaim.purchase.archives.flow.purchase.vo.CustomClearancePackingVo;
import com.newaim.purchase.archives.flow.purchase.vo.PurchaseContractOtherDetailVo;
import com.newaim.purchase.archives.flow.purchase.vo.PurchaseContractVo;
import com.newaim.purchase.archives.flow.shipping.vo.ServiceProviderQuotationChargeItemVo;
import com.newaim.purchase.archives.flow.shipping.vo.ServiceProviderQuotationPortVo;
import com.newaim.purchase.archives.product.entity.CostChargeItem;
import com.newaim.purchase.archives.product.entity.CostProduct;
import com.newaim.purchase.archives.product.vo.CostChargeItemVo;
import com.newaim.purchase.archives.product.vo.CostProductVo;
import com.newaim.purchase.archives.product.vo.TariffVo;
import com.newaim.purchase.config.json.JsonMoney;
import com.newaim.purchase.desktop.sta.vo.StaCostVo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ServiceProviderInvoiceVo implements Serializable{

    private String id;

    /**
     * 服务商相关字段
     */
    private String serviceProviderId;
    private String serviceProviderCnName;
    private String serviceProviderEnName;

    /**生效、失效日期*/
    private Date effectiveDate;
    private Date validUntil;


    /**结算货币、汇率*/
    private Integer currency;
    @JsonMoney
    private BigDecimal rateAudToRmb;
    @JsonMoney
    private BigDecimal rateAudToUsd;
    /**调节汇率*/
    @JsonMoney
    private BigDecimal currencyAdjustment;
    private String contractId;
    private String type;
    private String flowOrderShippingPlanId;
    private String quotationId;

    private  Integer vendorNumber;

    private List<CostProductVo> costProducts = Lists.newArrayList();
    private List<ServiceProviderQuotationPortVo> ports = Lists.newArrayList();

    private List<ServiceProviderQuotationChargeItemVo> chargeItems = Lists.newArrayList();

    private List<TariffVo> tariffs = Lists.newArrayList();

    private List<CustomClearancePackingVo> packings = Lists.newArrayList();

    private List<PurchaseContractVo> orders = Lists.newArrayList();
    private List<PurchaseContractDepositVo> orderDeposits = Lists.newArrayList();
    private List<FeePaymentVo> orderBalances = Lists.newArrayList();
    private List<FeeRegisterDetailVo> FeeRegisterDetails = Lists.newArrayList();

    private List<CostChargeItemVo> costChargeItems = Lists.newArrayList();

    private BigDecimal cubicWeight;
    private BigDecimal totalShippingCbm;

    private List<PurchaseContractOtherDetailVo> otherDetails = Lists.newArrayList();

    public List<FeeRegisterDetailVo> getFeeRegisterDetails() {
        return FeeRegisterDetails;
    }


    public List<CostProductVo> getCostProducts() {
        return costProducts;
    }

    public void setCostProducts(List<CostProductVo> costProducts) {
        this.costProducts = costProducts;
    }

    public void setFeeRegisterDetails(List<FeeRegisterDetailVo> feeRegisterDetails) {
        FeeRegisterDetails = feeRegisterDetails;
    }

    public List<CostChargeItemVo> getCostChargeItems() {
        return costChargeItems;
    }

    public void setCostChargeItems(List<CostChargeItemVo> costChargeItems) {
        this.costChargeItems = costChargeItems;
    }

    public List<PurchaseContractOtherDetailVo> getOtherDetails() {
        return otherDetails;
    }

    public void setOtherDetails(List<PurchaseContractOtherDetailVo> otherDetails) {
        this.otherDetails = otherDetails;
    }

    public BigDecimal getCubicWeight() {
        return cubicWeight;
    }

    public void setCubicWeight(BigDecimal cubicWeight) {
        this.cubicWeight = cubicWeight;
    }

    public BigDecimal getTotalShippingCbm() {
        return totalShippingCbm;
    }

    public void setTotalShippingCbm(BigDecimal totalShippingCbm) {
        this.totalShippingCbm = totalShippingCbm;
    }

    public List<PurchaseContractDepositVo> getOrderDeposits() {
        return orderDeposits;
    }

    public void setOrderDeposits(List<PurchaseContractDepositVo> orderDeposits) {
        this.orderDeposits = orderDeposits;
    }

    public List<FeePaymentVo> getOrderBalances() {
        return orderBalances;
    }

    public void setOrderBalances(List<FeePaymentVo> orderBalances) {
        this.orderBalances = orderBalances;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceProviderId() {
        return serviceProviderId;
    }

    public void setServiceProviderId(String serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
    }

    public String getServiceProviderCnName() {
        return serviceProviderCnName;
    }

    public void setServiceProviderCnName(String serviceProviderCnName) {
        this.serviceProviderCnName = serviceProviderCnName;
    }

    public String getServiceProviderEnName() {
        return serviceProviderEnName;
    }

    public void setServiceProviderEnName(String serviceProviderEnName) {
        this.serviceProviderEnName = serviceProviderEnName;
    }



    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Date getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(Date validUntil) {
        this.validUntil = validUntil;
    }

    public Integer getCurrency() {
        return currency;
    }

    public void setCurrency(Integer currency) {
        this.currency = currency;
    }

    public BigDecimal getRateAudToRmb() {
        return rateAudToRmb;
    }

    public void setRateAudToRmb(BigDecimal rateAudToRmb) {
        this.rateAudToRmb = rateAudToRmb;
    }

    public BigDecimal getRateAudToUsd() {
        return rateAudToUsd;
    }

    public void setRateAudToUsd(BigDecimal rateAudToUsd) {
        this.rateAudToUsd = rateAudToUsd;
    }

    public BigDecimal getCurrencyAdjustment() {
        return currencyAdjustment;
    }

    public void setCurrencyAdjustment(BigDecimal currencyAdjustment) {
        this.currencyAdjustment = currencyAdjustment;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFlowOrderShippingPlanId() {
        return flowOrderShippingPlanId;
    }

    public void setFlowOrderShippingPlanId(String flowOrderShippingPlanId) {
        this.flowOrderShippingPlanId = flowOrderShippingPlanId;
    }

    public String getQuotationId() {
        return quotationId;
    }

    public void setQuotationId(String quotationId) {
        this.quotationId = quotationId;
    }

    public List<ServiceProviderQuotationPortVo> getPorts() {
        return ports;
    }

    public void setPorts(List<ServiceProviderQuotationPortVo> ports) {
        this.ports = ports;
    }

    public List<ServiceProviderQuotationChargeItemVo> getChargeItems() {
        return chargeItems;
    }

    public void setChargeItems(List<ServiceProviderQuotationChargeItemVo> chargeItems) {
        this.chargeItems = chargeItems;
    }

    public List<TariffVo> getTariffs() {
        return tariffs;
    }

    public void setTariffs(List<TariffVo> tariffs) {
        this.tariffs = tariffs;
    }

    public List<CustomClearancePackingVo> getPackings() {
        return packings;
    }

    public void setPackings(List<CustomClearancePackingVo> packings) {
        this.packings = packings;
    }

    public List<PurchaseContractVo> getOrders() {
        return orders;
    }

    public void setOrders(List<PurchaseContractVo> orders) {
        this.orders = orders;
    }

    public Integer getVendorNumber() {
        return vendorNumber;
    }

    public void setVendorNumber(Integer vendorNumber) {
        this.vendorNumber = vendorNumber;
    }
}
