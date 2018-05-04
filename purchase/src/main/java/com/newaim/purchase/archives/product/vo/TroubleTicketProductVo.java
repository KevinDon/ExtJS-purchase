package com.newaim.purchase.archives.product.vo;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;

public class TroubleTicketProductVo implements Serializable{
    
    private String id;

    private String troubleTicketId;
    private String orderId;
    private String orderNumber;
    private String productId;
    private String sku;
    private String jobNo;
    private String productName;
    private Integer combined;
    private String categoryId;
    private String categoryCnName;
    private String categoryEnName;
    private Integer sellQty;

    private List<TroubleTicketProductDetailVo> troubleDetails = Lists.newArrayList();

    private ProductVo product;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTroubleTicketId() {
        return troubleTicketId;
    }

    public void setTroubleTicketId(String troubleTicketId) {
        this.troubleTicketId = troubleTicketId;
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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getCombined() {
        return combined;
    }

    public void setCombined(Integer combined) {
        this.combined = combined;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryCnName() {
        return categoryCnName;
    }

    public void setCategoryCnName(String categoryCnName) {
        this.categoryCnName = categoryCnName;
    }

    public String getCategoryEnName() {
        return categoryEnName;
    }

    public void setCategoryEnName(String categoryEnName) {
        this.categoryEnName = categoryEnName;
    }

    public Integer getSellQty() {
        return sellQty;
    }

    public void setSellQty(Integer sellQty) {
        this.sellQty = sellQty;
    }

    public List<TroubleTicketProductDetailVo> getTroubleDetails() {
        return troubleDetails;
    }

    public void setTroubleDetails(List<TroubleTicketProductDetailVo> troubleDetails) {
        this.troubleDetails = troubleDetails;
    }

    public ProductVo getProduct() {
        return product;
    }

    public void setProduct(ProductVo product) {
        this.product = product;
    }

    public String getJobNo() {
        return jobNo;
    }

    public void setJobNo(String jobNo) {
        this.jobNo = jobNo;
    }
}
