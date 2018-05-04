package com.newaim.purchase.archives.product.entity;

import com.google.common.collect.Lists;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "na_trouble_ticket_product")
public class TroubleTicketProduct implements Serializable{
    
    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.newaim.core.jpa.IdGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name = "prefix", value = "TTPT")})
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

    @Transient
    private List<TroubleTicketProductDetail> troubleDetails = Lists.newArrayList();

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

    public String getJobNo() {
        return jobNo;
    }

    public void setJobNo(String jobNo) {
        this.jobNo = jobNo;
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

    public List<TroubleTicketProductDetail> getTroubleDetails() {
        return troubleDetails;
    }

    public void setTroubleDetails(List<TroubleTicketProductDetail> troubleDetails) {
        this.troubleDetails = troubleDetails;
    }
}
