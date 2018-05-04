package com.newaim.purchase.archives.flow.shipping.vo;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;

public class SpQuotationForOrderPlansVo implements Serializable{

    private List<SpQuotationForOrderPlanVo> queryParams = Lists.newArrayList();

    public List<SpQuotationForOrderPlanVo> getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(List<SpQuotationForOrderPlanVo> queryParams) {
        this.queryParams = queryParams;
    }

}
