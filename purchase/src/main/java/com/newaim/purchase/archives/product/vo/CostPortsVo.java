package com.newaim.purchase.archives.product.vo;

import com.google.common.collect.Lists;
import com.newaim.purchase.archives.product.entity.CostPort;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mark on 2017/12/12.
 */
public class CostPortsVo implements Serializable {

    private List<CostPort> costPorts = Lists.newArrayList();

    public List<CostPort> getCostPorts() {
        return costPorts;
    }

    public void setCostPorts(List<CostPort> costPorts) {
        this.costPorts = costPorts;
    }
}
