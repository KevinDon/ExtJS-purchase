package com.newaim.purchase.archives.flow.shipping.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.newaim.purchase.config.json.JsonMoney;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class OrderShippingPlanDetailVo implements Serializable{

    @JsonIgnore
    private String id;

    private String orderShippingPlanId;

    private String orderId;//訂單Id
    private String orderBusinessId;
    private String orderNumber;
    private String orderTitle;

    private String serviceProviderId;//服務商Id
    private String serviceProviderCnName;
    private String serviceProviderEnName;

    private String originPortId;
    private String originPortCnName;
    private String originPortEnName;
    private String destinationPortId;
    private String destinationPortCnName;
    private String destinationPortEnName;
    private Integer containerType;//貨櫃類型
    private BigDecimal containerQty;//貨櫃數量
    private Date readyDate;//预计完货时间
    private Date etd;//预计发运时间
    private String serviceProviderQuotationId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderShippingPlanId() {
        return orderShippingPlanId;
    }

    public void setOrderShippingPlanId(String orderShippingPlanId) {
        this.orderShippingPlanId = orderShippingPlanId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderBusinessId() {
        return orderBusinessId;
    }

    public void setOrderBusinessId(String orderBusinessId) {
        this.orderBusinessId = orderBusinessId;
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

    public String getServiceProviderId() {
        return serviceProviderId;
    }

    public void setServiceProviderId(String serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
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

    public Date getReadyDate() {
        return readyDate;
    }

    public void setReadyDate(Date readyDate) {
        this.readyDate = readyDate;
    }

    public Date getEtd() {
        return etd;
    }

    public void setEtd(Date etd) {
        this.etd = etd;
    }

    public String getServiceProviderQuotationId() {
        return serviceProviderQuotationId;
    }

    public void setServiceProviderQuotationId(String serviceProviderQuotationId) {
        this.serviceProviderQuotationId = serviceProviderQuotationId;
    }
}
