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


@Entity
@Table(name = "na_flow_purchase_plan")
public class FlowPurchasePlan implements FlowVendorWithCurrencyObject, Serializable {

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.newaim.core.jpa.IdGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name = "prefix", value = "FPP")})
    private String id;
    private String vendorId;
    private String vendorCnName;
    private String vendorEnName;

    private Integer currency;//结算货币

    /**总价、汇率*/
    private BigDecimal totalPriceAud;
    private BigDecimal totalPriceRmb;
    private BigDecimal totalPriceUsd;
    private BigDecimal rateAudToRmb;
    private BigDecimal rateAudToUsd;

    private Integer totalOrderQty;//总采购数量
    private Integer totalOrderQtyCarton;//总箱数
    private BigDecimal totalCbm;//总体积
    private BigDecimal depositRate;//定金率
    private Integer leadTime;//生产周期
    private String terapeakResearch;
    private String productReport;//生产报告

    /**创建、更新、申请、完成时间*/
    private Date createdAt;
    private Date updatedAt;
    private Date startTime;
    private Date endTime;

    /**创建人信息*/
    private String creatorId;
    private String creatorCnName;
    private String creatorEnName;

    /**创建部门信息*/
    private String departmentId;
    private String departmentCnName;
    private String departmentEnName;

    private Integer status;
    private Integer flowStatus;//流程状态

    /**流程实例ID*/
    private String processInstanceId;

    /**确认人、部门信息*/
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
    @Transient
    private Integer hold;
    private String tradeTerm;
    /**审核*/
    @Transient
    private int approved;

    public int getApproved() {
        return approved;
    }

    public void setApproved(int approved) {
        this.approved = approved;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
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

    public BigDecimal getTotalCbm() {
        return totalCbm;
    }

    public void setTotalCbm(BigDecimal totalCbm) {
        this.totalCbm = totalCbm;
    }

    public BigDecimal getDepositRate() {
        return depositRate;
    }

    public void setDepositRate(BigDecimal depositRate) {
        this.depositRate = depositRate;
    }

    public Integer getLeadTime() {
        return leadTime;
    }

    public void setLeadTime(Integer leadTime) {
        this.leadTime = leadTime;
    }

    public String getTerapeakResearch() {
        return terapeakResearch;
    }

    public void setTerapeakResearch(String terapeakResearch) {
        this.terapeakResearch = terapeakResearch;
    }

    public String getProductReport() {
        return productReport;
    }

    public void setProductReport(String productReport) {
        this.productReport = productReport;
    }

    public Integer getTotalOrderQty() {
        return totalOrderQty;
    }

    public void setTotalOrderQty(Integer totalOrderQty) {
        this.totalOrderQty = totalOrderQty;
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

    public Integer getTotalOrderQtyCarton() {
        return totalOrderQtyCarton;
    }

    public void setTotalOrderQtyCarton(Integer totalOrderQtyCarton) {
        this.totalOrderQtyCarton = totalOrderQtyCarton;
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
            return  this.vendorCnName;
        }else if(SessionUtils.isEnLang()){
            return this.vendorEnName;
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
}
