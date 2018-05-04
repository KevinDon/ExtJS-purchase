package com.newaim.purchase.flow.shipping.vo;


import com.google.common.collect.Lists;
import com.newaim.purchase.flow.shipping.entity.FlowServiceProviderQuotationChargeItem;

import java.io.Serializable;
import java.util.List;

public class FlowServiceProviderQuotationChargeItemsVo implements Serializable{

    private List<FlowServiceProviderQuotationChargeItem> chargeItems = Lists.newArrayList();

    public List<FlowServiceProviderQuotationChargeItem> getChargeItems() {
        return chargeItems;
    }

    public void setChargeItems(List<FlowServiceProviderQuotationChargeItem> chargeItems) {
        this.chargeItems = chargeItems;
    }
}
