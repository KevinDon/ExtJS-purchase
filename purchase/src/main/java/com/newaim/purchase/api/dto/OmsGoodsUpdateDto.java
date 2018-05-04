package com.newaim.purchase.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.newaim.purchase.config.json.JsonMoney;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Mark
 * @date 2017/12/12
 */
public class OmsGoodsUpdateDto implements Serializable{

    /**商品id*/
    @JsonProperty("goods_id")
    private String goodsId;
    /**货主代码*/
    @JsonProperty("owner_code")
    private String ownerCode;
    /**string, 货号*/
    @JsonProperty("goods_no")
    private String goodsNo;
    /**string, 商品名称*/
    @JsonProperty("goods_name")
    private String goodsName;
    /**string, 商品英文名称*/
    @JsonProperty("goods_name_english")
    private String goodsNameEnglish;
    /**double, 重量*/
    @JsonProperty("weight")
    private BigDecimal goodsWeight;
    /**double, 成本价*/
    @JsonProperty("cost_price")
    private BigDecimal costPrice;
    /**double, 市场价*/
    @JsonProperty("market_price")
    private BigDecimal marketPrice;
    /**double, 体积*/
    @JsonMoney
    private BigDecimal volume;
    /**double, 长*/
    private BigDecimal length;
    /**double,宽*/
    private BigDecimal width;
    /**double,高*/
    private BigDecimal height;
    /**long,是否检测属性值, 0不检测，如果不存在就新增属性值，1检测，如果属性值不存在就报错，默认是1*/
    @JsonProperty("attr_val_check")
    private Integer attrValCheck;
    /**
     * <pre>
     *      商品属性值:
     *      "goods_attr_1": "string,属性值1",
     *      "goods_attr_2": "string,属性值2",
     *      "goods_attr_3": "string,属性值3",
     *      "goods_attr_4": "string,属性值4",
     *      "goods_attr_5": "string,属性值5",
     *      "goods_attr_6": "string,属性值6",
     *      "goods_attr_7": "string,属性值7",
     *      "goods_attr_8": "string,属性值8",
     *      "goods_attr_9": "string,属性值9",
     *      "goods_attr_10": "string,属性值10"
     * </pre>
     */
    @JsonProperty("goods_attrs_1")
    private Map<String, String> goodsAttrs = Maps.newHashMap();
    /**
     * <pre>
     *     扩展字段：
     *      "field_1": "string,自定义字段1",
     "      "field_2": "string,自定义字段2",
     "      "field_n": "string,自定义字段n"
     * </pre>
     */
    @JsonProperty("extends")
    private Map<String, String> _extends = Maps.newHashMap();

    /**
     * 是否禁用，0-有效，1-禁用
     */
    private Integer disabled;

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    public String getGoodsNo() {
        return goodsNo;
    }

    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsNameEnglish() {
        return goodsNameEnglish;
    }

    public void setGoodsNameEnglish(String goodsNameEnglish) {
        this.goodsNameEnglish = goodsNameEnglish;
    }

    public BigDecimal getGoodsWeight() {
        return goodsWeight;
    }

    public void setGoodsWeight(BigDecimal goodsWeight) {
        this.goodsWeight = goodsWeight;
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

    public Integer getAttrValCheck() {
        return attrValCheck;
    }

    public void setAttrValCheck(Integer attrValCheck) {
        this.attrValCheck = attrValCheck;
    }

    public Map<String, String> getGoodsAttrs() {
        return goodsAttrs;
    }

    public void setGoodsAttrs(Map<String, String> goodsAttrs) {
        this.goodsAttrs = goodsAttrs;
    }

    public Map<String, String> get_extends() {
        return _extends;
    }

    public void set_extends(Map<String, String> _extends) {
        this._extends = _extends;
    }

    public Integer getDisabled() {
        return disabled;
    }

    public void setDisabled(Integer disabled) {
        this.disabled = disabled;
    }
}
