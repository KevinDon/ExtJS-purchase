package com.newaim.purchase.api.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Mark on 2017/12/14.
 */
public class WmsAsnSkuDto implements Serializable {

    private String sku;

    @JsonProperty("expected_qty")
    private Integer expectedQty;

    private String cost;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getExpectedQty() {
        return expectedQty;
    }

    public void setExpectedQty(Integer expectedQty) {
        this.expectedQty = expectedQty;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }
}
