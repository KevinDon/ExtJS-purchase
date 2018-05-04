package com.newaim.purchase.flow.finance.vo;

import com.newaim.purchase.flow.finance.entity.FlowFeeRegisterDetail;

import java.io.Serializable;
import java.util.List;

public class FlowFeeRegisterDetailsVo implements Serializable{

    private List<FlowFeeRegisterDetail> details;

    private List<FlowFeeRegisterDetail> otherDetails;

    public List<FlowFeeRegisterDetail> getDetails() {
        return details;
    }

    public void setDetails(List<FlowFeeRegisterDetail> details) {
        this.details = details;
    }

    public List<FlowFeeRegisterDetail> getOtherDetails() {
        return otherDetails;
    }

    public void setOtherDetails(List<FlowFeeRegisterDetail> otherDetails) {
        this.otherDetails = otherDetails;
    }
}
