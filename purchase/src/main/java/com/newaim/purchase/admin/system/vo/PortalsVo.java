package com.newaim.purchase.admin.system.vo;

import com.google.common.collect.Lists;
import com.newaim.purchase.admin.system.entity.Portal;

import java.io.Serializable;
import java.util.List;

public class PortalsVo implements Serializable{

	private List<Portal> items = Lists.newArrayList();

    public List<Portal> getItems() {
        return items;
    }

    public void setItems(List<Portal> items) {
        this.items = items;
    }
}