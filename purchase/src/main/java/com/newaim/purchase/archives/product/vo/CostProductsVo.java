package com.newaim.purchase.archives.product.vo;

import com.google.common.collect.Lists;
import com.newaim.purchase.archives.product.entity.CostProduct;

import java.io.Serializable;
import java.util.List;

public class CostProductsVo implements Serializable{

    private List<CostProduct> CostProducts = Lists.newArrayList();

    public List<CostProduct> getCostProducts() {
        return CostProducts;
    }

    public void setCostProducts(List<CostProduct> costProducts) {
        CostProducts = costProducts;
    }
}
