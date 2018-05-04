package com.newaim.purchase.archives.product.vo;

import com.google.common.collect.Lists;
import com.newaim.purchase.config.json.JsonMoney;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


public class ProductCombinedVo implements Serializable{

    private String id;

    private String combinedSku;
    private String combinedName;
    private String ean;
    private String barcode;
    private String categoryId;
    private String categoryCnName;
    private String categoryEnName;
    private String categoryName;
    private Integer currency;
    /**单价、汇率*/
    private BigDecimal priceAud;
    private BigDecimal priceRmb;
    private BigDecimal priceUsd;
    @JsonMoney
    private BigDecimal rateAudToRmb;
    @JsonMoney
    private BigDecimal rateAudToUsd;
    private Integer status;
    private Integer comboType;

    private String creatorId;
    private String creatorName;
    private String creatorCnName;
    private String creatorEnName;

    private Date createdAt;
    private Date updatedAt;

    private String departmentId;
    private String departmentName;
    private String departmentCnName;
    private String departmentEnName;

    private Integer flagSyncStatus;//是否同步
    private Date flagSyncDate;//同步时间

    //组合产品的详细信息
    private List<ProductCombinedDetailVo> details = Lists.newArrayList();

    public Integer getFlagSyncStatus() {
        return flagSyncStatus;
    }

    public void setFlagSyncStatus(Integer flagSyncStatus) {
        this.flagSyncStatus = flagSyncStatus;
    }

    public Date getFlagSyncDate() {
        return flagSyncDate;
    }

    public void setFlagSyncDate(Date flagSyncDate) {
        this.flagSyncDate = flagSyncDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCombinedSku() {
        return combinedSku;
    }

    public void setCombinedSku(String combinedSku) {
        this.combinedSku = combinedSku;
    }

    public String getCombinedName() {
        return combinedName;
    }

    public void setCombinedName(String combinedName) {
        this.combinedName = combinedName;
    }

    public String getEan() {
        return ean;
    }

    public void setEan(String ean) {
        this.ean = ean;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryCnName() {
        return categoryCnName;
    }

    public void setCategoryCnName(String categoryCnName) {
        this.categoryCnName = categoryCnName;
    }

    public String getCategoryEnName() {
        return categoryEnName;
    }

    public void setCategoryEnName(String categoryEnName) {
        this.categoryEnName = categoryEnName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getComboType() {
        return comboType;
    }

    public void setComboType(Integer comboType) {
        this.comboType = comboType;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getCurrency() {
        return currency;
    }

    public void setCurrency(Integer currency) {
        this.currency = currency;
    }

    public BigDecimal getPriceAud() {
        return priceAud;
    }

    public void setPriceAud(BigDecimal priceAud) {
        this.priceAud = priceAud;
    }

    public BigDecimal getPriceRmb() {
        return priceRmb;
    }

    public void setPriceRmb(BigDecimal priceRmb) {
        this.priceRmb = priceRmb;
    }

    public BigDecimal getPriceUsd() {
        return priceUsd;
    }

    public void setPriceUsd(BigDecimal priceUsd) {
        this.priceUsd = priceUsd;
    }

    public BigDecimal getRateAudToRmb() {
        return rateAudToRmb;
    }

    public void setRateAudToRmb(BigDecimal rateAudToRmb) {
        this.rateAudToRmb = rateAudToRmb;
    }

    public BigDecimal getRateAudToUsd() {
        return rateAudToUsd;
    }

    public void setRateAudToUsd(BigDecimal rateAudToUsd) {
        this.rateAudToUsd = rateAudToUsd;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreatorCnName() {
        return creatorCnName;
    }

    public void setCreatorCnName(String creatorCnName) {
        this.creatorCnName = creatorCnName;
    }

    public String getCreatorEnName() {
        return creatorEnName;
    }

    public void setCreatorEnName(String creatorEnName) {
        this.creatorEnName = creatorEnName;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentCnName() {
        return departmentCnName;
    }

    public void setDepartmentCnName(String departmentCnName) {
        this.departmentCnName = departmentCnName;
    }

    public String getDepartmentEnName() {
        return departmentEnName;
    }

    public void setDepartmentEnName(String departmentEnName) {
        this.departmentEnName = departmentEnName;
    }

    public List<ProductCombinedDetailVo> getDetails() {
        return details;
    }

    public void setDetails(List<ProductCombinedDetailVo> details) {
        this.details = details;
    }
}
