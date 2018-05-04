package com.newaim.purchase.flow.workflow.entity;

/**
 * 流程通用供应商接口
 * Created by Mark on 2017/11/2.
 */
public interface FlowVendorWithCurrencyObject extends FlowVendorObject{

    /**
     * 获取交易币种
     * @return 交易币种
     */
    Integer getCurrency();

    /**
     * 设置交易币种
     * @param currency 交易币种
     */
    void setCurrency(Integer currency);
}
