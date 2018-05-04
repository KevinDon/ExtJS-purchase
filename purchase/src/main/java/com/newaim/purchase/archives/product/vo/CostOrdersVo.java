package com.newaim.purchase.archives.product.vo;

import com.google.common.collect.Lists;
import com.newaim.purchase.archives.product.entity.CostOrder;
import com.newaim.purchase.archives.product.entity.CostProductCost;

import java.io.Serializable;
import java.util.List;


public class CostOrdersVo implements Serializable{



	private List<CostOrder> costOrders = Lists.newArrayList();

	public List<CostOrder> getCostOrders() {
		return costOrders;
	}

	public void setCostOrders(List<CostOrder> costOrders) {
		this.costOrders = costOrders;
	}
    
}
