package com.newaim.purchase.desktop.sta.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="na_sta_orders_cycle")
public class StaOrdersCycle implements Serializable{

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.newaim.core.jpa.IdGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name = "prefix", value = "SOC")})
    private String id;

    private String orderNumber;//订单编号
    private String orderTitle;//订单名

    /**供应商信息*/
    private String vendorId;
    private String vendorCnName;
    private String vendorEnName;

    private Date createdTime;//创建时间
    private Date orderConfirmedDate;//订单确认日期
    private Date depositDate;//付订金日
    private Date originalReadyDate;//计划完货日
    private Date adjustReadyDate;//订单实货日
    private Date originalEtd;//原ETD
    private Date adjustEtd;//ETD
    private Date agentNotificationDate;//通知货代日
    private Date balanceDate;//尾款付款日
    private Date qcTime;//QC日期
    private Date shippingDocReceivedDate;//清关文件接收日
    private Date shippingDocForwardedDate;//清关文件转发日
    private Date eta;//ETA
    private Date containerArrivingTime;//货柜到仓日
    private Date putAwayTime;//货柜归还日
    private Integer orderConfirmedCycle;//订单确认周期
    private Integer leadTime;//工厂生产周期
    private Integer containerPortCycle;//装柜送港周期
    private Integer customClearCycle;//清关周期
    private Integer containerArrivingCycle;//货柜到仓周期
    private Integer unloadingCycle;//拆柜周期
    private Integer orderCycle;//订单总周期
    private Integer status;


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
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

    public Date getEta() {
        return eta;
    }

    public void setEta(Date eta) {
        this.eta = eta;
    }

    public Date getOrderConfirmedDate() {
        return orderConfirmedDate;
    }

    public void setOrderConfirmedDate(Date orderConfirmedDate) {
        this.orderConfirmedDate = orderConfirmedDate;
    }

    public Date getDepositDate() {
        return depositDate;
    }

    public void setDepositDate(Date depositDate) {
        this.depositDate = depositDate;
    }

    public Date getOriginalReadyDate() {
        return originalReadyDate;
    }

    public void setOriginalReadyDate(Date originalReadyDate) {
        this.originalReadyDate = originalReadyDate;
    }

    public Date getAdjustReadyDate() {
        return adjustReadyDate;
    }

    public void setAdjustReadyDate(Date adjustReadyDate) {
        this.adjustReadyDate = adjustReadyDate;
    }

    public Date getOriginalEtd() {
        return originalEtd;
    }

    public void setOriginalEtd(Date originalEtd) {
        this.originalEtd = originalEtd;
    }

    public Date getAdjustEtd() {
        return adjustEtd;
    }

    public void setAdjustEtd(Date adjustEtd) {
        this.adjustEtd = adjustEtd;
    }

    public Date getAgentNotificationDate() {
        return agentNotificationDate;
    }

    public void setAgentNotificationDate(Date agentNotificationDate) {
        this.agentNotificationDate = agentNotificationDate;
    }

    public Date getBalanceDate() {
        return balanceDate;
    }

    public void setBalanceDate(Date balanceDate) {
        this.balanceDate = balanceDate;
    }

    public Date getQcTime() {
        return qcTime;
    }

    public void setQcTime(Date qcTime) {
        this.qcTime = qcTime;
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

    public Date getContainerArrivingTime() {
        return containerArrivingTime;
    }

    public void setContainerArrivingTime(Date containerArrivingTime) {
        this.containerArrivingTime = containerArrivingTime;
    }

    public Date getPutAwayTime() {
        return putAwayTime;
    }

    public void setPutAwayTime(Date putAwayTime) {
        this.putAwayTime = putAwayTime;
    }

    public Integer getOrderConfirmedCycle() {
        return orderConfirmedCycle;
    }

    public void setOrderConfirmedCycle(Integer orderConfirmedCycle) {
        this.orderConfirmedCycle = orderConfirmedCycle;
    }

    public Integer getLeadTime() {
        return leadTime;
    }

    public void setLeadTime(Integer leadTime) {
        this.leadTime = leadTime;
    }

    public Integer getContainerPortCycle() {
        return containerPortCycle;
    }

    public void setContainerPortCycle(Integer containerPortCycle) {
        this.containerPortCycle = containerPortCycle;
    }

    public Integer getCustomClearCycle() {
        return customClearCycle;
    }

    public void setCustomClearCycle(Integer customClearCycle) {
        this.customClearCycle = customClearCycle;
    }

    public Integer getContainerArrivingCycle() {
        return containerArrivingCycle;
    }

    public void setContainerArrivingCycle(Integer containerArrivingCycle) {
        this.containerArrivingCycle = containerArrivingCycle;
    }

    public Integer getUnloadingCycle() {
        return unloadingCycle;
    }

    public void setUnloadingCycle(Integer unloadingCycle) {
        this.unloadingCycle = unloadingCycle;
    }

    public Integer getOrderCycle() {
        return orderCycle;
    }

    public void setOrderCycle(Integer orderCycle) {
        this.orderCycle = orderCycle;
    }
}
