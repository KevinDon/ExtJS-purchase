package com.newaim.purchase.archives.flow.shipping.vo;
import com.newaim.purchase.archives.flow.shipping.entity.OrderShippingPlanDetail;

import java.io.Serializable;
import java.util.List;

public class OrderShippingPlanDetailsVo implements Serializable{

    private List<OrderShippingPlanDetail> details;

    public List<OrderShippingPlanDetail> getDetails() {
        return details;
    }

    public void setDetails(List<OrderShippingPlanDetail> details) {
        this.details = details;
    }
}
