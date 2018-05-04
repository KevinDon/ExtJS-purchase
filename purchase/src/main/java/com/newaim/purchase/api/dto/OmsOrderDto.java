package com.newaim.purchase.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Mark on 2017/12/22.
 */
public class OmsOrderDto implements Serializable {

    /**OMS单号*/
    @JsonProperty("order_no")
    private String orderNo;

    /**交易号*/
    private String tid;

    /**交易渠道*/
    @JsonProperty("shop_id")
    private String shopId;

    /**交易渠道*/
    @JsonProperty("shop_code")
    private String shopCode;

    /**用户昵称*/
    @JsonProperty("user_nick")
    private String userNick;

    private String email;

    @JsonProperty("order_time")
    private Date orderTime;

    @JsonProperty("buyer_remark")
    private String buyerRemark;

    @JsonProperty("seller_remark")
    private String sellerRemark;

    @JsonProperty("order_items")
    private List<OmsOrderDetailDto> details;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public String getUserNick() {
        return userNick;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public String getBuyerRemark() {
        return buyerRemark;
    }

    public void setBuyerRemark(String buyerRemark) {
        this.buyerRemark = buyerRemark;
    }

    public String getSellerRemark() {
        return sellerRemark;
    }

    public void setSellerRemark(String sellerRemark) {
        this.sellerRemark = sellerRemark;
    }

    public List<OmsOrderDetailDto> getDetails() {
        return details;
    }

    public void setDetails(List<OmsOrderDetailDto> details) {
        this.details = details;
    }
}
