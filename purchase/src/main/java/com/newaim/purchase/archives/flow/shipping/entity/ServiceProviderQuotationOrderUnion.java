package com.newaim.purchase.archives.flow.shipping.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "na_service_provider_quotation_order_union")
public class ServiceProviderQuotationOrderUnion implements Serializable{

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.newaim.core.jpa.IdGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name = "prefix", value = "SPQO")})
    private String id;

    /**
     * 服务商相关字段
     */
    private String serviceProviderQuotationId;
    private String orderId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceProviderQuotationId() {
        return serviceProviderQuotationId;
    }

    public void setServiceProviderQuotationId(String serviceProviderQuotationId) {
        this.serviceProviderQuotationId = serviceProviderQuotationId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
