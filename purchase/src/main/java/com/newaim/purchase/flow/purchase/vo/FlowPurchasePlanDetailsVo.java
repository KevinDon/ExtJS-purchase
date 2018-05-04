package com.newaim.purchase.flow.purchase.vo;


import com.newaim.purchase.flow.purchase.entity.FlowPurchasePlanDetail;

import java.io.Serializable;
import java.util.List;

public class FlowPurchasePlanDetailsVo implements Serializable{

    private List<FlowPurchasePlanDetail> details;

    public List<FlowPurchasePlanDetail> getDetails() {
        return details;
    }

    public void setDetails(List<FlowPurchasePlanDetail> details) {
        this.details = details;
    }
}
