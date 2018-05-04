package com.newaim.purchase.archives.flow.purchase.entity;

import com.newaim.purchase.flow.workflow.entity.BusinessObject;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "na_new_product")
public class NewProduct implements BusinessObject, Serializable{

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.newaim.core.jpa.IdGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name = "prefix", value = "NP")})
    private String id;

    private String vendorId;
    private String vendorCnName;
    private String vendorEnName;
    private String productId;
    private Integer currency;
    private BigDecimal priceAud;
    private BigDecimal priceRmb;
    private BigDecimal priceUsd;
    private BigDecimal rateAudToRmb;
    private BigDecimal rateAudToUsd;
    private Integer orderQty;
    private BigDecimal orderValueAud;
    private BigDecimal orderValueRmb;
    private BigDecimal orderValueUsd;
    private BigDecimal productPredictProfitAud;
    private BigDecimal productPredictProfitRmb;
    private BigDecimal productPredictProfitUsd;
    private BigDecimal competitorPriceAud;
    private BigDecimal competitorPriceRmb;
    private BigDecimal competitorPriceUsd;
    private Integer competitorSaleRecord;
    private BigDecimal ebayMonthlySalesAud;
    private BigDecimal ebayMonthlySalesRmb;
    private BigDecimal ebayMonthlySalesUsd;

    private Date startTime;
    private Date endTime;

    private String creatorId;
    private String creatorCnName;
    private String creatorEnName;

    private String departmentId;
    private String departmentCnName;
    private String departmentEnName;

    private Date createdAt;
    private Date updatedAt;
    private Integer status;
    private Integer flowStatus;
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
    /**挂起状态*/
    private Integer hold;

    private String productQuotationId;
    private String productQuotationBusinessId;
    private String productQuotationDetailBusinessId;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getCurrency() {
        return currency;
    }

    public void setCurrency(Integer currency) {
        this.currency = currency;
    }

    public BigDecimal getPriceAud() {
        return priceAud;
    }

    public void setPriceAud(BigDecimal priceAud) {
        this.priceAud = priceAud;
    }

    public BigDecimal getPriceRmb() {
        return priceRmb;
    }

    public void setPriceRmb(BigDecimal priceRmb) {
        this.priceRmb = priceRmb;
    }

    public BigDecimal getPriceUsd() {
        return priceUsd;
    }

    public void setPriceUsd(BigDecimal priceUsd) {
        this.priceUsd = priceUsd;
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

    public Integer getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(Integer orderQty) {
        this.orderQty = orderQty;
    }

    public BigDecimal getOrderValueAud() {
        return orderValueAud;
    }

    public void setOrderValueAud(BigDecimal orderValueAud) {
        this.orderValueAud = orderValueAud;
    }

    public BigDecimal getOrderValueRmb() {
        return orderValueRmb;
    }

    public void setOrderValueRmb(BigDecimal orderValueRmb) {
        this.orderValueRmb = orderValueRmb;
    }

    public BigDecimal getOrderValueUsd() {
        return orderValueUsd;
    }

    public void setOrderValueUsd(BigDecimal orderValueUsd) {
        this.orderValueUsd = orderValueUsd;
    }

    public BigDecimal getProductPredictProfitAud() {
        return productPredictProfitAud;
    }

    public void setProductPredictProfitAud(BigDecimal productPredictProfitAud) {
        this.productPredictProfitAud = productPredictProfitAud;
    }

    public BigDecimal getProductPredictProfitRmb() {
        return productPredictProfitRmb;
    }

    public void setProductPredictProfitRmb(BigDecimal productPredictProfitRmb) {
        this.productPredictProfitRmb = productPredictProfitRmb;
    }

    public BigDecimal getProductPredictProfitUsd() {
        return productPredictProfitUsd;
    }

    public void setProductPredictProfitUsd(BigDecimal productPredictProfitUsd) {
        this.productPredictProfitUsd = productPredictProfitUsd;
    }

    public BigDecimal getCompetitorPriceAud() {
        return competitorPriceAud;
    }

    public void setCompetitorPriceAud(BigDecimal competitorPriceAud) {
        this.competitorPriceAud = competitorPriceAud;
    }

    public BigDecimal getCompetitorPriceRmb() {
        return competitorPriceRmb;
    }

    public void setCompetitorPriceRmb(BigDecimal competitorPriceRmb) {
        this.competitorPriceRmb = competitorPriceRmb;
    }

    public BigDecimal getCompetitorPriceUsd() {
        return competitorPriceUsd;
    }

    public void setCompetitorPriceUsd(BigDecimal competitorPriceUsd) {
        this.competitorPriceUsd = competitorPriceUsd;
    }

    public Integer getCompetitorSaleRecord() {
        return competitorSaleRecord;
    }

    public void setCompetitorSaleRecord(Integer competitorSaleRecord) {
        this.competitorSaleRecord = competitorSaleRecord;
    }

    public BigDecimal getEbayMonthlySalesAud() {
        return ebayMonthlySalesAud;
    }

    public void setEbayMonthlySalesAud(BigDecimal ebayMonthlySalesAud) {
        this.ebayMonthlySalesAud = ebayMonthlySalesAud;
    }

    public BigDecimal getEbayMonthlySalesRmb() {
        return ebayMonthlySalesRmb;
    }

    public void setEbayMonthlySalesRmb(BigDecimal ebayMonthlySalesRmb) {
        this.ebayMonthlySalesRmb = ebayMonthlySalesRmb;
    }

    public BigDecimal getEbayMonthlySalesUsd() {
        return ebayMonthlySalesUsd;
    }

    public void setEbayMonthlySalesUsd(BigDecimal ebayMonthlySalesUsd) {
        this.ebayMonthlySalesUsd = ebayMonthlySalesUsd;
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
    public String getBusinessId() {
        return businessId;
    }

    @Override
    public void setBusinessId(String businessId) {
        this.businessId = businessId;
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
    public Integer getHold() {
        return hold;
    }

    @Override
    public void setHold(Integer hold) {
        this.hold = hold;
    }

    public String getProductQuotationId() {
        return productQuotationId;
    }

    public void setProductQuotationId(String productQuotationId) {
        this.productQuotationId = productQuotationId;
    }

    public String getProductQuotationBusinessId() {
        return productQuotationBusinessId;
    }

    public void setProductQuotationBusinessId(String productQuotationBusinessId) {
        this.productQuotationBusinessId = productQuotationBusinessId;
    }

    public String getProductQuotationDetailBusinessId() {
        return productQuotationDetailBusinessId;
    }

    public void setProductQuotationDetailBusinessId(String productQuotationDetailBusinessId) {
        this.productQuotationDetailBusinessId = productQuotationDetailBusinessId;
    }
}
