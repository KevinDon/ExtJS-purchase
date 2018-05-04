package com.newaim.purchase.flow.inspection.entity;


import com.newaim.purchase.archives.product.entity.Product;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "na_flow_product_certification_detail")
public class FlowProductCertificationDetail implements Serializable{

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.newaim.core.jpa.IdGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name = "prefix", value = "FPCFD")})
    private String id;

    @Column(name = "business_id")
    private String businessId;

    @Column(name = "product_id")
    private String productId;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "product_id", insertable=false, updatable=false)
    private Product product;

    @Column(name = "product_certificate_id")
    private String productCertificateId;//产品证书ID
//    private Text files;

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

    public String getProductCertificateId() {
        return productCertificateId;
    }

    public void setProductCertificateId(String productCertificateId) {
        this.productCertificateId = productCertificateId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

//    public Text getFiles() {
//        return files;
//    }
//
//    public void setFiles(Text files) {
//        this.files = files;
//    }
}
