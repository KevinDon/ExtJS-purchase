package com.newaim.purchase.archives.product.vo;

import com.google.common.collect.Lists;
import com.newaim.purchase.archives.product.entity.CostChargeItem;

import java.io.Serializable;
import java.util.List;


public class CostChargeItemsVo implements Serializable{

    private List<CostChargeItem> costChargeItems = Lists.newArrayList();

    public List<CostChargeItem> getCostChargeItems() {
        return costChargeItems;
    }

    public void setCostChargeItems(List<CostChargeItem> costChargeItems) {
        this.costChargeItems = costChargeItems;
    }
}
