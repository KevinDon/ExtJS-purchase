package com.newaim.purchase.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Mark on 2018/1/31.
 */
public class SyncBarcodeDto {

    @JsonProperty("SKU")
    private String sku;

    @JsonProperty("BARCODE")
    private String barcode;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
