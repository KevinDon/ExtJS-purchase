package com.newaim.purchase.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Mark on 2017/12/14.
 */
public class WmsAsnCreateResultDto {

    @JsonProperty("asn_no")
    private String AsnNo;
    
    @JsonProperty("invalid_skus")
    private String[] invalidSkus;

    public String getAsnNo() {
        return AsnNo;
    }

    public void setAsnNo(String asnNo) {
        AsnNo = asnNo;
    }

	public String[] getInvalidSkus() {
		return invalidSkus;
	}

	public void setInvalidSkus(String[] invalidSkus) {
		this.invalidSkus = invalidSkus;
	}

}
