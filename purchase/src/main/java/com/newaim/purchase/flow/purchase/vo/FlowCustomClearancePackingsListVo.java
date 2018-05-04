package com.newaim.purchase.flow.purchase.vo;

import com.newaim.purchase.flow.purchase.entity.FlowCustomClearancePacking;

import java.io.Serializable;
import java.util.List;

public class FlowCustomClearancePackingsListVo implements Serializable{

    private List<FlowCustomClearancePacking> details;

    public List<FlowCustomClearancePacking> getDetails() {
        return details;
    }

    public void setDetails(List<FlowCustomClearancePacking> details) {
        this.details = details;
    }
}
