package com.newaim.purchase.flow.shipping.vo;

import com.newaim.purchase.flow.shipping.entity.FlowAsnPackingDetail;

import java.io.Serializable;
import java.util.List;

public class FlowAsnPackingDetailsVo implements Serializable{

    private List<FlowAsnPackingDetail> packingList;

    public List<FlowAsnPackingDetail> getPackingList() {
        return packingList;
    }

    public void setPackingList(List<FlowAsnPackingDetail> packingList) {
        this.packingList = packingList;
    }
}
