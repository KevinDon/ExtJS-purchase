package com.newaim.purchase.flow.shipping.vo;

import com.newaim.purchase.flow.shipping.entity.FlowAsnPacking;

import java.io.Serializable;
import java.util.List;

public class FlowAsnPackingsVo implements Serializable{

    private List<FlowAsnPacking> details;

    public List<FlowAsnPacking> getDetails() {
        return details;
    }

    public void setDetails(List<FlowAsnPacking> details) {
        this.details = details;
    }
}
