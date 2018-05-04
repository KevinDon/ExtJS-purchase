package com.newaim.purchase.flow.purchase.entity;


import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.flow.workflow.entity.FlowVendorWithCurrencyObject;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 采购合同实体类
 */
@Entity
@Table(name = "na_flow_purchase_contract")
public class FlowPurchaseContract implements FlowVendorWithCurrencyObject, Serializable {

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.newaim.core.jpa.IdGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name = "prefix", value = "FPC")})
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

    /**卖方信息*/
    private String sellerCnName;
    private String sellerEnName;
    private String sellerCnAddress;
    private String sellerEnAddress;
    private String sellerPhone;
    private String sellerFax;
    private String sellerContactCnName;
    private String sellerContactEnName;
    private String sellerEmail;

    private String sellerContactId;

    /**买方信息*/
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

    /**总价格*/
    private BigDecimal totalPriceAud;
    private BigDecimal totalPriceRmb;
    private BigDecimal totalPriceUsd;

    /**总数量*/
    private Integer totalOrderQty;
    /**总体积*/
    private BigDecimal totalCbm;
    /**汇率*/
    private BigDecimal rateAudToRmb;
    private BigDecimal rateAudToUsd;

    /**定金*/
    private BigDecimal depositAud;
    private BigDecimal depositRmb;
    private BigDecimal depositUsd;

    /**冲销金额*/
    private BigDecimal writeOffAud;
    private BigDecimal writeOffRmb;
    private BigDecimal writeOffUsd;

    private String shippingMethod;//运输方式
    private Date readyDate;//完货时间
    private Date shippingDate;//出货日期
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
    private Integer depositType;
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

    /**流程实例ID*/
    private String processInstanceId;

    /**确认人信息*/
    private String reviewerId;
    private String reviewerCnName;
    private String reviewerEnName;
    private String reviewerDepartmentId;
    private String reviewerDepartmentCnName;
    private String reviewerDepartmentEnName;

    /**当前处理人*/
    private String assigneeId;
    private String assigneeCnName;
    private String assigneeEnName;
    /**挂起状态*/
    private Integer hold;

    /**实付定金、汇率*/
    private BigDecimal paymentRateAudToRmb;
    private BigDecimal paymentRateAudToUsd;
    private BigDecimal paymentDepositAud;
    private BigDecimal paymentDepositRmb;
    private BigDecimal paymentDepositUsd;

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

    private BigDecimal totalOtherAud;   //其它总费用
    private BigDecimal totalOtherRmb;
    private BigDecimal totalOtherUsd;

    private BigDecimal totalValueAud;  //订单总货值
    private BigDecimal totalValueRmb;
    private BigDecimal totalValueUsd;
    private BigDecimal totalOtherDepositAud;  //其它总费用订金
    private BigDecimal totalOtherDepositRmb;
    private BigDecimal totalOtherDepositUsd;


    private BigDecimal cubicWeight;  //订单总体积重量
    private BigDecimal totalPackingCbm;

    private  Integer flagCostStatus;//成本计算完成标记
    private  String flagCostId;//成本id
    private Date flagCostTime;//成本计算完成时间
    private Integer flagAsnCreateStatus;//ASN创建标记
    private  String flagAsnCreateId;//ASN 的id
    private  Date flagAsnCreateTime;//ASN创建时间
    private  Integer flagCompleteStatus;//订单完成状态
    private String  flagCompleteId;//订单完成id
    private Date flagCompleteTime;//订单完成时间
    /**审核*/
    @Transient
    private int approved;

    @Override
    public String getId() {
        return id;
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

    public BigDecimal getCubicWeight() {
        return cubicWeight;
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

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public Date getReadyDate() {
        return readyDate;
    }

    public void setReadyDate(Date readyDate) {
        this.readyDate = readyDate;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public int getApproved() {
        return approved;
    }

    public void setApproved(int approved) {
        this.approved = approved;
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

    public String getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(String orderIndex) {
        this.orderIndex = orderIndex;
    }

    @Override
    public String getVendorId() {
        return vendorId;
    }

    @Override
    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    @Override
    public String getVendorCnName() {
        return vendorCnName;
    }

    @Override
    public void setVendorCnName(String vendorCnName) {
        this.vendorCnName = vendorCnName;
    }

    @Override
    public String getVendorEnName() {
        return vendorEnName;
    }

    @Override
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

    public String getSellerContactId() {
        return sellerContactId;
    }

    public void setSellerContactId(String sellerContactId) {
        this.sellerContactId = sellerContactId;
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

    @Override
    public Integer getCurrency() {
        return currency;
    }

    @Override
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

    public Integer getTotalOrderQty() {
        return totalOrderQty;
    }

    public void setTotalOrderQty(Integer totalOrderQty) {
        this.totalOrderQty = totalOrderQty;
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

    public Integer getDepositType() {
        return depositType;
    }

    public void setDepositType(Integer depositType) {
        this.depositType = depositType;
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

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public Date getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public Date getStartTime() {
        return startTime;
    }

    @Override
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @Override
    public Date getEndTime() {
        return endTime;
    }

    @Override
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    public String getCreatorId() {
        return creatorId;
    }

    @Override
    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    @Override
    public String getCreatorCnName() {
        return creatorCnName;
    }

    @Override
    public void setCreatorCnName(String creatorCnName) {
        this.creatorCnName = creatorCnName;
    }

    @Override
    public String getCreatorEnName() {
        return creatorEnName;
    }

    @Override
    public void setCreatorEnName(String creatorEnName) {
        this.creatorEnName = creatorEnName;
    }

    @Override
    public String getDepartmentId() {
        return departmentId;
    }

    @Override
    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public String getDepartmentCnName() {
        return departmentCnName;
    }

    @Override
    public void setDepartmentCnName(String departmentCnName) {
        this.departmentCnName = departmentCnName;
    }

    @Override
    public String getDepartmentEnName() {
        return departmentEnName;
    }

    @Override
    public void setDepartmentEnName(String departmentEnName) {
        this.departmentEnName = departmentEnName;
    }

    @Override
    public Integer getStatus() {
        return status;
    }

    @Override
    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public Integer getFlowStatus() {
        return flowStatus;
    }

    @Override
    public void setFlowStatus(Integer flowStatus) {
        this.flowStatus = flowStatus;
    }

    @Override
    public String getProcessInstanceId() {
        return processInstanceId;
    }

    @Override
    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    @Override
    public String getReviewerId() {
        return reviewerId;
    }

    @Override
    public void setReviewerId(String reviewerId) {
        this.reviewerId = reviewerId;
    }

    @Override
    public String getReviewerCnName() {
        return reviewerCnName;
    }

    @Override
    public void setReviewerCnName(String reviewerCnName) {
        this.reviewerCnName = reviewerCnName;
    }

    @Override
    public String getReviewerEnName() {
        return reviewerEnName;
    }

    @Override
    public void setReviewerEnName(String reviewerEnName) {
        this.reviewerEnName = reviewerEnName;
    }

    @Override
    public String getReviewerDepartmentId() {
        return reviewerDepartmentId;
    }

    @Override
    public void setReviewerDepartmentId(String reviewerDepartmentId) {
        this.reviewerDepartmentId = reviewerDepartmentId;
    }

    @Override
    public String getReviewerDepartmentCnName() {
        return reviewerDepartmentCnName;
    }

    @Override
    public void setReviewerDepartmentCnName(String reviewerDepartmentCnName) {
        this.reviewerDepartmentCnName = reviewerDepartmentCnName;
    }

    @Override
    public String getReviewerDepartmentEnName() {
        return reviewerDepartmentEnName;
    }

    @Override
    public void setReviewerDepartmentEnName(String reviewerDepartmentEnName) {
        this.reviewerDepartmentEnName = reviewerDepartmentEnName;
    }

    @Override
    public String getAssigneeId() {
        return assigneeId;
    }

    @Override
    public void setAssigneeId(String assigneeId) {
        this.assigneeId = assigneeId;
    }

    @Override
    public String getAssigneeCnName() {
        return assigneeCnName;
    }

    @Override
    public void setAssigneeCnName(String assigneeCnName) {
        this.assigneeCnName = assigneeCnName;
    }

    @Override
    public String getAssigneeEnName() {
        return assigneeEnName;
    }

    @Override
    public void setAssigneeEnName(String assigneeEnName) {
        this.assigneeEnName = assigneeEnName;
    }

    @Override
    public Integer getHold() {
        return hold;
    }

    @Override
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

    public String getTradeTerm() {
        return tradeTerm;
    }

    public void setTradeTerm(String tradeTerm) {
        this.tradeTerm = tradeTerm;
    }

    @Transient
    public String getVendorName() {
        if(SessionUtils.isCnLang()){
            return  this.getVendorCnName();
        }else if(SessionUtils.isEnLang()){
            return this.getVendorEnName();
        }
        return null;
    }

    @Transient
    public String getCreatorName() {
        if(SessionUtils.isCnLang()){
            return  this.getCreatorCnName();
        }else if(SessionUtils.isEnLang()){
            return this.getCreatorEnName();
        }
        return null;
    }

    @Transient
    public String getDepartmentName() {
        if(SessionUtils.isCnLang()){
            return  this.getDepartmentCnName();
        }else if(SessionUtils.isEnLang()){
            return this.getDepartmentEnName();
        }
        return null;
    }

    @Transient
    public String getBuyerInfoName() {
        if(SessionUtils.isCnLang()){
            return  this.getBuyerInfoCnName();
        }else if(SessionUtils.isEnLang()){
            return this.getBuyerInfoEnName();
        }
        return null;
    }

    @Transient
    public String getBuyerInfoAddress() {
        if(SessionUtils.isCnLang()){
            return  this.getBuyerInfoCnAddress();
        }else if(SessionUtils.isEnLang()){
            return this.getBuyerInfoEnAddress();
        }
        return null;
    }

    @Transient
    public String getBuyerInfoContactName() {
        if(SessionUtils.isCnLang()){
            return  this.getBuyerInfoContactCnName();
        }else if(SessionUtils.isEnLang()){
            return this.getBuyerInfoContactEnName();
        }
        return null;
    }

    @Transient
    public String getSellerName() {
        if(SessionUtils.isCnLang()){
            return  this.getSellerCnName();
        }else if(SessionUtils.isEnLang()){
            return this.getSellerEnName();
        }
        return null;
    }

    @Transient
    public String getSellerAddress() {
        if(SessionUtils.isCnLang()){
            return  this.getSellerCnAddress();
        }else if(SessionUtils.isEnLang()){
            return this.getSellerEnAddress();
        }
        return null;
    }

    @Transient
    public String getSellerContactName() {
        if(SessionUtils.isCnLang()){
            return  this.getSellerContactCnName();
        }else if(SessionUtils.isEnLang()){
            return this.getSellerContactEnName();
        }
        return null;
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

    public BigDecimal getTotalCbm() {
        return totalCbm;
    }

    public void setTotalCbm(BigDecimal totalCbm) {
        this.totalCbm = totalCbm;
    }
}
