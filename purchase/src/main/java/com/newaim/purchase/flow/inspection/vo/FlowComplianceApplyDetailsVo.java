package com.newaim.purchase.flow.inspection.vo;

import com.newaim.purchase.flow.inspection.entity.FlowComplianceApplyDetail;

import java.io.Serializable;
import java.util.List;

public class FlowComplianceApplyDetailsVo implements Serializable{

    private List<FlowComplianceApplyDetail> details;

    public List<FlowComplianceApplyDetail> getDetails() {
        return details;
    }

    public void setDetails(List<FlowComplianceApplyDetail> details) {
        this.details = details;
    }
}
