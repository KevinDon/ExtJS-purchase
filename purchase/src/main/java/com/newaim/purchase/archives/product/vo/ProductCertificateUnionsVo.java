package com.newaim.purchase.archives.product.vo;

import com.newaim.purchase.archives.product.entity.ProductCertificateUnion;

import java.io.Serializable;
import java.util.List;

public class ProductCertificateUnionsVo implements Serializable{

    private List<ProductCertificateUnion> details;

    public List<ProductCertificateUnion> getDetails() {
        return details;
    }

    public void setDetails(List<ProductCertificateUnion> details) {
        this.details = details;
    }
}
