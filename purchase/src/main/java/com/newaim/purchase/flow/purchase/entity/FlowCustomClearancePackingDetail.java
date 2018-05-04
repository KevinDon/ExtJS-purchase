package com.newaim.purchase.flow.purchase.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "na_flow_custom_clearance_packing_detail")
public class FlowCustomClearancePackingDetail implements Serializable{

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.newaim.core.jpa.IdGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name = "prefix", value = "FCCPD")})
    private String id;

    @Column(name = "packing_id")
    private String packingId;

    private String productId;

    /**产品编码*/
    private String sku;
    /**工厂号*/
    private String factoryCode;
    /**条码*/
    private String barcode;
    /**颜色*/
    private String color;
    /**型号*/
    private String size;
    /**规格*/
    private String style;
    /**采购数量（应收）*/
    private Integer orderQty;
    /**应收箱数*/
    private Integer cartons;
    /**装箱件数*/
    private Integer packingQty;
    /**装箱箱数*/
    private Integer packingCartons;
    /**编辑前装箱件数*/
    @Transient
    private Integer srcPackingQty;
    /**编辑前装箱件数*/
    @Transient
    private Integer srcPackingCartons;

    /**单位体积*/
    private BigDecimal unitCbm;
    /**合计体积*/
    private BigDecimal totalCbm;

    /**结算币种*/
    private Integer currency;
    /**单价（AUD）*/
    private BigDecimal priceAud;
    /**单价（RMB）*/
    private BigDecimal priceRmb;
    /**单价（USD）*/
    private BigDecimal priceUsd;
    /**RMB汇率*/
    private BigDecimal rateAudToRmb;
    /**USD汇率*/
    private BigDecimal rateAudToUsd;

    /**总净重*/
    private BigDecimal totalNw;
    /**总毛重*/
    private BigDecimal totalGw;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPackingId() {
        return packingId;
    }

    public void setPackingId(String packingId) {
        this.packingId = packingId;
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

    public String getFactoryCode() {
        return factoryCode;
    }

    public void setFactoryCode(String factoryCode) {
        this.factoryCode = factoryCode;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public Integer getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(Integer orderQty) {
        this.orderQty = orderQty;
    }

    public Integer getCartons() {
        return cartons;
    }

    public void setCartons(Integer cartons) {
        this.cartons = cartons;
    }

    public Integer getPackingQty() {
        return packingQty;
    }

    public void setPackingQty(Integer packingQty) {
        this.packingQty = packingQty;
    }

    public Integer getPackingCartons() {
        return packingCartons;
    }

    public void setPackingCartons(Integer packingCartons) {
        this.packingCartons = packingCartons;
    }

    public BigDecimal getUnitCbm() {
        return unitCbm;
    }

    public void setUnitCbm(BigDecimal unitCbm) {
        this.unitCbm = unitCbm;
    }

    public BigDecimal getTotalCbm() {
        return totalCbm;
    }

    public void setTotalCbm(BigDecimal totalCbm) {
        this.totalCbm = totalCbm;
    }


    public BigDecimal getTotalNw() {
        return totalNw;
    }

    public void setTotalNw(BigDecimal totalNw) {
        this.totalNw = totalNw;
    }

    public BigDecimal getTotalGw() {
        return totalGw;
    }

    public void setTotalGw(BigDecimal totalGw) {
        this.totalGw = totalGw;
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

    public Integer getSrcPackingQty() {
        return srcPackingQty;
    }

    public void setSrcPackingQty(Integer srcPackingQty) {
        this.srcPackingQty = srcPackingQty;
    }

    public Integer getSrcPackingCartons() {
        return srcPackingCartons;
    }

    public void setSrcPackingCartons(Integer srcPackingCartons) {
        this.srcPackingCartons = srcPackingCartons;
    }
}
