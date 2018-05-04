package com.newaim.purchase.desktop.reports.vo;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;


public class ProductReportVo implements Serializable{

    private String id;

    private String productId;

    private List<ReportsVo> reports = Lists.newArrayList();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public List<ReportsVo> getReports() {
        return reports;
    }

    public void setReports(List<ReportsVo> reports) {
        this.reports = reports;
    }
}
