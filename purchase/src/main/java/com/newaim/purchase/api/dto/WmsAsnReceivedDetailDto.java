package com.newaim.purchase.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by Mark on 2017/12/14.
 */
public class WmsAsnReceivedDetailDto implements Serializable{

    private String sku;

    private Integer qty;
    /**
     * 2017-09-08
     */
    @JsonProperty("warehousing_date")
    private String warehousingDate;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getWarehousingDate() {
        return warehousingDate;
    }

    public void setWarehousingDate(String warehousingDate) {
        this.warehousingDate = warehousingDate;
    }
}
