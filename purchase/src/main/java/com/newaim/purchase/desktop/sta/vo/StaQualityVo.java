package com.newaim.purchase.desktop.sta.vo;

import com.newaim.purchase.config.json.JsonMoney;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class StaQualityVo implements Serializable{

    private String id;

    /**买方信息*/
    private String buyerId;
    private String buyerCnName;
    private String buyerEnName;

    private String orderId;
    private String orderTitle;//订单标题
    private String orderNumber;//订单号

    /**供应商信息*/
    private String vendorId;
    private String vendorCnName;
    private String vendorEnName;

    private String productId;
    private String sku;
    private String productName;
    private Date reportTime;
    private Integer result;
    private Integer issueA;
    private Integer issueB;
    private Integer issueC;
    private Integer issueD;
    private Integer issueR;
    private Integer issueF;
    private Integer issueG;

    private String file;
    private String departmentId;
    private String departmentCnName;
    private String departmentEnName;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderTitle() {
        return orderTitle;
    }

    public void setOrderTitle(String orderTitle) {
        this.orderTitle = orderTitle;
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

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getBuyerCnName() {
        return buyerCnName;
    }

    public void setBuyerCnName(String buyerCnName) {
        this.buyerCnName = buyerCnName;
    }

    public String getBuyerEnName() {
        return buyerEnName;
    }

    public void setBuyerEnName(String buyerEnName) {
        this.buyerEnName = buyerEnName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Date getReportTime() {
        return reportTime;
    }

    public void setReportTime(Date reportTime) {
        this.reportTime = reportTime;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public Integer getIssueA() {
        return issueA;
    }

    public void setIssueA(Integer issueA) {
        this.issueA = issueA;
    }

    public Integer getIssueB() {
        return issueB;
    }

    public void setIssueB(Integer issueB) {
        this.issueB = issueB;
    }

    public Integer getIssueC() {
        return issueC;
    }

    public void setIssueC(Integer issueC) {
        this.issueC = issueC;
    }

    public Integer getIssueD() {
        return issueD;
    }

    public void setIssueD(Integer issueD) {
        this.issueD = issueD;
    }

    public Integer getIssueR() {
        return issueR;
    }

    public void setIssueR(Integer issueR) {
        this.issueR = issueR;
    }

    public Integer getIssueF() {
        return issueF;
    }

    public void setIssueF(Integer issueF) {
        this.issueF = issueF;
    }

    public Integer getIssueG() {
        return issueG;
    }

    public void setIssueG(Integer issueG) {
        this.issueG = issueG;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

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
}
