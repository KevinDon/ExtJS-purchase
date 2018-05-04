package com.newaim.purchase.archives.flow.purchase.vo;

import com.google.common.collect.Lists;
import com.newaim.purchase.archives.finance.vo.BankAccountVo;
import com.newaim.purchase.config.json.JsonMoney;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class PurchaseContractVo implements Serializable{

    private String id;
    private Date orderDate;//订单日期
    private String orderTitle;//订单标题
    private String orderIndex;//批次号
    private String orderNumber;//订单号

    private String vendorId;//供应商Id
    private String vendorCnName;
    private String vendorEnName;
    private String vendorProductCategoryId;
    private String vendorProductCategoryAlias;
    private String factoryContractNumber;//工厂合同号

    private String sellerCnName;//卖方信息
    private String sellerEnName;
    private String sellerCnAddress;
    private String sellerEnAddress;
    private String sellerPhone;
    private String sellerFax;
    private String sellerContactCnName;
    private String sellerContactEnName;
    private String sellerEmail;

    private String buyerInfoId;//买方ID
    private String buyerInfoCnName;//买方信息
    private String buyerInfoEnName;
    private String buyerInfoCnAddress;
    private String buyerInfoEnAddress;
    private String buyerInfoPhone;
    private String buyerInfoFax;
    private String buyerInfoContactCnName;
    private String buyerInfoContactEnName;
    private String buyerInfoEmail;

    private Integer currency;//结算货币
    private BigDecimal totalPriceAud;//总价格
    private BigDecimal totalPriceRmb;
    private BigDecimal totalPriceUsd;
    private Integer totalOrderQty;//总数量
    @JsonMoney
    private BigDecimal rateAudToRmb;//汇率
    @JsonMoney
    private BigDecimal rateAudToUsd;
    private BigDecimal depositAud;//定金
    private BigDecimal depositRmb;
    private BigDecimal depositUsd;
    private BigDecimal writeOffAud;//冲销金额
    private BigDecimal writeOffRmb;
    private BigDecimal writeOffUsd;

    /**实付定金、汇率*/
    @JsonMoney
    private BigDecimal paymentRateAudToRmb;
    @JsonMoney
    private BigDecimal paymentRateAudToUsd;
    private BigDecimal paymentDepositAud;
    private BigDecimal paymentDepositRmb;
    private BigDecimal paymentDepositUsd;

    private String shippingMethod;//运输方式
    private Date shippingDate;//出货日期
    private Date readyDate;
    /**发货时间*/
    private Date etd;
    /**到岸时间*/
    private Date eta;
    private String originPortId;
    private String originPortCnName;
    private String originPortEnName;
    private String destinationPortId;
    private String destinationPortCnName;
    private String destinationPortEnName;
    private String paymentTerms;//付款条款
    private String otherTerms;//其他条款

    private BigDecimal depositRate;//定金率
    private String contractFile;//合同文件
    private Integer balancePaymentTerm;
    private Integer balanceCreditTerm;
    private String tradeTerm;

    private Date createdAt;//创建时间
    private Date updatedAt;//更新时间
    private Date startTime;//申请时间
    private Date endTime;//完成时间

    private String creatorId;//创建人Id
    private String creatorCnName;
    private String creatorEnName;
    private String departmentId;//部门Id
    private String departmentCnName;
    private String departmentEnName;
    private Integer status;//状态
    private Integer flowStatus;//流程状态
    /**
     * 流程实例ID
     */
    private String processInstanceId;
    private String businessId;
    private String reviewerId;
    private String reviewerCnName;
    private String reviewerEnName;
    private String reviewerDepartmentId;
    private String reviewerDepartmentCnName;
    private String reviewerDepartmentEnName;

    private Integer flagOrderQcStatus;
    private Date flagOrderQcTime;
    private String flagOrderQcId;
    private Integer flagCustomClearanceStatus;
    private Date flagCustomClearanceTime;
    private String flagCustomClearanceId;
    private Integer flagContractDepositStatus;
    private Date flagContractDepositTime;
    private String flagContractDepositId;
    private Integer flagFeePaymentStatus;
    private Date flagFeePaymentTime;
    private String flagFeePaymentId;
    private Integer flagOrderShippingPlanStatus;
    private Date flagOrderShippingPlanTime;
    private String flagOrderShippingPlanId;
    private Integer flagOrderShippingApplyStatus;
    private Date flagOrderShippingApplyTime;
    private String flagOrderShippingApplyId;
    private Integer flagWarehousePlanStatus;
    private Date flagWarehousePlanTime;
    private String flagWarehousePlanId;
    private Integer flagAsnStatus;
    private Date flagAsnTime;
    private String flagAsnId;
    private Integer flagBalanceRefundStatus;
    private Date flagBalanceRefundTime;
    private String flagBalanceRefundId;
    /**挂起状态*/
    private Integer hold;
    private Integer isNeededQc;
    private Date retd;
    private Date reta;
    private Integer containerType;
    private BigDecimal containerQty;

    private BigDecimal electronicProcessingFeeAud;
    private BigDecimal electronicProcessingFeeRmb;
    private BigDecimal electronicProcessingFeeUsd;
    private BigDecimal balanceAud;
    private BigDecimal balanceRmb;
    private BigDecimal balanceUsd;
    private BigDecimal adjustBalanceAud;  //清关后的实际尾款
    private BigDecimal adjustBalanceRmb;
    private BigDecimal adjustBalanceUsd;
    private BigDecimal adjustValueBalanceAud;  //清关后的实际货值尾款
    private BigDecimal adjustValueBalanceRmb;
    private BigDecimal adjustValueBalanceUsd;
    private BigDecimal totalOtherAud;
    private BigDecimal totalOtherRmb;
    private BigDecimal totalOtherUsd;

    private BigDecimal totalValueAud;
    private BigDecimal totalValueRmb;
    private BigDecimal totalValueUsd;
    private BigDecimal totalOtherDepositAud;
    private BigDecimal totalOtherDepositRmb;
    private BigDecimal totalOtherDepositUsd;

    private BigDecimal cubicWeight;
    //实装体积
    private BigDecimal totalPackingCbm;
    //订单体积
    private BigDecimal totalCbm;

    private  Integer flagCostStatus;//成本计算完成标记
    private  String flagCostId;//成本id
    private Date flagCostTime;//成本计算完成时间
    private Integer flagAsnCreateStatus;//ASN创建标记
    private  String flagAsnCreateId;//ASN 的id
    private  Date flagAsnCreateTime;//ASN创建时间
    private  Integer flagCompleteStatus;//订单完成状态
    private String  flagCompleteId;//订单完成id
    private Date flagCompleteTime;//订单完成时间
    private List<PurchaseContractDetailVo> details = Lists.newArrayList();

    private List<CustomClearancePackingDetailVo> customClearancePackingDetailVos = Lists.newArrayList();

    private List<PurchaseContractOtherDetailVo> otherDetails = Lists.newArrayList();

    private BankAccountVo vendorBank;

    public BigDecimal getCubicWeight() {
        return cubicWeight;
    }

    public BigDecimal getTotalCbm() {
        return totalCbm;
    }

    public void setTotalCbm(BigDecimal totalCbm) {
        this.totalCbm = totalCbm;
    }

    public void setCubicWeight(BigDecimal cubicWeight) {
        this.cubicWeight = cubicWeight;
    }

    public BigDecimal getTotalPackingCbm() {
        return totalPackingCbm;
    }

    public void setTotalPackingCbm(BigDecimal totalPackingCbm) {
        this.totalPackingCbm = totalPackingCbm;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderTitle() {
        return orderTitle;
    }

    public void setOrderTitle(String orderTitle) {
        this.orderTitle = orderTitle;
    }

    public String getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(String orderIndex) {
        this.orderIndex = orderIndex;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
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

    public String getFactoryContractNumber() {
        return factoryContractNumber;
    }

    public void setFactoryContractNumber(String factoryContractNumber) {
        this.factoryContractNumber = factoryContractNumber;
    }

    public String getSellerCnName() {
        return sellerCnName;
    }

    public void setSellerCnName(String sellerCnName) {
        this.sellerCnName = sellerCnName;
    }

    public String getSellerEnName() {
        return sellerEnName;
    }

    public void setSellerEnName(String sellerEnName) {
        this.sellerEnName = sellerEnName;
    }

    public String getSellerCnAddress() {
        return sellerCnAddress;
    }

    public void setSellerCnAddress(String sellerCnAddress) {
        this.sellerCnAddress = sellerCnAddress;
    }

    public String getSellerEnAddress() {
        return sellerEnAddress;
    }

    public void setSellerEnAddress(String sellerEnAddress) {
        this.sellerEnAddress = sellerEnAddress;
    }

    public String getSellerPhone() {
        return sellerPhone;
    }

    public void setSellerPhone(String sellerPhone) {
        this.sellerPhone = sellerPhone;
    }

    public String getSellerFax() {
        return sellerFax;
    }

    public void setSellerFax(String sellerFax) {
        this.sellerFax = sellerFax;
    }

    public String getSellerContactCnName() {
        return sellerContactCnName;
    }

    public void setSellerContactCnName(String sellerContactCnName) {
        this.sellerContactCnName = sellerContactCnName;
    }

    public String getSellerContactEnName() {
        return sellerContactEnName;
    }

    public void setSellerContactEnName(String sellerContactEnName) {
        this.sellerContactEnName = sellerContactEnName;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
    }

    public String getBuyerInfoId() {
        return buyerInfoId;
    }

    public void setBuyerInfoId(String buyerInfoId) {
        this.buyerInfoId = buyerInfoId;
    }

    public String getBuyerInfoCnName() {
        return buyerInfoCnName;
    }

    public void setBuyerInfoCnName(String buyerInfoCnName) {
        this.buyerInfoCnName = buyerInfoCnName;
    }

    public String getBuyerInfoEnName() {
        return buyerInfoEnName;
    }

    public void setBuyerInfoEnName(String buyerInfoEnName) {
        this.buyerInfoEnName = buyerInfoEnName;
    }

    public String getBuyerInfoCnAddress() {
        return buyerInfoCnAddress;
    }

    public void setBuyerInfoCnAddress(String buyerInfoCnAddress) {
        this.buyerInfoCnAddress = buyerInfoCnAddress;
    }

    public String getBuyerInfoEnAddress() {
        return buyerInfoEnAddress;
    }

    public void setBuyerInfoEnAddress(String buyerInfoEnAddress) {
        this.buyerInfoEnAddress = buyerInfoEnAddress;
    }

    public String getBuyerInfoPhone() {
        return buyerInfoPhone;
    }

    public void setBuyerInfoPhone(String buyerInfoPhone) {
        this.buyerInfoPhone = buyerInfoPhone;
    }

    public String getBuyerInfoFax() {
        return buyerInfoFax;
    }

    public void setBuyerInfoFax(String buyerInfoFax) {
        this.buyerInfoFax = buyerInfoFax;
    }

    public String getBuyerInfoContactCnName() {
        return buyerInfoContactCnName;
    }

    public void setBuyerInfoContactCnName(String buyerInfoContactCnName) {
        this.buyerInfoContactCnName = buyerInfoContactCnName;
    }

    public String getBuyerInfoContactEnName() {
        return buyerInfoContactEnName;
    }

    public void setBuyerInfoContactEnName(String buyerInfoContactEnName) {
        this.buyerInfoContactEnName = buyerInfoContactEnName;
    }

    public String getBuyerInfoEmail() {
        return buyerInfoEmail;
    }

    public void setBuyerInfoEmail(String buyerInfoEmail) {
        this.buyerInfoEmail = buyerInfoEmail;
    }

    public Integer getCurrency() {
        return currency;
    }

    public void setCurrency(Integer currency) {
        this.currency = currency;
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

    public Integer getTotalOrderQty() {
        return totalOrderQty;
    }

    public void setTotalOrderQty(Integer totalOrderQty) {
        this.totalOrderQty = totalOrderQty;
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

    public String getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(String shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public Date getShippingDate() {
        return shippingDate;
    }

    public void setShippingDate(Date shippingDate) {
        this.shippingDate = shippingDate;
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

    public String getDestinationPortId() {
        return destinationPortId;
    }

    public void setDestinationPortId(String destinationPortId) {
        this.destinationPortId = destinationPortId;
    }

    public String getDestinationPortCnName() {
        return destinationPortCnName;
    }

    public void setDestinationPortCnName(String destinationPortCnName) {
        this.destinationPortCnName = destinationPortCnName;
    }

    public String getDestinationPortEnName() {
        return destinationPortEnName;
    }

    public void setDestinationPortEnName(String destinationPortEnName) {
        this.destinationPortEnName = destinationPortEnName;
    }

    public String getPaymentTerms() {
        return paymentTerms;
    }

    public void setPaymentTerms(String paymentTerms) {
        this.paymentTerms = paymentTerms;
    }

    public String getOtherTerms() {
        return otherTerms;
    }

    public void setOtherTerms(String otherTerms) {
        this.otherTerms = otherTerms;
    }

    public BigDecimal getDepositRate() {
        return depositRate;
    }

    public void setDepositRate(BigDecimal depositRate) {
        this.depositRate = depositRate;
    }

    public String getContractFile() {
        return contractFile;
    }

    public void setContractFile(String contractFile) {
        this.contractFile = contractFile;
    }

    public Integer getBalancePaymentTerm() {
        return balancePaymentTerm;
    }

    public void setBalancePaymentTerm(Integer balancePaymentTerm) {
        this.balancePaymentTerm = balancePaymentTerm;
    }

    public Integer getBalanceCreditTerm() {
        return balanceCreditTerm;
    }

    public void setBalanceCreditTerm(Integer balanceCreditTerm) {
        this.balanceCreditTerm = balanceCreditTerm;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorCnName() {
        return creatorCnName;
    }

    public void setCreatorCnName(String creatorCnName) {
        this.creatorCnName = creatorCnName;
    }

    public String getCreatorEnName() {
        return creatorEnName;
    }

    public void setCreatorEnName(String creatorEnName) {
        this.creatorEnName = creatorEnName;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentCnName() {
        return departmentCnName;
    }

    public void setDepartmentCnName(String departmentCnName) {
        this.departmentCnName = departmentCnName;
    }

    public String getDepartmentEnName() {
        return departmentEnName;
    }

    public void setDepartmentEnName(String departmentEnName) {
        this.departmentEnName = departmentEnName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getFlowStatus() {
        return flowStatus;
    }

    public void setFlowStatus(Integer flowStatus) {
        this.flowStatus = flowStatus;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(String reviewerId) {
        this.reviewerId = reviewerId;
    }

    public String getReviewerCnName() {
        return reviewerCnName;
    }

    public void setReviewerCnName(String reviewerCnName) {
        this.reviewerCnName = reviewerCnName;
    }

    public String getReviewerEnName() {
        return reviewerEnName;
    }

    public void setReviewerEnName(String reviewerEnName) {
        this.reviewerEnName = reviewerEnName;
    }

    public String getReviewerDepartmentId() {
        return reviewerDepartmentId;
    }

    public void setReviewerDepartmentId(String reviewerDepartmentId) {
        this.reviewerDepartmentId = reviewerDepartmentId;
    }

    public String getReviewerDepartmentCnName() {
        return reviewerDepartmentCnName;
    }

    public void setReviewerDepartmentCnName(String reviewerDepartmentCnName) {
        this.reviewerDepartmentCnName = reviewerDepartmentCnName;
    }

    public String getReviewerDepartmentEnName() {
        return reviewerDepartmentEnName;
    }

    public void setReviewerDepartmentEnName(String reviewerDepartmentEnName) {
        this.reviewerDepartmentEnName = reviewerDepartmentEnName;
    }

    public Integer getFlagOrderQcStatus() {
        return flagOrderQcStatus;
    }

    public void setFlagOrderQcStatus(Integer flagOrderQcStatus) {
        this.flagOrderQcStatus = flagOrderQcStatus;
    }

    public Date getFlagOrderQcTime() {
        return flagOrderQcTime;
    }

    public void setFlagOrderQcTime(Date flagOrderQcTime) {
        this.flagOrderQcTime = flagOrderQcTime;
    }

    public String getFlagOrderQcId() {
        return flagOrderQcId;
    }

    public void setFlagOrderQcId(String flagOrderQcId) {
        this.flagOrderQcId = flagOrderQcId;
    }

    public Integer getFlagCustomClearanceStatus() {
        return flagCustomClearanceStatus;
    }

    public void setFlagCustomClearanceStatus(Integer flagCustomClearanceStatus) {
        this.flagCustomClearanceStatus = flagCustomClearanceStatus;
    }

    public Date getFlagCustomClearanceTime() {
        return flagCustomClearanceTime;
    }

    public void setFlagCustomClearanceTime(Date flagCustomClearanceTime) {
        this.flagCustomClearanceTime = flagCustomClearanceTime;
    }

    public String getFlagCustomClearanceId() {
        return flagCustomClearanceId;
    }

    public void setFlagCustomClearanceId(String flagCustomClearanceId) {
        this.flagCustomClearanceId = flagCustomClearanceId;
    }

    public Integer getFlagContractDepositStatus() {
        return flagContractDepositStatus;
    }

    public void setFlagContractDepositStatus(Integer flagContractDepositStatus) {
        this.flagContractDepositStatus = flagContractDepositStatus;
    }

    public Date getFlagContractDepositTime() {
        return flagContractDepositTime;
    }

    public void setFlagContractDepositTime(Date flagContractDepositTime) {
        this.flagContractDepositTime = flagContractDepositTime;
    }

    public String getFlagContractDepositId() {
        return flagContractDepositId;
    }

    public void setFlagContractDepositId(String flagContractDepositId) {
        this.flagContractDepositId = flagContractDepositId;
    }

    public Integer getFlagFeePaymentStatus() {
        return flagFeePaymentStatus;
    }

    public void setFlagFeePaymentStatus(Integer flagFeePaymentStatus) {
        this.flagFeePaymentStatus = flagFeePaymentStatus;
    }

    public Date getFlagFeePaymentTime() {
        return flagFeePaymentTime;
    }

    public void setFlagFeePaymentTime(Date flagFeePaymentTime) {
        this.flagFeePaymentTime = flagFeePaymentTime;
    }

    public String getFlagFeePaymentId() {
        return flagFeePaymentId;
    }

    public void setFlagFeePaymentId(String flagFeePaymentId) {
        this.flagFeePaymentId = flagFeePaymentId;
    }

    public Integer getFlagOrderShippingPlanStatus() {
        return flagOrderShippingPlanStatus;
    }

    public void setFlagOrderShippingPlanStatus(Integer flagOrderShippingPlanStatus) {
        this.flagOrderShippingPlanStatus = flagOrderShippingPlanStatus;
    }

    public Date getFlagOrderShippingPlanTime() {
        return flagOrderShippingPlanTime;
    }

    public void setFlagOrderShippingPlanTime(Date flagOrderShippingPlanTime) {
        this.flagOrderShippingPlanTime = flagOrderShippingPlanTime;
    }

    public String getFlagOrderShippingPlanId() {
        return flagOrderShippingPlanId;
    }

    public void setFlagOrderShippingPlanId(String flagOrderShippingPlanId) {
        this.flagOrderShippingPlanId = flagOrderShippingPlanId;
    }

    public Integer getFlagOrderShippingApplyStatus() {
        return flagOrderShippingApplyStatus;
    }

    public void setFlagOrderShippingApplyStatus(Integer flagOrderShippingApplyStatus) {
        this.flagOrderShippingApplyStatus = flagOrderShippingApplyStatus;
    }

    public Date getFlagOrderShippingApplyTime() {
        return flagOrderShippingApplyTime;
    }

    public void setFlagOrderShippingApplyTime(Date flagOrderShippingApplyTime) {
        this.flagOrderShippingApplyTime = flagOrderShippingApplyTime;
    }

    public String getFlagOrderShippingApplyId() {
        return flagOrderShippingApplyId;
    }

    public void setFlagOrderShippingApplyId(String flagOrderShippingApplyId) {
        this.flagOrderShippingApplyId = flagOrderShippingApplyId;
    }

    public Integer getFlagWarehousePlanStatus() {
        return flagWarehousePlanStatus;
    }

    public void setFlagWarehousePlanStatus(Integer flagWarehousePlanStatus) {
        this.flagWarehousePlanStatus = flagWarehousePlanStatus;
    }

    public Date getFlagWarehousePlanTime() {
        return flagWarehousePlanTime;
    }

    public void setFlagWarehousePlanTime(Date flagWarehousePlanTime) {
        this.flagWarehousePlanTime = flagWarehousePlanTime;
    }

    public String getFlagWarehousePlanId() {
        return flagWarehousePlanId;
    }

    public void setFlagWarehousePlanId(String flagWarehousePlanId) {
        this.flagWarehousePlanId = flagWarehousePlanId;
    }

    public Integer getFlagAsnStatus() {
        return flagAsnStatus;
    }

    public void setFlagAsnStatus(Integer flagAsnStatus) {
        this.flagAsnStatus = flagAsnStatus;
    }

    public Date getFlagAsnTime() {
        return flagAsnTime;
    }

    public void setFlagAsnTime(Date flagAsnTime) {
        this.flagAsnTime = flagAsnTime;
    }

    public String getFlagAsnId() {
        return flagAsnId;
    }

    public void setFlagAsnId(String flagAsnId) {
        this.flagAsnId = flagAsnId;
    }

    public Integer getFlagBalanceRefundStatus() {
        return flagBalanceRefundStatus;
    }

    public void setFlagBalanceRefundStatus(Integer flagBalanceRefundStatus) {
        this.flagBalanceRefundStatus = flagBalanceRefundStatus;
    }

    public Date getFlagBalanceRefundTime() {
        return flagBalanceRefundTime;
    }

    public void setFlagBalanceRefundTime(Date flagBalanceRefundTime) {
        this.flagBalanceRefundTime = flagBalanceRefundTime;
    }

    public String getFlagBalanceRefundId() {
        return flagBalanceRefundId;
    }

    public void setFlagBalanceRefundId(String flagBalanceRefundId) {
        this.flagBalanceRefundId = flagBalanceRefundId;
    }

    public List<PurchaseContractDetailVo> getDetails() {
        return details;
    }

    public void setDetails(List<PurchaseContractDetailVo> details) {
        this.details = details;
    }

    public List<CustomClearancePackingDetailVo> getCustomClearancePackingDetailVos() {
        return customClearancePackingDetailVos;
    }

    public void setCustomClearancePackingDetailVos(List<CustomClearancePackingDetailVo> customClearancePackingDetailVos) {
        this.customClearancePackingDetailVos = customClearancePackingDetailVos;
    }

    public Integer getHold() {
        return hold;
    }

    public void setHold(Integer hold) {
        this.hold = hold;
    }

    public BigDecimal getPaymentRateAudToRmb() {
        return paymentRateAudToRmb;
    }

    public void setPaymentRateAudToRmb(BigDecimal paymentRateAudToRmb) {
        this.paymentRateAudToRmb = paymentRateAudToRmb;
    }

    public BigDecimal getPaymentRateAudToUsd() {
        return paymentRateAudToUsd;
    }

    public void setPaymentRateAudToUsd(BigDecimal paymentRateAudToUsd) {
        this.paymentRateAudToUsd = paymentRateAudToUsd;
    }

    public BigDecimal getPaymentDepositAud() {
        return paymentDepositAud;
    }

    public void setPaymentDepositAud(BigDecimal paymentDepositAud) {
        this.paymentDepositAud = paymentDepositAud;
    }

    public BigDecimal getPaymentDepositRmb() {
        return paymentDepositRmb;
    }

    public void setPaymentDepositRmb(BigDecimal paymentDepositRmb) {
        this.paymentDepositRmb = paymentDepositRmb;
    }

    public BigDecimal getPaymentDepositUsd() {
        return paymentDepositUsd;
    }

    public void setPaymentDepositUsd(BigDecimal paymentDepositUsd) {
        this.paymentDepositUsd = paymentDepositUsd;
    }

    public Integer getIsNeededQc() {
        return isNeededQc;
    }

    public void setIsNeededQc(Integer isNeededQc) {
        this.isNeededQc = isNeededQc;
    }

    public Date getRetd() {
        return retd;
    }

    public void setRetd(Date retd) {
        this.retd = retd;
    }

    public Date getReta() {
        return reta;
    }

    public void setReta(Date reta) {
        this.reta = reta;
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

    public BigDecimal getElectronicProcessingFeeAud() {
        return electronicProcessingFeeAud;
    }

    public void setElectronicProcessingFeeAud(BigDecimal electronicProcessingFeeAud) {
        this.electronicProcessingFeeAud = electronicProcessingFeeAud;
    }

    public BigDecimal getElectronicProcessingFeeRmb() {
        return electronicProcessingFeeRmb;
    }

    public void setElectronicProcessingFeeRmb(BigDecimal electronicProcessingFeeRmb) {
        this.electronicProcessingFeeRmb = electronicProcessingFeeRmb;
    }

    public BigDecimal getElectronicProcessingFeeUsd() {
        return electronicProcessingFeeUsd;
    }

    public void setElectronicProcessingFeeUsd(BigDecimal electronicProcessingFeeUsd) {
        this.electronicProcessingFeeUsd = electronicProcessingFeeUsd;
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

    public BigDecimal getAdjustBalanceAud() {
        return adjustBalanceAud;
    }

    public void setAdjustBalanceAud(BigDecimal adjustBalanceAud) {
        this.adjustBalanceAud = adjustBalanceAud;
    }

    public BigDecimal getAdjustBalanceRmb() {
        return adjustBalanceRmb;
    }

    public void setAdjustBalanceRmb(BigDecimal adjustBalanceRmb) {
        this.adjustBalanceRmb = adjustBalanceRmb;
    }

    public BigDecimal getAdjustBalanceUsd() {
        return adjustBalanceUsd;
    }

    public void setAdjustBalanceUsd(BigDecimal adjustBalanceUsd) {
        this.adjustBalanceUsd = adjustBalanceUsd;
    }

    public BigDecimal getAdjustValueBalanceAud() {
        return adjustValueBalanceAud;
    }

    public void setAdjustValueBalanceAud(BigDecimal adjustValueBalanceAud) {
        this.adjustValueBalanceAud = adjustValueBalanceAud;
    }

    public BigDecimal getAdjustValueBalanceRmb() {
        return adjustValueBalanceRmb;
    }

    public void setAdjustValueBalanceRmb(BigDecimal adjustValueBalanceRmb) {
        this.adjustValueBalanceRmb = adjustValueBalanceRmb;
    }

    public BigDecimal getAdjustValueBalanceUsd() {
        return adjustValueBalanceUsd;
    }

    public void setAdjustValueBalanceUsd(BigDecimal adjustValueBalanceUsd) {
        this.adjustValueBalanceUsd = adjustValueBalanceUsd;
    }

    public BankAccountVo getVendorBank() {
        return vendorBank;
    }

    public void setVendorBank(BankAccountVo vendorBank) {
        this.vendorBank = vendorBank;
    }

    public String getTradeTerm() {
        return tradeTerm;
    }

    public void setTradeTerm(String tradeTerm) {
        this.tradeTerm = tradeTerm;
    }

    public BigDecimal getTotalOtherAud() {
        return totalOtherAud;
    }

    public void setTotalOtherAud(BigDecimal totalOtherAud) {
        this.totalOtherAud = totalOtherAud;
    }

    public BigDecimal getTotalOtherRmb() {
        return totalOtherRmb;
    }

    public void setTotalOtherRmb(BigDecimal totalOtherRmb) {
        this.totalOtherRmb = totalOtherRmb;
    }

    public BigDecimal getTotalOtherUsd() {
        return totalOtherUsd;
    }

    public void setTotalOtherUsd(BigDecimal totalOtherUsd) {
        this.totalOtherUsd = totalOtherUsd;
    }

    public BigDecimal getTotalValueAud() {
        return totalValueAud;
    }

    public void setTotalValueAud(BigDecimal totalValueAud) {
        this.totalValueAud = totalValueAud;
    }

    public BigDecimal getTotalValueRmb() {
        return totalValueRmb;
    }

    public void setTotalValueRmb(BigDecimal totalValueRmb) {
        this.totalValueRmb = totalValueRmb;
    }

    public BigDecimal getTotalValueUsd() {
        return totalValueUsd;
    }

    public void setTotalValueUsd(BigDecimal totalValueUsd) {
        this.totalValueUsd = totalValueUsd;
    }

    public List<PurchaseContractOtherDetailVo> getOtherDetails() {
        return otherDetails;
    }

    public void setOtherDetails(List<PurchaseContractOtherDetailVo> otherDetails) {
        this.otherDetails = otherDetails;
    }

    public BigDecimal getTotalOtherDepositAud() {
        return totalOtherDepositAud;
    }

    public void setTotalOtherDepositAud(BigDecimal totalOtherDepositAud) {
        this.totalOtherDepositAud = totalOtherDepositAud;
    }

    public BigDecimal getTotalOtherDepositRmb() {
        return totalOtherDepositRmb;
    }

    public void setTotalOtherDepositRmb(BigDecimal totalOtherDepositRmb) {
        this.totalOtherDepositRmb = totalOtherDepositRmb;
    }

    public BigDecimal getTotalOtherDepositUsd() {
        return totalOtherDepositUsd;
    }

    public void setTotalOtherDepositUsd(BigDecimal totalOtherDepositUsd) {
        this.totalOtherDepositUsd = totalOtherDepositUsd;
    }

    public Integer getFlagCostStatus() {
        return flagCostStatus;
    }

    public void setFlagCostStatus(Integer flagCostStatus) {
        this.flagCostStatus = flagCostStatus;
    }

    public String getFlagCostId() {
        return flagCostId;
    }

    public void setFlagCostId(String flagCostId) {
        this.flagCostId = flagCostId;
    }

    public Date getFlagCostTime() {
        return flagCostTime;
    }

    public void setFlagCostTime(Date flagCostTime) {
        this.flagCostTime = flagCostTime;
    }

    public Integer getFlagAsnCreateStatus() {
        return flagAsnCreateStatus;
    }

    public void setFlagAsnCreateStatus(Integer flagAsnCreateStatus) {
        this.flagAsnCreateStatus = flagAsnCreateStatus;
    }

    public String getFlagAsnCreateId() {
        return flagAsnCreateId;
    }

    public void setFlagAsnCreateId(String flagAsnCreateId) {
        this.flagAsnCreateId = flagAsnCreateId;
    }

    public Date getFlagAsnCreateTime() {
        return flagAsnCreateTime;
    }

    public void setFlagAsnCreateTime(Date flagAsnCreateTime) {
        this.flagAsnCreateTime = flagAsnCreateTime;
    }

    public Integer getFlagCompleteStatus() {
        return flagCompleteStatus;
    }

    public void setFlagCompleteStatus(Integer flagCompleteStatus) {
        this.flagCompleteStatus = flagCompleteStatus;
    }

    public String getFlagCompleteId() {
        return flagCompleteId;
    }

    public void setFlagCompleteId(String flagCompleteId) {
        this.flagCompleteId = flagCompleteId;
    }

    public Date getFlagCompleteTime() {
        return flagCompleteTime;
    }

    public void setFlagCompleteTime(Date flagCompleteTime) {
        this.flagCompleteTime = flagCompleteTime;
    }
}
