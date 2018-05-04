package com.newaim.purchase.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


public class OmsPackageAddDto {

    /**
     * string,货主代码
     */
    @JsonProperty("owner_code")
    private String ownerCode;

    /**string, 品牌代码,OMS获取*/
    @JsonProperty("brand_code")
    private String brandCode;

    /**
     * string,套餐商品条码
     */
    @JsonProperty("package_sku")
    private String packageSku;

    /**
     * string,套餐商品名称
     */
    @JsonProperty("package_name")
    private String packageName;

    /**
     * string,EAN码
     */
    @JsonProperty("package_ean")
    private String packageEan;

    /**
     * string,有效开始时间，格式为yyyy-MM-dd HH:mm:ss
     */
    @JsonProperty("valid_time_start")
    private String validTimeStart;

    /**
     * string,有效结束时间，格式为yyyy-MM-dd HH:mm:ss
     */
    @JsonProperty("valid_time_end")
    private String validTimeEnd;

    @JsonProperty("is_cainiao")
    private Integer isCainiao;


    private List<OmsPackageSkuDto> skus;

    public Integer getIsCainiao() {
        return isCainiao;
    }

    public void setIsCainiao(Integer isCainiao) {
        this.isCainiao = isCainiao;
    }

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    public String getBrandCode() {
        return brandCode;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }

    public String getPackageSku() {
        return packageSku;
    }

    public void setPackageSku(String packageSku) {
        this.packageSku = packageSku;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageEan() {
        return packageEan;
    }

    public void setPackageEan(String packageEan) {
        this.packageEan = packageEan;
    }

    public String getValidTimeStart() {
        return validTimeStart;
    }

    public void setValidTimeStart(String validTimeStart) {
        this.validTimeStart = validTimeStart;
    }

    public String getValidTimeEnd() {
        return validTimeEnd;
    }

    public void setValidTimeEnd(String validTimeEnd) {
        this.validTimeEnd = validTimeEnd;
    }

    public List<OmsPackageSkuDto> getSkus() {
        return skus;
    }

    public void setSkus(List<OmsPackageSkuDto> skus) {
        this.skus = skus;
    }
}
