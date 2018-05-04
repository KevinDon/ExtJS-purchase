package com.newaim.purchase.archives.service_provider.vo;

import com.google.common.collect.Lists;
import com.newaim.purchase.archives.service_provider.entity.ServiceProviderChargeItem;

import java.io.Serializable;
import java.util.List;

public class ServiceProviderChargeItemsVo implements Serializable{

    private List<ServiceProviderChargeItem> chargeItems = Lists.newArrayList();

    public List<ServiceProviderChargeItem> getChargeItems() {
        return chargeItems;
    }

    public void setChargeItems(List<ServiceProviderChargeItem> chargeItems) {
        this.chargeItems = chargeItems;
    }
}
