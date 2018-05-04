package com.newaim.purchase.flow.workflow.entity;

import java.io.Serializable;

/**
 * 流程通用供应商接口
 * Created by Mark on 2017/11/2.
 */
public interface FlowServiceProviderObject extends FlowObject{

    /**
     * 获取服务商id
     * @return 服务商id
     */
    String getServiceProviderId();

    /**
     * 设置服务商id
     * @param serviceProviderId 服务商id
     */
    void setServiceProviderId(String serviceProviderId);

    /**
     * 获取服务商中文名
     * @return 服务商中文名
     */
    String getServiceProviderCnName();

    /**
     * 服务商中文名
     * @param serviceProviderCnName 服务商中文名
     */
    void setServiceProviderCnName(String serviceProviderCnName);

    /**
     * 获取服务商英文名
     * @return 服务商英文名
     */
    String getServiceProviderEnName();

    /**
     * 设置服务商英文名
     * @param serviceProviderEnName 服务商英文名
     */
    void setServiceProviderEnName(String serviceProviderEnName);
}
