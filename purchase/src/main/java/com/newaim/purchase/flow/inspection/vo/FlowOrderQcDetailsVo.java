package com.newaim.purchase.flow.inspection.vo;

import com.newaim.purchase.flow.inspection.entity.FlowOrderQcDetail;

import java.io.Serializable;
import java.util.List;

public class FlowOrderQcDetailsVo implements Serializable{

    private List<FlowOrderQcDetail> details;

    public List<FlowOrderQcDetail> getDetails() {
        return details;
    }

    public void setDetails(List<FlowOrderQcDetail> details) {
        this.details = details;
    }
}
