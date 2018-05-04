package com.newaim.purchase.archives.service_provider.vo;

import com.google.common.collect.Lists;
import com.newaim.purchase.archives.service_provider.entity.ServiceProviderPort;

import java.io.Serializable;
import java.util.List;

public class ServiceProviderPortsVo implements Serializable{

    private List<ServiceProviderPort> ports = Lists.newArrayList();

    public List<ServiceProviderPort> getPorts() {
        return ports;
    }

    public void setPorts(List<ServiceProviderPort> ports) {
        this.ports = ports;
    }
}
