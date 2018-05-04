package com.newaim.purchase.desktop.sta.vo;

import java.io.Serializable;
import java.util.Date;

public class StaOrdersCycleVo implements Serializable {

    private String id;

    private String orderNumber;
    private String orderTitle;

    /**
     * 供应商信息
     */
    private String vendorId;
    private String vendorCnName;
    private String vendorEnName;

    private Date createdTime;
    private Date orderConfirmedDate;
    private Date depositDate;
    private Date originalReadyDate;
    private Date adjustReadyDate;
    private Date originalEtd;
    private Date adjustEtd;
    private Date agentNotificationDate;
    private Date balanceDate;
    private Date qcTime;
    private Date shippingDocReceivedDate;
    private Date shippingDocForwardedDate;
    private Date eta;
    private Date containerArrivingTime;
    private Date putAwayTime;
    private Integer orderConfirmedCycle;
    private Integer leadTime;
    private Integer containerPortCycle;
    private Integer customClearCycle;
    private Integer containerArrivingCycle;
    private Integer unloadingCycle;
    private Integer orderCycle;
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

    public Date getEta() {
        return eta;
    }

    public void setEta(Date eta) {
        this.eta = eta;
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

    public Integer getLeadTime() {
        return leadTime;
    }

    public void setLeadTime(Integer leadTime) {
        this.leadTime = leadTime;
    }

    public Integer getOrderConfirmedCycle() {
        return orderConfirmedCycle;
    }

    public void setOrderConfirmedCycle(Integer orderConfirmedCycle) {
        this.orderConfirmedCycle = orderConfirmedCycle;
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
