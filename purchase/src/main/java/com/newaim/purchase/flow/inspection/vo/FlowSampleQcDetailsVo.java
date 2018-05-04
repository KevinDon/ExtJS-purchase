package com.newaim.purchase.flow.inspection.vo;

import com.newaim.purchase.flow.inspection.entity.FlowSampleQcDetail;

import java.io.Serializable;
import java.util.List;

public class FlowSampleQcDetailsVo implements Serializable{

    private List<FlowSampleQcDetail> details;

    public List<FlowSampleQcDetail> getDetails() {
        return details;
    }

    public void setDetails(List<FlowSampleQcDetail> details) {
        this.details = details;
    }
}
