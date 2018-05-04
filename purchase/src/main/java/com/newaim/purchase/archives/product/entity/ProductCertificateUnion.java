package com.newaim.purchase.archives.product.entity;


import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name = "na_product_certificate_union")
public class ProductCertificateUnion implements Serializable{

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(
            name = "idGenerator", strategy = "com.newaim.core.jpa.IdGenerator",
            parameters = {@Parameter(name = "prefix", value = "PC")}
    )
    private String id;

    @Column(name = "product_id")
    private String productId;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "product_id", insertable=false, updatable=false)
    private Product product;

    @Column(name = "certificate_id")
    private String certificateId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(String certificateId) {
        this.certificateId = certificateId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
