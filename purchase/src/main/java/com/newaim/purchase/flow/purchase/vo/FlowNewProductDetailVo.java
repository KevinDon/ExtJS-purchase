package com.newaim.purchase.flow.purchase.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.newaim.purchase.archives.product.vo.ProductVo;
import com.newaim.purchase.config.json.JsonMoney;

import java.io.Serializable;
import java.math.BigDecimal;

public class FlowNewProductDetailVo implements Serializable{

	@JsonIgnore
    private String id;

    private String businessId;

    private String productId;
    
    private ProductVo product;

    private Integer currency;
    private BigDecimal priceAud;
    private BigDecimal priceRmb;
    private BigDecimal priceUsd;
    @JsonMoney
    private BigDecimal rateAudToRmb;
    @JsonMoney
    private BigDecimal rateAudToUsd;
    private Integer orderQty;
    private BigDecimal orderValueAud;
    private BigDecimal orderValueRmb;
    private BigDecimal orderValueUsd;
    private BigDecimal productPredictProfitAud;
    private BigDecimal productPredictProfitRmb;
    private BigDecimal productPredictProfitUsd;
    private BigDecimal competitorPriceAud;
    private BigDecimal competitorPriceRmb;
    private BigDecimal competitorPriceUsd;
    private Integer competitorSaleRecord;
    private BigDecimal ebayMonthlySalesAud;
    private BigDecimal ebayMonthlySalesRmb;
    private BigDecimal ebayMonthlySalesUsd;

    private Integer newProduct;

    private String productQuotationId;
    private String productQuotationBusinessId;
    private String productQuotationDetailBusinessId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
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

    public Integer getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(Integer orderQty) {
        this.orderQty = orderQty;
    }

    public BigDecimal getOrderValueAud() {
        return orderValueAud;
    }

    public void setOrderValueAud(BigDecimal orderValueAud) {
        this.orderValueAud = orderValueAud;
    }

    public BigDecimal getOrderValueRmb() {
        return orderValueRmb;
    }

    public void setOrderValueRmb(BigDecimal orderValueRmb) {
        this.orderValueRmb = orderValueRmb;
    }

    public BigDecimal getOrderValueUsd() {
        return orderValueUsd;
    }

    public void setOrderValueUsd(BigDecimal orderValueUsd) {
        this.orderValueUsd = orderValueUsd;
    }

    public BigDecimal getProductPredictProfitAud() {
        return productPredictProfitAud;
    }

    public void setProductPredictProfitAud(BigDecimal productPredictProfitAud) {
        this.productPredictProfitAud = productPredictProfitAud;
    }

    public BigDecimal getProductPredictProfitRmb() {
        return productPredictProfitRmb;
    }

    public void setProductPredictProfitRmb(BigDecimal productPredictProfitRmb) {
        this.productPredictProfitRmb = productPredictProfitRmb;
    }

    public BigDecimal getProductPredictProfitUsd() {
        return productPredictProfitUsd;
    }

    public void setProductPredictProfitUsd(BigDecimal productPredictProfitUsd) {
        this.productPredictProfitUsd = productPredictProfitUsd;
    }

    public BigDecimal getCompetitorPriceAud() {
        return competitorPriceAud;
    }

    public void setCompetitorPriceAud(BigDecimal competitorPriceAud) {
        this.competitorPriceAud = competitorPriceAud;
    }

    public BigDecimal getCompetitorPriceRmb() {
        return competitorPriceRmb;
    }

    public void setCompetitorPriceRmb(BigDecimal competitorPriceRmb) {
        this.competitorPriceRmb = competitorPriceRmb;
    }

    public BigDecimal getCompetitorPriceUsd() {
        return competitorPriceUsd;
    }

    public void setCompetitorPriceUsd(BigDecimal competitorPriceUsd) {
        this.competitorPriceUsd = competitorPriceUsd;
    }

    public Integer getCompetitorSaleRecord() {
        return competitorSaleRecord;
    }

    public void setCompetitorSaleRecord(Integer competitorSaleRecord) {
        this.competitorSaleRecord = competitorSaleRecord;
    }

    public BigDecimal getEbayMonthlySalesAud() {
        return ebayMonthlySalesAud;
    }

    public void setEbayMonthlySalesAud(BigDecimal ebayMonthlySalesAud) {
        this.ebayMonthlySalesAud = ebayMonthlySalesAud;
    }

    public BigDecimal getEbayMonthlySalesRmb() {
        return ebayMonthlySalesRmb;
    }

    public void setEbayMonthlySalesRmb(BigDecimal ebayMonthlySalesRmb) {
        this.ebayMonthlySalesRmb = ebayMonthlySalesRmb;
    }

    public BigDecimal getEbayMonthlySalesUsd() {
        return ebayMonthlySalesUsd;
    }

    public void setEbayMonthlySalesUsd(BigDecimal ebayMonthlySalesUsd) {
        this.ebayMonthlySalesUsd = ebayMonthlySalesUsd;
    }

    public Integer getNewProduct() {
        return newProduct;
    }

    public void setNewProduct(Integer newProduct) {
        this.newProduct = newProduct;
    }

    public String getProductQuotationId() {
        return productQuotationId;
    }

    public void setProductQuotationId(String productQuotationId) {
        this.productQuotationId = productQuotationId;
    }

    public String getProductQuotationBusinessId() {
        return productQuotationBusinessId;
    }

    public void setProductQuotationBusinessId(String productQuotationBusinessId) {
        this.productQuotationBusinessId = productQuotationBusinessId;
    }

    public String getProductQuotationDetailBusinessId() {
        return productQuotationDetailBusinessId;
    }

    public void setProductQuotationDetailBusinessId(String productQuotationDetailBusinessId) {
        this.productQuotationDetailBusinessId = productQuotationDetailBusinessId;
    }
}
