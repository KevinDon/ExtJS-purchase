package com.newaim.purchase.desktop.sta.vo;

import java.io.Serializable;
import java.math.BigDecimal;

public class StaProblemTeamVo implements Serializable{

    private String id;
    private String departmentId;
    private String departmentCnName;
    private String departmentEnName;
    private String sku;

    private BigDecimal courierIssueUnknownReasonAmountAud;
    private BigDecimal courierIssueUnknownReasonAmountRmb;
    private BigDecimal courierIssueUnknownReasonAmountUsd;
    private Integer courierIssueUnknownReasonQty;

    private BigDecimal courierIssueLostAlliedAmountAud;
    private BigDecimal courierIssueLostAlliedAmountRmb;
    private BigDecimal courierIssueLostAlliedAmountUsd;
    private Integer courierIssueLostAlliedQty;
    private BigDecimal courierIssueLostAupostAmountAud;
    private BigDecimal courierIssueLostAupostAmountRmb;
    private BigDecimal courierIssueLostAupostAmountUsd;
    private Integer courierIssueLostAupostQty;
    private BigDecimal courierIssueLostFastwayAmountAud;
    private BigDecimal courierIssueLostFastwayAmountRmb;
    private BigDecimal courierIssueLostFastwayAmountUsd;
    private Integer courierIssueLostFastwayQty;
    private BigDecimal courierIssueDamagedAmountAud;
    private BigDecimal courierIssueDamagedAmountRmb;
    private BigDecimal courierIssueDamagedAmountUsd;
    private Integer courierIssueDamagedQty;
    private BigDecimal courierIssueDelayedAmountAud;
    private BigDecimal courierIssueDelayedAmountRmb;
    private BigDecimal courierIssueDelayedAmountUsd;
    private Integer courierIssueDelayedQty;
    private BigDecimal courierIssueLostDirectAmountAud;
    private BigDecimal courierIssueLostDirectAmountRmb;
    private BigDecimal courierIssueLostDirectAmountUsd;
    private Integer courierIssueLostDirectQty;
    private BigDecimal customerOrderCancelledAmountAud;
    private BigDecimal customerOrderCancelledAmountRmb;
    private BigDecimal customerOrderCancelledAmountUsd;
    private Integer customerOrderCancelledQty;
    private BigDecimal customerIncorrectAddressAmountAud;
    private BigDecimal customerIncorrectAddressAmountRmb;
    private BigDecimal customerIncorrectAddressAmountUsd;
    private Integer customerIncorrectAddressQty;
    private BigDecimal customerOrderedIncorrectAmountAud;
    private BigDecimal customerOrderedIncorrectAmountRmb;
    private BigDecimal customerOrderedIncorrectAmountUsd;
    private Integer customerOrderedIncorrectQty;
    private BigDecimal customerPaymentAmountAud;
    private BigDecimal customerPaymentAmountRmb;
    private BigDecimal customerPaymentAmountUsd;
    private Integer customerPaymentQty;

    private BigDecimal customerPriceDiffAmountAud;
    private BigDecimal customerPriceDiffAmountRmb;
    private BigDecimal customerPriceDiffAmountUsd;
    private Integer customerPriceDiffQty;

    private BigDecimal dispatchIssueWrongItemAmountAud;
    private BigDecimal dispatchIssueWrongItemAmountRmb;
    private BigDecimal dispatchIssueWrongItemAmountUsd;
    private Integer dispatchIssueWrongItemQty;
    private BigDecimal dispatchIssueWrongSparePartAmountAud;
    private BigDecimal dispatchIssueWrongSparePartAmountRmb;
    private BigDecimal dispatchIssueWrongSparePartAmountUsd;
    private Integer dispatchIssueWrongSparePartQty;
    private BigDecimal dispatchIssueNotDispatchedAmountAud;
    private BigDecimal dispatchIssueNotDispatchedAmountRmb;
    private BigDecimal dispatchIssueNotDispatchedAmountUsd;
    private Integer dispatchIssueNotDispatchedQty;
    private BigDecimal listingIssueStockOutAmountAud;
    private BigDecimal listingIssueStockOutAmountRmb;
    private BigDecimal listingIssueStockOutAmountUsd;
    private Integer listingIssueStockOutQty;
    private BigDecimal listingIssueDescribedAmountAud;
    private BigDecimal listingIssueDescribedAmountRmb;
    private BigDecimal listingIssueDescribedAmountUsd;
    private Integer listingIssueDescribedQty;
    private BigDecimal listingIssueNoDeliveryAreaAmountAud;
    private BigDecimal listingIssueNoDeliveryAreaAmountRmb;
    private BigDecimal listingIssueNoDeliveryAreaAmountUsd;
    private Integer listingIssueNoDeliveryAreaQty;
    private BigDecimal othersSystemErrorAmountAud;
    private BigDecimal othersSystemErrorAmountRmb;
    private BigDecimal othersSystemErrorAmountUsd;
    private Integer othersSystemErrorQty;
    private BigDecimal othersFraudulentPurchaseAmountAud;
    private BigDecimal othersFraudulentPurchaseAmountRmb;
    private BigDecimal othersFraudulentPurchaseAmountUsd;
    private Integer othersFraudulentPurchaseQty;
    private BigDecimal othersPoorCommunicationAmountAud;
    private BigDecimal othersPoorCommunicationAmountRmb;
    private BigDecimal othersPoorCommunicationAmountUsd;
    private Integer othersPoorCommunicationQty;
    private BigDecimal productQualityMissDamagedPartsAmountAud;
    private BigDecimal productQualityMissDamagedPartsAmountRmb;
    private BigDecimal productQualityMissDamagedPartsAmountUsd;
    private Integer productQualityMissDamagedPartsQty;
    private BigDecimal productQualityCannotBeUsedAmountAud;
    private BigDecimal productQualityCannotBeUsedAmountRmb;
    private BigDecimal productQualityCannotBeUsedAmountUsd;
    private Integer productQualityCannotBeUsedQty;
    private BigDecimal recallAmountAud;
    private BigDecimal recallAmountRmb;
    private BigDecimal recallAmountUsd;
    private Integer recallQty;

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentCnName() {
        return departmentCnName;
    }

    public void setDepartmentCnName(String departmentCnName) {
        this.departmentCnName = departmentCnName;
    }

    public String getDepartmentEnName() {
        return departmentEnName;
    }

    public void setDepartmentEnName(String departmentEnName) {
        this.departmentEnName = departmentEnName;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getCourierIssueUnknownReasonAmountAud() {
        return courierIssueUnknownReasonAmountAud;
    }

    public void setCourierIssueUnknownReasonAmountAud(BigDecimal courierIssueUnknownReasonAmountAud) {
        this.courierIssueUnknownReasonAmountAud = courierIssueUnknownReasonAmountAud;
    }

    public BigDecimal getCourierIssueUnknownReasonAmountRmb() {
        return courierIssueUnknownReasonAmountRmb;
    }

    public void setCourierIssueUnknownReasonAmountRmb(BigDecimal courierIssueUnknownReasonAmountRmb) {
        this.courierIssueUnknownReasonAmountRmb = courierIssueUnknownReasonAmountRmb;
    }

    public BigDecimal getCourierIssueUnknownReasonAmountUsd() {
        return courierIssueUnknownReasonAmountUsd;
    }

    public void setCourierIssueUnknownReasonAmountUsd(BigDecimal courierIssueUnknownReasonAmountUsd) {
        this.courierIssueUnknownReasonAmountUsd = courierIssueUnknownReasonAmountUsd;
    }

    public Integer getCourierIssueUnknownReasonQty() {
        return courierIssueUnknownReasonQty;
    }

    public void setCourierIssueUnknownReasonQty(Integer courierIssueUnknownReasonQty) {
        this.courierIssueUnknownReasonQty = courierIssueUnknownReasonQty;
    }

    public BigDecimal getCourierIssueLostAlliedAmountAud() {
        return courierIssueLostAlliedAmountAud;
    }

    public void setCourierIssueLostAlliedAmountAud(BigDecimal courierIssueLostAlliedAmountAud) {
        this.courierIssueLostAlliedAmountAud = courierIssueLostAlliedAmountAud;
    }

    public BigDecimal getCourierIssueLostAlliedAmountRmb() {
        return courierIssueLostAlliedAmountRmb;
    }

    public void setCourierIssueLostAlliedAmountRmb(BigDecimal courierIssueLostAlliedAmountRmb) {
        this.courierIssueLostAlliedAmountRmb = courierIssueLostAlliedAmountRmb;
    }

    public BigDecimal getCourierIssueLostAlliedAmountUsd() {
        return courierIssueLostAlliedAmountUsd;
    }

    public void setCourierIssueLostAlliedAmountUsd(BigDecimal courierIssueLostAlliedAmountUsd) {
        this.courierIssueLostAlliedAmountUsd = courierIssueLostAlliedAmountUsd;
    }

    public Integer getCourierIssueLostAlliedQty() {
        return courierIssueLostAlliedQty;
    }

    public void setCourierIssueLostAlliedQty(Integer courierIssueLostAlliedQty) {
        this.courierIssueLostAlliedQty = courierIssueLostAlliedQty;
    }

    public BigDecimal getCourierIssueLostAupostAmountAud() {
        return courierIssueLostAupostAmountAud;
    }

    public void setCourierIssueLostAupostAmountAud(BigDecimal courierIssueLostAupostAmountAud) {
        this.courierIssueLostAupostAmountAud = courierIssueLostAupostAmountAud;
    }

    public BigDecimal getCourierIssueLostAupostAmountRmb() {
        return courierIssueLostAupostAmountRmb;
    }

    public void setCourierIssueLostAupostAmountRmb(BigDecimal courierIssueLostAupostAmountRmb) {
        this.courierIssueLostAupostAmountRmb = courierIssueLostAupostAmountRmb;
    }

    public BigDecimal getCourierIssueLostAupostAmountUsd() {
        return courierIssueLostAupostAmountUsd;
    }

    public void setCourierIssueLostAupostAmountUsd(BigDecimal courierIssueLostAupostAmountUsd) {
        this.courierIssueLostAupostAmountUsd = courierIssueLostAupostAmountUsd;
    }

    public Integer getCourierIssueLostAupostQty() {
        return courierIssueLostAupostQty;
    }

    public void setCourierIssueLostAupostQty(Integer courierIssueLostAupostQty) {
        this.courierIssueLostAupostQty = courierIssueLostAupostQty;
    }

    public BigDecimal getCourierIssueLostFastwayAmountAud() {
        return courierIssueLostFastwayAmountAud;
    }

    public void setCourierIssueLostFastwayAmountAud(BigDecimal courierIssueLostFastwayAmountAud) {
        this.courierIssueLostFastwayAmountAud = courierIssueLostFastwayAmountAud;
    }

    public BigDecimal getCourierIssueLostFastwayAmountRmb() {
        return courierIssueLostFastwayAmountRmb;
    }

    public void setCourierIssueLostFastwayAmountRmb(BigDecimal courierIssueLostFastwayAmountRmb) {
        this.courierIssueLostFastwayAmountRmb = courierIssueLostFastwayAmountRmb;
    }

    public BigDecimal getCourierIssueLostFastwayAmountUsd() {
        return courierIssueLostFastwayAmountUsd;
    }

    public void setCourierIssueLostFastwayAmountUsd(BigDecimal courierIssueLostFastwayAmountUsd) {
        this.courierIssueLostFastwayAmountUsd = courierIssueLostFastwayAmountUsd;
    }

    public Integer getCourierIssueLostFastwayQty() {
        return courierIssueLostFastwayQty;
    }

    public void setCourierIssueLostFastwayQty(Integer courierIssueLostFastwayQty) {
        this.courierIssueLostFastwayQty = courierIssueLostFastwayQty;
    }

    public BigDecimal getCourierIssueDamagedAmountAud() {
        return courierIssueDamagedAmountAud;
    }

    public void setCourierIssueDamagedAmountAud(BigDecimal courierIssueDamagedAmountAud) {
        this.courierIssueDamagedAmountAud = courierIssueDamagedAmountAud;
    }

    public BigDecimal getCourierIssueDamagedAmountRmb() {
        return courierIssueDamagedAmountRmb;
    }

    public void setCourierIssueDamagedAmountRmb(BigDecimal courierIssueDamagedAmountRmb) {
        this.courierIssueDamagedAmountRmb = courierIssueDamagedAmountRmb;
    }

    public BigDecimal getCourierIssueDamagedAmountUsd() {
        return courierIssueDamagedAmountUsd;
    }

    public void setCourierIssueDamagedAmountUsd(BigDecimal courierIssueDamagedAmountUsd) {
        this.courierIssueDamagedAmountUsd = courierIssueDamagedAmountUsd;
    }

    public Integer getCourierIssueDamagedQty() {
        return courierIssueDamagedQty;
    }

    public void setCourierIssueDamagedQty(Integer courierIssueDamagedQty) {
        this.courierIssueDamagedQty = courierIssueDamagedQty;
    }

    public BigDecimal getCourierIssueDelayedAmountAud() {
        return courierIssueDelayedAmountAud;
    }

    public void setCourierIssueDelayedAmountAud(BigDecimal courierIssueDelayedAmountAud) {
        this.courierIssueDelayedAmountAud = courierIssueDelayedAmountAud;
    }

    public BigDecimal getCourierIssueDelayedAmountRmb() {
        return courierIssueDelayedAmountRmb;
    }

    public void setCourierIssueDelayedAmountRmb(BigDecimal courierIssueDelayedAmountRmb) {
        this.courierIssueDelayedAmountRmb = courierIssueDelayedAmountRmb;
    }

    public BigDecimal getCourierIssueDelayedAmountUsd() {
        return courierIssueDelayedAmountUsd;
    }

    public void setCourierIssueDelayedAmountUsd(BigDecimal courierIssueDelayedAmountUsd) {
        this.courierIssueDelayedAmountUsd = courierIssueDelayedAmountUsd;
    }

    public Integer getCourierIssueDelayedQty() {
        return courierIssueDelayedQty;
    }

    public void setCourierIssueDelayedQty(Integer courierIssueDelayedQty) {
        this.courierIssueDelayedQty = courierIssueDelayedQty;
    }

    public BigDecimal getCourierIssueLostDirectAmountAud() {
        return courierIssueLostDirectAmountAud;
    }

    public void setCourierIssueLostDirectAmountAud(BigDecimal courierIssueLostDirectAmountAud) {
        this.courierIssueLostDirectAmountAud = courierIssueLostDirectAmountAud;
    }

    public BigDecimal getCourierIssueLostDirectAmountRmb() {
        return courierIssueLostDirectAmountRmb;
    }

    public void setCourierIssueLostDirectAmountRmb(BigDecimal courierIssueLostDirectAmountRmb) {
        this.courierIssueLostDirectAmountRmb = courierIssueLostDirectAmountRmb;
    }

    public BigDecimal getCourierIssueLostDirectAmountUsd() {
        return courierIssueLostDirectAmountUsd;
    }

    public void setCourierIssueLostDirectAmountUsd(BigDecimal courierIssueLostDirectAmountUsd) {
        this.courierIssueLostDirectAmountUsd = courierIssueLostDirectAmountUsd;
    }

    public Integer getCourierIssueLostDirectQty() {
        return courierIssueLostDirectQty;
    }

    public void setCourierIssueLostDirectQty(Integer courierIssueLostDirectQty) {
        this.courierIssueLostDirectQty = courierIssueLostDirectQty;
    }

    public BigDecimal getCustomerOrderCancelledAmountAud() {
        return customerOrderCancelledAmountAud;
    }

    public void setCustomerOrderCancelledAmountAud(BigDecimal customerOrderCancelledAmountAud) {
        this.customerOrderCancelledAmountAud = customerOrderCancelledAmountAud;
    }

    public BigDecimal getCustomerOrderCancelledAmountRmb() {
        return customerOrderCancelledAmountRmb;
    }

    public void setCustomerOrderCancelledAmountRmb(BigDecimal customerOrderCancelledAmountRmb) {
        this.customerOrderCancelledAmountRmb = customerOrderCancelledAmountRmb;
    }

    public BigDecimal getCustomerOrderCancelledAmountUsd() {
        return customerOrderCancelledAmountUsd;
    }

    public void setCustomerOrderCancelledAmountUsd(BigDecimal customerOrderCancelledAmountUsd) {
        this.customerOrderCancelledAmountUsd = customerOrderCancelledAmountUsd;
    }

    public Integer getCustomerOrderCancelledQty() {
        return customerOrderCancelledQty;
    }

    public void setCustomerOrderCancelledQty(Integer customerOrderCancelledQty) {
        this.customerOrderCancelledQty = customerOrderCancelledQty;
    }

    public BigDecimal getCustomerIncorrectAddressAmountAud() {
        return customerIncorrectAddressAmountAud;
    }

    public void setCustomerIncorrectAddressAmountAud(BigDecimal customerIncorrectAddressAmountAud) {
        this.customerIncorrectAddressAmountAud = customerIncorrectAddressAmountAud;
    }

    public BigDecimal getCustomerIncorrectAddressAmountRmb() {
        return customerIncorrectAddressAmountRmb;
    }

    public void setCustomerIncorrectAddressAmountRmb(BigDecimal customerIncorrectAddressAmountRmb) {
        this.customerIncorrectAddressAmountRmb = customerIncorrectAddressAmountRmb;
    }

    public BigDecimal getCustomerIncorrectAddressAmountUsd() {
        return customerIncorrectAddressAmountUsd;
    }

    public void setCustomerIncorrectAddressAmountUsd(BigDecimal customerIncorrectAddressAmountUsd) {
        this.customerIncorrectAddressAmountUsd = customerIncorrectAddressAmountUsd;
    }

    public Integer getCustomerIncorrectAddressQty() {
        return customerIncorrectAddressQty;
    }

    public void setCustomerIncorrectAddressQty(Integer customerIncorrectAddressQty) {
        this.customerIncorrectAddressQty = customerIncorrectAddressQty;
    }

    public BigDecimal getCustomerOrderedIncorrectAmountAud() {
        return customerOrderedIncorrectAmountAud;
    }

    public void setCustomerOrderedIncorrectAmountAud(BigDecimal customerOrderedIncorrectAmountAud) {
        this.customerOrderedIncorrectAmountAud = customerOrderedIncorrectAmountAud;
    }

    public BigDecimal getCustomerOrderedIncorrectAmountRmb() {
        return customerOrderedIncorrectAmountRmb;
    }

    public void setCustomerOrderedIncorrectAmountRmb(BigDecimal customerOrderedIncorrectAmountRmb) {
        this.customerOrderedIncorrectAmountRmb = customerOrderedIncorrectAmountRmb;
    }

    public BigDecimal getCustomerOrderedIncorrectAmountUsd() {
        return customerOrderedIncorrectAmountUsd;
    }

    public void setCustomerOrderedIncorrectAmountUsd(BigDecimal customerOrderedIncorrectAmountUsd) {
        this.customerOrderedIncorrectAmountUsd = customerOrderedIncorrectAmountUsd;
    }

    public Integer getCustomerOrderedIncorrectQty() {
        return customerOrderedIncorrectQty;
    }

    public void setCustomerOrderedIncorrectQty(Integer customerOrderedIncorrectQty) {
        this.customerOrderedIncorrectQty = customerOrderedIncorrectQty;
    }

    public BigDecimal getCustomerPaymentAmountAud() {
        return customerPaymentAmountAud;
    }

    public void setCustomerPaymentAmountAud(BigDecimal customerPaymentAmountAud) {
        this.customerPaymentAmountAud = customerPaymentAmountAud;
    }

    public BigDecimal getCustomerPaymentAmountRmb() {
        return customerPaymentAmountRmb;
    }

    public void setCustomerPaymentAmountRmb(BigDecimal customerPaymentAmountRmb) {
        this.customerPaymentAmountRmb = customerPaymentAmountRmb;
    }

    public BigDecimal getCustomerPaymentAmountUsd() {
        return customerPaymentAmountUsd;
    }

    public void setCustomerPaymentAmountUsd(BigDecimal customerPaymentAmountUsd) {
        this.customerPaymentAmountUsd = customerPaymentAmountUsd;
    }

    public Integer getCustomerPaymentQty() {
        return customerPaymentQty;
    }

    public void setCustomerPaymentQty(Integer customerPaymentQty) {
        this.customerPaymentQty = customerPaymentQty;
    }

    public BigDecimal getCustomerPriceDiffAmountAud() {
        return customerPriceDiffAmountAud;
    }

    public void setCustomerPriceDiffAmountAud(BigDecimal customerPriceDiffAmountAud) {
        this.customerPriceDiffAmountAud = customerPriceDiffAmountAud;
    }

    public BigDecimal getCustomerPriceDiffAmountRmb() {
        return customerPriceDiffAmountRmb;
    }

    public void setCustomerPriceDiffAmountRmb(BigDecimal customerPriceDiffAmountRmb) {
        this.customerPriceDiffAmountRmb = customerPriceDiffAmountRmb;
    }

    public BigDecimal getCustomerPriceDiffAmountUsd() {
        return customerPriceDiffAmountUsd;
    }

    public void setCustomerPriceDiffAmountUsd(BigDecimal customerPriceDiffAmountUsd) {
        this.customerPriceDiffAmountUsd = customerPriceDiffAmountUsd;
    }

    public Integer getCustomerPriceDiffQty() {
        return customerPriceDiffQty;
    }

    public void setCustomerPriceDiffQty(Integer customerPriceDiffQty) {
        this.customerPriceDiffQty = customerPriceDiffQty;
    }

    public BigDecimal getDispatchIssueWrongItemAmountAud() {
        return dispatchIssueWrongItemAmountAud;
    }

    public void setDispatchIssueWrongItemAmountAud(BigDecimal dispatchIssueWrongItemAmountAud) {
        this.dispatchIssueWrongItemAmountAud = dispatchIssueWrongItemAmountAud;
    }

    public BigDecimal getDispatchIssueWrongItemAmountRmb() {
        return dispatchIssueWrongItemAmountRmb;
    }

    public void setDispatchIssueWrongItemAmountRmb(BigDecimal dispatchIssueWrongItemAmountRmb) {
        this.dispatchIssueWrongItemAmountRmb = dispatchIssueWrongItemAmountRmb;
    }

    public BigDecimal getDispatchIssueWrongItemAmountUsd() {
        return dispatchIssueWrongItemAmountUsd;
    }

    public void setDispatchIssueWrongItemAmountUsd(BigDecimal dispatchIssueWrongItemAmountUsd) {
        this.dispatchIssueWrongItemAmountUsd = dispatchIssueWrongItemAmountUsd;
    }

    public Integer getDispatchIssueWrongItemQty() {
        return dispatchIssueWrongItemQty;
    }

    public void setDispatchIssueWrongItemQty(Integer dispatchIssueWrongItemQty) {
        this.dispatchIssueWrongItemQty = dispatchIssueWrongItemQty;
    }

    public BigDecimal getDispatchIssueWrongSparePartAmountAud() {
        return dispatchIssueWrongSparePartAmountAud;
    }

    public void setDispatchIssueWrongSparePartAmountAud(BigDecimal dispatchIssueWrongSparePartAmountAud) {
        this.dispatchIssueWrongSparePartAmountAud = dispatchIssueWrongSparePartAmountAud;
    }

    public BigDecimal getDispatchIssueWrongSparePartAmountRmb() {
        return dispatchIssueWrongSparePartAmountRmb;
    }

    public void setDispatchIssueWrongSparePartAmountRmb(BigDecimal dispatchIssueWrongSparePartAmountRmb) {
        this.dispatchIssueWrongSparePartAmountRmb = dispatchIssueWrongSparePartAmountRmb;
    }

    public BigDecimal getDispatchIssueWrongSparePartAmountUsd() {
        return dispatchIssueWrongSparePartAmountUsd;
    }

    public void setDispatchIssueWrongSparePartAmountUsd(BigDecimal dispatchIssueWrongSparePartAmountUsd) {
        this.dispatchIssueWrongSparePartAmountUsd = dispatchIssueWrongSparePartAmountUsd;
    }

    public Integer getDispatchIssueWrongSparePartQty() {
        return dispatchIssueWrongSparePartQty;
    }

    public void setDispatchIssueWrongSparePartQty(Integer dispatchIssueWrongSparePartQty) {
        this.dispatchIssueWrongSparePartQty = dispatchIssueWrongSparePartQty;
    }

    public BigDecimal getDispatchIssueNotDispatchedAmountAud() {
        return dispatchIssueNotDispatchedAmountAud;
    }

    public void setDispatchIssueNotDispatchedAmountAud(BigDecimal dispatchIssueNotDispatchedAmountAud) {
        this.dispatchIssueNotDispatchedAmountAud = dispatchIssueNotDispatchedAmountAud;
    }

    public BigDecimal getDispatchIssueNotDispatchedAmountRmb() {
        return dispatchIssueNotDispatchedAmountRmb;
    }

    public void setDispatchIssueNotDispatchedAmountRmb(BigDecimal dispatchIssueNotDispatchedAmountRmb) {
        this.dispatchIssueNotDispatchedAmountRmb = dispatchIssueNotDispatchedAmountRmb;
    }

    public BigDecimal getDispatchIssueNotDispatchedAmountUsd() {
        return dispatchIssueNotDispatchedAmountUsd;
    }

    public void setDispatchIssueNotDispatchedAmountUsd(BigDecimal dispatchIssueNotDispatchedAmountUsd) {
        this.dispatchIssueNotDispatchedAmountUsd = dispatchIssueNotDispatchedAmountUsd;
    }

    public Integer getDispatchIssueNotDispatchedQty() {
        return dispatchIssueNotDispatchedQty;
    }

    public void setDispatchIssueNotDispatchedQty(Integer dispatchIssueNotDispatchedQty) {
        this.dispatchIssueNotDispatchedQty = dispatchIssueNotDispatchedQty;
    }

    public BigDecimal getListingIssueStockOutAmountAud() {
        return listingIssueStockOutAmountAud;
    }

    public void setListingIssueStockOutAmountAud(BigDecimal listingIssueStockOutAmountAud) {
        this.listingIssueStockOutAmountAud = listingIssueStockOutAmountAud;
    }

    public BigDecimal getListingIssueStockOutAmountRmb() {
        return listingIssueStockOutAmountRmb;
    }

    public void setListingIssueStockOutAmountRmb(BigDecimal listingIssueStockOutAmountRmb) {
        this.listingIssueStockOutAmountRmb = listingIssueStockOutAmountRmb;
    }

    public BigDecimal getListingIssueStockOutAmountUsd() {
        return listingIssueStockOutAmountUsd;
    }

    public void setListingIssueStockOutAmountUsd(BigDecimal listingIssueStockOutAmountUsd) {
        this.listingIssueStockOutAmountUsd = listingIssueStockOutAmountUsd;
    }

    public Integer getListingIssueStockOutQty() {
        return listingIssueStockOutQty;
    }

    public void setListingIssueStockOutQty(Integer listingIssueStockOutQty) {
        this.listingIssueStockOutQty = listingIssueStockOutQty;
    }

    public BigDecimal getListingIssueDescribedAmountAud() {
        return listingIssueDescribedAmountAud;
    }

    public void setListingIssueDescribedAmountAud(BigDecimal listingIssueDescribedAmountAud) {
        this.listingIssueDescribedAmountAud = listingIssueDescribedAmountAud;
    }

    public BigDecimal getListingIssueDescribedAmountRmb() {
        return listingIssueDescribedAmountRmb;
    }

    public void setListingIssueDescribedAmountRmb(BigDecimal listingIssueDescribedAmountRmb) {
        this.listingIssueDescribedAmountRmb = listingIssueDescribedAmountRmb;
    }

    public BigDecimal getListingIssueDescribedAmountUsd() {
        return listingIssueDescribedAmountUsd;
    }

    public void setListingIssueDescribedAmountUsd(BigDecimal listingIssueDescribedAmountUsd) {
        this.listingIssueDescribedAmountUsd = listingIssueDescribedAmountUsd;
    }

    public Integer getListingIssueDescribedQty() {
        return listingIssueDescribedQty;
    }

    public void setListingIssueDescribedQty(Integer listingIssueDescribedQty) {
        this.listingIssueDescribedQty = listingIssueDescribedQty;
    }

    public BigDecimal getListingIssueNoDeliveryAreaAmountAud() {
        return listingIssueNoDeliveryAreaAmountAud;
    }

    public void setListingIssueNoDeliveryAreaAmountAud(BigDecimal listingIssueNoDeliveryAreaAmountAud) {
        this.listingIssueNoDeliveryAreaAmountAud = listingIssueNoDeliveryAreaAmountAud;
    }

    public BigDecimal getListingIssueNoDeliveryAreaAmountRmb() {
        return listingIssueNoDeliveryAreaAmountRmb;
    }

    public void setListingIssueNoDeliveryAreaAmountRmb(BigDecimal listingIssueNoDeliveryAreaAmountRmb) {
        this.listingIssueNoDeliveryAreaAmountRmb = listingIssueNoDeliveryAreaAmountRmb;
    }

    public BigDecimal getListingIssueNoDeliveryAreaAmountUsd() {
        return listingIssueNoDeliveryAreaAmountUsd;
    }

    public void setListingIssueNoDeliveryAreaAmountUsd(BigDecimal listingIssueNoDeliveryAreaAmountUsd) {
        this.listingIssueNoDeliveryAreaAmountUsd = listingIssueNoDeliveryAreaAmountUsd;
    }

    public Integer getListingIssueNoDeliveryAreaQty() {
        return listingIssueNoDeliveryAreaQty;
    }

    public void setListingIssueNoDeliveryAreaQty(Integer listingIssueNoDeliveryAreaQty) {
        this.listingIssueNoDeliveryAreaQty = listingIssueNoDeliveryAreaQty;
    }

    public BigDecimal getOthersSystemErrorAmountAud() {
        return othersSystemErrorAmountAud;
    }

    public void setOthersSystemErrorAmountAud(BigDecimal othersSystemErrorAmountAud) {
        this.othersSystemErrorAmountAud = othersSystemErrorAmountAud;
    }

    public BigDecimal getOthersSystemErrorAmountRmb() {
        return othersSystemErrorAmountRmb;
    }

    public void setOthersSystemErrorAmountRmb(BigDecimal othersSystemErrorAmountRmb) {
        this.othersSystemErrorAmountRmb = othersSystemErrorAmountRmb;
    }

    public BigDecimal getOthersSystemErrorAmountUsd() {
        return othersSystemErrorAmountUsd;
    }

    public void setOthersSystemErrorAmountUsd(BigDecimal othersSystemErrorAmountUsd) {
        this.othersSystemErrorAmountUsd = othersSystemErrorAmountUsd;
    }

    public Integer getOthersSystemErrorQty() {
        return othersSystemErrorQty;
    }

    public void setOthersSystemErrorQty(Integer othersSystemErrorQty) {
        this.othersSystemErrorQty = othersSystemErrorQty;
    }

    public BigDecimal getOthersFraudulentPurchaseAmountAud() {
        return othersFraudulentPurchaseAmountAud;
    }

    public void setOthersFraudulentPurchaseAmountAud(BigDecimal othersFraudulentPurchaseAmountAud) {
        this.othersFraudulentPurchaseAmountAud = othersFraudulentPurchaseAmountAud;
    }

    public BigDecimal getOthersFraudulentPurchaseAmountRmb() {
        return othersFraudulentPurchaseAmountRmb;
    }

    public void setOthersFraudulentPurchaseAmountRmb(BigDecimal othersFraudulentPurchaseAmountRmb) {
        this.othersFraudulentPurchaseAmountRmb = othersFraudulentPurchaseAmountRmb;
    }

    public BigDecimal getOthersFraudulentPurchaseAmountUsd() {
        return othersFraudulentPurchaseAmountUsd;
    }

    public void setOthersFraudulentPurchaseAmountUsd(BigDecimal othersFraudulentPurchaseAmountUsd) {
        this.othersFraudulentPurchaseAmountUsd = othersFraudulentPurchaseAmountUsd;
    }

    public Integer getOthersFraudulentPurchaseQty() {
        return othersFraudulentPurchaseQty;
    }

    public void setOthersFraudulentPurchaseQty(Integer othersFraudulentPurchaseQty) {
        this.othersFraudulentPurchaseQty = othersFraudulentPurchaseQty;
    }

    public BigDecimal getOthersPoorCommunicationAmountAud() {
        return othersPoorCommunicationAmountAud;
    }

    public void setOthersPoorCommunicationAmountAud(BigDecimal othersPoorCommunicationAmountAud) {
        this.othersPoorCommunicationAmountAud = othersPoorCommunicationAmountAud;
    }

    public BigDecimal getOthersPoorCommunicationAmountRmb() {
        return othersPoorCommunicationAmountRmb;
    }

    public void setOthersPoorCommunicationAmountRmb(BigDecimal othersPoorCommunicationAmountRmb) {
        this.othersPoorCommunicationAmountRmb = othersPoorCommunicationAmountRmb;
    }

    public BigDecimal getOthersPoorCommunicationAmountUsd() {
        return othersPoorCommunicationAmountUsd;
    }

    public void setOthersPoorCommunicationAmountUsd(BigDecimal othersPoorCommunicationAmountUsd) {
        this.othersPoorCommunicationAmountUsd = othersPoorCommunicationAmountUsd;
    }

    public Integer getOthersPoorCommunicationQty() {
        return othersPoorCommunicationQty;
    }

    public void setOthersPoorCommunicationQty(Integer othersPoorCommunicationQty) {
        this.othersPoorCommunicationQty = othersPoorCommunicationQty;
    }

    public BigDecimal getProductQualityMissDamagedPartsAmountAud() {
        return productQualityMissDamagedPartsAmountAud;
    }

    public void setProductQualityMissDamagedPartsAmountAud(BigDecimal productQualityMissDamagedPartsAmountAud) {
        this.productQualityMissDamagedPartsAmountAud = productQualityMissDamagedPartsAmountAud;
    }

    public BigDecimal getProductQualityMissDamagedPartsAmountRmb() {
        return productQualityMissDamagedPartsAmountRmb;
    }

    public void setProductQualityMissDamagedPartsAmountRmb(BigDecimal productQualityMissDamagedPartsAmountRmb) {
        this.productQualityMissDamagedPartsAmountRmb = productQualityMissDamagedPartsAmountRmb;
    }

    public BigDecimal getProductQualityMissDamagedPartsAmountUsd() {
        return productQualityMissDamagedPartsAmountUsd;
    }

    public void setProductQualityMissDamagedPartsAmountUsd(BigDecimal productQualityMissDamagedPartsAmountUsd) {
        this.productQualityMissDamagedPartsAmountUsd = productQualityMissDamagedPartsAmountUsd;
    }

    public Integer getProductQualityMissDamagedPartsQty() {
        return productQualityMissDamagedPartsQty;
    }

    public void setProductQualityMissDamagedPartsQty(Integer productQualityMissDamagedPartsQty) {
        this.productQualityMissDamagedPartsQty = productQualityMissDamagedPartsQty;
    }

    public BigDecimal getProductQualityCannotBeUsedAmountAud() {
        return productQualityCannotBeUsedAmountAud;
    }

    public void setProductQualityCannotBeUsedAmountAud(BigDecimal productQualityCannotBeUsedAmountAud) {
        this.productQualityCannotBeUsedAmountAud = productQualityCannotBeUsedAmountAud;
    }

    public BigDecimal getProductQualityCannotBeUsedAmountRmb() {
        return productQualityCannotBeUsedAmountRmb;
    }

    public void setProductQualityCannotBeUsedAmountRmb(BigDecimal productQualityCannotBeUsedAmountRmb) {
        this.productQualityCannotBeUsedAmountRmb = productQualityCannotBeUsedAmountRmb;
    }

    public BigDecimal getProductQualityCannotBeUsedAmountUsd() {
        return productQualityCannotBeUsedAmountUsd;
    }

    public void setProductQualityCannotBeUsedAmountUsd(BigDecimal productQualityCannotBeUsedAmountUsd) {
        this.productQualityCannotBeUsedAmountUsd = productQualityCannotBeUsedAmountUsd;
    }

    public Integer getProductQualityCannotBeUsedQty() {
        return productQualityCannotBeUsedQty;
    }

    public void setProductQualityCannotBeUsedQty(Integer productQualityCannotBeUsedQty) {
        this.productQualityCannotBeUsedQty = productQualityCannotBeUsedQty;
    }

    public BigDecimal getRecallAmountAud() {
        return recallAmountAud;
    }

    public void setRecallAmountAud(BigDecimal recallAmountAud) {
        this.recallAmountAud = recallAmountAud;
    }

    public BigDecimal getRecallAmountRmb() {
        return recallAmountRmb;
    }

    public void setRecallAmountRmb(BigDecimal recallAmountRmb) {
        this.recallAmountRmb = recallAmountRmb;
    }

    public BigDecimal getRecallAmountUsd() {
        return recallAmountUsd;
    }

    public void setRecallAmountUsd(BigDecimal recallAmountUsd) {
        this.recallAmountUsd = recallAmountUsd;
    }

    public Integer getRecallQty() {
        return recallQty;
    }

    public void setRecallQty(Integer recallQty) {
        this.recallQty = recallQty;
    }
}
