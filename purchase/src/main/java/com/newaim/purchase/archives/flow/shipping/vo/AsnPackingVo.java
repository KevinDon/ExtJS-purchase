package com.newaim.purchase.archives.flow.shipping.vo;

import com.google.common.collect.Lists;
import com.newaim.purchase.flow.shipping.vo.FlowAsnPackingDetailVo;

import java.io.Serializable;
import java.util.List;

public class AsnPackingVo implements Serializable{

    private String id;
    private String asnId;
    private String ccPackingId;
    /**
     * 订单相关字段
     */
    private String orderId;
    private String orderNumber;
    private String orderTitle;

    private List<AsnPackingDetailVo> packingList = Lists.newArrayList();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAsnId() {
        return asnId;
    }

    public void setAsnId(String asnId) {
        this.asnId = asnId;
    }

    public String getCcPackingId() {
        return ccPackingId;
    }

    public void setCcPackingId(String ccPackingId) {
        this.ccPackingId = ccPackingId;
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

    public List<AsnPackingDetailVo> getPackingList() {
        return packingList;
    }

    public void setPackingList(List<AsnPackingDetailVo> packingList) {
        this.packingList = packingList;
    }
}
