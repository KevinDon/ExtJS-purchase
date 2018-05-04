package com.newaim.purchase.flow.shipping.vo;

import com.newaim.purchase.flow.shipping.entity.FlowOrderShippingApplyDetail;

import java.io.Serializable;
import java.util.List;

public class FlowOrderShippingApplyDetailsVo implements Serializable{

    private List<FlowOrderShippingApplyDetail> details;

    public List<FlowOrderShippingApplyDetail> getDetails() {
        return details;
    }

    public void setDetails(List<FlowOrderShippingApplyDetail> details) {
        this.details = details;
    }
}
