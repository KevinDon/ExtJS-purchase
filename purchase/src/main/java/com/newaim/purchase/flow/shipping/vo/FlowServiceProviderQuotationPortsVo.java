package com.newaim.purchase.flow.shipping.vo;

import com.google.common.collect.Lists;
import com.newaim.purchase.flow.shipping.entity.FlowServiceProviderQuotationPort;

import java.io.Serializable;
import java.util.List;


public class FlowServiceProviderQuotationPortsVo implements Serializable{

    private List<FlowServiceProviderQuotationPort> ports = Lists.newArrayList();

    public List<FlowServiceProviderQuotationPort> getPorts() {
        return ports;
    }

    public void setPorts(List<FlowServiceProviderQuotationPort> ports) {
        this.ports = ports;
    }
}
