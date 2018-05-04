package com.newaim.purchase.archives.flow.shipping.entity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "na_warehouse_plan_detail")
public class WarehousePlanDetail implements Serializable{

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.newaim.core.jpa.IdGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name = "prefix", value = "WPD")})
    private String id;

    private String warehousePlanId;

    /**订单信息*/
    private String orderId;
    private String orderNumber;
    private String orderTitle;
    private Integer orderIndex;

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

}
