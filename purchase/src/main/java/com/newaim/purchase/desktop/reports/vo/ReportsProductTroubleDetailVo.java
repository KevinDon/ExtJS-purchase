package com.newaim.purchase.desktop.reports.vo;


public class ReportsProductTroubleDetailVo {

    private String id;

    private String reportsId;

    private String reportsProductId;

    private String productId;

    private String sku;

    private String troubleDetailId;

    private String codeMain;

    private String codeSub;

    private Integer checked;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReportsId() {
        return reportsId;
    }

    public void setReportsId(String reportsId) {
        this.reportsId = reportsId;
    }

    public String getReportsProductId() {
        return reportsProductId;
    }

    public void setReportsProductId(String reportsProductId) {
        this.reportsProductId = reportsProductId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getTroubleDetailId() {
        return troubleDetailId;
    }

    public void setTroubleDetailId(String troubleDetailId) {
        this.troubleDetailId = troubleDetailId;
    }

    public String getCodeMain() {
        return codeMain;
    }

    public void setCodeMain(String codeMain) {
        this.codeMain = codeMain;
    }

    public String getCodeSub() {
        return codeSub;
    }

    public void setCodeSub(String codeSub) {
        this.codeSub = codeSub;
    }

    public Integer getChecked() {
        return checked;
    }

    public void setChecked(Integer checked) {
        this.checked = checked;
    }
}
