package com.newaim.purchase.archives.product.vo;


import com.google.common.collect.Lists;


import java.io.Serializable;


public class ProductCertificateUnionVo implements Serializable{

    private String id;
    private String productId;
    private String certificateId;
    private ProductVo product = null;
    private ProductCertificateVo productCertificate = null;

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

    public ProductVo getProduct() {
        return product;
    }

    public void setProduct(ProductVo product) {
        this.product = product;
    }

    public ProductCertificateVo getProductCertificate() {
        return productCertificate;
    }

    public void setProductCertificate(ProductCertificateVo productCertificate) {
        this.productCertificate = productCertificate;
    }
}
