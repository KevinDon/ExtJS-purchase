package com.newaim.purchase.flow.workflow.entity;

/**
 * 流程通用供应商接口
 * Created by Mark on 2017/11/2.
 */
public interface FlowVendorObject extends FlowObject{

    /**
     * 获取供应商id
     * @return 供应商id
     */
    String getVendorId();

    /**
     * 设置供应商id
     * @param vendorId 供应商id
     */
    void setVendorId(String vendorId);

    /**
     * 获取供应商中文名
     * @return 供应商中文名
     */
    String getVendorCnName();

    /**
     * 供应商中文名
     * @param vendorCnName 供应商中文名
     */
    void setVendorCnName(String vendorCnName);

    /**
     * 获取供应商英文名
     * @return 供应商英文名
     */
    String getVendorEnName();

    /**
     * 设置供应商英文名
     * @param vendorEnName 供应商英文名
     */
    void setVendorEnName(String vendorEnName);
}
