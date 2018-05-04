package com.newaim.purchase.flow.purchase.vo;

import com.newaim.purchase.flow.purchase.entity.FlowSampleOtherDetail;

import java.io.Serializable;
import java.util.List;

public class FlowSampleOtherDetailsVo implements Serializable{

    private List<FlowSampleOtherDetail> otherDetails;

    public List<FlowSampleOtherDetail> getOtherDetails() {
        return otherDetails;
    }

    public void setOtherDetails(List<FlowSampleOtherDetail> otherDetails) {
        this.otherDetails = otherDetails;
    }
}
