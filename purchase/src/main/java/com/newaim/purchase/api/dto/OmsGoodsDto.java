package com.newaim.purchase.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by Mark on 2017/12/12.
 */
public class OmsGoodsDto implements Serializable{

    @JsonProperty("sku_id")
    private String skuId;

    private String sku;

    private String ean;

    @JsonProperty("goods_id")
    private String goodsId;

    @JsonProperty("goods_no")
    private String goodsNo;

    @JsonProperty("goods_name")
    private String goodsName;

    @JsonProperty("goods_name_english")
    private String goodsNameEnglish;

    @JsonProperty("category_id")
    private String categoryId;

    @JsonProperty("category_name")
    private String categoryName;

    @JsonProperty("category_code")
    private String categoryCode;

    @JsonProperty("brand_id")
    private String brandId;

    @JsonProperty("cost_price")
    private String costPrice;

    @JsonProperty("market_price")
    private String market_price;

    @JsonProperty("is_virtual")
    private String isVirtual;

    private String created;

    private String modified;

    private String volume;

    @JsonProperty("is_group")
    private String isGroup;

    @JsonProperty("owner_id")
    private String ownerId;

    private String length;
    private String width;
    private String height;

    @JsonProperty("sku_attr_vals")
    private String skuAttrVals;

    private String weight;

    @JsonProperty("sku_attr_val_names")
    private String skuAttrValNames;

    @JsonProperty("sku_attr_1_names")
    private String skuAttr1Names;
    @JsonProperty("sku_attr_2_names")
    private String skuAttr2Names;
    @JsonProperty("sku_attr_3_names")
    private String skuAttr3Names;
    @JsonProperty("sku_attr_4_names")
    private String skuAttr4Names;
    @JsonProperty("sku_attr_5_names")
    private String skuAttr5Names;
    @JsonProperty("sku_attr_6_names")
    private String skuAttr6Names;
    @JsonProperty("sku_attr_7_names")
    private String skuAttr7Names;
    @JsonProperty("sku_attr_8_names")
    private String skuAttr8Names;
    @JsonProperty("sku_attr_9_names")
    private String skuAttr9Names;
    @JsonProperty("sku_attr_10_names")
    private String skuAttr10Names;

    @JsonProperty("brand_name")
    private String brandName;
    @JsonProperty("brand_code")
    private String brandCode;
    @JsonProperty("owner_name")
    private String ownerName;
    @JsonProperty("owner_code")
    private String ownerCode;

    @JsonProperty("goods_attr_val_names")
    private String goodsAttrValNames;

    @JsonProperty("goods_attr_1_names")
    private String goodsAttr1Names;
    @JsonProperty("goods_attr_2_names")
    private String goodsAttr2Names;
    @JsonProperty("goods_attr_3_names")
    private String goodsAttr3Names;
    @JsonProperty("goods_attr_4_names")
    private String goodsAttr4Names;
    @JsonProperty("goods_attr_5_names")
    private String goodsAttr5Names;
    @JsonProperty("goods_attr_6_names")
    private String goodsAttr6Names;
    @JsonProperty("goods_attr_7_names")
    private String goodsAttr7Names;
    @JsonProperty("goods_attr_8_names")
    private String goodsAttr8Names;
    @JsonProperty("goods_attr_9_names")
    private String goodsAttr9Names;
    @JsonProperty("goods_attr_10_names")
    private String goodsAttr10Names;


    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getEan() {
        return ean;
    }

    public void setEan(String ean) {
        this.ean = ean;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
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

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(String costPrice) {
        this.costPrice = costPrice;
    }

    public String getMarket_price() {
        return market_price;
    }

    public void setMarket_price(String market_price) {
        this.market_price = market_price;
    }

    public String getIsVirtual() {
        return isVirtual;
    }

    public void setIsVirtual(String isVirtual) {
        this.isVirtual = isVirtual;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getIsGroup() {
        return isGroup;
    }

    public void setIsGroup(String isGroup) {
        this.isGroup = isGroup;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getSkuAttrVals() {
        return skuAttrVals;
    }

    public void setSkuAttrVals(String skuAttrVals) {
        this.skuAttrVals = skuAttrVals;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getSkuAttrValNames() {
        return skuAttrValNames;
    }

    public void setSkuAttrValNames(String skuAttrValNames) {
        this.skuAttrValNames = skuAttrValNames;
    }

    public String getSkuAttr1Names() {
        return skuAttr1Names;
    }

    public void setSkuAttr1Names(String skuAttr1Names) {
        this.skuAttr1Names = skuAttr1Names;
    }

    public String getSkuAttr2Names() {
        return skuAttr2Names;
    }

    public void setSkuAttr2Names(String skuAttr2Names) {
        this.skuAttr2Names = skuAttr2Names;
    }

    public String getSkuAttr3Names() {
        return skuAttr3Names;
    }

    public void setSkuAttr3Names(String skuAttr3Names) {
        this.skuAttr3Names = skuAttr3Names;
    }

    public String getSkuAttr4Names() {
        return skuAttr4Names;
    }

    public void setSkuAttr4Names(String skuAttr4Names) {
        this.skuAttr4Names = skuAttr4Names;
    }

    public String getSkuAttr5Names() {
        return skuAttr5Names;
    }

    public void setSkuAttr5Names(String skuAttr5Names) {
        this.skuAttr5Names = skuAttr5Names;
    }

    public String getSkuAttr6Names() {
        return skuAttr6Names;
    }

    public void setSkuAttr6Names(String skuAttr6Names) {
        this.skuAttr6Names = skuAttr6Names;
    }

    public String getSkuAttr7Names() {
        return skuAttr7Names;
    }

    public void setSkuAttr7Names(String skuAttr7Names) {
        this.skuAttr7Names = skuAttr7Names;
    }

    public String getSkuAttr8Names() {
        return skuAttr8Names;
    }

    public void setSkuAttr8Names(String skuAttr8Names) {
        this.skuAttr8Names = skuAttr8Names;
    }

    public String getSkuAttr9Names() {
        return skuAttr9Names;
    }

    public void setSkuAttr9Names(String skuAttr9Names) {
        this.skuAttr9Names = skuAttr9Names;
    }

    public String getSkuAttr10Names() {
        return skuAttr10Names;
    }

    public void setSkuAttr10Names(String skuAttr10Names) {
        this.skuAttr10Names = skuAttr10Names;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandCode() {
        return brandCode;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    public String getGoodsAttrValNames() {
        return goodsAttrValNames;
    }

    public void setGoodsAttrValNames(String goodsAttrValNames) {
        this.goodsAttrValNames = goodsAttrValNames;
    }

    public String getGoodsAttr1Names() {
        return goodsAttr1Names;
    }

    public void setGoodsAttr1Names(String goodsAttr1Names) {
        this.goodsAttr1Names = goodsAttr1Names;
    }

    public String getGoodsAttr2Names() {
        return goodsAttr2Names;
    }

    public void setGoodsAttr2Names(String goodsAttr2Names) {
        this.goodsAttr2Names = goodsAttr2Names;
    }

    public String getGoodsAttr3Names() {
        return goodsAttr3Names;
    }

    public void setGoodsAttr3Names(String goodsAttr3Names) {
        this.goodsAttr3Names = goodsAttr3Names;
    }

    public String getGoodsAttr4Names() {
        return goodsAttr4Names;
    }

    public void setGoodsAttr4Names(String goodsAttr4Names) {
        this.goodsAttr4Names = goodsAttr4Names;
    }

    public String getGoodsAttr5Names() {
        return goodsAttr5Names;
    }

    public void setGoodsAttr5Names(String goodsAttr5Names) {
        this.goodsAttr5Names = goodsAttr5Names;
    }

    public String getGoodsAttr6Names() {
        return goodsAttr6Names;
    }

    public void setGoodsAttr6Names(String goodsAttr6Names) {
        this.goodsAttr6Names = goodsAttr6Names;
    }

    public String getGoodsAttr7Names() {
        return goodsAttr7Names;
    }

    public void setGoodsAttr7Names(String goodsAttr7Names) {
        this.goodsAttr7Names = goodsAttr7Names;
    }

    public String getGoodsAttr8Names() {
        return goodsAttr8Names;
    }

    public void setGoodsAttr8Names(String goodsAttr8Names) {
        this.goodsAttr8Names = goodsAttr8Names;
    }

    public String getGoodsAttr9Names() {
        return goodsAttr9Names;
    }

    public void setGoodsAttr9Names(String goodsAttr9Names) {
        this.goodsAttr9Names = goodsAttr9Names;
    }

    public String getGoodsAttr10Names() {
        return goodsAttr10Names;
    }

    public void setGoodsAttr10Names(String goodsAttr10Names) {
        this.goodsAttr10Names = goodsAttr10Names;
    }
}
