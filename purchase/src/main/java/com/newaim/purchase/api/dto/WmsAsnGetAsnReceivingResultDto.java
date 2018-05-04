package com.newaim.purchase.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;


public class WmsAsnGetAsnReceivingResultDto implements Serializable{

    /**
     * ASN1709099
     */
    @JsonProperty("asn_no")
    private String asnNumber;

    /**
     * WH01
     */
    private String warehouse;

    /**
     * J17-0943
     */
    @JsonProperty("purchase_order_no")
    private String purchaseOrderNo;
    /**
     * SUPPLIER TEST
     */
    @JsonProperty("supplier_id")
    private String supplierId;
    /**
     * 2017-09-09
     */
    @JsonProperty("close_date")
    private String close_date;

    @JsonProperty("received_detail")
    private List<WmsAsnReceivedDetailDto> receivedDetail;

    @JsonProperty("exception_detail")
    private List<WmsAsnExceptionDetailDto> exceptionDetail;

    public String getAsnNumber() {
        return asnNumber;
    }

    public void setAsnNumber(String asnNumber) {
        this.asnNumber = asnNumber;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public String getPurchaseOrderNo() {
        return purchaseOrderNo;
    }

    public void setPurchaseOrderNo(String purchaseOrderNo) {
        this.purchaseOrderNo = purchaseOrderNo;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getClose_date() {
        return close_date;
    }

    public void setClose_date(String close_date) {
        this.close_date = close_date;
    }

    public List<WmsAsnReceivedDetailDto> getReceivedDetail() {
        return receivedDetail;
    }

    public void setReceivedDetail(List<WmsAsnReceivedDetailDto> receivedDetail) {
        this.receivedDetail = receivedDetail;
    }

    public List<WmsAsnExceptionDetailDto> getExceptionDetail() {
        return exceptionDetail;
    }

    public void setExceptionDetail(List<WmsAsnExceptionDetailDto> exceptionDetail) {
        this.exceptionDetail = exceptionDetail;
    }
}
