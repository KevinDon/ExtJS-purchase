package com.newaim.purchase.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mark on 2017/12/14.
 */
public class WmsAsnCreateDto implements Serializable{

    /**J17-0123*/
    @JsonProperty("purchase_order_no")
    private String purchaseOrderNo;
    /**WH01*/
    private String warehouse;

    /**2017-09-07 12:59*/
    @JsonProperty("expected_arrive_time_from")
    private String expectedArriveTimeFrom;
    /**2017-09-07 12:59*/
    @JsonProperty("expected_arrive_time_to")
    private String expectedArriveTimeTo;
    /**SUPPLIER TEST*/
    @JsonProperty("supplier_id")
    private String supplierId;
    /**SUPPLIER NAME*/
    @JsonProperty("supplier_name")
    private String supplierName;
    /**Kangshan Industry Zone*/
    @JsonProperty("supplier_address1")
    private String supplierAddress1;
    /**Dipu Town*/
    @JsonProperty("supplier_address2")
    private String supplierAddress2;
    /**Anji County*/
    @JsonProperty("supplier_city")
    private String supplierCity;
    /**HuZhou*/
    @JsonProperty("supplier_province")
    private String supplierProvince;
    /**CN*/
    @JsonProperty("supplier_country")
    private String supplierCountry;
    /**000000*/
    @JsonProperty("supplier_postcode")
    private String supplierPostcode;
    /**John*/
    @JsonProperty("supplier_contact")
    private String supplierContact;
    /**86-567-8876234*/
    @JsonProperty("supplier_tel1")
    private String supplierTel1;
    /**13823456789*/
    @JsonProperty("supplier_tel2")
    private String supplierTel2;
    /**supplier@gmail.com*/
    @JsonProperty("supplier_email")
    private String supplierEmail;
    /**86-567-8876255*/
    @JsonProperty("supplier_fax")
    private String supplierFax;
    /**FSCU1234567*/
    @JsonProperty("container_no")
    private String containerNo;

    /**
     * <pre>
     *     "sku": "MATTRESS-21-S",
     *     "expected_qty": 10,
     *     "cost": 45.2
     * </pre>
     */
    private List<WmsAsnSkuDto> detail = Lists.newArrayList();

    public String getPurchaseOrderNo() {
        return purchaseOrderNo;
    }

    public void setPurchaseOrderNo(String purchaseOrderNo) {
        this.purchaseOrderNo = purchaseOrderNo;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public String getExpectedArriveTimeFrom() {
        return expectedArriveTimeFrom;
    }

    public void setExpectedArriveTimeFrom(String expectedArriveTimeFrom) {
        this.expectedArriveTimeFrom = expectedArriveTimeFrom;
    }

    public String getExpectedArriveTimeTo() {
        return expectedArriveTimeTo;
    }

    public void setExpectedArriveTimeTo(String expectedArriveTimeTo) {
        this.expectedArriveTimeTo = expectedArriveTimeTo;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierAddress1() {
        return supplierAddress1;
    }

    public void setSupplierAddress1(String supplierAddress1) {
        this.supplierAddress1 = supplierAddress1;
    }

    public String getSupplierAddress2() {
        return supplierAddress2;
    }

    public void setSupplierAddress2(String supplierAddress2) {
        this.supplierAddress2 = supplierAddress2;
    }

    public String getSupplierCity() {
        return supplierCity;
    }

    public void setSupplierCity(String supplierCity) {
        this.supplierCity = supplierCity;
    }

    public String getSupplierProvince() {
        return supplierProvince;
    }

    public void setSupplierProvince(String supplierProvince) {
        this.supplierProvince = supplierProvince;
    }

    public String getSupplierCountry() {
        return supplierCountry;
    }

    public void setSupplierCountry(String supplierCountry) {
        this.supplierCountry = supplierCountry;
    }

    public String getSupplierPostcode() {
        return supplierPostcode;
    }

    public void setSupplierPostcode(String supplierPostcode) {
        this.supplierPostcode = supplierPostcode;
    }

    public String getSupplierContact() {
        return supplierContact;
    }

    public void setSupplierContact(String supplierContact) {
        this.supplierContact = supplierContact;
    }

    public String getSupplierTel1() {
        return supplierTel1;
    }

    public void setSupplierTel1(String supplierTel1) {
        this.supplierTel1 = supplierTel1;
    }

    public String getSupplierTel2() {
        return supplierTel2;
    }

    public void setSupplierTel2(String supplierTel2) {
        this.supplierTel2 = supplierTel2;
    }

    public String getSupplierEmail() {
        return supplierEmail;
    }

    public void setSupplierEmail(String supplierEmail) {
        this.supplierEmail = supplierEmail;
    }

    public String getSupplierFax() {
        return supplierFax;
    }

    public void setSupplierFax(String supplierFax) {
        this.supplierFax = supplierFax;
    }

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public List<WmsAsnSkuDto> getDetail() {
        return detail;
    }

    public void setDetail(List<WmsAsnSkuDto> detail) {
        this.detail = detail;
    }
}
