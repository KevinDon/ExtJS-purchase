package com.newaim.purchase.archives.product.vo;

import com.google.common.collect.Lists;
import com.newaim.purchase.archives.product.entity.TroubleTicketProduct;

import java.io.Serializable;
import java.util.List;

public class TroubleTicketProductsVo implements Serializable{


    private List<TroubleTicketProduct> products = Lists.newArrayList();

    public List<TroubleTicketProduct> getProducts() {
        return products;
    }

    public void setProducts(List<TroubleTicketProduct> products) {
        this.products = products;
    }
}
