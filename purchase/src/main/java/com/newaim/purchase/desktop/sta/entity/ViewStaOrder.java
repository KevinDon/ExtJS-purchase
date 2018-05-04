package com.newaim.purchase.desktop.sta.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name="na_view_sta_order")
public class ViewStaOrder implements Serializable{

    @Id
    private String id;

    private String orderId;
    private String orderNumber;//订单号
    private String orderTitle;
    private String vendorProductCategoryId;//产品线
    private String vendorProductCategoryAlias;
    private String orderIndex;//订单批次
    /**供应商信息*/
    private String vendorId;
    private String vendorCnName;//供应商
    private String vendorEnName;
    /**出货港口*/
    private String originPortId;
    private String originPortCnName;//起始港
    private String originPortEnName;
    /**货柜类型*/
    private Integer containerType;//侯贵类型
    /**货柜数量*/
    private BigDecimal containerQty;

    private Integer currency;//结算货币
    /**汇率*/
    private BigDecimal rateAudToRmb;
    private BigDecimal rateAudToUsd;
    private BigDecimal totalCbm;//采购总体积
    /**总价格*/
    private BigDecimal totalPriceAud;//合同金额
    private BigDecimal totalPriceRmb;
    private BigDecimal totalPriceUsd;
    /**冲销金额*/
    private BigDecimal writeOffAud;//冲销金额
    private BigDecimal writeOffRmb;
    private BigDecimal writeOffUsd;
    /**运费税金*/
    private BigDecimal freightGstAud;//运费税金
    private BigDecimal freightGstUsd;//运费税金
    private BigDecimal freightGstRmb;//运费税金

    /**工厂交货价或总货值*/
    private BigDecimal exWorkAud;
    private BigDecimal exWorkRmb ;
    private BigDecimal exWorkUsd;

    private String buyerId;
    private String buyerCnName;//采购员
    private String buyerEnName;
    /**预计到港时间*/
    private Date estimatedEta;
    /**到港月份*/
    private Date monthEta;
    /**到港年份*/
    private Date yearEta;


    private Date readyDate;//完货时间
    private Date etd;//预计发货时间
    private Date eta;//预计到岸时间
    /**定金*/
    private BigDecimal depositAud;//订金
    private BigDecimal depositRmb;
    private BigDecimal depositUsd;
    private BigDecimal depositRate;//订金率
    /**最后总价格*/
    private BigDecimal finalTotalPriceAud;//最终发票金额
    private BigDecimal finalTotalPriceRmb;
    private BigDecimal finalTotalPriceUsd;

    private BigDecimal balanceAud;//尾款
    private BigDecimal balanceRmb;
    private BigDecimal balanceUsd;
    private BigDecimal estimated_balance;//预计尾款

    private String serviceProviderId;
    private String serviceProviderCnName;//货代
    private String serviceProviderEnName;

    private Date agentNotificationDate;//通知货代日期
    private Date deliveryTime;//到仓时间
    private Integer arrivalDays;//抵达天数
    private Date shippingDocReceivedDate;//清关文件接收日期
    private Date shippingDocForwardedDate;//清关文件转发日期

    /**海运保险费*/
    private BigDecimal freightInsuranceAud;
    private BigDecimal freightInsuranceUsd;
    private BigDecimal freightInsuranceRmb;

    /**本地费*/
    private BigDecimal chargeItemFeeAud;//本地费用
    private BigDecimal chargeItemFeeRmb;
    private BigDecimal chargeItemFeeUsd;
    /**海运成本费*/
    private BigDecimal portFeeAud;//海运费用
    private BigDecimal portFeeRmb;
    private BigDecimal portFeeUsd;

    private BigDecimal totalFreightAud;//总运费
    private BigDecimal totalFreightRmb;
    private BigDecimal totalFreightUsd;
    private BigDecimal totalValueAud;//工厂交货价或总货值

    /**关税*/
    private BigDecimal tariffAud;//关税
    private BigDecimal tariffRmb;
    private BigDecimal tariffUsd;

    private BigDecimal customProcessingFeeAud;
    private BigDecimal customProcessingFeeRmb;
    private BigDecimal customProcessingFeeUsd;

    private BigDecimal gstAud;//商品服务税
    private BigDecimal gstRmb;
    private BigDecimal gstUsd;

    private BigDecimal electronicProcessingFeeAud;//电放费
    private Integer telexReleased;//是否电放
    private Integer costCurrency;
    private BigDecimal rateAudToRmbContract;
    private BigDecimal rateAudToUsdContract;//澳元兑美元汇率

    private Date leadTime;//工厂生产周期
    private Integer sailingDays;//海运时间
    private Date depositDate;//订金时间
    private Date balanceDate;//费用分支付时间
    private Integer credit_terms;//尾款条款
    private BigDecimal totalCostAud;//总费用

    private Integer status;//状态
    private Date createdAt;//创建时间
    private Date endTime;//流程完成时间
    private Date startTime;//流程申请时间

    private Integer depositType;//定金类型
    /**定金汇率*/
    private BigDecimal depositRateAudToRmb;
    private BigDecimal depositRateAudToUsd;
    /**尾款汇率*/
    private BigDecimal balanceRateAudToRmb;
    private BigDecimal balanceRateAudToUsd;
    /**f服务商汇率*/
    private BigDecimal serviceRateAudToRmb;
    private BigDecimal serviceRateAudToUsd;
    /**成本汇率*/
    private BigDecimal costRateAudToRmb;
    private BigDecimal costRateAudToUsd;
    /**总售价*/
    private BigDecimal totalSalesPriceAud;
    private BigDecimal totalSalesPriceRmb;
    private BigDecimal totalSalesPriceUsd;

    private String buyerDeptId;
    private String buyerDeptEnName;//采购部门
    private String buyerDeptCnName;

    private Integer totalOrderQty;//总数量

    public Integer getTotalOrderQty() {
        return totalOrderQty;
    }

    public void setTotalOrderQty(Integer totalOrderQty) {
        this.totalOrderQty = totalOrderQty;
    }




    public BigDecimal getFreightGstAud() {
        return freightGstAud;
    }

    public void setFreightGstAud(BigDecimal freightGstAud) {
        this.freightGstAud = freightGstAud;
    }

    public BigDecimal getFreightGstUsd() {
        return freightGstUsd;
    }

    public void setFreightGstUsd(BigDecimal freightGstUsd) {
        this.freightGstUsd = freightGstUsd;
    }

    public BigDecimal getFreightGstRmb() {
        return freightGstRmb;
    }

    public void setFreightGstRmb(BigDecimal freightGstRmb) {
        this.freightGstRmb = freightGstRmb;
    }

    public BigDecimal getFreightInsuranceAud() {
        return freightInsuranceAud;
    }

    public void setFreightInsuranceAud(BigDecimal freightInsuranceAud) {
        this.freightInsuranceAud = freightInsuranceAud;
    }

    public BigDecimal getFreightInsuranceUsd() {
        return freightInsuranceUsd;
    }

    public void setFreightInsuranceUsd(BigDecimal freightInsuranceUsd) {
        this.freightInsuranceUsd = freightInsuranceUsd;
    }

    public BigDecimal getFreightInsuranceRmb() {
        return freightInsuranceRmb;
    }

    public void setFreightInsuranceRmb(BigDecimal freightInsuranceRmb) {
        this.freightInsuranceRmb = freightInsuranceRmb;
    }

    public BigDecimal getExWorkAud() {
        return exWorkAud;
    }

    public void setExWorkAud(BigDecimal exWorkAud) {
        this.exWorkAud = exWorkAud;
    }

    public BigDecimal getExWorkRmb() {
        return exWorkRmb;
    }

    public void setExWorkRmb(BigDecimal exWorkRmb) {
        this.exWorkRmb = exWorkRmb;
    }

    public BigDecimal getExWorkUsd() {
        return exWorkUsd;
    }

    public void setExWorkUsd(BigDecimal exWorkUsd) {
        this.exWorkUsd = exWorkUsd;
    }

    public Integer getDepositType() {
        return depositType;
    }

    public void setDepositType(Integer depositType) {
        this.depositType = depositType;
    }

    public BigDecimal getDepositRateAudToRmb() {
        return depositRateAudToRmb;
    }

    public void setDepositRateAudToRmb(BigDecimal depositRateAudToRmb) {
        this.depositRateAudToRmb = depositRateAudToRmb;
    }

    public BigDecimal getDepositRateAudToUsd() {
        return depositRateAudToUsd;
    }

    public void setDepositRateAudToUsd(BigDecimal depositRateAudToUsd) {
        this.depositRateAudToUsd = depositRateAudToUsd;
    }

    public BigDecimal getBalanceRateAudToRmb() {
        return balanceRateAudToRmb;
    }

    public void setBalanceRateAudToRmb(BigDecimal balanceRateAudToRmb) {
        this.balanceRateAudToRmb = balanceRateAudToRmb;
    }

    public BigDecimal getBalanceRateAudToUsd() {
        return balanceRateAudToUsd;
    }

    public void setBalanceRateAudToUsd(BigDecimal balanceRateAudToUsd) {
        this.balanceRateAudToUsd = balanceRateAudToUsd;
    }

    public BigDecimal getServiceRateAudToRmb() {
        return serviceRateAudToRmb;
    }

    public void setServiceRateAudToRmb(BigDecimal serviceRateAudToRmb) {
        this.serviceRateAudToRmb = serviceRateAudToRmb;
    }

    public BigDecimal getServiceRateAudToUsd() {
        return serviceRateAudToUsd;
    }

    public void setServiceRateAudToUsd(BigDecimal serviceRateAudToUsd) {
        this.serviceRateAudToUsd = serviceRateAudToUsd;
    }

    public BigDecimal getCostRateAudToRmb() {
        return costRateAudToRmb;
    }

    public void setCostRateAudToRmb(BigDecimal costRateAudToRmb) {
        this.costRateAudToRmb = costRateAudToRmb;
    }

    public BigDecimal getCostRateAudToUsd() {
        return costRateAudToUsd;
    }

    public void setCostRateAudToUsd(BigDecimal costRateAudToUsd) {
        this.costRateAudToUsd = costRateAudToUsd;
    }

    public BigDecimal getTotalSalesPriceAud() {
        return totalSalesPriceAud;
    }

    public void setTotalSalesPriceAud(BigDecimal totalSalesPriceAud) {
        this.totalSalesPriceAud = totalSalesPriceAud;
    }

    public BigDecimal getTotalSalesPriceRmb() {
        return totalSalesPriceRmb;
    }

    public void setTotalSalesPriceRmb(BigDecimal totalSalesPriceRmb) {
        this.totalSalesPriceRmb = totalSalesPriceRmb;
    }

    public BigDecimal getTotalSalesPriceUsd() {
        return totalSalesPriceUsd;
    }

    public void setTotalSalesPriceUsd(BigDecimal totalSalesPriceUsd) {
        this.totalSalesPriceUsd = totalSalesPriceUsd;
    }

    public String getBuyerDeptId() {
        return buyerDeptId;
    }

    public void setBuyerDeptId(String buyerDeptId) {
        this.buyerDeptId = buyerDeptId;
    }

    public String getBuyerDeptEnName() {
        return buyerDeptEnName;
    }

    public void setBuyerDeptEnName(String buyerDeptEnName) {
        this.buyerDeptEnName = buyerDeptEnName;
    }

    public String getBuyerDeptCnName() {
        return buyerDeptCnName;
    }

    public void setBuyerDeptCnName(String buyerDeptCnName) {
        this.buyerDeptCnName = buyerDeptCnName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }


    public Date getEstimatedEta() {
        return estimatedEta;
    }

    public void setEstimatedEta(Date estimatedEta) {
        this.estimatedEta = estimatedEta;
    }

    public Date getMonthEta() {
        return monthEta;
    }

    public void setMonthEta(Date monthEta) {
        this.monthEta = monthEta;
    }

    public Date getYearEta() {
        return yearEta;
    }

    public void setYearEta(Date yearEta) {
        this.yearEta = yearEta;
    }

    public BigDecimal getEstimated_balance() {
        return estimated_balance;
    }

    public void setEstimated_balance(BigDecimal estimated_balance) {
        this.estimated_balance = estimated_balance;
    }

    public BigDecimal getTotalValueAud() {
        return totalValueAud;
    }

    public void setTotalValueAud(BigDecimal totalValueAud) {
        this.totalValueAud = totalValueAud;
    }

    public BigDecimal getElectronicProcessingFeeAud() {
        return electronicProcessingFeeAud;
    }

    public void setElectronicProcessingFeeAud(BigDecimal electronicProcessingFeeAud) {
        this.electronicProcessingFeeAud = electronicProcessingFeeAud;
    }

    public Integer getTelexReleased() {
        return telexReleased;
    }

    public void setTelexReleased(Integer telexReleased) {
        this.telexReleased = telexReleased;
    }

    public Date getDepositDate() {
        return depositDate;
    }

    public void setDepositDate(Date depositDate) {
        this.depositDate = depositDate;
    }

    public Date getBalanceDate() {
        return balanceDate;
    }

    public void setBalanceDate(Date balanceDate) {
        this.balanceDate = balanceDate;
    }

    public Integer getCredit_terms() {
        return credit_terms;
    }

    public void setCredit_terms(Integer credit_terms) {
        this.credit_terms = credit_terms;
    }

    public BigDecimal getTotalCostAud() {
        return totalCostAud;
    }

    public void setTotalCostAud(BigDecimal totalCostAud) {
        this.totalCostAud = totalCostAud;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderTitle() {
        return orderTitle;
    }

    public void setOrderTitle(String orderTitle) {
        this.orderTitle = orderTitle;
    }

    public String getVendorProductCategoryId() {
        return vendorProductCategoryId;
    }

    public void setVendorProductCategoryId(String vendorProductCategoryId) {
        this.vendorProductCategoryId = vendorProductCategoryId;
    }

    public String getVendorProductCategoryAlias() {
        return vendorProductCategoryAlias;
    }

    public void setVendorProductCategoryAlias(String vendorProductCategoryAlias) {
        this.vendorProductCategoryAlias = vendorProductCategoryAlias;
    }

    public String getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(String orderIndex) {
        this.orderIndex = orderIndex;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendorCnName() {
        return vendorCnName;
    }

    public void setVendorCnName(String vendorCnName) {
        this.vendorCnName = vendorCnName;
    }

    public String getVendorEnName() {
        return vendorEnName;
    }

    public void setVendorEnName(String vendorEnName) {
        this.vendorEnName = vendorEnName;
    }

    public String getOriginPortId() {
        return originPortId;
    }

    public void setOriginPortId(String originPortId) {
        this.originPortId = originPortId;
    }

    public String getOriginPortCnName() {
        return originPortCnName;
    }

    public void setOriginPortCnName(String originPortCnName) {
        this.originPortCnName = originPortCnName;
    }

    public String getOriginPortEnName() {
        return originPortEnName;
    }

    public void setOriginPortEnName(String originPortEnName) {
        this.originPortEnName = originPortEnName;
    }

    public Integer getContainerType() {
        return containerType;
    }

    public void setContainerType(Integer containerType) {
        this.containerType = containerType;
    }

    public BigDecimal getContainerQty() {
        return containerQty;
    }

    public void setContainerQty(BigDecimal containerQty) {
        this.containerQty = containerQty;
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

    public BigDecimal getTotalCbm() {
        return totalCbm;
    }

    public void setTotalCbm(BigDecimal totalCbm) {
        this.totalCbm = totalCbm;
    }

    public BigDecimal getTotalPriceAud() {
        return totalPriceAud;
    }

    public void setTotalPriceAud(BigDecimal totalPriceAud) {
        this.totalPriceAud = totalPriceAud;
    }

    public BigDecimal getTotalPriceRmb() {
        return totalPriceRmb;
    }

    public void setTotalPriceRmb(BigDecimal totalPriceRmb) {
        this.totalPriceRmb = totalPriceRmb;
    }

    public BigDecimal getTotalPriceUsd() {
        return totalPriceUsd;
    }

    public void setTotalPriceUsd(BigDecimal totalPriceUsd) {
        this.totalPriceUsd = totalPriceUsd;
    }

    public BigDecimal getWriteOffAud() {
        return writeOffAud;
    }

    public void setWriteOffAud(BigDecimal writeOffAud) {
        this.writeOffAud = writeOffAud;
    }

    public BigDecimal getWriteOffRmb() {
        return writeOffRmb;
    }

    public void setWriteOffRmb(BigDecimal writeOffRmb) {
        this.writeOffRmb = writeOffRmb;
    }

    public BigDecimal getWriteOffUsd() {
        return writeOffUsd;
    }

    public void setWriteOffUsd(BigDecimal writeOffUsd) {
        this.writeOffUsd = writeOffUsd;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getBuyerCnName() {
        return buyerCnName;
    }

    public void setBuyerCnName(String buyerCnName) {
        this.buyerCnName = buyerCnName;
    }

    public String getBuyerEnName() {
        return buyerEnName;
    }

    public void setBuyerEnName(String buyerEnName) {
        this.buyerEnName = buyerEnName;
    }

    public Date getReadyDate() {
        return readyDate;
    }

    public void setReadyDate(Date readyDate) {
        this.readyDate = readyDate;
    }

    public Date getEtd() {
        return etd;
    }

    public void setEtd(Date etd) {
        this.etd = etd;
    }

    public Date getEta() {
        return eta;
    }

    public void setEta(Date eta) {
        this.eta = eta;
    }

    public BigDecimal getDepositAud() {
        return depositAud;
    }

    public void setDepositAud(BigDecimal depositAud) {
        this.depositAud = depositAud;
    }

    public BigDecimal getDepositRmb() {
        return depositRmb;
    }

    public void setDepositRmb(BigDecimal depositRmb) {
        this.depositRmb = depositRmb;
    }

    public BigDecimal getDepositUsd() {
        return depositUsd;
    }

    public void setDepositUsd(BigDecimal depositUsd) {
        this.depositUsd = depositUsd;
    }

    public BigDecimal getDepositRate() {
        return depositRate;
    }

    public void setDepositRate(BigDecimal depositRate) {
        this.depositRate = depositRate;
    }

    public BigDecimal getFinalTotalPriceAud() {
        return finalTotalPriceAud;
    }

    public void setFinalTotalPriceAud(BigDecimal finalTotalPriceAud) {
        this.finalTotalPriceAud = finalTotalPriceAud;
    }

    public BigDecimal getFinalTotalPriceRmb() {
        return finalTotalPriceRmb;
    }

    public void setFinalTotalPriceRmb(BigDecimal finalTotalPriceRmb) {
        this.finalTotalPriceRmb = finalTotalPriceRmb;
    }

    public BigDecimal getFinalTotalPriceUsd() {
        return finalTotalPriceUsd;
    }

    public void setFinalTotalPriceUsd(BigDecimal finalTotalPriceUsd) {
        this.finalTotalPriceUsd = finalTotalPriceUsd;
    }

    public BigDecimal getBalanceAud() {
        return balanceAud;
    }

    public void setBalanceAud(BigDecimal balanceAud) {
        this.balanceAud = balanceAud;
    }

    public BigDecimal getBalanceRmb() {
        return balanceRmb;
    }

    public void setBalanceRmb(BigDecimal balanceRmb) {
        this.balanceRmb = balanceRmb;
    }

    public BigDecimal getBalanceUsd() {
        return balanceUsd;
    }

    public void setBalanceUsd(BigDecimal balanceUsd) {
        this.balanceUsd = balanceUsd;
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

    public Date getAgentNotificationDate() {
        return agentNotificationDate;
    }

    public void setAgentNotificationDate(Date agentNotificationDate) {
        this.agentNotificationDate = agentNotificationDate;
    }

    public Date getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public Integer getArrivalDays() {
        return arrivalDays;
    }

    public void setArrivalDays(Integer arrivalDays) {
        this.arrivalDays = arrivalDays;
    }

    public Date getShippingDocReceivedDate() {
        return shippingDocReceivedDate;
    }

    public void setShippingDocReceivedDate(Date shippingDocReceivedDate) {
        this.shippingDocReceivedDate = shippingDocReceivedDate;
    }

    public Date getShippingDocForwardedDate() {
        return shippingDocForwardedDate;
    }

    public void setShippingDocForwardedDate(Date shippingDocForwardedDate) {
        this.shippingDocForwardedDate = shippingDocForwardedDate;
    }

    public BigDecimal getChargeItemFeeAud() {
        return chargeItemFeeAud;
    }

    public void setChargeItemFeeAud(BigDecimal chargeItemFeeAud) {
        this.chargeItemFeeAud = chargeItemFeeAud;
    }

    public BigDecimal getChargeItemFeeRmb() {
        return chargeItemFeeRmb;
    }

    public void setChargeItemFeeRmb(BigDecimal chargeItemFeeRmb) {
        this.chargeItemFeeRmb = chargeItemFeeRmb;
    }

    public BigDecimal getChargeItemFeeUsd() {
        return chargeItemFeeUsd;
    }

    public void setChargeItemFeeUsd(BigDecimal chargeItemFeeUsd) {
        this.chargeItemFeeUsd = chargeItemFeeUsd;
    }

    public BigDecimal getPortFeeAud() {
        return portFeeAud;
    }

    public void setPortFeeAud(BigDecimal portFeeAud) {
        this.portFeeAud = portFeeAud;
    }

    public BigDecimal getPortFeeRmb() {
        return portFeeRmb;
    }

    public void setPortFeeRmb(BigDecimal portFeeRmb) {
        this.portFeeRmb = portFeeRmb;
    }

    public BigDecimal getPortFeeUsd() {
        return portFeeUsd;
    }

    public void setPortFeeUsd(BigDecimal portFeeUsd) {
        this.portFeeUsd = portFeeUsd;
    }

    public BigDecimal getTotalFreightAud() {
        return totalFreightAud;
    }

    public void setTotalFreightAud(BigDecimal totalFreightAud) {
        this.totalFreightAud = totalFreightAud;
    }

    public BigDecimal getTotalFreightRmb() {
        return totalFreightRmb;
    }

    public void setTotalFreightRmb(BigDecimal totalFreightRmb) {
        this.totalFreightRmb = totalFreightRmb;
    }

    public BigDecimal getTotalFreightUsd() {
        return totalFreightUsd;
    }

    public void setTotalFreightUsd(BigDecimal totalFreightUsd) {
        this.totalFreightUsd = totalFreightUsd;
    }

    public BigDecimal getTariffAud() {
        return tariffAud;
    }

    public void setTariffAud(BigDecimal tariffAud) {
        this.tariffAud = tariffAud;
    }

    public BigDecimal getTariffRmb() {
        return tariffRmb;
    }

    public void setTariffRmb(BigDecimal tariffRmb) {
        this.tariffRmb = tariffRmb;
    }

    public BigDecimal getTariffUsd() {
        return tariffUsd;
    }

    public void setTariffUsd(BigDecimal tariffUsd) {
        this.tariffUsd = tariffUsd;
    }

    public BigDecimal getCustomProcessingFeeAud() {
        return customProcessingFeeAud;
    }

    public void setCustomProcessingFeeAud(BigDecimal customProcessingFeeAud) {
        this.customProcessingFeeAud = customProcessingFeeAud;
    }

    public BigDecimal getCustomProcessingFeeRmb() {
        return customProcessingFeeRmb;
    }

    public void setCustomProcessingFeeRmb(BigDecimal customProcessingFeeRmb) {
        this.customProcessingFeeRmb = customProcessingFeeRmb;
    }

    public BigDecimal getCustomProcessingFeeUsd() {
        return customProcessingFeeUsd;
    }

    public void setCustomProcessingFeeUsd(BigDecimal customProcessingFeeUsd) {
        this.customProcessingFeeUsd = customProcessingFeeUsd;
    }

    public BigDecimal getGstAud() {
        return gstAud;
    }

    public void setGstAud(BigDecimal gstAud) {
        this.gstAud = gstAud;
    }

    public BigDecimal getGstRmb() {
        return gstRmb;
    }

    public void setGstRmb(BigDecimal gstRmb) {
        this.gstRmb = gstRmb;
    }

    public BigDecimal getGstUsd() {
        return gstUsd;
    }

    public void setGstUsd(BigDecimal gstUsd) {
        this.gstUsd = gstUsd;
    }

    public Integer getCostCurrency() {
        return costCurrency;
    }

    public void setCostCurrency(Integer costCurrency) {
        this.costCurrency = costCurrency;
    }

    public BigDecimal getRateAudToRmbContract() {
        return rateAudToRmbContract;
    }

    public void setRateAudToRmbContract(BigDecimal rateAudToRmbContract) {
        this.rateAudToRmbContract = rateAudToRmbContract;
    }

    public BigDecimal getRateAudToUsdContract() {
        return rateAudToUsdContract;
    }

    public void setRateAudToUsdContract(BigDecimal rateAudToUsdContract) {
        this.rateAudToUsdContract = rateAudToUsdContract;
    }

    public Date getLeadTime() {
        return leadTime;
    }

    public void setLeadTime(Date leadTime) {
        this.leadTime = leadTime;
    }

    public Integer getSailingDays() {
        return sailingDays;
    }

    public void setSailingDays(Integer sailingDays) {
        this.sailingDays = sailingDays;
    }
}
