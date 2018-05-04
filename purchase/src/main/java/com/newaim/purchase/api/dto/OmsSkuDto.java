package com.newaim.purchase.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Maps;
import com.newaim.purchase.config.json.JsonMoney;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by Mark on 2017/12/13.
 */
public class OmsSkuDto implements Serializable {

    /**string,条码*/
    private String sku;
    /**string,副条码,多个用英文逗号隔开*/
    @JsonProperty("slave_skus")
    private String slaveSkus;
    /**string,EAN码*/
    private String ean;
    /**double,重量*/
    private String weight;
    /**double,成本价*/
    @JsonProperty("cost_price")
    private BigDecimal costPrice;
    /**double,市场价*/
    @JsonProperty("market_price")
    private BigDecimal marketPrice;
    /**double,体积*/
    @JsonMoney
    private BigDecimal volume;
    /**double,长*/
    private BigDecimal length;
    /**double,宽*/
    private BigDecimal width;
    /**double,高*/
    private BigDecimal height;
    /**double,启用/禁用,0-启用 1-禁用,默认启用*/
    private Integer disabled;
    /**
     *<pre>
     *      "sku_attr_1": "string,属性值1",
     *      "sku_attr_2": "string,属性值2",
     *      "sku_attr_3": "string,属性值3",
     *      "sku_attr_4": "string,属性值4",
     *      "sku_attr_5": "string,属性值5",
     *      "sku_attr_6": "string,属性值6",
     *      "sku_attr_7": "string,属性值7",
     *      "sku_attr_8": "string,属性值8",
     *      "sku_attr_9": "string,属性值9",
     *      "sku_attr_10": "string,属性值10"
     *</pre>
     */
    @JsonProperty("sku_attrs")
    private Map<String,String> skuAttrs = Maps.newHashMap();

    /**
     * <pre>
     *      "field_1": "string,自定义字段1",
     *      "field_2": "string,自定义字段2",
     *      "field_n": "string,自定义字段n"
     * </pre>
     */
    @JsonProperty("extends")
    private Map<String, String> _extends = Maps.newHashMap();

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getSlaveSkus() {
        return slaveSkus;
    }

    public void setSlaveSkus(String slaveSkus) {
        this.slaveSkus = slaveSkus;
    }

    public String getEan() {
        return ean;
    }

    public void setEan(String ean) {
        this.ean = ean;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
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

    public Integer getDisabled() {
        return disabled;
    }

    public void setDisabled(Integer disabled) {
        this.disabled = disabled;
    }

    public Map<String, String> getSkuAttrs() {
        return skuAttrs;
    }

    public void setSkuAttrs(Map<String, String> skuAttrs) {
        this.skuAttrs = skuAttrs;
    }

    public Map<String, String> get_extends() {
        return _extends;
    }

    public void set_extends(Map<String, String> _extends) {
        this._extends = _extends;
    }
}
