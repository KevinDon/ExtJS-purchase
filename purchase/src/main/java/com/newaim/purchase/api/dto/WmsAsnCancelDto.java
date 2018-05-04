package com.newaim.purchase.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;


public class WmsAsnCancelDto implements Serializable{

    @JsonProperty("asn_no")
    private String asnNumber;

    public String getAsnNumber() {
        return asnNumber;
    }

    public void setAsnNumber(String asnNumber) {
        this.asnNumber = asnNumber;
    }
}
