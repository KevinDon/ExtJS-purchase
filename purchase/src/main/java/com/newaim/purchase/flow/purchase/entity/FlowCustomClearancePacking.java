package com.newaim.purchase.flow.purchase.entity;

import com.google.common.collect.Lists;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "na_flow_custom_clearance_packing")
public class FlowCustomClearancePacking implements Serializable{

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.newaim.core.jpa.IdGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name = "prefix", value = "FCCP")})
    private String id;
    /**订单流水号*/
    @Column(name = "business_id")
    private String businessId;

    /**订单信息*/
    private String orderId;
    private String orderNumber;
    private String orderTitle;

    /**集装箱编号*/
    private String containerNumber;
    /**封印编号*/
    private String sealsNumber;
    /**柜号*/
    private String containerOrder;
    /**柜型*/
    private String containerType;
    /**对方订单号*/
    private String ciNumber;

    /**挂起状态*/
    private Integer hold;

    private  Integer flagCostStatus;//成本计算完成标记
    private  String flagCostId;//成本id
    private Date flagCostTime;//成本计算完成时间

    @Transient
    private List<FlowCustomClearancePackingDetail> packingDetails = Lists.newArrayList();

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

    public String getContainerType() {
        return containerType;
    }

    public void setContainerType(String containerType) {
        this.containerType = containerType;
    }

    public String getCiNumber() {
        return ciNumber;
    }

    public void setCiNumber(String ciNumber) {
        this.ciNumber = ciNumber;
    }

    public List<FlowCustomClearancePackingDetail> getPackingDetails() {
        return packingDetails;
    }

    public void setPackingDetails(List<FlowCustomClearancePackingDetail> packingDetails) {
        this.packingDetails = packingDetails;
    }

    public Integer getHold() {
        return hold;
    }

    public void setHold(Integer hold) {
        this.hold = hold;
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
}
