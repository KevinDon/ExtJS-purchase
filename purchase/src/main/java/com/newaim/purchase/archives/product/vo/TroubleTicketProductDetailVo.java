package com.newaim.purchase.archives.product.vo;

import java.io.Serializable;

public class TroubleTicketProductDetailVo implements Serializable{

    private String id;

    private String troubleTicketId;

    private String troubleTicketProductId;

    private String productId;

    private String sku;

    private String troubleDetailId;

    private String codeMain;

    private String codeSub;

    private Integer qty;

    private Integer checked;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTroubleTicketId() {
        return troubleTicketId;
    }

    public void setTroubleTicketId(String troubleTicketId) {
        this.troubleTicketId = troubleTicketId;
    }

    public String getTroubleTicketProductId() {
        return troubleTicketProductId;
    }

    public void setTroubleTicketProductId(String troubleTicketProductId) {
        this.troubleTicketProductId = troubleTicketProductId;
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

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }
}
