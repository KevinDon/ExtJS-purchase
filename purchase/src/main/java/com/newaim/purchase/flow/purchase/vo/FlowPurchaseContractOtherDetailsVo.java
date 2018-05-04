package com.newaim.purchase.flow.purchase.vo;

import com.newaim.purchase.flow.purchase.entity.FlowPurchaseContractOtherDetail;

import java.io.Serializable;
import java.util.List;

public class FlowPurchaseContractOtherDetailsVo implements Serializable{

    private List<FlowPurchaseContractOtherDetail> otherDetails;

    public List<FlowPurchaseContractOtherDetail> getOtherDetails() {
        return otherDetails;
    }

    public void setOtherDetails(List<FlowPurchaseContractOtherDetail> otherDetails) {
        this.otherDetails = otherDetails;
    }
}
