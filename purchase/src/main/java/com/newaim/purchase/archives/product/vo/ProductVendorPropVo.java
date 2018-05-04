package com.newaim.purchase.archives.product.vo;

import com.google.common.collect.Lists;
import com.newaim.purchase.admin.system.vo.AttachmentVo;
import com.newaim.purchase.config.json.JsonMoney;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ProductVendorPropVo implements Serializable{


    private String id;
    private String sku;
    private String vendorId;
    private String vendorName;
    private String vendorCnName;
    private String vendorEnName;
    private String vendorCode;
    private Integer riskRating;
    private String photos;
    private String productParameter;
    private String productDetail;
    private String productLink;
    private String keywords;
    private BigDecimal masterCartonL;
    private BigDecimal masterCartonW;
    private BigDecimal masterCartonH;
    @JsonMoney
    private BigDecimal masterCartonCbm;
    private BigDecimal masterCartonGrossWeight;
    private BigDecimal masterCartonCubicWeight;
    private BigDecimal innerCartonL;
    private BigDecimal innerCartonW;
    private BigDecimal innerCartonH;
    @JsonMoney
    private BigDecimal innerCartonCbm;
    private BigDecimal innerCartonGrossWeight;
    private BigDecimal innerCartonCubicWeight;
    private BigDecimal innerCartonNetWeight;
    private String masterCartonStructure;
    private String masterCartonWeight;
    private Integer pcsPerCarton;
    private Integer pcsPerPallets;
    private String shippingMark;//唛头格式
    private String userManual;//说明书
    private String productCert;
    private String serviceProviderName;
    private String serviceProviderId;//推荐的物流公司
    private BigDecimal estimatedAvgPostage;//预估邮费
    private Integer moq;//起订量
    private String originPortId;
    private String originPortCnName;
    private String originPortEnName;
    private Integer leadTime; //生产周期
    private String factoryCode;
    private BigDecimal estimatedAvgPostageAud;//预估邮费
    private BigDecimal estimatedAvgPostageRmb;
    private BigDecimal estimatedAvgPostageUsd;
    private BigDecimal targetBinAud;//目标售价
    private BigDecimal targetBinRmb;
    private BigDecimal targetBinUsd;
    @JsonMoney
    private BigDecimal rateAudToRmb;
    @JsonMoney
    private BigDecimal rateAudToUsd;
    private Integer currency;
    private String hsCode;
    private BigDecimal dutyRate;
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
    @JsonMoney
    private BigDecimal quotationRateAudToRmb;
    @JsonMoney
    private BigDecimal quotationRateAudToUsd;
    private Integer quotationCurrency;

    /**开发状态*/
    private Integer flagDevStatus;
    private String flagDevId;
    private String flagDevFlowId;
    private Date flagDevTime;
    /**安检状态*/
    private Integer flagComplianceStatus;
    private String flagComplianceId;
    private String flagComplianceFlowId;
    private Date flagComplianceTime;
    /**质检状态*/
    private Integer flagQcStatus;
    private String flagQcId;
    private String flagQcFlowId;
    private Date flagQcTime;

    private String images;

    private Integer isApply;//是否应用（1是2否）

    //图片原型
    private List<AttachmentVo> imagesDoc = Lists.newArrayList();


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

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
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

    public String getServiceProviderName() {
        return serviceProviderName;
    }

    public void setServiceProviderName(String serviceProviderName) {
        this.serviceProviderName = serviceProviderName;
    }

    public String getServiceProviderId() {
        return serviceProviderId;
    }

    public void setServiceProviderId(String serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
    }

    public BigDecimal getEstimatedAvgPostage() {
        return estimatedAvgPostage;
    }

    public void setEstimatedAvgPostage(BigDecimal estimatedAvgPostage) {
        this.estimatedAvgPostage = estimatedAvgPostage;
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

    public String getFactoryCode() {
        return factoryCode;
    }

    public void setFactoryCode(String factoryCode) {
        this.factoryCode = factoryCode;
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

    public BigDecimal getDutyRate() {
        return dutyRate;
    }

    public void setDutyRate(BigDecimal dutyRate) {
        this.dutyRate = dutyRate;
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

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public List<AttachmentVo> getImagesDoc() {
        return imagesDoc;
    }

    public void setImagesDoc(List<AttachmentVo> imagesDoc) {
        this.imagesDoc = imagesDoc;
    }

    public Integer getIsApply() {
        return isApply;
    }

    public void setIsApply(Integer isApply) {
        this.isApply = isApply;
    }

    public String getFlagDevFlowId() {
        return flagDevFlowId;
    }

    public void setFlagDevFlowId(String flagDevFlowId) {
        this.flagDevFlowId = flagDevFlowId;
    }

    public String getFlagComplianceFlowId() {
        return flagComplianceFlowId;
    }

    public void setFlagComplianceFlowId(String flagComplianceFlowId) {
        this.flagComplianceFlowId = flagComplianceFlowId;
    }

    public String getFlagQcFlowId() {
        return flagQcFlowId;
    }

    public void setFlagQcFlowId(String flagQcFlowId) {
        this.flagQcFlowId = flagQcFlowId;
    }

    public String getVendorCode() {
        return vendorCode;
    }

    public void setVendorCode(String vendorCode) {
        this.vendorCode = vendorCode;
    }
}
