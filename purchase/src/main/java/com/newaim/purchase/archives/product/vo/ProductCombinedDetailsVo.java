package com.newaim.purchase.archives.product.vo;

import com.newaim.purchase.archives.product.entity.ProductCombinedDetail;

import java.io.Serializable;
import java.util.List;

public class ProductCombinedDetailsVo implements Serializable{

    private List<ProductCombinedDetail> details;

    public List<ProductCombinedDetail> getDetails() {
        return details;
    }

    public void setDetails(List<ProductCombinedDetail> details) {
        this.details = details;
    }
}
