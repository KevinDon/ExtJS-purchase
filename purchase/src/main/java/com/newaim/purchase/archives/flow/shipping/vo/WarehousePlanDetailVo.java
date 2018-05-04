package com.newaim.purchase.archives.flow.shipping.vo;

import com.google.common.collect.Lists;
import com.newaim.purchase.archives.flow.purchase.vo.CustomClearancePackingDetailVo;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class WarehousePlanDetailVo implements Serializable{

    private String id;

    private String warehousePlanId;
    private String flowWarehousePlanId;
    /**订单信息*/
    private String orderId;
    private String orderNumber;
    private String orderTitle;
    private Integer orderIndex;
    private String businessId;


    /**装柜编号*/
    private String containerNumber;
    /**柜型*/
    private Integer containerType;
    /**预售*/
    private Integer presale;
    /**检查*/
    private String inspection;
    /**起始地址*/
    private String originPlace;
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

    private String warehouseId;

    @Transient
    List<CustomClearancePackingDetailVo> packingDetails = Lists.newArrayList();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWarehousePlanId() {
        return warehousePlanId;
    }

    public void setWarehousePlanId(String warehousePlanId) {
        this.warehousePlanId = warehousePlanId;
    }

    public String getFlowWarehousePlanId() {
        return flowWarehousePlanId;
    }

    public void setFlowWarehousePlanId(String flowWarehousePlanId) {
        this.flowWarehousePlanId = flowWarehousePlanId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
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

    public String getOriginPlace() {
        return originPlace;
    }

    public void setOriginPlace(String originPlace) {
        this.originPlace = originPlace;
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

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public List<CustomClearancePackingDetailVo> getPackingDetails() {
        return packingDetails;
    }

    public void setPackingDetails(List<CustomClearancePackingDetailVo> packingDetails) {
        this.packingDetails = packingDetails;
    }
}
