package com.newaim.purchase.flow.purchase.vo;

import com.newaim.purchase.flow.purchase.entity.FlowPurchaseContractDetail;

import java.io.Serializable;
import java.util.List;

public class FlowPurchaseContractDetailsVo implements Serializable{

    private List<FlowPurchaseContractDetail> details;

    public List<FlowPurchaseContractDetail> getDetails() {
        return details;
    }

    public void setDetails(List<FlowPurchaseContractDetail> details) {
        this.details = details;
    }
}
