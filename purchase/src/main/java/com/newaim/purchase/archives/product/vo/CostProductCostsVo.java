package com.newaim.purchase.archives.product.vo;

import com.google.common.collect.Lists;
import com.newaim.purchase.archives.product.entity.CostProductCost;

import java.io.Serializable;
import java.util.List;


public class CostProductCostsVo implements Serializable{

    private List<CostProductCost> costProductCosts = Lists.newArrayList();

    public List<CostProductCost> getCostProductCosts() {
        return costProductCosts;
    }

    public void setCostProductCosts(List<CostProductCost> costProductCosts) {
        this.costProductCosts = costProductCosts;
    }
}
