package com.newaim.purchase.flow.shipping.vo;

import com.google.common.collect.Lists;
import com.newaim.purchase.archives.flow.purchase.vo.CustomClearancePackingDetailVo;
import com.newaim.purchase.flow.shipping.entity.FlowAsnPacking;
import com.newaim.purchase.flow.shipping.entity.FlowAsnPackingDetail;

import java.io.Serializable;
import java.util.List;

public class FlowAsnPackingVo implements Serializable{

    private String id;
    private String businessId;
    private String ccPackingId;
    /**集装箱编号*/
    private String containerNumber;
    /**封印编号*/
    private String sealsNumber;
    /**柜型*/
    private String containerType;
    /**
     * 订单相关字段
     */
    private String orderId;
    private String orderNumber;
    private String orderTitle;

    private List<FlowAsnPackingDetailVo> asnPackingDetails = Lists.newArrayList();
//    private List<CustomClearancePackingDetailVo> packingDetailList = Lists.newArrayList();


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

    public String getCcPackingId() {
        return ccPackingId;
    }

    public void setCcPackingId(String ccPackingId) {
        this.ccPackingId = ccPackingId;
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

    public String getContainerType() {
        return containerType;
    }

    public void setContainerType(String containerType) {
        this.containerType = containerType;
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

    public List<FlowAsnPackingDetailVo> getAsnPackingDetails() {
        return asnPackingDetails;
    }

    public void setAsnPackingDetails(List<FlowAsnPackingDetailVo> asnPackingDetails) {
        this.asnPackingDetails = asnPackingDetails;
    }

//    public List<CustomClearancePackingDetailVo> getPackingDetailList() {
//        return packingDetailList;
//    }
//
//    public void setPackingDetailList(List<CustomClearancePackingDetailVo> packingDetailList) {
//        this.packingDetailList = packingDetailList;
//    }
}
