package com.newaim.purchase.archives.flow.purchase.vo;
import com.newaim.purchase.archives.product.vo.ProductVo;
import com.newaim.purchase.config.json.JsonMoney;

import java.io.Serializable;
import java.math.BigDecimal;

public class CustomClearancePackingDetailVo implements Serializable{

    private String id;

    private String packingId;
    private String productId;
    private ProductVo product;

    private String orderTitle;
    private String orderIndex;//批次号
    private String orderNumber;
    /**订单质检状态*/
    private Integer flagOrderQcStatus;

    private String vendorCnName;
    private String vendorEnName;
    private String serviceProviderCnName;
    private String serviceProviderEnName;

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
    /**单位体积*/
    private BigDecimal unitCbm;
    /**合计体积*/
    private BigDecimal totalCbm;

    /**结算币种、单价（AUD/RMB/USD）、汇率*/
    private Integer currency;
    private BigDecimal priceAud;
    private BigDecimal priceRmb;
    private BigDecimal priceUsd;
    @JsonMoney
    private BigDecimal rateAudToRmb;
    @JsonMoney
    private BigDecimal rateAudToUsd;
    /**总净重*/
    private BigDecimal totalNw;
    /**总毛重*/
    private BigDecimal totalGw;

    private BigDecimal priceCostAud;
    private BigDecimal priceCostRmb;
    private BigDecimal priceCostUsd;

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

    public String getOrderTitle() {
        return orderTitle;
    }

    public void setOrderTitle(String orderTitle) {
        this.orderTitle = orderTitle;
    }

    public Integer getFlagOrderQcStatus() {
        return flagOrderQcStatus;
    }

    public void setFlagOrderQcStatus(Integer flagOrderQcStatus) {
        this.flagOrderQcStatus = flagOrderQcStatus;
    }

    public String getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(String orderIndex) {
        this.orderIndex = orderIndex;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public ProductVo getProduct() {
        return product;
    }

    public void setProduct(ProductVo product) {
        this.product = product;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getVendorCnName() {
        return vendorCnName;
    }

    public void setVendorCnName(String vendorCnName) {
        this.vendorCnName = vendorCnName;
    }

    public String getVendorEnName() {
        return vendorEnName;
    }

    public void setVendorEnName(String vendorEnName) {
        this.vendorEnName = vendorEnName;
    }

    public String getServiceProviderCnName() {
        return serviceProviderCnName;
    }

    public void setServiceProviderCnName(String serviceProviderCnName) {
        this.serviceProviderCnName = serviceProviderCnName;
    }

    public String getServiceProviderEnName() {
        return serviceProviderEnName;
    }

    public void setServiceProviderEnName(String serviceProviderEnName) {
        this.serviceProviderEnName = serviceProviderEnName;
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

    public BigDecimal getPriceCostAud() {
        return priceCostAud;
    }

    public void setPriceCostAud(BigDecimal priceCostAud) {
        this.priceCostAud = priceCostAud;
    }

    public BigDecimal getPriceCostRmb() {
        return priceCostRmb;
    }

    public void setPriceCostRmb(BigDecimal priceCostRmb) {
        this.priceCostRmb = priceCostRmb;
    }

    public BigDecimal getPriceCostUsd() {
        return priceCostUsd;
    }

    public void setPriceCostUsd(BigDecimal priceCostUsd) {
        this.priceCostUsd = priceCostUsd;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }
}
