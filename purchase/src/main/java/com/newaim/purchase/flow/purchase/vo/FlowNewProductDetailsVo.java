package com.newaim.purchase.flow.purchase.vo;

import com.newaim.purchase.flow.purchase.entity.FlowNewProductDetail;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mark on 2017/10/20.
 */
public class FlowNewProductDetailsVo implements Serializable{

    private List<FlowNewProductDetail> details;

    public List<FlowNewProductDetail> getDetails() {
        return details;
    }

    public void setDetails(List<FlowNewProductDetail> details) {
        this.details = details;
    }
}
