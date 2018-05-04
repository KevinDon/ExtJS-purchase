package com.newaim.purchase.flow.purchase.vo;


import com.newaim.purchase.flow.purchase.entity.FlowSampleDetail;

import java.io.Serializable;
import java.util.List;

public class FlowSampleDetailsVo implements Serializable{
    private List<FlowSampleDetail> details;

    public List<FlowSampleDetail> getDetails() {
        return details;
    }

    public void setDetails(List<FlowSampleDetail> details) {
        this.details = details;
    }


}
