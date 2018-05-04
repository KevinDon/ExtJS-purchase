package com.newaim.purchase.archives.product.entity;

import com.newaim.core.utils.SessionUtils;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Mark on 2017/9/15.
 */
@Entity
@Table(name = "na_product")
public class Product implements Serializable{

    /**
     * 新品
     */
    public static final Integer NEW_PRODUCT = 1;

    /**
     * 正常产品
     */
    public static final Integer NORMAL_PRODUCT = 2;

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.newaim.core.jpa.IdGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name = "prefix", value = "PDT")})
    private String id;
    private String sku;
    private String name;
    private String packageName;
    private String barcode;
    private String ean;
    private Integer combined;

    private String categoryId;
    private String categoryCnName;
    private String categoryEnName;

    private String color;
    private String model;
    private String style;
    private BigDecimal length;
    private BigDecimal width;
    private BigDecimal height;

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

    private Integer status;

    @OneToOne(optional = true, cascade = CascadeType.ALL, mappedBy = "product")
    @JoinColumn(name = "id", referencedColumnName = "product_id")
    private ProductVendorProp prop;

    private Integer isSync;
    private Date syncTime;

    private String creatorId;
    private String creatorCnName;
    private String creatorEnName;

    private Date createdAt;
    private Date updatedAt;

    private String departmentId;
    private String departmentCnName;
    private String departmentEnName;

    private Integer flagFirst;
    private Integer qcIndex;

    private Integer purchaseType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    @Transient
    public String getCategoryName() {
        if(SessionUtils.isCnLang()){
            return  this.categoryCnName;
        }else if(SessionUtils.isEnLang()){
            return this.categoryEnName;
        }
        return null;
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

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public ProductVendorProp getProp() {
        return prop;
    }

    public void setProp(ProductVendorProp prop) {
        this.prop = prop;
    }

    public Integer getIsSync() {
        return isSync;
    }

    public void setIsSync(Integer isSync) {
        this.isSync = isSync;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
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

    @Transient
    public String getCreatorName() {
        if(SessionUtils.isCnLang()){
            return  this.creatorCnName;
        }else if(SessionUtils.isEnLang()){
            return this.creatorEnName;
        }
        return null;
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

    public Integer getFlagFirst() {
        return flagFirst;
    }

    public void setFlagFirst(Integer flagFirst) {
        this.flagFirst = flagFirst;
    }

    public Integer getQcIndex() {
        return qcIndex;
    }

    public void setQcIndex(Integer qcIndex) {
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

    @Transient
    public String getDepartmentName() {
        if(SessionUtils.isCnLang()){
            return  this.departmentCnName;
        }else if(SessionUtils.isEnLang()){
            return this.departmentEnName;
        }
        return null;
    }
}
