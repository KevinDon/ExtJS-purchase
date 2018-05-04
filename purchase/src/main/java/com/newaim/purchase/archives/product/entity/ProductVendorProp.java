package com.newaim.purchase.archives.product.entity;

import com.newaim.core.utils.SessionUtils;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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
@Table(name = "na_product_vendor_prop")
public class ProductVendorProp implements Serializable{

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.newaim.core.jpa.IdGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name = "prefix", value = "VPP")})
    private String id;

    @OneToOne(optional = false, cascade = {CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinColumn(name = "product_id")
    private Product product;

    private String sku;
    private String factoryCode;

    private String vendorId;
    private String vendorCnName;
    private String vendorEnName;
    private String vendorCode;
    private String serviceProviderCnName;
    private String serviceProviderEnName;
    /**推荐的物流公司*/
    private String serviceProviderId;
    private Integer riskRating;

    @Column(name = "images")
    private String photos;

    private String productParameter;
    private String productDetail;
    private String productLink;
    private String keywords;

    @Column(name = "master_carton_l")
    private BigDecimal masterCartonL;
    @Column(name = "master_carton_w")
    private BigDecimal masterCartonW;
    @Column(name = "master_carton_h")
    private BigDecimal masterCartonH;
    private BigDecimal masterCartonCbm;
    private BigDecimal masterCartonGrossWeight;
    private BigDecimal masterCartonCubicWeight;
    @Column(name = "inner_carton_l")
    private BigDecimal innerCartonL;
    @Column(name = "inner_carton_w")
    private BigDecimal innerCartonW;
    @Column(name = "inner_carton_h")
    private BigDecimal innerCartonH;
    private BigDecimal innerCartonCbm;
    private BigDecimal innerCartonGrossWeight;
    private BigDecimal innerCartonCubicWeight;
    private BigDecimal innerCartonNetWeight;
    private String masterCartonStructure;
    private String masterCartonWeight;
    private Integer pcsPerCarton;
    private Integer pcsPerPallets;
    /**唛头格式*/
    private String shippingMark;
    private String userManual;
    private String productCert;
    /**起订量*/
    private Integer moq;
    private String originPortId;
    private String originPortCnName;
    private String originPortEnName;
    /**生产周期*/
    private Integer leadTime;
    /**预估邮费*/
    private BigDecimal estimatedAvgPostageAud;
    private BigDecimal estimatedAvgPostageRmb;
    private BigDecimal estimatedAvgPostageUsd;
    /**目标售价*/
    private BigDecimal targetBinAud;
    private BigDecimal targetBinRmb;
    private BigDecimal targetBinUsd;
    private BigDecimal rateAudToRmb;
    private BigDecimal rateAudToUsd;
    private Integer currency;
    private String hsCode;

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

    /**报价，报价汇率、报价币种*/
    private BigDecimal quotationPriceAud;
    private BigDecimal quotationPriceRmb;
    private BigDecimal quotationPriceUsd;
    private BigDecimal quotationRateAudToRmb;
    private BigDecimal quotationRateAudToUsd;
    private Integer quotationCurrency;

    private Integer isApply;//是否应用（1是2否）
    /**开发状态*/
    private Integer flagDevStatus;
    private String flagDevId;
    private Date flagDevTime;
    /**安检状态*/
    private Integer flagComplianceStatus;
    private String flagComplianceId;
    private Date flagComplianceTime;
    /**质检状态*/
    private Integer flagQcStatus;
    private String flagQcId;
    private Date flagQcTime;

    @Transient
    private String images;


    public BigDecimal getTargetBinAud() {
        return targetBinAud;
    }

    public void setTargetBinAud(BigDecimal targetBinAud) {
        this.targetBinAud = targetBinAud;
    }

    public BigDecimal getTargetBinRmb() {
        return targetBinRmb;
    }

    public void setTargetBinRmb(BigDecimal targetBinRmb) {
        this.targetBinRmb = targetBinRmb;
    }

    public BigDecimal getTargetBinUsd() {
        return targetBinUsd;
    }

    public void setTargetBinUsd(BigDecimal targetBinUsd) {
        this.targetBinUsd = targetBinUsd;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
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

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public Integer getCurrency() {
        return currency;
    }

    public void setCurrency(Integer currency) {
        this.currency = currency;
    }

    public String getHsCode() {
        return hsCode;
    }

    public void setHsCode(String hsCode) {
        this.hsCode = hsCode;
    }

    public String getVendorCnName() {
        return vendorCnName;
    }

    public void setVendorCnName(String vendorCnName) {
        this.vendorCnName = vendorCnName;
    }

    public BigDecimal getEstimatedAvgPostageAud() {
        return estimatedAvgPostageAud;
    }

    public void setEstimatedAvgPostageAud(BigDecimal estimatedAvgPostageAud) {
        this.estimatedAvgPostageAud = estimatedAvgPostageAud;
    }

    public BigDecimal getEstimatedAvgPostageRmb() {
        return estimatedAvgPostageRmb;
    }

    public void setEstimatedAvgPostageRmb(BigDecimal estimatedAvgPostageRmb) {
        this.estimatedAvgPostageRmb = estimatedAvgPostageRmb;
    }

    public BigDecimal getEstimatedAvgPostageUsd() {
        return estimatedAvgPostageUsd;
    }

    public void setEstimatedAvgPostageUsd(BigDecimal estimatedAvgPostageUsd) {
        this.estimatedAvgPostageUsd = estimatedAvgPostageUsd;
    }

    public String getVendorEnName() {
        return vendorEnName;
    }

    public void setVendorEnName(String vendorEnName) {
        this.vendorEnName = vendorEnName;
    }

    public Integer getRiskRating() {
        return riskRating;
    }

    public void setRiskRating(Integer riskRating) {
        this.riskRating = riskRating;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public String getProductParameter() {
        return productParameter;
    }

    public void setProductParameter(String productParameter) {
        this.productParameter = productParameter;
    }

    public String getProductDetail() {
        return productDetail;
    }

    public void setProductDetail(String productDetail) {
        this.productDetail = productDetail;
    }

    public String getProductLink() {
        return productLink;
    }

    public void setProductLink(String productLink) {
        this.productLink = productLink;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public BigDecimal getMasterCartonL() {
        return masterCartonL;
    }

    public void setMasterCartonL(BigDecimal masterCartonL) {
        this.masterCartonL = masterCartonL;
    }

    public BigDecimal getMasterCartonW() {
        return masterCartonW;
    }

    public void setMasterCartonW(BigDecimal masterCartonW) {
        this.masterCartonW = masterCartonW;
    }

    public BigDecimal getMasterCartonH() {
        return masterCartonH;
    }

    public void setMasterCartonH(BigDecimal masterCartonH) {
        this.masterCartonH = masterCartonH;
    }

    public BigDecimal getMasterCartonCbm() {
        return masterCartonCbm;
    }

    public void setMasterCartonCbm(BigDecimal masterCartonCbm) {
        this.masterCartonCbm = masterCartonCbm;
    }

    public BigDecimal getMasterCartonGrossWeight() {
        return masterCartonGrossWeight;
    }

    public void setMasterCartonGrossWeight(BigDecimal masterCartonGrossWeight) {
        this.masterCartonGrossWeight = masterCartonGrossWeight;
    }

    public BigDecimal getMasterCartonCubicWeight() {
        return masterCartonCubicWeight;
    }

    public void setMasterCartonCubicWeight(BigDecimal masterCartonCubicWeight) {
        this.masterCartonCubicWeight = masterCartonCubicWeight;
    }

    public BigDecimal getInnerCartonL() {
        return innerCartonL;
    }

    public void setInnerCartonL(BigDecimal innerCartonL) {
        this.innerCartonL = innerCartonL;
    }

    public BigDecimal getInnerCartonW() {
        return innerCartonW;
    }

    public void setInnerCartonW(BigDecimal innerCartonW) {
        this.innerCartonW = innerCartonW;
    }

    public BigDecimal getInnerCartonH() {
        return innerCartonH;
    }

    public void setInnerCartonH(BigDecimal innerCartonH) {
        this.innerCartonH = innerCartonH;
    }

    public BigDecimal getInnerCartonCbm() {
        return innerCartonCbm;
    }

    public void setInnerCartonCbm(BigDecimal innerCartonCbm) {
        this.innerCartonCbm = innerCartonCbm;
    }

    public BigDecimal getInnerCartonGrossWeight() {
        return innerCartonGrossWeight;
    }

    public void setInnerCartonGrossWeight(BigDecimal innerCartonGrossWeight) {
        this.innerCartonGrossWeight = innerCartonGrossWeight;
    }

    public BigDecimal getInnerCartonCubicWeight() {
        return innerCartonCubicWeight;
    }

    public void setInnerCartonCubicWeight(BigDecimal innerCartonCubicWeight) {
        this.innerCartonCubicWeight = innerCartonCubicWeight;
    }

    public BigDecimal getInnerCartonNetWeight() {
        return innerCartonNetWeight;
    }

    public void setInnerCartonNetWeight(BigDecimal innerCartonNetWeight) {
        this.innerCartonNetWeight = innerCartonNetWeight;
    }

    public String getMasterCartonStructure() {
        return masterCartonStructure;
    }

    public void setMasterCartonStructure(String masterCartonStructure) {
        this.masterCartonStructure = masterCartonStructure;
    }

    public String getMasterCartonWeight() {
        return masterCartonWeight;
    }

    public void setMasterCartonWeight(String masterCartonWeight) {
        this.masterCartonWeight = masterCartonWeight;
    }

    public Integer getPcsPerCarton() {
        return pcsPerCarton;
    }

    public void setPcsPerCarton(Integer pcsPerCarton) {
        this.pcsPerCarton = pcsPerCarton;
    }

    public Integer getPcsPerPallets() {
        return pcsPerPallets;
    }

    public void setPcsPerPallets(Integer pcsPerPallets) {
        this.pcsPerPallets = pcsPerPallets;
    }

    public String getShippingMark() {
        return shippingMark;
    }

    public void setShippingMark(String shippingMark) {
        this.shippingMark = shippingMark;
    }

    public String getUserManual() {
        return userManual;
    }

    public void setUserManual(String userManual) {
        this.userManual = userManual;
    }

    public String getProductCert() {
        return productCert;
    }

    public void setProductCert(String productCert) {
        this.productCert = productCert;
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

    public String getServiceProviderId() {
        return serviceProviderId;
    }

    public void setServiceProviderId(String serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
    }

    public Integer getMoq() {
        return moq;
    }

    public void setMoq(Integer moq) {
        this.moq = moq;
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

    public Integer getLeadTime() {
        return leadTime;
    }

    public void setLeadTime(Integer leadTime) {
        this.leadTime = leadTime;
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

    public Integer getFlagDevStatus() {
        return flagDevStatus;
    }

    public void setFlagDevStatus(Integer flagDevStatus) {
        this.flagDevStatus = flagDevStatus;
    }

    public String getFlagDevId() {
        return flagDevId;
    }

    public void setFlagDevId(String flagDevId) {
        this.flagDevId = flagDevId;
    }

    public Date getFlagDevTime() {
        return flagDevTime;
    }

    public void setFlagDevTime(Date flagDevTime) {
        this.flagDevTime = flagDevTime;
    }

    public Integer getFlagComplianceStatus() {
        return flagComplianceStatus;
    }

    public void setFlagComplianceStatus(Integer flagComplianceStatus) {
        this.flagComplianceStatus = flagComplianceStatus;
    }

    public String getFlagComplianceId() {
        return flagComplianceId;
    }

    public void setFlagComplianceId(String flagComplianceId) {
        this.flagComplianceId = flagComplianceId;
    }

    public Date getFlagComplianceTime() {
        return flagComplianceTime;
    }

    public void setFlagComplianceTime(Date flagComplianceTime) {
        this.flagComplianceTime = flagComplianceTime;
    }

    public Integer getFlagQcStatus() {
        return flagQcStatus;
    }

    public void setFlagQcStatus(Integer flagQcStatus) {
        this.flagQcStatus = flagQcStatus;
    }

    public String getFlagQcId() {
        return flagQcId;
    }

    public void setFlagQcId(String flagQcId) {
        this.flagQcId = flagQcId;
    }

    public Date getFlagQcTime() {
        return flagQcTime;
    }

    public void setFlagQcTime(Date flagQcTime) {
        this.flagQcTime = flagQcTime;
    }

    public BigDecimal getQuotationPriceAud() {
        return quotationPriceAud;
    }

    public void setQuotationPriceAud(BigDecimal quotationPriceAud) {
        this.quotationPriceAud = quotationPriceAud;
    }

    public BigDecimal getQuotationPriceRmb() {
        return quotationPriceRmb;
    }

    public void setQuotationPriceRmb(BigDecimal quotationPriceRmb) {
        this.quotationPriceRmb = quotationPriceRmb;
    }

    public BigDecimal getQuotationPriceUsd() {
        return quotationPriceUsd;
    }

    public void setQuotationPriceUsd(BigDecimal quotationPriceUsd) {
        this.quotationPriceUsd = quotationPriceUsd;
    }

    public BigDecimal getQuotationRateAudToRmb() {
        return quotationRateAudToRmb;
    }

    public void setQuotationRateAudToRmb(BigDecimal quotationRateAudToRmb) {
        this.quotationRateAudToRmb = quotationRateAudToRmb;
    }

    public BigDecimal getQuotationRateAudToUsd() {
        return quotationRateAudToUsd;
    }

    public void setQuotationRateAudToUsd(BigDecimal quotationRateAudToUsd) {
        this.quotationRateAudToUsd = quotationRateAudToUsd;
    }

    public Integer getQuotationCurrency() {
        return quotationCurrency;
    }

    public void setQuotationCurrency(Integer quotationCurrency) {
        this.quotationCurrency = quotationCurrency;
    }

    @Transient
    public String getVendorName() {
        if(SessionUtils.isCnLang()){
            return  this.vendorCnName;
        }else if(SessionUtils.isEnLang()){
            return this.vendorEnName;
        }
        return null;
    }

    @Transient
    public String getServiceProviderName() {
        if(SessionUtils.isCnLang()){
            return this.serviceProviderCnName;
        }else if(SessionUtils.isEnLang()){
            return this.serviceProviderEnName;
        }
        return null;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public Integer getIsApply() {
        return isApply;
    }

    public void setIsApply(Integer isApply) {
        this.isApply = isApply;
    }

    public String getVendorCode() {
        return vendorCode;
    }

    public void setVendorCode(String vendorCode) {
        this.vendorCode = vendorCode;
    }
}
