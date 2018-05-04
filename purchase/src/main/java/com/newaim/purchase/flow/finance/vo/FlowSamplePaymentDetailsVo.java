package com.newaim.purchase.flow.finance.vo;

import com.newaim.purchase.flow.finance.entity.FlowSamplePaymentDetail;

import java.io.Serializable;
import java.util.List;

public class FlowSamplePaymentDetailsVo implements Serializable{

    private List<FlowSamplePaymentDetail> details;

    public List<FlowSamplePaymentDetail> getDetails() {
        return details;
    }

    public void setDetails(List<FlowSamplePaymentDetail> details) {
        this.details = details;
    }

}
