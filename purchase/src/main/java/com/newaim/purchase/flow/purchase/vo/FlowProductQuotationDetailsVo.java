package com.newaim.purchase.flow.purchase.vo;


import com.newaim.purchase.flow.purchase.entity.FlowProductQuotationDetail;

import java.io.Serializable;
import java.util.List;

public class FlowProductQuotationDetailsVo implements Serializable{

    private List<FlowProductQuotationDetail> details;

    public List<FlowProductQuotationDetail> getDetails() {
        return details;
    }

    public void setDetails(List<FlowProductQuotationDetail> details) {
        this.details = details;
    }
}
