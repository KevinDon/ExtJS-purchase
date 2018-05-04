package com.newaim.purchase.archives.product.vo;

import com.google.common.collect.Lists;
import com.newaim.purchase.archives.product.entity.CostTariff;

import java.io.Serializable;
import java.util.List;

public class CostTariffsVo implements Serializable{

    private List<CostTariff> costTariffs = Lists.newArrayList();

    public List<CostTariff> getCostTariffs() {
        return costTariffs;
    }

    public void setCostTariffs(List<CostTariff> costTariffs) {
        this.costTariffs = costTariffs;
    }
}
