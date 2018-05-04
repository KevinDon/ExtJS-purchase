package com.newaim.purchase.desktop.reports.vo;

import com.google.common.collect.Lists;
import com.newaim.purchase.desktop.reports.entity.ReportsProduct;

import java.io.Serializable;
import java.util.List;


public class ReportsProductsVo implements Serializable{

    private List<ReportsProduct> products = Lists.newArrayList();

    public List<ReportsProduct> getProducts() {
        return products;
    }

    public void setProducts(List<ReportsProduct> products) {
        this.products = products;
    }
}
