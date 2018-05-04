package com.newaim.purchase.archives.flow.purchase.vo;

import com.newaim.purchase.archives.product.vo.ProductVo;
import com.newaim.purchase.config.json.JsonMoney;

import java.math.BigDecimal;

public class PurchasePlanDetailViewVo {

    private String id;

    private String purchasePlanId;
    private String productId;
    private String sku;
    private String vendorId;//供应商Id
    private String vendorCnName;
    private String vendorEnName;

    private BigDecimal priceAud;
    private BigDecimal priceRmb;
    private BigDecimal priceUsd;
    @JsonMoney
    private BigDecimal rateAudToRmb;
    @JsonMoney
    private BigDecimal rateAudToUsd;
    private BigDecimal prevPriceAud;//上次报价
    private BigDecimal prevPriceRmb;
    private BigDecimal prevPriceUsd;
    @JsonMoney
    private BigDecimal prevRateAudToRmb;//上次人民币汇率
    @JsonMoney
    private BigDecimal prevRateAudToUsd;//上次美元汇率
    private Integer moq;//起订量
    private Integer prevMoq;//上次起订量
    private Integer currency;//结算货币
    private String originPortId;
    private String originPortCnName;
    private String originPortEnName;
    private String destinationPortId;
    private String destinationPortCnName;
    private String destinationPortEnName;
    private Integer orderQtyCarton;//采购箱数
    private Integer orderQty;//采购数量
    private Integer alreadyOrderQty;//已采购数量
    private Integer totalOrderQty;//总数量
    private Integer availableQty;//可用数量
    private Integer availableCarton;
    private Integer pcsPerCarton;
    private ProductVo product;
    private String flowPurchasePlanId;
    private String creatorId;
    private String creatorCnName;
    private String creatorEnName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPurchasePlanId() {
        return purchasePlanId;
    }

    public void setPurchasePlanId(String purchasePlanId) {
        this.purchasePlanId = purchasePlanId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
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

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
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

    public BigDecimal getPrevPriceAud() {
        return prevPriceAud;
    }

    public void setPrevPriceAud(BigDecimal prevPriceAud) {
        this.prevPriceAud = prevPriceAud;
    }

    public BigDecimal getPrevPriceRmb() {
        return prevPriceRmb;
    }

    public void setPrevPriceRmb(BigDecimal prevPriceRmb) {
        this.prevPriceRmb = prevPriceRmb;
    }

    public BigDecimal getPrevPriceUsd() {
        return prevPriceUsd;
    }

    public void setPrevPriceUsd(BigDecimal prevPriceUsd) {
        this.prevPriceUsd = prevPriceUsd;
    }

    public BigDecimal getPrevRateAudToRmb() {
        return prevRateAudToRmb;
    }

    public void setPrevRateAudToRmb(BigDecimal prevRateAudToRmb) {
        this.prevRateAudToRmb = prevRateAudToRmb;
    }

    public BigDecimal getPrevRateAudToUsd() {
        return prevRateAudToUsd;
    }

    public void setPrevRateAudToUsd(BigDecimal prevRateAudToUsd) {
        this.prevRateAudToUsd = prevRateAudToUsd;
    }

    public Integer getMoq() {
        return moq;
    }

    public void setMoq(Integer moq) {
        this.moq = moq;
    }

    public Integer getPrevMoq() {
        return prevMoq;
    }

    public void setPrevMoq(Integer prevMoq) {
        this.prevMoq = prevMoq;
    }

    public Integer getCurrency() {
        return currency;
    }

    public void setCurrency(Integer currency) {
        this.currency = currency;
    }

    public String getOriginPortId() {
        return originPortId;
    }

    public void setOriginPortId(String originPortId) {
        this.originPortId = originPortId;
    }

    public String getOriginPortCnName() {
        return originPortCnName;
    }

    public void setOriginPortCnName(String originPortCnName) {
        this.originPortCnName = originPortCnName;
    }

    public String getOriginPortEnName() {
        return originPortEnName;
    }

    public void setOriginPortEnName(String originPortEnName) {
        this.originPortEnName = originPortEnName;
    }

    public String getDestinationPortId() {
        return destinationPortId;
    }

    public void setDestinationPortId(String destinationPortId) {
        this.destinationPortId = destinationPortId;
    }

    public String getDestinationPortCnName() {
        return destinationPortCnName;
    }

    public void setDestinationPortCnName(String destinationPortCnName) {
        this.destinationPortCnName = destinationPortCnName;
    }

    public String getDestinationPortEnName() {
        return destinationPortEnName;
    }

    public void setDestinationPortEnName(String destinationPortEnName) {
        this.destinationPortEnName = destinationPortEnName;
    }

    public Integer getOrderQtyCarton() {
        return orderQtyCarton;
    }

    public void setOrderQtyCarton(Integer orderQtyCarton) {
        this.orderQtyCarton = orderQtyCarton;
    }

    public Integer getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(Integer orderQty) {
        this.orderQty = orderQty;
    }

    public Integer getAlreadyOrderQty() {
        return alreadyOrderQty;
    }

    public void setAlreadyOrderQty(Integer alreadyOrderQty) {
        this.alreadyOrderQty = alreadyOrderQty;
    }

    public Integer getTotalOrderQty() {
        return totalOrderQty;
    }

    public void setTotalOrderQty(Integer totalOrderQty) {
        this.totalOrderQty = totalOrderQty;
    }


    public Integer getAvailableQty() {
        if (alreadyOrderQty !=null) {
            this.availableQty = orderQty - alreadyOrderQty;
        }else {
            this.availableQty = orderQty;
        }
        return availableQty;
    }

    public void setAvailableQty(Integer availableQty) {
        this.availableQty = availableQty;
    }

    public Integer getAvailableCarton() {
        if(getAvailableQty() != null){
            return Double.valueOf(Math.ceil(getAvailableQty() / (pcsPerCarton * 1.0))).intValue();
        }
        return 0;
    }

    public void setAvailableCarton(Integer availableCarton) {
        this.availableCarton = availableCarton;
    }

    public ProductVo getProduct() {
        return product;
    }

    public void setProduct(ProductVo product) {
        this.product = product;
    }

    public String getFlowPurchasePlanId() {
        return flowPurchasePlanId;
    }

    public void setFlowPurchasePlanId(String flowPurchasePlanId) {
        this.flowPurchasePlanId = flowPurchasePlanId;
    }

    public Integer getPcsPerCarton() {
        return pcsPerCarton;
    }

    public void setPcsPerCarton(Integer pcsPerCarton) {
        this.pcsPerCarton = pcsPerCarton;
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
}
