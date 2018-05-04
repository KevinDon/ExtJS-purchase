package com.newaim.purchase.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by Mark on 2017/12/14.
 */
public class WmsAsnExceptionDetailDto implements Serializable{

    @JsonProperty("exp_sku")
    private String expSku;

    @JsonProperty("exp_qty")
    private Integer expQty;

    /**
     * QC
     */
    @JsonProperty("exception_code")
    private String exceptionCode;

    /**
     * Quality check not passed
     */
    @JsonProperty("exception_descr")
    private String exceptionDescr;

    /**
     * 2017-09-08
     */
    @JsonProperty("warehousing_date")
    private String warehousingDate;

    public String getExpSku() {
        return expSku;
    }

    public void setExpSku(String expSku) {
        this.expSku = expSku;
    }

    public Integer getExpQty() {
        return expQty;
    }

    public void setExpQty(Integer expQty) {
        this.expQty = expQty;
    }

    public String getExceptionCode() {
        return exceptionCode;
    }

    public void setExceptionCode(String exceptionCode) {
        this.exceptionCode = exceptionCode;
    }

    public String getExceptionDescr() {
        return exceptionDescr;
    }

    public void setExceptionDescr(String exceptionDescr) {
        this.exceptionDescr = exceptionDescr;
    }

    public String getWarehousingDate() {
        return warehousingDate;
    }

    public void setWarehousingDate(String warehousingDate) {
        this.warehousingDate = warehousingDate;
    }
}
