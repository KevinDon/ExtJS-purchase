package com.newaim.purchase.flow.inspection.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.newaim.purchase.archives.product.vo.ProductVo;

import java.io.Serializable;

import javax.xml.soap.Text;

public class FlowProductCertificationDetailVo implements Serializable{

    private String id;
    @JsonIgnore
    private String businessId;

    private String productCertificateId;//产品证书ID

    private String productId;
    private ProductVo product;

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

    public ProductVo getProduct() {
        return product;
    }

    public void setProduct(ProductVo product) {
        this.product = product;
    }
}
