package com.newaim.purchase.flow.shipping.entity;

import com.google.common.collect.Lists;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "na_flow_asn_packing")
public class FlowAsnPacking implements Serializable{

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.newaim.core.jpa.IdGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name = "prefix", value = "FASNP")})
    private String id;
    @Column(name = "business_id")
    private String businessId;
    private String ccPackingId;
    /**
     * 订单相关字段
     */
    private String orderId;
    private String orderNumber;
    private String orderTitle;
    @Transient
    private List<FlowAsnPackingDetail> packingDetails = Lists.newArrayList();

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

    public List<FlowAsnPackingDetail> getPackingDetails() {
        return packingDetails;
    }

    public void setPackingDetails(List<FlowAsnPackingDetail> packingDetails) {
        this.packingDetails = packingDetails;
    }
}
