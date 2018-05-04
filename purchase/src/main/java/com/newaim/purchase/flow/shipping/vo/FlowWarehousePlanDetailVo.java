package com.newaim.purchase.flow.shipping.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;

public class FlowWarehousePlanDetailVo implements Serializable{

    @JsonIgnore
    private String id;

    private String businessId;

    /**订单信息*/
    private String orderId;
    private String orderNumber;
    private String orderTitle;
    private Integer orderIndex;
    private Date eta;

    /**装柜编号*/
    private String containerNumber;
    /**柜型*/
    private Integer containerType;
    /**预售*/
    private Integer presale;
    /**检查*/
    private String inspection;
    /**目的地址*/
    private String destinationPlace;
    /**接收时间*/
    private Date receiveDate;
    /**接收起始时间*/
    private Date receiveStartDate;
    /**接收结束时间*/
    private Date receiveEndDate;

    /**服务商分类ID*/
    private String vendorProductCategoryId;
    private String vendorProductCategoryAlias;
    private String orderShippingPlanId;
    private String flowOrderShippingPlanId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public Date getEta() {
        return eta;
    }

    public void setEta(Date eta) {
        this.eta = eta;
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

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public String getContainerNumber() {
        return containerNumber;
    }

    public void setContainerNumber(String containerNumber) {
        this.containerNumber = containerNumber;
    }

    public Integer getContainerType() {
        return containerType;
    }

    public void setContainerType(Integer containerType) {
        this.containerType = containerType;
    }

    public Integer getPresale() {
        return presale;
    }

    public void setPresale(Integer presale) {
        this.presale = presale;
    }

    public String getInspection() {
        return inspection;
    }

    public void setInspection(String inspection) {
        this.inspection = inspection;
    }

    public String getDestinationPlace() {
        return destinationPlace;
    }

    public void setDestinationPlace(String destinationPlace) {
        this.destinationPlace = destinationPlace;
    }

    public Date getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Date receiveDate) {
        this.receiveDate = receiveDate;
    }

    public Date getReceiveStartDate() {
        return receiveStartDate;
    }

    public void setReceiveStartDate(Date receiveStartDate) {
        this.receiveStartDate = receiveStartDate;
    }

    public Date getReceiveEndDate() {
        return receiveEndDate;
    }

    public void setReceiveEndDate(Date receiveEndDate) {
        this.receiveEndDate = receiveEndDate;
    }

    public String getOrderShippingPlanId() {
        return orderShippingPlanId;
    }

    public void setOrderShippingPlanId(String orderShippingPlanId) {
        this.orderShippingPlanId = orderShippingPlanId;
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

    public String getFlowOrderShippingPlanId() {
        return flowOrderShippingPlanId;
    }

    public void setFlowOrderShippingPlanId(String flowOrderShippingPlanId) {
        this.flowOrderShippingPlanId = flowOrderShippingPlanId;
    }
}
