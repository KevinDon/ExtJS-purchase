package com.newaim.purchase.archives.flow.shipping.entity;

import com.google.common.collect.Lists;
import com.newaim.purchase.flow.shipping.entity.FlowAsnPackingDetail;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "na_asn_packing")
public class AsnPacking implements Serializable{

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.newaim.core.jpa.IdGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name = "prefix", value = "AP")})
    private String id;
    private String asnId;
    private String ccPackingId;
    /**
     * 订单相关字段
     */
    private String orderId;
    private String orderNumber;
    private String orderTitle;
    @Transient
    private List<AsnPackingDetail> packingDetails = Lists.newArrayList();

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

    public List<AsnPackingDetail> getPackingDetails() {
        return packingDetails;
    }

    public void setPackingDetails(List<AsnPackingDetail> packingDetails) {
        this.packingDetails = packingDetails;
    }
}
