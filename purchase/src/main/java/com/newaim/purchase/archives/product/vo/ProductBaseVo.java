package com.newaim.purchase.archives.product.vo;

import com.newaim.purchase.config.json.JsonMoney;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Lance on 2017/10/28
 */
public class ProductBaseVo implements Serializable {

    private String id;
    private String sku;
    private String name;
    private String packageName;
    private String barcode;
    private String ean;
    private Integer combined;
    private String categoryId;
    private String categoryName;
    private String color;
    private String model;
    private String style;
    private BigDecimal length;
    private BigDecimal width;
    private BigDecimal height;
    @JsonMoney
    private BigDecimal cbm;
    private BigDecimal cubicWeight;
    private BigDecimal netWeight;
    private String seasonal;
    private String indoorOutdoor;
    private Integer electricalProduct;
    private String powerRequirements;
    private Integer mandatory;
    private Integer newProduct;
    private String ageLimit;
    private Integer isSync;
    private Date syncTime;
    private Integer flagFirst;
    private Integer status;

    private String creatorId;
    private String creatorName;
    private String creatorCnName;
    private String creatorEnName;

    private Date createdAt;
    private Date updatedAt;//更新时间

    private String departmentId;
    private String departmentName;
    private String departmentCnName;
    private String departmentEnName;
    private Long qcIndex;
    private Integer purchaseType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getEan() {
        return ean;
    }

    public void setEan(String ean) {
        this.ean = ean;
    }

    public Integer getCombined() {
        return combined;
    }

    public void setCombined(Integer combined) {
        this.combined = combined;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public BigDecimal getLength() {
        return length;
    }

    public void setLength(BigDecimal length) {
        this.length = length;
    }

    public BigDecimal getWidth() {
        return width;
    }

    public void setWidth(BigDecimal width) {
        this.width = width;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public BigDecimal getCbm() {
        return cbm;
    }

    public void setCbm(BigDecimal cbm) {
        this.cbm = cbm;
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

    public BigDecimal getCubicWeight() {
        return cubicWeight;
    }

    public void setCubicWeight(BigDecimal cubicWeight) {
        this.cubicWeight = cubicWeight;
    }

    public BigDecimal getNetWeight() {
        return netWeight;
    }

    public void setNetWeight(BigDecimal netWeight) {
        this.netWeight = netWeight;
    }

    public String getSeasonal() {
        return seasonal;
    }

    public void setSeasonal(String seasonal) {
        this.seasonal = seasonal;
    }

    public String getIndoorOutdoor() {
        return indoorOutdoor;
    }

    public void setIndoorOutdoor(String indoorOutdoor) {
        this.indoorOutdoor = indoorOutdoor;
    }

    public Integer getElectricalProduct() {
        return electricalProduct;
    }

    public void setElectricalProduct(Integer electricalProduct) {
        this.electricalProduct = electricalProduct;
    }

    public String getPowerRequirements() {
        return powerRequirements;
    }

    public void setPowerRequirements(String powerRequirements) {
        this.powerRequirements = powerRequirements;
    }

    public Integer getMandatory() {
        return mandatory;
    }

    public void setMandatory(Integer mandatory) {
        this.mandatory = mandatory;
    }

    public Integer getNewProduct() {
        return newProduct;
    }

    public void setNewProduct(Integer newProduct) {
        this.newProduct = newProduct;
    }

    public String getAgeLimit() {
        return ageLimit;
    }

    public void setAgeLimit(String ageLimit) {
        this.ageLimit = ageLimit;
    }

    public Integer getIsSync() {
        return isSync;
    }

    public void setIsSync(Integer isSync) {
        this.isSync = isSync;
    }

    public Integer getFlagFirst() {
        return flagFirst;
    }

    public void setFlagFirst(Integer flagFirst) {
        this.flagFirst = flagFirst;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
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

    public Long getQcIndex() {
        return qcIndex;
    }

    public void setQcIndex(Long qcIndex) {
        this.qcIndex = qcIndex;
    }

    public Date getSyncTime() {
        return syncTime;
    }

    public void setSyncTime(Date syncTime) {
        this.syncTime = syncTime;
    }

    public Integer getPurchaseType() {
        return purchaseType;
    }

    public void setPurchaseType(Integer purchaseType) {
        this.purchaseType = purchaseType;
    }
}
