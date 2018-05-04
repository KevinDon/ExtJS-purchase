package com.newaim.purchase.flow.finance.vo;

import com.newaim.purchase.flow.finance.entity.FlowBalanceRefundDetail;

import java.io.Serializable;
import java.util.List;

public class FlowBalanceRefundDetailsVo implements Serializable{

    private List<FlowBalanceRefundDetail> details;

    public List<FlowBalanceRefundDetail> getDetails() {
        return details;
    }

    public void setDetails(List<FlowBalanceRefundDetail> details) {
        this.details = details;
    }
}
