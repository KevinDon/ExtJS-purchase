package com.newaim.purchase.archives.flow.purchase.entity;

import com.newaim.purchase.flow.workflow.entity.BusinessObject;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "na_custom_clearance")
public class CustomClearance implements BusinessObject, Serializable {

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.newaim.core.jpa.IdGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name = "prefix", value = "CC")})
    private String id;

    /**订单信息*/
    private String orderShippingApplyId;
    private String orderId;
    private String orderNumber;
    private String orderTitle;

    /**服务商信息*/
    private String serviceProviderId;
    private String serviceProviderCnName;
    private String serviceProviderEnName;
    /**有配件*/
    private Integer accessories;
    /**预计发货、到岸、完货时间*/
    private Date etd;
    private Date eta;
    private Date readyDate;

    /**全新声明*/
    private String newUsedDeclaration;
    /**采购发票*/
    private String commercialInvoice;
    /**装箱声明*/
    private String packingDeclaration;
    /**船公司*/
    private String vessel;
    /**船次*/
    private String voy;
    /**贸易条款*/
    private String tradeTerm;
    /**集装箱编号*/
    private String containerNumber;
    /**封印编号*/
    private String sealsNumber;
    /**柜号*/
    private String containerOrder;
    /**柜数量*/
    private Integer containerQty;
    /**对方订单编号*/
    private String ciNumber;
    private Date packingDate;
    /**备注*/
    private String remark;

    /**创建、更新、开始、结束时间*/
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

    /**状态、流程状态、流程实例ID*/
    private Integer status;
    private Integer flowStatus;
    private String processInstanceId;
    private String businessId;

    /**确认人信息*/
    private String reviewerId;
    private String reviewerCnName;
    private String reviewerEnName;

    /**确认人部门信息*/
    private String reviewerDepartmentId;
    private String reviewerDepartmentCnName;
    private String reviewerDepartmentEnName;

    /**挂起状态*/
    private Integer hold;

    /*相关图片*/
    private String photos;
    @Transient
    private String images;

    /**审核*/
    @Transient
    private int approved;

    //实收体积
    private BigDecimal totalPackingCbm;

    //船运体积
    private BigDecimal totalShippingCbm;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getOrderShippingApplyId() {
        return orderShippingApplyId;
    }

    public void setOrderShippingApplyId(String orderShippingApplyId) {
        this.orderShippingApplyId = orderShippingApplyId;
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

    public Integer getAccessories() {
        return accessories;
    }

    public void setAccessories(Integer accessories) {
        this.accessories = accessories;
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

    public Date getReadyDate() {
        return readyDate;
    }

    public void setReadyDate(Date readyDate) {
        this.readyDate = readyDate;
    }

    public String getNewUsedDeclaration() {
        return newUsedDeclaration;
    }

    public void setNewUsedDeclaration(String newUsedDeclaration) {
        this.newUsedDeclaration = newUsedDeclaration;
    }

    public String getCommercialInvoice() {
        return commercialInvoice;
    }

    public void setCommercialInvoice(String commercialInvoice) {
        this.commercialInvoice = commercialInvoice;
    }

    public String getPackingDeclaration() {
        return packingDeclaration;
    }

    public void setPackingDeclaration(String packingDeclaration) {
        this.packingDeclaration = packingDeclaration;
    }

    public String getVessel() {
        return vessel;
    }

    public void setVessel(String vessel) {
        this.vessel = vessel;
    }

    public String getVoy() {
        return voy;
    }

    public void setVoy(String voy) {
        this.voy = voy;
    }

    public String getTradeTerm() {
        return tradeTerm;
    }

    public void setTradeTerm(String tradeTerm) {
        this.tradeTerm = tradeTerm;
    }

    public String getContainerNumber() {
        return containerNumber;
    }

    public void setContainerNumber(String containerNumber) {
        this.containerNumber = containerNumber;
    }

    public String getSealsNumber() {
        return sealsNumber;
    }

    public void setSealsNumber(String sealsNumber) {
        this.sealsNumber = sealsNumber;
    }

    public String getContainerOrder() {
        return containerOrder;
    }

    public void setContainerOrder(String containerOrder) {
        this.containerOrder = containerOrder;
    }

    public Integer getContainerQty() {
        return containerQty;
    }

    public void setContainerQty(Integer containerQty) {
        this.containerQty = containerQty;
    }

    public String getCiNumber() {
        return ciNumber;
    }

    public void setCiNumber(String ciNumber) {
        this.ciNumber = ciNumber;
    }

    public Date getPackingDate() {
        return packingDate;
    }

    public void setPackingDate(Date packingDate) {
        this.packingDate = packingDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public int getApproved() {
        return approved;
    }

    public void setApproved(int approved) {
        this.approved = approved;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }


    public BigDecimal getTotalPackingCbm() {
        return totalPackingCbm;
    }

    public void setTotalPackingCbm(BigDecimal totalPackingCbm) {
        this.totalPackingCbm = totalPackingCbm;
    }

    public BigDecimal getTotalShippingCbm() {
        return totalShippingCbm;
    }

    public void setTotalShippingCbm(BigDecimal totalShippingCbm) {
        this.totalShippingCbm = totalShippingCbm;
    }
}
