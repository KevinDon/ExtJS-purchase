package com.newaim.purchase.flow.shipping.vo;

import com.newaim.purchase.flow.shipping.entity.FlowWarehousePlanDetail;

import java.io.Serializable;
import java.util.List;

public class FlowWarehousePlanDetailsVo implements Serializable{

    private List<FlowWarehousePlanDetail> details;

    public List<FlowWarehousePlanDetail> getDetails() {
        return details;
    }

    public void setDetails(List<FlowWarehousePlanDetail> details) {
        this.details = details;
    }
}
