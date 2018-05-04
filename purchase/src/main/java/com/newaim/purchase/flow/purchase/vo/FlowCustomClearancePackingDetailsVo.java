package com.newaim.purchase.flow.purchase.vo;

import com.newaim.purchase.flow.purchase.entity.FlowCustomClearancePackingDetail;

import java.io.Serializable;
import java.util.List;

public class FlowCustomClearancePackingDetailsVo implements Serializable{

    private List<FlowCustomClearancePackingDetail> packingList;

    public List<FlowCustomClearancePackingDetail> getPackingList() {
        return packingList;
    }

    public void setPackingList(List<FlowCustomClearancePackingDetail> packingList) {
        this.packingList = packingList;
    }
}

