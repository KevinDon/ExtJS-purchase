package com.newaim.purchase.flow.shipping.vo;
import com.newaim.purchase.flow.shipping.entity.FlowOrderShippingPlanDetail;

import java.io.Serializable;
import java.util.List;

public class FlowOrderShippingPlanDetailsVo implements Serializable{

    private List<FlowOrderShippingPlanDetail> details;

    public List<FlowOrderShippingPlanDetail> getDetails() {
        return details;
    }

    public void setDetails(List<FlowOrderShippingPlanDetail> details) {
        this.details = details;
    }
}
