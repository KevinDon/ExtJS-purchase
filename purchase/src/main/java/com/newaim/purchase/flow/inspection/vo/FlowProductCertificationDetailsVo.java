package com.newaim.purchase.flow.inspection.vo;


import com.newaim.purchase.flow.inspection.entity.FlowProductCertificationDetail;

import java.io.Serializable;
import java.util.List;

public class FlowProductCertificationDetailsVo implements Serializable{

    private List<FlowProductCertificationDetail> details;

    public List<FlowProductCertificationDetail> getDetails() {
        return details;
    }

    public void setDetails(List<FlowProductCertificationDetail> details) {
        this.details = details;
    }
}
