package com.newaim.purchase.archives.flow.shipping.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "na_service_provider_quotation_port")
public class ServiceProviderQuotationPort implements Serializable{

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.newaim.core.jpa.IdGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name = "prefix", value = "SPQP")})
    private String id;

    private String serviceProviderQuotationId;

    /**
     * 港口相关字段
     */
    private String originPortId;
    private String originPortCnName;
    private String originPortEnName;
    private String destinationPortId;
    private String destinationPortCnName;
    private String destinationPortEnName;
    private Integer sailingDays;
    private Integer frequency;

    /**结算货币、汇率*/
    private Integer currency;
    private BigDecimal rateAudToRmb;
    private BigDecimal rateAudToUsd;

    /**
     * 价格相关
     */
    @Column(name = "price_gp20_aud")
    private BigDecimal priceGp20Aud;
    @Column(name = "price_gp20_rmb")
    private BigDecimal priceGp20Rmb;
    @Column(name = "price_gp20_usd")
    private BigDecimal priceGp20Usd;
    @Column(name = "price_gp40_aud")
    private BigDecimal priceGp40Aud;
    @Column(name = "price_gp40_rmb")
    private BigDecimal priceGp40Rmb;
    @Column(name = "price_gp40_usd")
    private BigDecimal priceGp40Usd;
    @Column(name = "price_hq40_aud")
    private BigDecimal priceHq40Aud;
    @Column(name = "price_hq40_rmb")
    private BigDecimal priceHq40Rmb;
    @Column(name = "price_hq40_usd")
    private BigDecimal priceHq40Usd;
    private BigDecimal priceLclAud;
    private BigDecimal priceLclRmb;
    private BigDecimal priceLclUsd;

    @Column(name = "price_gp20_insurance_aud")
    private BigDecimal priceGp20InsuranceAud;
    @Column(name = "price_gp20_insurance_rmb")
    private BigDecimal priceGp20InsuranceRmb;
    @Column(name = "price_gp20_insurance_usd")
    private BigDecimal priceGp20InsuranceUsd;
    @Column(name = "price_gp40_insurance_aud")
    private BigDecimal priceGp40InsuranceAud;
    @Column(name = "price_gp40_insurance_rmb")
    private BigDecimal priceGp40InsuranceRmb;
    @Column(name = "price_gp40_insurance_usd")
    private BigDecimal priceGp40InsuranceUsd;
    @Column(name = "price_hq40_insurance_aud")
    private BigDecimal priceHq40InsuranceAud;
    @Column(name = "price_hq40_insurance_rmb")
    private BigDecimal priceHq40InsuranceRmb;
    @Column(name = "price_hq40_insurance_usd")
    private BigDecimal priceHq40InsuranceUsd;
    private BigDecimal priceLclInsuranceAud;
    private BigDecimal priceLclInsuranceRmb;
    private BigDecimal priceLclInsuranceUsd;

    /**柜量*/
    @Column(name = "gp20_qty")
    private BigDecimal gp20Qty;
    @Column(name = "gp40_qty")
    private BigDecimal gp40Qty;
    @Column(name = "hq40_qty")
    private BigDecimal hq40Qty;
    private BigDecimal lclCbm;

    private Integer prevSailingDays;
    private Integer prevFrequency;

    private BigDecimal prevRateAudToRmb;
    private BigDecimal prevRateAudToUsd;

    @Column(name = "prev_price_gp20_aud")
    private BigDecimal prevPriceGp20Aud;
    @Column(name = "prev_price_gp20_rmb")
    private BigDecimal prevPriceGp20Rmb;
    @Column(name = "prev_price_gp20_usd")
    private BigDecimal prevPriceGp20Usd;
    @Column(name = "prev_price_gp40_aud")
    private BigDecimal prevPriceGp40Aud;
    @Column(name = "prev_price_gp40_rmb")
    private BigDecimal prevPriceGp40Rmb;
    @Column(name = "prev_price_gp40_usd")
    private BigDecimal prevPriceGp40Usd;
    @Column(name = "prev_price_hq40_aud")
    private BigDecimal prevPriceHq40Aud;
    @Column(name = "prev_price_hq40_rmb")
    private BigDecimal prevPriceHq40Rmb;
    @Column(name = "prev_price_hq40_usd")
    private BigDecimal prevPriceHq40Usd;
    private BigDecimal prevPriceLclAud;
    private BigDecimal prevPriceLclRmb;
    private BigDecimal prevPriceLclUsd;
    @Column(name = "prev_price_gp20_insurance_aud")
    private BigDecimal prevPriceGp20InsuranceAud;
    @Column(name = "prev_price_gp20_insurance_rmb")
    private BigDecimal prevPriceGp20InsuranceRmb;
    @Column(name = "prev_price_gp20_insurance_usd")
    private BigDecimal prevPriceGp20InsuranceUsd;
    @Column(name = "prev_price_gp40_insurance_aud")
    private BigDecimal prevPriceGp40InsuranceAud;
    @Column(name = "prev_price_gp40_insurance_rmb")
    private BigDecimal prevPriceGp40InsuranceRmb;
    @Column(name = "prev_price_gp40_insurance_usd")
    private BigDecimal prevPriceGp40InsuranceUsd;
    @Column(name = "prev_price_hq40_insurance_aud")
    private BigDecimal prevPriceHq40InsuranceAud;
    @Column(name = "prev_price_hq40_insurance_rmb")
    private BigDecimal prevPriceHq40InsuranceRmb;
    @Column(name = "prev_price_hq40_insurance_usd")
    private BigDecimal prevPriceHq40InsuranceUsd;
    private BigDecimal prevPriceLclInsuranceAud;
    private BigDecimal prevPriceLclInsuranceRmb;
    private BigDecimal prevPriceLclInsuranceUsd;

    /**上次柜量*/
    @Column(name = "prev_gp20_qty")
    private BigDecimal prevGp20Qty;
    @Column(name = "prev_gp40_qty")
    private BigDecimal prevGp40Qty;
    @Column(name = "prev_hq40_qty")
    private BigDecimal prevHq40Qty;
    private BigDecimal prevLclCbm;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceProviderQuotationId() {
        return serviceProviderQuotationId;
    }

    public void setServiceProviderQuotationId(String serviceProviderQuotationId) {
        this.serviceProviderQuotationId = serviceProviderQuotationId;
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

    public Integer getSailingDays() {
        return sailingDays;
    }

    public void setSailingDays(Integer sailingDays) {
        this.sailingDays = sailingDays;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public Integer getCurrency() {
        return currency;
    }

    public void setCurrency(Integer currency) {
        this.currency = currency;
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

    public BigDecimal getPriceGp20Aud() {
        return priceGp20Aud;
    }

    public void setPriceGp20Aud(BigDecimal priceGp20Aud) {
        this.priceGp20Aud = priceGp20Aud;
    }

    public BigDecimal getPriceGp20Rmb() {
        return priceGp20Rmb;
    }

    public void setPriceGp20Rmb(BigDecimal priceGp20Rmb) {
        this.priceGp20Rmb = priceGp20Rmb;
    }

    public BigDecimal getPriceGp20Usd() {
        return priceGp20Usd;
    }

    public void setPriceGp20Usd(BigDecimal priceGp20Usd) {
        this.priceGp20Usd = priceGp20Usd;
    }

    public BigDecimal getPriceGp40Aud() {
        return priceGp40Aud;
    }

    public void setPriceGp40Aud(BigDecimal priceGp40Aud) {
        this.priceGp40Aud = priceGp40Aud;
    }

    public BigDecimal getPriceGp40Rmb() {
        return priceGp40Rmb;
    }

    public void setPriceGp40Rmb(BigDecimal priceGp40Rmb) {
        this.priceGp40Rmb = priceGp40Rmb;
    }

    public BigDecimal getPriceGp40Usd() {
        return priceGp40Usd;
    }

    public void setPriceGp40Usd(BigDecimal priceGp40Usd) {
        this.priceGp40Usd = priceGp40Usd;
    }

    public BigDecimal getPriceHq40Aud() {
        return priceHq40Aud;
    }

    public void setPriceHq40Aud(BigDecimal priceHq40Aud) {
        this.priceHq40Aud = priceHq40Aud;
    }

    public BigDecimal getPriceHq40Rmb() {
        return priceHq40Rmb;
    }

    public void setPriceHq40Rmb(BigDecimal priceHq40Rmb) {
        this.priceHq40Rmb = priceHq40Rmb;
    }

    public BigDecimal getPriceHq40Usd() {
        return priceHq40Usd;
    }

    public void setPriceHq40Usd(BigDecimal priceHq40Usd) {
        this.priceHq40Usd = priceHq40Usd;
    }

    public BigDecimal getPriceLclAud() {
        return priceLclAud;
    }

    public void setPriceLclAud(BigDecimal priceLclAud) {
        this.priceLclAud = priceLclAud;
    }

    public BigDecimal getPriceLclRmb() {
        return priceLclRmb;
    }

    public void setPriceLclRmb(BigDecimal priceLclRmb) {
        this.priceLclRmb = priceLclRmb;
    }

    public BigDecimal getPriceLclUsd() {
        return priceLclUsd;
    }

    public void setPriceLclUsd(BigDecimal priceLclUsd) {
        this.priceLclUsd = priceLclUsd;
    }

    public BigDecimal getPriceGp20InsuranceAud() {
        return priceGp20InsuranceAud;
    }

    public void setPriceGp20InsuranceAud(BigDecimal priceGp20InsuranceAud) {
        this.priceGp20InsuranceAud = priceGp20InsuranceAud;
    }

    public BigDecimal getPriceGp20InsuranceRmb() {
        return priceGp20InsuranceRmb;
    }

    public void setPriceGp20InsuranceRmb(BigDecimal priceGp20InsuranceRmb) {
        this.priceGp20InsuranceRmb = priceGp20InsuranceRmb;
    }

    public BigDecimal getPriceGp20InsuranceUsd() {
        return priceGp20InsuranceUsd;
    }

    public void setPriceGp20InsuranceUsd(BigDecimal priceGp20InsuranceUsd) {
        this.priceGp20InsuranceUsd = priceGp20InsuranceUsd;
    }

    public BigDecimal getPriceGp40InsuranceAud() {
        return priceGp40InsuranceAud;
    }

    public void setPriceGp40InsuranceAud(BigDecimal priceGp40InsuranceAud) {
        this.priceGp40InsuranceAud = priceGp40InsuranceAud;
    }

    public BigDecimal getPriceGp40InsuranceRmb() {
        return priceGp40InsuranceRmb;
    }

    public void setPriceGp40InsuranceRmb(BigDecimal priceGp40InsuranceRmb) {
        this.priceGp40InsuranceRmb = priceGp40InsuranceRmb;
    }

    public BigDecimal getPriceGp40InsuranceUsd() {
        return priceGp40InsuranceUsd;
    }

    public void setPriceGp40InsuranceUsd(BigDecimal priceGp40InsuranceUsd) {
        this.priceGp40InsuranceUsd = priceGp40InsuranceUsd;
    }

    public BigDecimal getPriceHq40InsuranceAud() {
        return priceHq40InsuranceAud;
    }

    public void setPriceHq40InsuranceAud(BigDecimal priceHq40InsuranceAud) {
        this.priceHq40InsuranceAud = priceHq40InsuranceAud;
    }

    public BigDecimal getPriceHq40InsuranceRmb() {
        return priceHq40InsuranceRmb;
    }

    public void setPriceHq40InsuranceRmb(BigDecimal priceHq40InsuranceRmb) {
        this.priceHq40InsuranceRmb = priceHq40InsuranceRmb;
    }

    public BigDecimal getPriceHq40InsuranceUsd() {
        return priceHq40InsuranceUsd;
    }

    public void setPriceHq40InsuranceUsd(BigDecimal priceHq40InsuranceUsd) {
        this.priceHq40InsuranceUsd = priceHq40InsuranceUsd;
    }

    public BigDecimal getPriceLclInsuranceAud() {
        return priceLclInsuranceAud;
    }

    public void setPriceLclInsuranceAud(BigDecimal priceLclInsuranceAud) {
        this.priceLclInsuranceAud = priceLclInsuranceAud;
    }

    public BigDecimal getPriceLclInsuranceRmb() {
        return priceLclInsuranceRmb;
    }

    public void setPriceLclInsuranceRmb(BigDecimal priceLclInsuranceRmb) {
        this.priceLclInsuranceRmb = priceLclInsuranceRmb;
    }

    public BigDecimal getPriceLclInsuranceUsd() {
        return priceLclInsuranceUsd;
    }

    public void setPriceLclInsuranceUsd(BigDecimal priceLclInsuranceUsd) {
        this.priceLclInsuranceUsd = priceLclInsuranceUsd;
    }

    public BigDecimal getGp20Qty() {
        return gp20Qty;
    }

    public void setGp20Qty(BigDecimal gp20Qty) {
        this.gp20Qty = gp20Qty;
    }

    public BigDecimal getGp40Qty() {
        return gp40Qty;
    }

    public void setGp40Qty(BigDecimal gp40Qty) {
        this.gp40Qty = gp40Qty;
    }

    public BigDecimal getHq40Qty() {
        return hq40Qty;
    }

    public void setHq40Qty(BigDecimal hq40Qty) {
        this.hq40Qty = hq40Qty;
    }

    public BigDecimal getLclCbm() {
        return lclCbm;
    }

    public void setLclCbm(BigDecimal lclCbm) {
        this.lclCbm = lclCbm;
    }

    public Integer getPrevSailingDays() {
        return prevSailingDays;
    }

    public void setPrevSailingDays(Integer prevSailingDays) {
        this.prevSailingDays = prevSailingDays;
    }

    public Integer getPrevFrequency() {
        return prevFrequency;
    }

    public void setPrevFrequency(Integer prevFrequency) {
        this.prevFrequency = prevFrequency;
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

    public BigDecimal getPrevPriceGp20Aud() {
        return prevPriceGp20Aud;
    }

    public void setPrevPriceGp20Aud(BigDecimal prevPriceGp20Aud) {
        this.prevPriceGp20Aud = prevPriceGp20Aud;
    }

    public BigDecimal getPrevPriceGp20Rmb() {
        return prevPriceGp20Rmb;
    }

    public void setPrevPriceGp20Rmb(BigDecimal prevPriceGp20Rmb) {
        this.prevPriceGp20Rmb = prevPriceGp20Rmb;
    }

    public BigDecimal getPrevPriceGp20Usd() {
        return prevPriceGp20Usd;
    }

    public void setPrevPriceGp20Usd(BigDecimal prevPriceGp20Usd) {
        this.prevPriceGp20Usd = prevPriceGp20Usd;
    }

    public BigDecimal getPrevPriceGp40Aud() {
        return prevPriceGp40Aud;
    }

    public void setPrevPriceGp40Aud(BigDecimal prevPriceGp40Aud) {
        this.prevPriceGp40Aud = prevPriceGp40Aud;
    }

    public BigDecimal getPrevPriceGp40Rmb() {
        return prevPriceGp40Rmb;
    }

    public void setPrevPriceGp40Rmb(BigDecimal prevPriceGp40Rmb) {
        this.prevPriceGp40Rmb = prevPriceGp40Rmb;
    }

    public BigDecimal getPrevPriceGp40Usd() {
        return prevPriceGp40Usd;
    }

    public void setPrevPriceGp40Usd(BigDecimal prevPriceGp40Usd) {
        this.prevPriceGp40Usd = prevPriceGp40Usd;
    }

    public BigDecimal getPrevPriceHq40Aud() {
        return prevPriceHq40Aud;
    }

    public void setPrevPriceHq40Aud(BigDecimal prevPriceHq40Aud) {
        this.prevPriceHq40Aud = prevPriceHq40Aud;
    }

    public BigDecimal getPrevPriceHq40Rmb() {
        return prevPriceHq40Rmb;
    }

    public void setPrevPriceHq40Rmb(BigDecimal prevPriceHq40Rmb) {
        this.prevPriceHq40Rmb = prevPriceHq40Rmb;
    }

    public BigDecimal getPrevPriceHq40Usd() {
        return prevPriceHq40Usd;
    }

    public void setPrevPriceHq40Usd(BigDecimal prevPriceHq40Usd) {
        this.prevPriceHq40Usd = prevPriceHq40Usd;
    }

    public BigDecimal getPrevPriceLclAud() {
        return prevPriceLclAud;
    }

    public void setPrevPriceLclAud(BigDecimal prevPriceLclAud) {
        this.prevPriceLclAud = prevPriceLclAud;
    }

    public BigDecimal getPrevPriceLclRmb() {
        return prevPriceLclRmb;
    }

    public void setPrevPriceLclRmb(BigDecimal prevPriceLclRmb) {
        this.prevPriceLclRmb = prevPriceLclRmb;
    }

    public BigDecimal getPrevPriceLclUsd() {
        return prevPriceLclUsd;
    }

    public void setPrevPriceLclUsd(BigDecimal prevPriceLclUsd) {
        this.prevPriceLclUsd = prevPriceLclUsd;
    }

    public BigDecimal getPrevPriceGp20InsuranceAud() {
        return prevPriceGp20InsuranceAud;
    }

    public void setPrevPriceGp20InsuranceAud(BigDecimal prevPriceGp20InsuranceAud) {
        this.prevPriceGp20InsuranceAud = prevPriceGp20InsuranceAud;
    }

    public BigDecimal getPrevPriceGp20InsuranceRmb() {
        return prevPriceGp20InsuranceRmb;
    }

    public void setPrevPriceGp20InsuranceRmb(BigDecimal prevPriceGp20InsuranceRmb) {
        this.prevPriceGp20InsuranceRmb = prevPriceGp20InsuranceRmb;
    }

    public BigDecimal getPrevPriceGp20InsuranceUsd() {
        return prevPriceGp20InsuranceUsd;
    }

    public void setPrevPriceGp20InsuranceUsd(BigDecimal prevPriceGp20InsuranceUsd) {
        this.prevPriceGp20InsuranceUsd = prevPriceGp20InsuranceUsd;
    }

    public BigDecimal getPrevPriceGp40InsuranceAud() {
        return prevPriceGp40InsuranceAud;
    }

    public void setPrevPriceGp40InsuranceAud(BigDecimal prevPriceGp40InsuranceAud) {
        this.prevPriceGp40InsuranceAud = prevPriceGp40InsuranceAud;
    }

    public BigDecimal getPrevPriceGp40InsuranceRmb() {
        return prevPriceGp40InsuranceRmb;
    }

    public void setPrevPriceGp40InsuranceRmb(BigDecimal prevPriceGp40InsuranceRmb) {
        this.prevPriceGp40InsuranceRmb = prevPriceGp40InsuranceRmb;
    }

    public BigDecimal getPrevPriceGp40InsuranceUsd() {
        return prevPriceGp40InsuranceUsd;
    }

    public void setPrevPriceGp40InsuranceUsd(BigDecimal prevPriceGp40InsuranceUsd) {
        this.prevPriceGp40InsuranceUsd = prevPriceGp40InsuranceUsd;
    }

    public BigDecimal getPrevPriceHq40InsuranceAud() {
        return prevPriceHq40InsuranceAud;
    }

    public void setPrevPriceHq40InsuranceAud(BigDecimal prevPriceHq40InsuranceAud) {
        this.prevPriceHq40InsuranceAud = prevPriceHq40InsuranceAud;
    }

    public BigDecimal getPrevPriceHq40InsuranceRmb() {
        return prevPriceHq40InsuranceRmb;
    }

    public void setPrevPriceHq40InsuranceRmb(BigDecimal prevPriceHq40InsuranceRmb) {
        this.prevPriceHq40InsuranceRmb = prevPriceHq40InsuranceRmb;
    }

    public BigDecimal getPrevPriceHq40InsuranceUsd() {
        return prevPriceHq40InsuranceUsd;
    }

    public void setPrevPriceHq40InsuranceUsd(BigDecimal prevPriceHq40InsuranceUsd) {
        this.prevPriceHq40InsuranceUsd = prevPriceHq40InsuranceUsd;
    }

    public BigDecimal getPrevPriceLclInsuranceAud() {
        return prevPriceLclInsuranceAud;
    }

    public void setPrevPriceLclInsuranceAud(BigDecimal prevPriceLclInsuranceAud) {
        this.prevPriceLclInsuranceAud = prevPriceLclInsuranceAud;
    }

    public BigDecimal getPrevPriceLclInsuranceRmb() {
        return prevPriceLclInsuranceRmb;
    }

    public void setPrevPriceLclInsuranceRmb(BigDecimal prevPriceLclInsuranceRmb) {
        this.prevPriceLclInsuranceRmb = prevPriceLclInsuranceRmb;
    }

    public BigDecimal getPrevPriceLclInsuranceUsd() {
        return prevPriceLclInsuranceUsd;
    }

    public void setPrevPriceLclInsuranceUsd(BigDecimal prevPriceLclInsuranceUsd) {
        this.prevPriceLclInsuranceUsd = prevPriceLclInsuranceUsd;
    }

    public BigDecimal getPrevGp20Qty() {
        return prevGp20Qty;
    }

    public void setPrevGp20Qty(BigDecimal prevGp20Qty) {
        this.prevGp20Qty = prevGp20Qty;
    }

    public BigDecimal getPrevGp40Qty() {
        return prevGp40Qty;
    }

    public void setPrevGp40Qty(BigDecimal prevGp40Qty) {
        this.prevGp40Qty = prevGp40Qty;
    }

    public BigDecimal getPrevHq40Qty() {
        return prevHq40Qty;
    }

    public void setPrevHq40Qty(BigDecimal prevHq40Qty) {
        this.prevHq40Qty = prevHq40Qty;
    }

    public BigDecimal getPrevLclCbm() {
        return prevLclCbm;
    }

    public void setPrevLclCbm(BigDecimal prevLclCbm) {
        this.prevLclCbm = prevLclCbm;
    }
}
